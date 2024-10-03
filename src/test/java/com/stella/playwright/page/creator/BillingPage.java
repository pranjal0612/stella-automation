package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.stella.playwright.base.BasePage;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

@Slf4j
public class BillingPage extends BasePage {
    private final Locator bankName;
    private final Locator accountNumber;
    private final Locator routingNumber;
    private final Locator bankContractNumber;
    private final Locator re_accountNumber;
    private final Locator re_routingNumber;
    private final Locator saveBtn;

    public BillingPage(Page page,String domain) {
        super(page,domain);
        this.bankName = page.locator("//input[@placeholder='Bank name']");
        this.accountNumber = page.locator("input[placeholder=\"Account number\"]");
        this.routingNumber = page.locator("input[placeholder=\"Rouitng number\"]");
        this.bankContractNumber = page.locator("input[placeholder=\"Bank contact number\"]");
        this.re_accountNumber = page.locator("input[placeholder=\"Re-enter account number\"]");
        this.re_routingNumber = page.locator("input[placeholder=\"Re-enter routing number\"]");
        this.saveBtn = page.locator("button[type=\"submit\"]");
    }

    public void navigate() {
        page.navigate(domain + "/billingMobile/in");
        page.waitForLoadState();
        assertEquals(page.url(), domain + "/billingMobile/in");
        log.info("Creator Billing Page is displayed");
    }

    public void enterBankDetails(String bankName, String accountNumber, String routingNumber, String bankContractNumber, String re_accountNumber, String re_routingNumber) {
        page.waitForTimeout(2000);
        this.bankName.fill(bankName);
        this.accountNumber.fill(accountNumber);
        this.routingNumber.fill(routingNumber);
        this.bankContractNumber.fill(bankContractNumber);
        this.re_accountNumber.fill(re_accountNumber);
        this.re_routingNumber.fill(re_routingNumber);
        page.waitForTimeout(2000);
        saveBtn.click();
        log.info("Bank details entered successfully");
        page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
        String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
        assertEquals(alertText, " Saved successfully ", "Alert text does not match.");
        log.info("Alert text verification successful.");
    }
}


