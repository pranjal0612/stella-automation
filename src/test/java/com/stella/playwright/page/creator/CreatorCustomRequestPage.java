package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.stella.playwright.base.BasePage;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;

@Slf4j
public class CreatorCustomRequestPage extends BasePage {
    private final Locator readyBtn;
    private final Locator reviewBtn;
    private final Locator acceptBtn;
    private final Locator counterOfferBtn;
    private final Locator declineBtn;
    private final Locator saveForLater;
    private final Locator uploadRequestBtn;
    private final Locator uploadLink;
    private final Locator next;
    private final Locator complete;
    private final Locator done;
    private final Locator closeCompletedRequestBtn;
    private final Locator counterOfferTitle;
    private final Locator counterOfferAmount;
    private final Locator submit;

    public CreatorCustomRequestPage(Page page, String domain) {
        super(page, domain);
        this.readyBtn = page.locator(".bedge.bedge-ready").nth(0);
        this.reviewBtn = page.locator(".bedge.bedge-review").nth(0);
        this.acceptBtn = page.locator(".d-flex.d-flex.align-items-center.justify-content-center.mt-3 > button.btn.stella-btn.gradient-a.w-100.text-dark.rounded-pill");
        this.counterOfferBtn = page.locator(".d-flex.d-flex.align-items-center.justify-content-center.mt-3 > button.btn.btn-bg-white.btn-outline-black.border-radius-6.font-size-12.px-3.py-2.mx-1.font-weight-bold");
        this.declineBtn = page.locator("a[class='btn stella-btn w-100 text-dark btn-danger rounded-pill']");
        this.saveForLater = page.locator(".mx-2.cursor-pointer");
        this.uploadRequestBtn = page.locator("#upload_image_section > input");
        this.uploadLink = page.locator("input[class='form-control py-2']");
        this.next = page.locator(".col-3.px-2.text-left > button");
        this.complete = page.locator("(//button[normalize-space()='Complete'])[1]");
        this.done = page.locator("(//button[@class='btn stella-btn gradient-a w-100 text-dark rounded-pill'][normalize-space()='Done'])[1]");
        this.closeCompletedRequestBtn = page.locator("div[id='influencer_custom_request_upload_content'] span[aria-hidden='true']");
        this.counterOfferTitle = page.locator("textarea[placeholder='(Optional)']");
        this.counterOfferAmount = page.locator("input[type='number']");
        this.submit = page.locator(".col-12.pl-0 > button");
    }

    public void navigate() {
        page.navigate(domain + "/customRequestList/in");
        page.waitForLoadState();
        assertEquals(page.url(), domain + "/customRequestList/in", "Navigation to creator custom request page failed");
        log.info("Navigated to creator custom request page");
    }

    public void readyRequest(String filePath, String link) {
        if (isElementVisible(readyBtn)) {
            readyBtn.click();
            uploadFile(filePath);
            clickNextAndComplete();
            log.info("Request uploaded successfully");
        } else {
            log.info("Request is not ready yet");
        }
    }

    public void reviewRequest(String filePath, String link) {
        if (isElementVisible(reviewBtn)) {
            reviewBtn.click();
            acceptRequest(filePath, link);
        } else {
            log.info("Request is not ready for review yet");
        }
    }

    public void counterOfferRequest(String title, String amount) {
        if (isElementVisible(reviewBtn)) {
            reviewBtn.click();
            counterOfferBtn.click();
            waitForTimeout(3000);
            counterOfferTitle.fill(title);
            counterOfferAmount.fill(amount);
            submit.click();
            closeCounterOfferModal();
            log.info("Counter Offer sent successfully");
        } else {
            log.info("Request is not ready for review yet");
        }
    }

    private void acceptRequest(String filePath, String link) {
        acceptBtn.click();
        waitForTimeout(2000);
        if (isElementVisible(done)) {
            done.click();
        }
        if (isElementVisible(uploadLink)) {
            uploadLink.fill(link);
            waitForTimeout(3000);
            clickNextAndComplete();
        } else {
            log.info("Request Accepted, but payment is pending!!");
        }
        waitForTimeout(3000);
    }

    private void uploadFile(String filePath) {
        uploadRequestBtn.setInputFiles(Paths.get(filePath));
        waitForTimeout(2000);
    }

    private void clickNextAndComplete() {
        next.click();
        complete.first().click();
        waitForTimeout(3000);
    }

    private void closeCounterOfferModal() {
        page.click("div[id='custom_request_counter_offer_complete'] span[aria-hidden='true']");
    }

    private boolean isElementVisible(Locator locator) {
        return locator.isVisible();
    }

    private void waitForTimeout(int timeout) {
        page.waitForTimeout(timeout);
    }
}
