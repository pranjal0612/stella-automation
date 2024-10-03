package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.SubscriptionPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class SubscriptionTest extends BaseTest {
    @Test
    public void subscribeToPlan() {
        try{
            Page page = getPage();
            String domain = getDomain();
            SubscriptionPage subscriptionPage  = new SubscriptionPage(page,domain);
            subscriptionPage.navigateToSubscriptionPage();
            subscriptionPage.doSubscription();
        }catch(PlaywrightException e){
            log.error("Error occurred : {}", e.getMessage());
            assertFalse(true, e.getMessage());
        }catch(Exception e){
            log.error("Error occurred while subscribing to plan: {}", e.getMessage());
            assertFalse(true, e.getMessage());
        }
    }
}
