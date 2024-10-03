package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.AnnouncementPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class AnnouncementTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "announcement-data")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] announcementData = new Object[1][13];
        announcementData[0][0] = excelData[0][0];
        announcementData[0][1] = excelData[0][1];
        announcementData[0][2] = excelData[0][2];
        announcementData[0][3] = excelData[0][3];
        announcementData[0][4] = excelData[0][4];
        announcementData[0][5] = excelData[0][5];
        announcementData[0][6] = excelData[0][6];
        announcementData[0][7] = excelData[0][7];
        announcementData[0][8] = excelData[0][8];
        announcementData[0][9] = excelData[0][9];
        announcementData[0][10] = excelData[0][10];
        announcementData[0][11] = excelData[0][11];
        announcementData[0][12] = excelData[0][12];

        return announcementData;
    }

    @Test(dataProvider = "announcement-data")
    public void announcementTest(String filterName, String tippedAmount, String spentAmount, String subscribedOverNumber, String inActiveNumber,String editFilterName,String newFilterName,String deleteFilterName,String existsFilterName,String message,String filePath,String uploadMsg,String minPrice) {
        try {
            String domain = getDomain();
            Page page = getPage();
            AnnouncementPage announcementPage = new AnnouncementPage(page, domain);
            announcementPage.navigate();
            announcementPage.createAnnouncement(filterName, tippedAmount, spentAmount, subscribedOverNumber, inActiveNumber);
            announcementPage.editAnnouncement(editFilterName,newFilterName);
            announcementPage.deleteAnnouncement(deleteFilterName);
            announcementPage.sendMessageAnnouncement(existsFilterName, message, filePath, uploadMsg, minPrice);
        } catch (PlaywrightException e) {
            log.error("Error occurred while navigating to announcement page", e.getMessage());
            assertFalse(true, "Test failed due to an exception: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while creating announcement", e.getMessage());
            assertFalse(true, "Test failed due to an error: " + e.getMessage());
        }
    }
}
