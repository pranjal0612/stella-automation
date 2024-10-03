package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.ManageFriendsPage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class ManageFriendsTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "manageFriends-data")
    public Object[][] readExcel(){
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath, sheetName);
        Object[][] manageFriendsData = new Object[1][2];
        manageFriendsData[0][0] = excelData[0][0];
        manageFriendsData[0][1] = excelData[0][1];

        return manageFriendsData;
    }

    @Test(dataProvider = "manageFriends-data")
    public void testManageFriends(String name, String message) {
        try{
            String domain = getDomain();
            Page page = getPage();
            ManageFriendsPage manageFriendsPage = new ManageFriendsPage(page, domain);
            manageFriendsPage.navigate();
            manageFriendsPage.searchFriend(name);
            manageFriendsPage.doFlag();
            manageFriendsPage.sendMessage(message);
            manageFriendsPage.addMember();
           manageFriendsPage.blockFriend();
            manageFriendsPage.removeFriend();
        } catch (PlaywrightException e) {
            log.error("Error in testManageFriends: ", e.getMessage());
            assertFalse(true, "Test failed due to an exception: " + e.getMessage());
        } catch(Exception e){
            log.error("Error occurred: {}", e.getMessage());
            assertFalse(true, "Test failed due to an error: " + e.getMessage());
        }
    }
}
