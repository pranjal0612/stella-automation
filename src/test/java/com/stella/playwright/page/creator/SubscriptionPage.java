package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

@Slf4j
public class SubscriptionPage {
    private final Page page;
    private final Locator annuallyCharged;
    private final Locator monthlyCharged;
    private final Locator subscribeButton;
    private final String domain;

    public SubscriptionPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.annuallyCharged = page.locator(".btn.btn-outline.customer-pricing-toggle.order-first.order-md-last");//*[@id='app']//section//div//p[substring-after(text(),'Charged Annuall')]
        this.monthlyCharged = page.locator("label[class='btn btn-outline customer-pricing-toggle']");//*[@id='app']//section//p[contains(.,'Charged Monthly')]
        this.subscribeButton = page.locator("button[type='button']");
    }

    public void navigateToSubscriptionPage() {
        assertEquals(page.url(), domain + "/customerSubscription/cu");
        log.info("Navigated to subscription page");
    }

    public void doSubscription() {
        try {
            boolean isAnnuallyChargedSelected = (boolean) annuallyCharged.evaluate("element => element.classList.contains('active')");
            if (isAnnuallyChargedSelected) {
                log.info("Annually Charged option is already selected. Clicking subscribe button.");
            } else {
                monthlyCharged.click();
                log.info("Monthly Charged option is selected. Clicking subscribe button.");
            }
            subscribeButton.click();
        } catch (Exception e) {
            log.error("Error during subscription: " + e.getMessage(), e);
        }
    }
}