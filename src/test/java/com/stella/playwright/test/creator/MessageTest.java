package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.ChatPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class MessageTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "messageData")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] messageData = new Object[1][5];
        messageData[0][0] = excelData[0][0];
        messageData[0][1] = excelData[0][1];
        messageData[0][2] = excelData[0][2];
        messageData[0][3] = excelData[0][3];
        messageData[0][4] = excelData[0][4];

        return messageData;
    }

    @Test(dataProvider = "messageData")
    public void sendMessageTest(String userName, String message, String filePath, String description, String price) {
     try{
         String domain = getDomain();
         Page page = getPage();
         ChatPage messagesPage = new ChatPage(page,domain);
         messagesPage.navigate();
         messagesPage.sendMessage(userName,message);
         messagesPage.uploadFile(filePath,description,price);
     }catch(PlaywrightException e){
         log.error("An error occurred during the test: {}", e.getMessage());
         assertFalse(true, "Test failed due to an exception");
     }catch(Exception e){
         log.error("An unexpected error occurred during the test: {}", e.getMessage());
        assertFalse(true, "Test failed due to an unexpected exception");
     }
    }
}
