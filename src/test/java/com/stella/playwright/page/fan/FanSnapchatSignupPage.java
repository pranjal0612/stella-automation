package com.stella.playwright.page.fan;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.testng.Assert.assertEquals;

@Slf4j
public class FanSnapchatSignupPage extends FanSignUpPage{
    private final Locator snapchat;

    private final Locator snapchatHandle;
    private final Locator re_snapchatHandle;

    public FanSnapchatSignupPage(Page page ,String domain){
        super(page, domain);
        this.snapchat = page.locator("//*[@id='app']/div/section/div/div/div[3]/button[text()=' Private Snapchat ']");
        this.snapchatHandle = page.locator("input[placeholder=\"Your Snapchat handle\"]");
        this.re_snapchatHandle = page.locator("input[placeholder=\"Re-enter your Snapchat handle\"]");
    }

    public void verifySnapchatPage(String url){
        navigate(url);
        page.waitForNavigation( () ->{
            this.snapchat.click();
        });
        assertEquals(page.url(), domain + "/snapchat/" + url);
        log.info("Snapchat page is displayed");
    }

    public void setValue(String handle, String email, ITestContext context){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");
        String formattedDateTime = currentDateTime.format(formatter);
        this.snapchatHandle.fill(handle);
        this.re_snapchatHandle.fill(handle);
        setEmail(formattedDateTime+email,context);
        log.info("SnapChat handle and email are entered and then click on checkout button");
    }
    
}
