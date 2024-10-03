package com.stella.playwright.page.creator;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Slf4j
public class CustomRequestOptionsPage {
    private final Page page;
    private final Locator createCustomRequestBtn;
    private final Locator title;
    private final Locator amount;
    private final Locator finishBtn;
    private final Locator deleteBtn;
    private List<ElementHandle> customRequestList;
    private final String domain;

    public CustomRequestOptionsPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.createCustomRequestBtn = page.locator(".w-100.text-center.button-class");
        this.title = page.locator(".form-control.stella-group");
        this.amount = page.locator("div[class=\"input-group stella-group\"] > input");
        this.finishBtn = page.locator("button[type=\"button\"]");
        this.deleteBtn = page.locator("//button[normalize-space()='Delete']");
    }

    public void navigate() {
        page.navigate(domain + "/customRequestOptionList/in");
        page.waitForLoadState();
        assertEquals(page.url(), domain + "/customRequestOptionList/in", "Navigation to custom request options page failed");
        log.info("Navigated to custom request options page");
    }

    public void setValue(String title, String amount) {
        createCustomRequestBtn.click();
        page.waitForSelector(".form-control.stella-group");
        this.title.fill(title);
        this.amount.fill(amount);
        finishBtn.click();
        page.waitForSelector(".w-100.btn-group-vertical > button");
        log.info("Successfully set value for custom request options");

        assertTrue(containsTitleOrAmount(title, amount), "Custom request with the given title or amount was not found");
    }

    public void deleteCustomRequestOption(String title, String amount) {
        initializeCustomRequestList();
        page.waitForTimeout(3000);
        for (ElementHandle requestElement : customRequestList) {
            String elementText = requestElement.innerText();
            if (elementText.contains(title) || elementText.contains(amount)) {
                requestElement.scrollIntoViewIfNeeded();
                requestElement.click();

                page.waitForNavigation(() ->{
                    page.waitForTimeout(3000);
                    deleteBtn.click();
                });
                log.info("Successfully deleted custom request option with title: {} or amount: {}", title, amount);
                return;
            }
        }
        log.warn("Custom request option with title: {} or amount: {} not found for deletion", title, amount);
    }

    public void editCustomRequestOption(String title, String amount, String newTitle, String newAmount) {
        initializeCustomRequestList();
        for (ElementHandle requestElement : customRequestList) {
            String elementText = requestElement.innerText();
            if (elementText.contains(title) || elementText.contains(amount)) {
                requestElement.scrollIntoViewIfNeeded();
                page.waitForTimeout(3000);
                requestElement.click();

                this.title.fill("");
                page.waitForTimeout(2000);
                this.title.fill(newTitle);
                this.amount.fill(newAmount);
                page.waitForTimeout(3000);
                page.click("//button[normalize-space()=\"Finish\"]");

                log.info("Successfully edited custom request option with title: {} or amount: {} to new title: {} and new amount: {}", title, amount, newTitle, newAmount);
                return;
            }
        }
    }

    private void initializeCustomRequestList() {
        page.waitForTimeout(3000);
        customRequestList = page.locator(".btn-custom-request").elementHandles();

        log.info("Custom request list initialized with {} items", customRequestList.size());
    }

    private boolean containsTitleOrAmount(String expectedTitle, String expectedAmount) {
        initializeCustomRequestList();
        page.waitForTimeout(3000);
        for (ElementHandle requestElement : customRequestList) {
            String elementText = requestElement.innerText();
            if (elementText.contains(expectedTitle) || elementText.contains(expectedAmount)) {
                log.info("Custom request with title: {} or amount: {} found", expectedTitle, expectedAmount);
                return true;
            }
        }
        log.error("Custom request with title: {} or amount: {} not found", expectedTitle, expectedAmount);
        return false;
    }
}
