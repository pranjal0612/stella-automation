package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

@Slf4j
public class TipPaymentPage {
    private final Page page;
    private final Locator amount;
    private final Locator donationMsg;
    private final Locator donationBtn;
    private final Locator skipBtn;
    private final String domain;

    public TipPaymentPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.amount = page.locator("input[type='number']");
        this.donationMsg = page.locator("textarea[placeholder='Donation Message']");
        this.donationBtn = page.locator("//button[normalize-space()='Donate']");
        this.skipBtn = page.locator("//button[normalize-space()='Skip']");
    }

    public void verifyTipPaymentPage() {
        assertEquals(page.url(), domain + "/tipPayment/cu");
        log.info("Verifying tip payment page");
    }

    public void enterTipAmount(String tipAmount, String donationMsg) {
        try {
            double amount = Double.parseDouble(tipAmount);
            if (amount >= 10) {
                this.amount.fill(tipAmount);
            }
            this.donationMsg.fill(donationMsg);
            page.waitForNavigation( () ->{
                    this.donationBtn.click();
            });
            log.info("Donation amount and message entered, donate button clicked");
        } catch (NumberFormatException e) {
            log.error("Invalid tip amount: " + tipAmount, e);
        }
}


    public void skipTip() {
        this.skipBtn.click();
        log.info("Skip button clicked");
    }
}
