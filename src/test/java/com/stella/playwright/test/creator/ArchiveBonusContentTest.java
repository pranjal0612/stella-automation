package com.stella.playwright.test.creator;


import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.EditBonusContentPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class ArchiveBonusContentTest extends BaseTest {

    @Test
    public void archiveBonusContentTest() {
        try{
            String domain = getDomain();
            Page page = getPage();
            EditBonusContentPage archiveBonus = new EditBonusContentPage(page, domain);
            archiveBonus.navigate();
            archiveBonus.archive();
            log.info("Archive Bonus Content Test");
        }catch (PlaywrightException e){
            log.error("Archive Bonus Content Test Failed",e.getMessage());
            assertFalse(true,e.getMessage());
        }catch (Exception e){
            log.error("Archive Bonus Content Test Failed",e.getMessage());
            assertFalse(true,e.getMessage());
        }

    }
}
