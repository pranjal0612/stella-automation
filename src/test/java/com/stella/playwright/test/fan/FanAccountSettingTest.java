package com.stella.playwright.test.fan;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.fan.FanAccountSettingPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class FanAccountSettingTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "accountSetting-data")
    public Object[][] readExcel() {
       String filePath = getFilePath();
       Object[][] excelData = ExcelReader.readExcelBySheetName(filePath,sheetName);
       Object[][] accountSettingData = new Object[1][4];
       accountSettingData[0][0] = excelData[0][0];
       accountSettingData[0][1] = excelData[0][1];
       accountSettingData[0][2] = excelData[0][2];
       accountSettingData[0][3] = excelData[0][3];

       return accountSettingData;
    }

    @Test(dataProvider = "accountSetting-data")
    public void testAccountSetting(String name, String handle, String password, String re_password) {
        try {
            String domain = getDomain();
            Page page = getPage();
            FanAccountSettingPage fanAccountSettingPage = new FanAccountSettingPage(page, domain);
            fanAccountSettingPage.navigate();
            fanAccountSettingPage.updateProfile(name, handle, password, re_password);
        } catch (PlaywrightException e) {
            log.error("Error occurred during the test: {}", e.getMessage());
            assertFalse(true, e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while updating profile: {}", e.getMessage());
            assertFalse(true, e.getMessage());
        }
    }
}
