package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.TipPaymentPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class TipPaymentTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "Tip-payment")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] tipData = new Object[1][2];
        tipData[0][0] = excelData[0][0];
        tipData[0][1] = excelData[0][1];

        return tipData;
    }

    @Test(dataProvider = "Tip-payment")
    public void tipPayment(String tip, String msg) {
        try{
            Page page = getPage();
            String domain = getDomain();
            TipPaymentPage tipPaymentPage = new TipPaymentPage(page, domain);
            tipPaymentPage.verifyTipPaymentPage();
            tipPaymentPage.enterTipAmount(tip,msg);
//            tipPaymentPage.skipTip();
        }catch (PlaywrightException e){
            log.error("Error occurred during tip payment: {}", e.getMessage());
            assertFalse(true, "Test failed due to an exception");
        }catch(Exception e){
            log.error("An unexpected error occurred: {}", e.getMessage());
            assertFalse(true, "Test failed due to an unexpected error");
        }
    }
}
