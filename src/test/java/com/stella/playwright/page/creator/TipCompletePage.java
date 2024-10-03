package com.stella.playwright.page.creator;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import static org.testng.Assert.assertEquals;

@Slf4j
public class TipCompletePage {
    private final Page page;
    private final Locator finishBtn;
    private final String domain;

    public TipCompletePage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
        this.finishBtn = page.locator("button[type='button']");
    }

    public void verifyTipCompletePage() {
        assertEquals(page.url(), domain + "/tipComplete/cu");
        log.info("Tip Complete Page is displayed");
    }

    public void clickFinishBtn() {
        page.waitForNavigation( () -> {
            this.finishBtn.click();
        });
        log.info("Finish button is clicked");
    }

}
