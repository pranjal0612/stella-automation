package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.TipCompletePage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class TipCompleteTest extends BaseTest {
    @Test
    public void tipCompleteTest() {
        try{
            String domain = getDomain();
            Page page = getPage();
            TipCompletePage completePage = new TipCompletePage(page,domain);
            completePage.verifyTipCompletePage();
            completePage.clickFinishBtn();
        }catch(PlaywrightException e){
            log.error("TipCompleteTest error:{}",e.getMessage());
            assertFalse(true,e.getMessage());
        }catch(Exception e){
            log.error("Error occurred while TipCompleteTest :{}",e.getMessage());
            assertFalse(true,e.getMessage());
        }
    }
}
