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
public class DeleteCustomRequestOptionsTest extends BaseTest {
    String sheetName;
    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "deleteCustom-data")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] deleteData = new Object[1][2];
        deleteData[0][0] = excelData[0][0];
        deleteData[0][1] = excelData[0][1];

        return deleteData;
    }

    @Test(dataProvider = "deleteCustom-data")
    public void deleteCustomRequestOptions(String title, String amount) {
        try{
            String domain = getDomain();
            Page page = getPage();
            CustomRequestOptionsPage customRequestOptionsPage = new CustomRequestOptionsPage(page,domain);
            customRequestOptionsPage.navigate();
            customRequestOptionsPage.deleteCustomRequestOption(title, amount);
        }catch (PlaywrightException e){
            log.error("Error occurred during test execution: {}", e.getMessage());
            assertFalse(true, "Test failed due to an exception: " + e.getMessage());
        }catch(Exception e){
            log.error("An unexpected error occurred: {}", e.getMessage());
            assertFalse(true, "Test failed due to an unexpected error: " + e.getMessage());
        }
    }
}
