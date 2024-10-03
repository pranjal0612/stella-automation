package com.stella.playwright.test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;
import com.stella.playwright.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Slf4j
public class LogoutTest extends BaseTest {

    @Test
    public void logoutTestSuccess() {
        Page page = getPage();
        try {
            page.waitForLoadState();

            page.click("//span[@class='hamburger-menu']//img");
            page.waitForTimeout(1000);
            page.click("//div[contains(text(),\"Logout\")]");
            log.info("Logout successfully executed.");
            page.waitForTimeout(3000);
            assertTrue(page.locator("//img[@class='img-fluid home-hero-img']").isVisible(), "Logout did not redirect to login page.");
        } catch (PlaywrightException e) {
            log.error("Playwright Error: " + e.getMessage());
            assertTrue(false, "Playwright error during logout.");
        } catch (Exception e) {
            log.error("Error during logout test: ", e);
            assertTrue(false, "Unexpected error during logout.");
        }
    }
}
