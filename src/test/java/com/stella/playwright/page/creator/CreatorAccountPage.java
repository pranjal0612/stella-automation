package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

@Slf4j
public class CreatorAccountPage {
    private final Page page;
    private final Locator name;
    private final Locator password;
    private final Locator country;
    private final Locator phoneNo;

    private final Locator finishBtn;
    private final String domain;

    public CreatorAccountPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.name = page.locator("input[placeholder='Enter your name']");
        this.password = page.locator("#password");
        this.country = page.locator("select[placeholder='Enter your country']");
        this.phoneNo = page.locator("input[placeholder='Phone Number']");
        this.finishBtn = page.locator("button[type='submit']");
    }

    public void verifyCreateAccountPage() {
        assertEquals(page.url(), domain + "/createAccountComplete/cu");
        log.info("Create Account Page is displayed");
    }

    public void createAccount(String name, String password, String country, String phoneNo) {
        this.name.fill(name);
        this.password.fill(password);
        this.country.selectOption(country);
        this.phoneNo.fill(phoneNo);
        page.waitForNavigation( () ->{
            this.finishBtn.click();
        });
      log.info("Account created successfully");
    }
}
