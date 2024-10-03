package com.stella.playwright.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.LoginPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class LoginFailedTest extends BaseTest {
    ObjectMapper mapper;
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName){
        mapper = new ObjectMapper();
        this.sheetName = sheetName;
    }

    @DataProvider(name = "login-failed-data")
    public Object[][] loginFailedData(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath,sheetName);
        Object[][] loginFailed = new Object[1][2];
        loginFailed[0][0] = excelData[1][0];
        loginFailed[0][1] = excelData[1][1];
        return loginFailed;
    }

    @Test(dataProvider = "login-failed-data")
    void loginTestFailed(String email, String password, ITestContext context){
        try{
            String domain = getDomain();
            Page page = getPage();

            LoginPage loginPage = new LoginPage(page,domain);
            loginPage.navigate();
            loginPage.setValue(email, password);
            loginPage.loginFailed(context);
        } catch (PlaywrightException e) {
            log.error("Playwright Error: " + e.getMessage());
            assertFalse(true);
        } catch (Exception e) {
            log.error("error while testing login page :: ", e);
            assertFalse(true);
        }
    }
}
