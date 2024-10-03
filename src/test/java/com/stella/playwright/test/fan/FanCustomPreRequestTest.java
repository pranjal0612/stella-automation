package com.stella.playwright.test.fan;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.fan.FanCustomRequestPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class FanCustomPreRequestTest extends BaseTest{
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "CreateCustomRequestFan")
    public Object[][] readExcel(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] createCustomRequestFan = new Object[1][7];
        createCustomRequestFan[0][0] = excelData[0][0];
        createCustomRequestFan[0][1] = excelData[0][1];
        createCustomRequestFan[0][2] = excelData[0][2];
        createCustomRequestFan[0][3] = excelData[0][3];
        createCustomRequestFan[0][4] = excelData[0][4];
        createCustomRequestFan[0][5] = excelData[0][5];
        createCustomRequestFan[0][6] = excelData[0][6];

        return createCustomRequestFan;
    }

    @Test(dataProvider = "CreateCustomRequestFan")
    public void createCustomPreRequestFanTest(String title,String amount, String cardNumber, String expiryDate, String cvc, String cardHolder,String country) {
        try{
            String domain = getDomain();
            Page page = getPage();
            FanCustomRequestPage fanCustomRequestPage = new FanCustomRequestPage(page, domain);
            fanCustomRequestPage.navigate();
           fanCustomRequestPage.createPreRequest(title, cardNumber, expiryDate, cvc, cardHolder, country);
        } catch (PlaywrightException e) {
            log.error("Error in createCustomPreRequestFanTest: ", e.getMessage());
            assertFalse(true,e.getMessage());
        }catch (Exception e) {
            log.error("An unexpected error occurred in createCustomPreRequestFanTest: ", e.getMessage());
            assertFalse(true,e.getMessage());
        }
    }
}
