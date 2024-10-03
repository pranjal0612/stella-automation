package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.OnlineCheckoutStripePage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class OnlineCheckoutStripeTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "paymentData")
    public Object[][] readExcel() {
       String filePath = getFilePath();
       Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
       Object[][] payment = new Object[1][5];
       payment[0][0] = excelData[0][0];
       payment[0][1] = excelData[0][1];
       payment[0][2] = excelData[0][2];
       payment[0][3] = excelData[0][3];
       payment[0][4] = excelData[0][4];

       return payment;
    }

    @Test(dataProvider = "paymentData")
    public void onlineStripeTest(String cardNumber, String cardExpiry, String cvv, String cardHolderName, String country){
        try{
            String domain = getDomain();
            Page page = getPage();
            OnlineCheckoutStripePage stripePage = new OnlineCheckoutStripePage(page, domain);
            stripePage.verifyCheckoutPage();
            stripePage.enterCardDetails(cardNumber, cardExpiry, cvv, cardHolderName, country);
        }catch(PlaywrightException e){
            log.error("Error occurred: " + e.getMessage());
            assertFalse(true, "Test failed due to an error");
        }catch(Exception e){
            log.error("An unexpected error occurred: " + e.getMessage());
            assertFalse(true, "Test failed due to an unexpected error");
        }

    }
}
