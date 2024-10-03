package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.CreatorCustomRequestPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import static org.testng.Assert.assertFalse;

@Slf4j
public class CreatorCustomRequestTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "customRequest")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] customRequestData = new Object[1][4];
        customRequestData[0][0] = excelData[0][0];
        customRequestData[0][1] = excelData[0][1];
        customRequestData[0][2] = excelData[0][2];
        customRequestData[0][3] = excelData[0][3];

        return customRequestData;
    }

    @Test(dataProvider = "customRequest")
    public void testCustomRequest(String filePath, String link, String title, String amount) {
        try {
            String domain = getDomain();
            Page page = getPage();
            CreatorCustomRequestPage creatorCustomRequestPage = new CreatorCustomRequestPage(page, domain);
            creatorCustomRequestPage.navigate();
            creatorCustomRequestPage.readyRequest(filePath, link);
            creatorCustomRequestPage.reviewRequest(filePath, link);
        } catch (PlaywrightException e) {
            log.error("Playwright Error: {}", e.getMessage());
            assertFalse(true, "Test failed due to an error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Test failed: {}", e.getMessage());
            assertFalse(true, "Test failed due to an exception: " + e.getMessage());
        }
    }
}
