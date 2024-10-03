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
public class LoginSuccessTest extends BaseTest {
    ObjectMapper mapper;
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName){
        mapper = new ObjectMapper();
        this.sheetName = sheetName;
    }

    @DataProvider(name = "logic-success-data")
    public Object[][]readExcel(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath,sheetName);
        Object[][] logicSuccess = new Object[1][2];
        logicSuccess[0][0] = excelData[0][0];
        logicSuccess[0][1] = excelData[0][1];
        return logicSuccess;
    }

    @Test(dataProvider = "logic-success-data")
    public void logicTestSuccess(String email, String password, ITestContext context){
        try{
            String domain = getDomain();
            Page page = getPage();

            LoginPage loginPage = new LoginPage(page,domain);
            loginPage.navigate();
            loginPage.setValue(email, password);
            loginPage.login(context);

            log.info("successfully logged in stella !!");
        }catch (PlaywrightException e){
            log.error("Playwright Error: " + e.getMessage());
            assertFalse(true);
        }catch (Exception e){
            log.error("error while testing login page :: ", e);
            assertFalse(true);
        }
    }
}
