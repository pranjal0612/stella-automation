package com.stella.playwright.page.fan;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.testng.Assert.assertEquals;

@Slf4j
public class FanInstagramCloseFriendsSignupPage extends FanSignUpPage{

    private final Locator instagramCloseFriends;

    private final Locator instagramHandle;
    private final Locator re_instagramHandle;

    public FanInstagramCloseFriendsSignupPage(Page page, String domain) {
        super(page,domain);
        this.instagramCloseFriends = page.locator("//*[@id=\"app\"]/div/section/div/div/div[3]/button[text()=' Instagram Close Friends ']");
        this.instagramHandle = page.locator("input[placeholder='Your Instagram handle']");
        this.re_instagramHandle = page.locator("input[placeholder='Re-enter your Instagram handle']");
    }

    public void verifyInstagramCloseFriendsPage(String url){
        navigate(url);
        page.waitForNavigation( () -> {
            this.instagramCloseFriends.click();
        });
        assertEquals(page.url(), domain + "/c/" + url);
        log.info("Instagram Close Friends page is displayed");
    }

    public void setValue(String handle , String email , ITestContext context){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");
        String formattedDateTime = currentDateTime.format(formatter);
        this.instagramHandle.fill(handle);
        this.re_instagramHandle.fill(handle);
        setEmail(formattedDateTime + email,context);
        log.info("Instagram handle and email are entered and then click on checkout button");
    }
}
