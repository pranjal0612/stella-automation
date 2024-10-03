package com.stella.playwright.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.testng.ITestContext;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.testng.Assert.assertEquals;

@Slf4j
public class SignUpPage {
    private final Page page;

    private final Locator avatar;
    private final Locator name;
    private final Locator customizeURL;
    private final Locator email;
    private final Locator password;
    private final Locator createBtn;
    private final String domain;

    ObjectMapper mapper;

    public SignUpPage(Page page,String domain){
        this.page = page;
        this.avatar = page.locator("#file_upload");
        this.name = page.locator("input[name=\"name\"]");
        this.customizeURL = page.locator("input[name=\"url\"]");
        this.email = page.locator("#email");
        this.password = page.locator("#password");
        this.createBtn = page.locator("button[type=\"submit\"]");
        this.domain = domain;
        mapper = new ObjectMapper();
    }

    public void navigate(){
        page.navigate(domain + "/createAccountMobile/in");
        page.waitForLoadState();
        assertEquals(page.url(), domain + "/createAccountMobile/in");
        log.info("Navigated to " + domain + "/createAccountMobile/in");
    }

    public void navigateSubscriptions(){
        page.waitForTimeout(2000);
        assertEquals(page.url(),domain + "/platforms/in");
        page.click("label[for=\"checkbox-6\"]");
        page.click("label[for=\"checkbox-4\"]");
     page.waitForNavigation( () -> {
         page.locator("button[type=\"button\"]").click();
     });

    }

    public void setValuePrice(int price){
    page.waitForTimeout(2000);
        assertEquals(page.url(),domain + "/setPriceMobile/in");
        if (price > 0) {
            page.locator("input[value='50']").fill(String.valueOf(price));
        }
        page.click("button[type=\"button\"]");
    }

    public void navigateYourLink(){
        page.waitForTimeout(2000);
        assertEquals(page.url(),domain + "/yourLinkMobile/in");
        if(isNotBlank("#url-suffix")){
            page.locator("//button[normalize-space()='Continue']").click();
        }
    }
    public void setValue(String filePath,String name,String url,String email,String password){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");
        String formattedDateTime = currentDateTime.format(formatter);
//        page.locator("#file_upload").setInputFiles(Paths.get(filePath));
        this.name.fill(name);
        this.customizeURL.fill(url+formattedDateTime);
        this.email.fill(email+formattedDateTime);
        this.password.fill(password);
    }

    public void signUpSuccess(ITestContext context){
        page.locator("label[for=\"agree-to-terms\"]").click();
        page.waitForTimeout(1000);
        this.createBtn.click();
    }

    @SuppressWarnings("rawtypes")
    public void signUpFailed(){
        try{
            Response response = page.waitForResponse(domain +"/identity/api/v1/influencers/preCheck", () -> {
                log.info("Request received");
                page.click("button[type=\"submit\"]");
            });
            String responseBody = response.text();

            Map map = mapper.readValue(responseBody,Map.class);
            JsonElement jsonElement = JsonParser.parseString(ObjectUtils.toString(map.get("fieldErrors")));
            JsonArray a = jsonElement.getAsJsonArray();
            Map fieldError = mapper.readValue(a.get(0).toString(),Map.class);
            assertEquals(fieldError.get("code").toString(), "EMAIL.ALREADY.EXISTS");

            ElementHandle validationMessage = page.waitForSelector("text=An account with entered email already exists, please try different email to register, or use login to use existing account" ,
                    new Page.WaitForSelectorOptions().setTimeout(10000));//span[contains(text(),'An account with entered email already exists, plea')]
            String messageText = validationMessage.innerText();
            System.out.println(messageText);
            assertEquals(messageText,"An account with entered email already exists, please try different email to register, or use login to use existing account");
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

    public void emailValidation(){
        createBtn.click();
        ElementHandle validationMessage = page.waitForSelector("text=Not a valid email address",
                new Page.WaitForSelectorOptions().setTimeout(1000));
        String messageText = validationMessage.innerText();
        assertEquals(messageText,"Not a valid email address");
    }
}
