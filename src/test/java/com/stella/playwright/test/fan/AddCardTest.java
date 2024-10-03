package com.stella.playwright.test.fan;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.fan.AddCardPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class AddCardTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "card-data")
    public Object[][] readExcel(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] cardData = new Object[1][2];
        cardData[0][0] = excelData[0][0];
        cardData[0][1] = excelData[0][1];

        return cardData;
    }

    @Test(dataProvider = "card-data")
    public void addCardTest(String cardNumber, String cvv) {
        try{
            String domain = getDomain();
            Page page = getPage();
            AddCardPage cardPage = new AddCardPage(page,domain);
            cardPage.navigate();
            cardPage.setValue(cardNumber, cvv);
//            cardPage.deleteCard();
        }catch(PlaywrightException e){
            log.error("Error occurred: {}", e.getMessage());
            assertFalse(true,"Test failed due to an error");
        }catch(Exception e){
            log.error("An unexpected error occurred: {}", e.getMessage());
            assertFalse(true,"Test failed due to an unexpected error");
        }
    }
}
