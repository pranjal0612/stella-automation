package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.CustomRequestOptionsPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class CustomRequestOptionsTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "custom-data")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] customData = new Object[1][2];
        customData[0][0] = excelData[0][0];
        customData[0][1] = excelData[0][1];

        return customData;
    }

    @Test(dataProvider = "custom-data")
    public void testCustomRequestOptions(String title, String amount) {
        try{
            String domain = getDomain();
            Page page = getPage();
            CustomRequestOptionsPage customRequestOptions = new CustomRequestOptionsPage(page, domain);
            customRequestOptions.navigate();
            customRequestOptions.setValue(title, amount);
        }catch(PlaywrightException e){
            log.error("Error occurred: {}", e.getMessage());
            assertFalse(true, "Test failed due to an exception: " + e.getMessage());
        }catch(Exception e){
            log.error("Error occurred: {}", e.getMessage());
            assertFalse(true, "Test failed due to an exception: " + e.getMessage());
        }
    }

}
