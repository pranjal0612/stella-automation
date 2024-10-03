package com.stella.playwright.page.creator;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;

@Slf4j
public class EditBonusContentPage {

    private final Page page;
    private List<ElementHandle> bonusList;
    private final Locator edit;
    private final Locator archive;
    private final Locator cancel;
    private final Locator description;
    private final Locator paymentToggle;
    private final Locator price;
    private final Locator secondArchive;
    private final Locator delete;
    private final Locator submit;
    private final String domain;

    public EditBonusContentPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.edit = page.locator("//button[normalize-space()='Edit']");
        this.archive = page.locator(".btn.stella-btn.w-100.text-dark.btn-danger.rounded-pill");
        this.cancel = page.locator("button[aria-label='Close']");
        this.description = page.locator("input[placeholder='Description']");
        this.paymentToggle = page.locator("#toggle_price");
        this.price = page.locator("//input[@id='price_section']");
        this.secondArchive = page.locator("xpath=//div[@role='group']//button[text()='Archive']");
        this.delete = page.locator("xpath=//div[@role='group']//button[text()='Delete']");
        this.submit = page.locator("button[type='submit']");
    }

    private void initializeBonusList() {
        page.waitForTimeout(3000);
        bonusList = page.locator(".mb-3.mx-1").elementHandles();
    }

    private void validateAndExtractIdFromUrl() {
        String currentUrl = page.url();
        String URL_PATTERN = domain + "/bonusContentEdit/in/(\\d+)";
        Pattern pattern = Pattern.compile(URL_PATTERN);
        Matcher matcher = pattern.matcher(currentUrl);

        if (matcher.matches()) {
            String id = matcher.group(1);
            log.info("Successfully navigated to the bonus content edit page. URL: " + currentUrl);
            log.info("Extracted ID: " + id);
        } else {
            log.error("Failed to navigate to the expected bonus content edit page or extract ID. Actual URL: " + currentUrl);
        }
    }


    public void navigate() {
        page.navigate(domain + "/bonusContentList/in");
        assertEquals(page.url(), domain + "/bonusContentList/in", "Failed to navigate to bonus content list page");
        log.info("Navigated to bonus content list page");
        initializeBonusList();
    }

    public void edit(String description, String price) {
        initializeBonusList();
        if (!bonusList.isEmpty()) {
            bonusList.get(0).click();
            edit.click();
            page.fill("input[placeholder='Description']", "");
            page.waitForTimeout(2000);
            this.description.fill(description);
            page.waitForTimeout(2000);
            boolean isPaymentToggleSelected = (boolean) paymentToggle.evaluate("element => element.classList.contains('active')");
            if (isPaymentToggleSelected) {
                this.price.fill("");
                this.price.fill(price);
                log.info("Payment toggle is already set to paid, no action needed.");
            } else {
                paymentToggle.click();
                this.price.fill(price);
            }
            validateAndExtractIdFromUrl();
            submit.click();
            page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
            String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
            assertEquals(alertText, " Uploaded successfully ", "Alert text does not match.");
            log.info("Edited bonus content with description: {} and price: {}", description, price);

        } else {
            log.error("Bonus list is empty, unable to edit bonus content.");
        }
    }

    public void archive() {
        initializeBonusList();
        if (!bonusList.isEmpty()) {
            bonusList.get(0).click();
            edit.click();
            secondArchive.click();
            page.waitForTimeout(2000);
            page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
            String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
            assertEquals(alertText, " Archived successfully ", "Alert text does not match.");
            assertEquals(page.url(), domain + "/bonusContentList/in", "Failed to navigate to bonus content edit page after deletion");
            log.info("Archived bonus content");
        } else {
            log.error("Bonus list is empty, unable to archive bonus content.");
        }
    }

    public void delete() {
        initializeBonusList();
        if (!bonusList.isEmpty()) {
            bonusList.get(0).click(new ElementHandle.ClickOptions().setForce(true));
            edit.click();
            delete.click();
            page.waitForTimeout(2000);
            page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
            String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
            assertEquals(alertText, " Deleted successfully ", "Alert text does not match.");
            assertEquals(page.url(), domain + "/bonusContentList/in", "Failed to navigate to bonus content edit page after deletion");
            log.info("Deleted bonus content");
        } else {
            log.error("Bonus list is empty, unable to delete bonus content.");
        }
    }
}
