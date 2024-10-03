package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.AddBonusContentPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class AddBonusContentTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "bonusContent-data")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] bonusContentData = new Object[1][3];
        bonusContentData[0][0] = excelData[0][0];
        bonusContentData[0][1] = excelData[0][1];
        bonusContentData[0][2] = excelData[0][2];

        return bonusContentData;
    }

    @Test(dataProvider = "bonusContent-data")
    public void bonusContentTest(String filePath, String description, String price) {
        try{
            String domain = getDomain();
            Page page = getPage();
            AddBonusContentPage bonusContentPage = new AddBonusContentPage(page,domain);
            bonusContentPage.navigate();
            bonusContentPage.setValue(filePath, description, price);
        }catch (PlaywrightException e){
            log.error("Error occurred: " + e.getMessage());
            assertFalse(true, e.getMessage());
        }catch(Exception e){
            log.error("Error occurred while bonusContent test: " + e.getMessage());
            assertFalse(true, e.getMessage());
        }
    }
}
