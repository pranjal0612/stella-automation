package com.stella.playwright.page.fan;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

@Slf4j
public class FanAccountSettingPage {
    private final Page page;
    private final Locator name;
    private final Locator handle;
    private final Locator password;
    private final Locator re_password;
    private final Locator saveBtn;
    private final String domain;

    public FanAccountSettingPage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.name = page.locator("input[placeholder=\"Example: Adam\"]");
        this.handle = page.locator("(//input[@placeholder='Example: myemail@com'])[1]");
        this.password = page.locator("input[placeholder='Update password']");
        this.re_password = page.locator("input[placeholder='Re-enter new password']");
        this.saveBtn = page.locator("button[class='btn rounded-pill text-dark gradient-a btn-lg w-100']");
    }

    public void navigate() {
        page.navigate(domain + "/customerSettings/cu");
        page.waitForLoadState();
        assertEquals(page.url(),domain + "/customerSettings/cu");
        log.info("Fan Account Setting Page is displayed");
    }

    public void updateProfile(String name, String handle, String password, String re_password) {
        page.waitForTimeout(3000);
        this.name.fill("");
        this.name.fill(name);
        this.handle.fill(handle);
        this.password.fill(password);
        this.re_password.fill(re_password);
        page.waitForTimeout(2000);

        this.saveBtn.click();
        page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
        String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent();
        assertEquals(alertText, " Saved successfully ", "Alert text does not match.");
        log.info("Alert text verification successful.");
        log.info("Profile updated successfully");
    }
}
