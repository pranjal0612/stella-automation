package com.stella.playwright.page.fan;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.stella.playwright.page.creator.OnlineCheckoutStripePage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.testng.Assert.assertEquals;

@Slf4j
public class FanCustomRequestPage {
    private final Page page;
    private final Locator createBtn;
    private final Locator title;
    private final Locator amount;
    private final Locator submitBtn;
    private final Locator continueBtn;
    private final Locator preRequestTitle;
    private final Locator preRequestSubmitBtn;
    private final Locator actionBtn;
    private final Locator payBtn;
    private final Locator declineBtn;
    private final Locator closeBtn;
    private final Locator doneBtn;
    private final Locator deleteBtn;
    private final String domain;

    public FanCustomRequestPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.createBtn = page.locator("//div[@class='row submit-subscribe-button']//div//button[text()='Submit ']");//.col-4.col-md-3.d-flex.align-items-center.justify-content-lg-start > button
        this.title = page.locator("div[id=\"submit_customer_offer\"] div[class=\"col-md-8 col-10\"] > textarea");
        this.amount = page.locator("div[class='input-group dollar-text border-dark border-radius-6'] input[type='number']");
        this.submitBtn = page.locator("div[class='col-md-8 col-10 offer-section'] button[class='btn stella-btn gradient-a w-100 text-dark rounded-pill']");
        this.continueBtn = page.locator("//button[normalize-space()='Continue']");
        this.preRequestTitle = page.locator("div[id='custom_request_option_info'] textarea[class='form-control stella-group my-2 p-2 border-dark border-radius-6']");
        this.preRequestSubmitBtn = page.locator(".col-12.my-2.continue-btn > button");
        this.actionBtn = page.locator(".row > div > span.bedge.btn-bg-purple.font-purple").nth(0);
        this.payBtn = page.locator("div[class='d-flex align-items-center justify-content-center mt-3'] button[class='btn stella-btn gradient-a w-100 text-dark rounded-pill']");
        this.declineBtn = page.locator("(//button[normalize-space()='Decline'])[1]");
        this.doneBtn = page.locator("//button[normalize-space()='Done']");
        this.closeBtn = page.locator("div[id='customer_request_accepted_modal'] span[aria-hidden='true']");
        this.deleteBtn = page.locator(".col-1.px-1 > button");
    }

    public void navigate() {
        page.navigate(domain + "/customerCustomRequestList/cu");
        assertEquals(page.url(), domain + "/customerCustomRequestList/cu");
        log.info("Navigated to " + domain + "/customerCustomRequestList/cu");
    }

    public void setValue(String title, String amount, String cardNumber, String expiryDate, String cvc, String cardHolder, String country) {
        try {
            page.waitForTimeout(2000);
            if (createBtn.isVisible()) {
                createBtn.click();
            }
            this.title.fill(title);
            double amt = Double.parseDouble(amount);

            if (amt >= 25) {
                this.amount.fill(amount);
            }
            submitBtn.click();
            doneBtn.click();
            page.waitForTimeout(2000);
            if (actionBtn.isVisible()) {
                actionBtn.click();
                handlePayment(cardNumber, expiryDate, cvc, cardHolder, country);
            }
            log.info("Created custom request successfully!");
        } catch (Exception e) {
            log.error("An error occurred while creating a custom request: " + e.getMessage(), e);
        }
    }

    public void createPreRequest(String title, String cardNumber, String expiryDate, String cvc, String cardHolder, String country) {
        try {
            page.waitForTimeout(2000);
            if (createBtn.isVisible()) {
                createBtn.click();
            }
            page.waitForTimeout(2000);

            List<ElementHandle> preRequestList = page.locator(".col-md-8.col-md-8.col-sm-12.text-left > div > button").elementHandles();
            if (!preRequestList.isEmpty()) {
                for (ElementHandle locator : preRequestList) {
                    locator.click();
                }
                continueBtn.click();

                preRequestTitle.fill(title);
                preRequestSubmitBtn.click();
                page.waitForTimeout(2000);

                handlePayment(cardNumber, expiryDate, cvc, cardHolder, country);


                log.info("Successfully created a pre-custom request.");
            } else {
                log.warn("No pre-request options available, cannot proceed.");
            }
        } catch (Exception e) {
            log.error("An error occurred while creating a pre-custom request: " + e.getMessage(), e);
        }
    }

    private void handlePayment(String cardNumber, String expiryDate, String cvc, String cardHolder, String country) {
        try {
            page.waitForTimeout(1000);
            if (page.locator("text=You Received A Counter-offer").isVisible()) {
                page.locator(".d-block.align-items-center.justify-content-center.mt-3 > button.btn.stella-btn.gradient-a.w-100.text-dark.rounded-pill.mb-3").click();
                log.info("Action Payment Completed!!");
            }
            page.waitForTimeout(2000);
            if (payBtn.isVisible()) {
                payBtn.click();
                page.waitForTimeout(2000);
                OnlineCheckoutStripePage checkoutStripePage = new OnlineCheckoutStripePage(page, domain);
                checkoutStripePage.verifyCheckoutPage();
                checkoutStripePage.enterCardDetails(cardNumber, expiryDate, cvc, cardHolder, country);
            } else {
                if (doneBtn.isVisible()) {
                    doneBtn.click();
                }
            }
        } catch (Exception e) {
            log.error("An error occurred during payment: " + e.getMessage(), e);
        }
    }

    public void deleteRequest() {
        try {
            if (deleteBtn.isVisible()) {
                deleteBtn.click();
                page.waitForSelector("//button[normalize-space()='Delete']");
                page.locator("//button[normalize-space()='Delete']").click();
                log.info("Custom request deleted successfully.");
            } else {
                log.warn("Delete button not visible, cannot delete the request.");
            }
        } catch (Exception e) {
            log.error("An error occurred while deleting the custom request: " + e.getMessage(), e);
        }
    }
}
