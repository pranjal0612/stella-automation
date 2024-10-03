package com.stella.playwright.test.fan;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.fan.FanSnapchatSignupPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Slf4j
public class FanSnapchatSignupTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName){
        this.sheetName = sheetName;
    }

    @DataProvider(name = "snapchat-data")
    public Object[][] readExcel(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath,sheetName);
        Object[][] snapchat = new Object[1][3];
        snapchat[0][0] = excelData[0][0];
        snapchat[0][1] = excelData[0][1];
        snapchat[0][2] = excelData[0][2];

        return snapchat;
    }

    @Test(dataProvider = "snapchat-data")
    public void snapchatSignupTest(String url, String handle, String email, ITestContext context){
        try{
            String domain = getDomain();
            Page page = getPage();
            FanSnapchatSignupPage snapchatSignup = new FanSnapchatSignupPage(page,domain);
            snapchatSignup.verifySnapchatPage(url);
            snapchatSignup.setValue(handle, email, context);
        }catch(PlaywrightException e){
            log.error("Error occurred: {}", e.getMessage());
            assertEquals(e.getMessage(),true);
        }catch (Exception e){
            log.error("Error occurred while testing snapchatSignupTest :: {}",e.getMessage());
            assertEquals(e.getMessage(),true);
        }
    }
}
