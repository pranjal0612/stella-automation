package com.stella.playwright.test.fan;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.fan.FanInstagramCloseFriendsSignupPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class FanInstagramCloseFriendsTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "instagram-data")
    public Object[][] readExcel(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] fanSignUp = new Object[1][3];
        fanSignUp[0][0] = excelData[0][0];
        fanSignUp[0][1] = excelData[0][1];
        fanSignUp[0][2] = excelData[0][2];

        return fanSignUp;
    }

    @Test(dataProvider = "instagram-data")
    public void fanInstagramCloseFriendsTest(String url, String Handle, String email , ITestContext context) {
        try{
            String domain = getDomain();
            Page page = getPage();
            FanInstagramCloseFriendsSignupPage fanInstagramPage = new FanInstagramCloseFriendsSignupPage(page,domain);
            fanInstagramPage.verifyInstagramCloseFriendsPage(url);
            fanInstagramPage.setValue(Handle,email,context);
        }catch (PlaywrightException e){
            log.error("Error occurred: {}", e.getMessage());
            assertFalse(true, "Test failed due to an exception");
        }catch (Exception e){
            log.error("Error occurred while testing fanInstagramCloseFriendsTest :: {}", e.getMessage());
            assertFalse(true, "Test failed due to an exception");
        }
    }
}
