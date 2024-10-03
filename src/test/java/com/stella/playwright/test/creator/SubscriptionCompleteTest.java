package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.SubscriptionCompletePage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class SubscriptionCompleteTest extends BaseTest {
    @Test
    public void subscriptionCompleteTest() {
        try{
            String domain = getDomain();
            Page page = getPage();
            SubscriptionCompletePage completePage = new SubscriptionCompletePage(page,domain);
            completePage.verifySubscriptionCompletePage();
            completePage.clickContinueButton();
        }catch(PlaywrightException e){
            log.error("Error occurred:" + e.getMessage());
            assertFalse(true,e.getMessage());
        }catch(Exception e){
            log.error("Error occurred while executing subscriptionCompleteTest: " + e.getMessage());
            assertFalse(true,e.getMessage());
        }
    }
}
