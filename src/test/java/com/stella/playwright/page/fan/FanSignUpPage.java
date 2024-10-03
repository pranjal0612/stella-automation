package com.stella.playwright.page.fan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.testng.ITestContext;

import java.util.Map;

import static org.testng.Assert.assertEquals;

@Slf4j
public class FanSignUpPage {

    public final Page page;
    private final  Locator email;
    private final Locator checkoutBtn;
    public final String domain;

    private final ObjectMapper mapper;

    public FanSignUpPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.email = page.locator("input[placeholder=\"Enter your email\"]");
        this.checkoutBtn = page.locator("button[type=\"submit\"]");
        this.mapper = new ObjectMapper();
    }

    public void navigate(String url){
        page.navigate(domain + "/platform/cu/" + url);
        page.waitForLoadState();
        assertEquals(page.url(),domain + "/platform/cu/" + url);
        log.info("Navigated to " + domain + "/platform/cu/" + url);
    }
    public void setEmail(String email, ITestContext context) {
        this.email.fill(email);
        try {
            Response response = page.waitForResponse(domain + "/identity/oauth/token", () -> {
                log.info("Request received");
                this.checkoutBtn.click();
            });
            log.info("Response received");

            String responseBody = response.text();
            Map<String, Object> map = mapper.readValue(responseBody, Map.class);

            String userId = ObjectUtils.toString(map.get("id"));
            String emailId = ObjectUtils.toString(map.get("email"));
            String token = map.get("access_token").toString();

            context.setAttribute("userId", Integer.valueOf(userId));
            context.setAttribute("email", emailId);
            context.setAttribute("token", "Bearer " + token);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error during login", e);
            }

    }
}
