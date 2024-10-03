package com.stella.playwright.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.SignUpPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class SignEmailValidationTest extends BaseTest {
    ObjectMapper mapper;
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName){
        mapper = new ObjectMapper();
        this.sheetName = sheetName;
    }
    @DataProvider(name = "signup-data")
    public Object[][] readExcel(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath,sheetName);

        Object[][] signUp = new Object[1][6];
        signUp[0][0] = excelData[2][0];
        signUp[0][1] = excelData[2][1];
        signUp[0][2] = excelData[2][2];
        signUp[0][3] = excelData[2][3];
        signUp[0][4] = excelData[2][4];
        signUp[0][5] = (int) Double.parseDouble(excelData[2][5].toString());

        return signUp;
    }

    @Test(dataProvider = "signup-data")
    void signUpEmailValidationTest(String filePath, String name, String url, String email, String password,int price){
        try{
            String domain = getDomain();
            Page page = getPage();
            SignUpPage signUpPage = new SignUpPage(page,domain);
            signUpPage.navigate();
            signUpPage.setValue(filePath, name, url, email, password);
            signUpPage.emailValidation();
        }catch (PlaywrightException e) {
            log.error("Playwright Error: " + e.getMessage());
            assertFalse(true, e.getMessage());
        } catch (Exception e) {
            log.error("error while testing signUpEmailValidationTest :: ", e);
            assertFalse(true);
        }
    }
}
