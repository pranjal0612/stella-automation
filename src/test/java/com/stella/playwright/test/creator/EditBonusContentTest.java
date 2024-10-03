package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.EditBonusContentPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class EditBonusContentTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "edit-data")
    public Object[][] getData() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] editBonus = new Object[1][2];
        editBonus[0][0] = excelData[0][0];
        editBonus[0][1] = excelData[0][1];

        return editBonus;
    }

    @Test(dataProvider = "edit-data")
    public void editBonusContentTest(String descriptionText, String price) {
        try{
            String domain = getDomain();
            Page page = getPage();
            EditBonusContentPage editBonusContentPage = new EditBonusContentPage(page, domain);
            editBonusContentPage.navigate();
            editBonusContentPage.edit(descriptionText, price);
            log.info("Edit bonus content test passed");
        }catch(PlaywrightException e){
            log.error("Edit bonus content test failed: " + e.getMessage());
            assertFalse(true,e.getMessage());
        }catch(Exception e){
            log.error("Edit bonus content test failed: " + e.getMessage());
            assertFalse(true,e.getMessage());
        }
    }
}
