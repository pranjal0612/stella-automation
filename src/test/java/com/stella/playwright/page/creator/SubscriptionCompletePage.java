package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

@Slf4j
public class SubscriptionCompletePage {
    private final Page page;
    private final Locator continueButton;
    private final String domain;

    public SubscriptionCompletePage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.continueButton = page.locator("//*[text()='Continue ']");
    }

    public void verifySubscriptionCompletePage() {
        assertEquals(page.url(), domain + "/subscriptionComplete/cu");
        log.info("Subscription Complete Page is displayed");
    }

    public void clickContinueButton() {
        page.waitForNavigation(() -> {
            this.continueButton.click();
            log.info("Continue button is clicked");
        });
    }
}
