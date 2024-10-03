package com.stella.playwright.page;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.stella.playwright.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.testng.ITestContext;

import java.util.Map;

import static org.testng.Assert.assertEquals;

@Slf4j
public class LoginPage{
    private final Page page;
    private final Locator email;
    private final Locator password;
    private final Locator checkBox;
    private final Locator continueBtn;
    private final ObjectMapper mapper;

    private final String domain;

    public LoginPage(Page page,String domain){
        this.page = page;
        this.email = page.locator("[type$='text']");
        this.password = page.locator("[placeholder=\"Enter your password\"]");
        this.checkBox = page.locator("label[for=\"remember-me\"]");
        this.continueBtn = page.locator(".btn.stella-btn.gradient-a.w-100.font-weight-900.rounded-pill");
        this.mapper = new ObjectMapper();
        this.domain = domain;
    }

    public void navigate(){
        page.navigate(domain + "/login");
        page.waitForLoadState();
        log.info("Navigating to login page: {}", domain + "/login");
    }

    public void setValue(String email,String password){
        this.email.fill(email);
        this.password.fill(password);
        this.checkBox.click();
        log.info("Checkbox clicked for 'Remember Me'");
    }

    @SuppressWarnings("rawtypes")
    public void login(ITestContext context){
        Response response = page.waitForResponse(domain + "/identity/oauth/token", () -> {
          page.waitForNavigation(() -> {
              this.continueBtn.click();
              page.waitForTimeout(3000);
          });
        });

        try{
            UserModel user = new UserModel();
            String responseBody = response.text();
            Map map = mapper.readValue(responseBody, Map.class);
            String userId = ObjectUtils.toString(map.get("id"));
            user.setUserId(Integer.valueOf(userId));
            user.setEmail(ObjectUtils.toString(map.get("email")));

            String token = map.get("access_token").toString();

            context.setAttribute("userDetails", user);
            context.setAttribute("token","Bearer" + token);
        }catch (Exception e){
            e.printStackTrace();
            log.error("Error during login", e);
        }
    }

    @SuppressWarnings("rawtypes")
    public void loginFailed(ITestContext context){
        Response response = page.waitForResponse(domain + "/identity/oauth/token", () -> {
            this.continueBtn.click();
        });

        try{
            String responseBody = response.text();
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(responseBody, Map.class);
            assertEquals(map.get("error_description"),"Bad credentials" );
        }catch (Exception e){
            e.printStackTrace();
            log.error("Error during failed login", e);
        }
    }
}
