package com.stella.playwright.page.fan;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.testng.Assert.assertEquals;

@Slf4j
public class FanAllAccessSignupPage extends FanSignUpPage {
    private final Locator allAccessButton;

    public FanAllAccessSignupPage(Page page, String domain) {
        super(page, domain);
        allAccessButton = page.locator("//*[@id=\"app\"]/div/section/div/div/div[3]/button[text()=' All-Access ']");
    }
    public void verifyAllAccessPage(String url) {
        navigate(url);
        page.waitForNavigation( () -> {
            this.allAccessButton.click();
        });
        assertEquals(page.url(), domain + "/platform/" + url);
        log.info("All Access Page is displayed");
    }

    public void setValue(String email, ITestContext context) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");
        String formattedDateTime = currentDateTime.format(formatter);
        setEmail(formattedDateTime+email, context);
        log.info("Email is set to and click on checkout Button");
    }
}
