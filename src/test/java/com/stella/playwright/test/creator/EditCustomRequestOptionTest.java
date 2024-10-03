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
public class EditCustomRequestOptionTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "customEdit-data")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] editCustom = new Object[1][4];
        editCustom[0][0] = excelData[0][0];
        editCustom[0][1] = excelData[0][1];
        editCustom[0][2] = excelData[0][2];
        editCustom[0][3] = excelData[0][3];

        return editCustom;
    }

    @Test(dataProvider = "customEdit-data")
    public void editCustomRequestTest(String title, String amount, String newTitle, String newAmount) {
     try{
         String domain = getDomain();
         Page page = getPage();
         CustomRequestOptionsPage editCustomRequest = new CustomRequestOptionsPage(page,domain);
         editCustomRequest.navigate();
         editCustomRequest.editCustomRequestOption(title, amount, newTitle, newAmount);
     }catch(PlaywrightException e){
         log.error("Error occurred while editing custom request option: {}", e.getMessage());
         assertFalse(true, "Test failed due to an exception");
     }catch(Exception e){
         log.error("An unexpected error occurred: {}", e.getMessage());
        assertFalse(true, "Test failed due to an unexpected error");
     }
    }
}
