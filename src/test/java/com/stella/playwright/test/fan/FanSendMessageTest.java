package com.stella.playwright.test.fan;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.fan.FanChatPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class FanSendMessageTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "fanMessage-data")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] fanMessageData = new Object[1][4];
        fanMessageData[0][0] = excelData[0][0];
        fanMessageData[0][1] = excelData[0][1];
        fanMessageData[0][2] = excelData[0][2];
        fanMessageData[0][3] = excelData[0][3];

        return fanMessageData;
    }

    @Test(dataProvider = "fanMessage-data")
    public void fanMessageTest(String userName, String message, String cardNumber, String cvv) {
        try{
            String domain = getDomain();
            Page page = getPage();
            FanChatPage messagePage = new FanChatPage(page,domain);
            messagePage.navigate();
            messagePage.sendMessageByFan(userName,message,cardNumber,cvv);
            messagePage.payAndView();
        }catch(PlaywrightException e){
            log.error("fanMessageTest error:{}",e.getMessage());
            assertFalse(true,e.getMessage());
        }catch(Exception e){
            log.error("fanMessageTest error:{}",e.getMessage());
            assertFalse(true,e.getMessage());
        }
    }

}
