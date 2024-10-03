package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.BillingPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class BillingTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "billing-data")
    public Object[][] readExcel(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] billingData = new Object[1][6];
        billingData[0][0] = excelData[0][0];
        billingData[0][1] = excelData[0][1];
        billingData[0][2] = excelData[0][2];
        billingData[0][3] = excelData[0][3];
        billingData[0][4] = excelData[0][4];
        billingData[0][5] = excelData[0][5];

        return billingData;
    }

    @Test(dataProvider = "billing-data")
    public void testBilling(String bankName, String accountNumber, String routingNumber, String bankContractNumber, String re_accountNumber, String re_routingNumber) {
        try{
            String domain = getDomain();
            Page page = getPage();
            BillingPage billing = new BillingPage(page, domain);
            billing.navigate();
            billing.enterBankDetails(bankName, accountNumber, routingNumber, bankContractNumber, re_accountNumber, re_routingNumber);
        }catch(PlaywrightException e){
            log.error("Error occurred during the test: {}", e.getMessage());
            assertFalse(true,e.getMessage());
        }catch(Exception e){
            log.error("An unexpected error occurred: {}", e.getMessage());
            assertFalse(true,e.getMessage());
        }
    }
}
