package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.testng.Assert.assertEquals;

@Slf4j
public class UpdateProfilePage {
    private final Page page;
    private final Locator avatar;
    private final Locator name;
    private final Locator bio;
    private final Locator passwordField;
    private final Locator re_password;
    private final Locator signalSubscriptions;
    private final Locator minPrice;
    private final Locator title;
    private final Locator url;
    private final Locator save;
    private final String domain;

    public UpdateProfilePage(Page page , String domain) {
        this.page = page;
        this.avatar = page.locator("#file_upload");
        this.name = page.locator("input[placeholder=\"Example: Adam\"]");
        this.bio = page.locator("input[placeholder=\"Bio\"]");
        this.passwordField = page.locator("input[placeholder='Update password']");
        this.re_password = page.locator("input[placeholder=\"Re-enter new password\"]");
        this.signalSubscriptions = page.locator("//label[text()='Signal']");
        this.minPrice = page.locator("(//input[@type='number'])[2]");
        this.title = page.locator("(//input[@placeholder='Enter a Title'])[1]");
        this.url = page.locator("(//input[@placeholder='Enter URL'])[1]");
        this.save = page.locator("//button[normalize-space()=\"Save\"]");
        this.domain = domain;
    }

    public void navigate() {
        page.waitForNavigation( () ->{
            page.navigate(domain + "/settingsMobile/in");
        });
        page.waitForLoadState();
        assertEquals(page.url(), domain + "/settingsMobile/in");
        log.info("Navigated to Profile Setting Page");
    }

    public void updateProfile(String filePath, String name, String bio,String password, String re_password, String minPrice, String title, String url) {
        try {
            this.avatar.setInputFiles(Paths.get(filePath));
            page.waitForTimeout(2000);

            this.name.fill("");
            this.name.fill(name);
            this.bio.fill(bio);
            String passwordValue = passwordField.inputValue();
            if(passwordValue.isEmpty()){
                    this.passwordField.fill(password);
            }else{
                log.info("Password field is already filled.");
            }
            page.waitForTimeout(3000);
            this.re_password.fill(re_password);

            if (!signalSubscriptions.isChecked()) {
                signalSubscriptions.click();
                log.info("Signal Subscription was not checked, now it is.");
            } else {
                log.info("Signal Subscription is already checked.");
            }

            double minPriceValue;
            try {
                minPriceValue = Double.parseDouble(minPrice);
                if (minPriceValue >= 25) {
                   this.minPrice.fill(minPrice);
                   page.waitForTimeout(2000);
                }else{
                    log.warn("Minimum Price should be greater than or equal to 25. Provided: " + minPrice);
                    return;
                }
            } catch (NumberFormatException e) {
                log.error("Invalid minimum price format: " + minPrice);
                return;
            }
            this.title.fill(title);
            page.waitForTimeout(2000);
            this.url.fill(url);
            this.save.click();

            page.waitForSelector("//*[@id=\"global_error\"]/div", new Page.WaitForSelectorOptions().setTimeout(5000));
            String alertText = page.locator("//*[@id=\"global_error\"]/div").textContent().trim();
            if ("Saved successfully".equals(alertText)) {
                log.info("Profile Updated Successfully");
            } else {
                log.error("Profile update failed: " + alertText);
            }
            page.waitForTimeout(3000);
            //delete url
            page.click(".text-center.py-2 > span");
            log.info("Deleted URL");
            page.waitForTimeout(2000);
            this.save.click();
            assertEquals(alertText, "Saved successfully", "Alert text does not match.");

        } catch (Exception e) {
            log.error("Unexpected error occurred during profile update: " + e.getMessage());
        }
    }

}


