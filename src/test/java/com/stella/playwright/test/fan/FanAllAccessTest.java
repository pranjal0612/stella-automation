package com.stella.playwright.test.fan;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.fan.FanAllAccessSignupPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class FanAllAccessTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "allAccess-data")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] fanSignUp = new Object[1][2];
        fanSignUp[0][0] = excelData[0][0];
        fanSignUp[0][1] = excelData[0][1];

        return fanSignUp;
    }

    @Test(dataProvider = "allAccess-data")
    void fanSignUpTest(String url, String email, ITestContext context) {
        try{
            String domain = getDomain();
            Page page = getPage();
            FanAllAccessSignupPage fanAllAccess = new FanAllAccessSignupPage(page, domain);
            fanAllAccess.verifyAllAccessPage(url);
            fanAllAccess.setValue(email,context);
        }catch(PlaywrightException e){
            log.error("Error occurred: {}", e.getMessage());
            assertFalse(true,e.getMessage());
        }catch(Exception e){
            log.error("Error occurred while testing fanSignUpTest ::  {}", e.getMessage());
            assertFalse(true,e.getMessage());
        }
    }
}
