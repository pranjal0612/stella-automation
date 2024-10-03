package com.stella.playwright.page.creator;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.List;

import static org.testng.Assert.assertEquals;

@Slf4j
public class AddBonusContentPage {

    private final Page page;
    private final Locator addBonusContentButton;
    private final Locator uploadFiles;
    private final Locator description;
    private final Locator paymentToggle;
    private final Locator price;
    private final Locator pinPost;
    private final Locator notifySubscribers;
    private final Locator submitButton;
    private final Locator loadingBar;
    private final String domain;

    public AddBonusContentPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.addBonusContentButton = page.locator(".login-body > button >span");
        this.uploadFiles = page.locator("//input[@type='file']");
        this.description = page.locator("textarea[placeholder='Description']");
        this.paymentToggle = page.locator("#toggle_price");
        this.price = page.locator("#price_section");
        this.pinPost = page.locator("label[for='pinned-id']");
        this.notifySubscribers = page.locator("label[for='notify-id']");
        this.submitButton = page.locator("button[type='submit']");
        this.loadingBar = page.locator(".modal-body.text-center.p-3 > span");
    }

    public void navigate() {
        page.navigate(domain + "/creator/bonus-content");
        page.waitForLoadState();
        assertEquals(page.url(), domain + "/creator/bonus-content", "Navigation to bonus content list failed.");
    }

    public void setValue(String filePath, String descriptionText, String priceText) {
        try {
            page.waitForSelector(".login-body > button >span");
            addBonusContentButton.click();

            uploadFiles.setInputFiles(Paths.get(filePath));
            page.waitForTimeout(2000);
            description.fill(descriptionText);

            if (!isPaymentToggleActive()) {
                paymentToggle.click();
            } else {
                log.info("Payment toggle is already active.");
            }

            if (isPaymentToggleActive()) {
                price.fill(priceText);
            }

            // pinPost.click();
            // notifySubscribers.click();

            // Clicking on submit button
            page.waitForNavigation(() -> {
                submitButton.click();
                page.waitForTimeout(5000);
                page.waitForSelector(".modal-body.text-center.p-3 > span", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
            });
            log.info("Clicking on submit button.");


            page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
            String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
            assertEquals(alertText, " Uploaded successfully ", "Alert text does not match.");
            log.info("Alert text verification successful.");

            verifyDescription(descriptionText);
            page.reload();
        }catch (Exception e){
            log.error("Unexpected error occurred during bonus content creation: " + e.getMessage());
            throw e;
        }

    }


    private boolean isPaymentToggleActive() {
        boolean isActive = (boolean) paymentToggle.evaluate("element => element.classList.contains('active')");
        return isActive;
    }

    private void verifyDescription(String expectedDescription) {
        page.waitForTimeout(3000);

        List<ElementHandle> elements = page.querySelectorAll("//*[@class='archive-section']//p");

        if (elements.isEmpty()) {
            log.error("No elements found with the specified selector.");
            throw new RuntimeException("No elements found with the specified selector.");
        }

        ElementHandle latestElement = elements.get(0);
        String descriptionMsg = latestElement.innerText().trim();
        log.info("Extracted description: {}", descriptionMsg);

        assertEquals(descriptionMsg, expectedDescription, "Description text does not match.");
        log.info("Description verification successful.");
    }
}
