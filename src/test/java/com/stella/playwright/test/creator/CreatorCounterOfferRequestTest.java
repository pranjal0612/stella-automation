package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.CreatorCustomRequestPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class CreatorCounterOfferRequestTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "counterOffer-data")
    public Object[][] readExcel(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] counterOfferData = new Object[1][2];
        counterOfferData[0][0] = excelData[0][0];
        counterOfferData[0][1] = excelData[0][1];

        return counterOfferData;
    }

    @Test(dataProvider = "counterOffer-data")
    public void testCounterOfferRequest(String title, String amount) {
        try{
            String domain = getDomain();
            Page page = getPage();
            CreatorCustomRequestPage counterOfferRequestPage = new CreatorCustomRequestPage(page, domain);
            counterOfferRequestPage.navigate();
            counterOfferRequestPage.counterOfferRequest(title, amount);
        } catch (PlaywrightException e) {
            log.error("Error in testCounterOfferRequest: {}", e.getMessage());
            assertFalse(true, "Test failed due to an error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Test failed: {}", e.getMessage());
            assertFalse(true, "Test failed due to an exception: " + e.getMessage());
        }
    }
}

