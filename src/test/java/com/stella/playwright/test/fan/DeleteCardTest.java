package com.stella.playwright.test.fan;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.fan.AddCardPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class DeleteCardTest extends BaseTest {
    @Test
    public void deleteCardTest() {
     try{
         String domain = getDomain();
         Page page = getPage();
         AddCardPage deleteCard = new AddCardPage(page,domain);
         deleteCard.navigate();
         deleteCard.deleteCard();
     }catch(PlaywrightException e){
         log.error("Error occurred while deleting card: {}", e.getMessage());
         assertFalse(true,"Asserts failed due to error: " + e.getMessage());
     }catch(Exception e){
         log.error("An unexpected error occurred: {}", e.getMessage());
        assertFalse(true,"Asserts failed due to error: " + e.getMessage());
     }
    }
}
