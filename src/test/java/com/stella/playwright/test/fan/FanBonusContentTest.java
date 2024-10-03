package com.stella.playwright.test.fan;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.fan.FanBonusContentPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class FanBonusContentTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "bonusContent-data")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData =  ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] bonusContent = new Object[1][2];
        bonusContent[0][0] = excelData[0][0];
        bonusContent[0][1] = excelData[0][1];

        return bonusContent;
    }

    @Test(dataProvider = "bonusContent-data")
    public void testBonusContent(String cardNumber, String Cvv) {
        try{
            String domain = getDomain();
            Page page = getPage();
            FanBonusContentPage fanBonusContentPage = new FanBonusContentPage(page, domain);
            fanBonusContentPage.navigate();
            fanBonusContentPage.setValue(cardNumber, Cvv);
        }catch (PlaywrightException e){
            log.error("Error occurred while adding bonus content: {}", e.getMessage());
            assertFalse(true, e.getMessage());
        }catch (Exception e){
            log.error("An unexpected error occurred while adding bonus content: {}", e.getMessage());
            assertFalse(true, e.getMessage());
        }
    }
}
