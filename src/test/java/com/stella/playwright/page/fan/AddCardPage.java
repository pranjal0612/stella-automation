package com.stella.playwright.page.fan;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

@Slf4j
public class AddCardPage {
    private final Page page;
    private final Locator addNewCardBtn;
    private final Locator cardNumber;
    private final Locator cvv;
    private final Locator saveBtn;
    private final Locator deleteCard;
    private final Locator deleteCardConfirmation;
    private final Locator editCard;
    private final Locator editCardNumber;
    private final String domain;

    public AddCardPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.addNewCardBtn = page.locator(".custom-control-label");
        this.cardNumber = page.locator("//input[@id='cardNumber']");
        this.cvv = page.locator("#cardCvv");
        this.saveBtn = page.locator("button[type='button']");
        this.deleteCard = page.locator("//img[@class=\"delete-payment-option\"]");
        this.deleteCardConfirmation = page.locator("button[class='btn w-100 font-family-poppins stella-modal-btn text-dark btn-success btn-danger border-0']");
        this.editCard = page.locator(".edit-payment-option");
        this.editCardNumber = page.locator("input[type='text']");
    }

    public void navigate() {
        page.waitForNavigation(() -> {
            page.navigate(domain + "/paymentOption/cu");
        });
        page.waitForLoadState();
        assertEquals(page.url(), domain + "/paymentOption/cu");
    }

    public void setValue(String cardNumber, String cvv) {
        try {
            page.waitForTimeout(3000);
            if (!editCard.isVisible()) {
                if (addNewCardBtn.isVisible()) {
                    addNewCardBtn.click();
                    log.info("Add New Card button clicked");
                }

                this.cardNumber.fill(cardNumber);
                page.selectOption("select[placeholder='mm']", "11");
                page.selectOption("select[placeholder='yyyy']", "2035");
                this.cvv.fill(cvv);

                page.waitForNavigation(() -> {
                    saveBtn.click();
                    page.waitForTimeout(3000);
                });
                log.info("Card added successfully");

            } else {
                log.info("A card already exists. No new card was added.");
            }
        } catch (Exception e) {
            log.error("An error occurred while adding the card: ", e);
        }
    }


    public void deleteCard() {
        deleteCard.nth(0).click();
        page.waitForTimeout(3000);
        this.deleteCardConfirmation.click();
        page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
        String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
        assertEquals(alertText, " Deleted successfully ", "Alert text does not match.");
        log.info("Alert text verification successful.");
        log.info("Card deleted successfully");
    }

    public void editCard(String cardNumber) {
        editCard.click();
        editCardNumber.fill(cardNumber);
        page.waitForNavigation(() -> {
            saveBtn.click();
        });
        log.info("Card edited successfully");
    }
}
