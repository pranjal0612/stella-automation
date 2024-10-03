package com.stella.playwright.test.creator;


import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.EditBonusContentPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class DeleteBonusContentTest extends BaseTest {

    @Test
    public void deleteBonusContentTest() {
        try{
            String domain = getDomain();
            Page page = getPage();
            EditBonusContentPage deleteBonus = new EditBonusContentPage(page, domain);
            deleteBonus.navigate();
            deleteBonus.delete();
            log.info("Delete Bonus Content Test");
        }catch (PlaywrightException e){
            log.error("Delete Bonus Content Test Failed",e.getMessage());
            assertFalse(true,e.getMessage());
        }catch (Exception e){
            log.error("Delete Bonus Content Test Failed",e.getMessage());
            assertFalse(true,e.getMessage());
        }

    }
}
