package com.stella.playwright.test.creator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import com.stella.playwright.page.creator.UpdateProfilePage;
import com.stella.playwright.util.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

@Slf4j
public class UpdateProfileTest extends BaseTest {
    String sheetName;

    @Parameters({"sheetName"})
    @BeforeClass
    public void setUp(String sheetName) {
        this.sheetName = sheetName;
    }

    @DataProvider(name = "profile-data")
    public Object[][] readExcel() {
        String filePath = getFilePath();
        Object[][] excelData = ExcelReader.readExcelBySheetName(filePath,sheetName);
        Object[][] profileData = new Object[1][8];
        profileData[0][0] = excelData[0][0];
        profileData[0][1] = excelData[0][1];
        profileData[0][2] = excelData[0][2];
        profileData[0][3] = excelData[0][3];
        profileData[0][4] = excelData[0][4];
        profileData[0][5] = excelData[0][5];
        profileData[0][6] = excelData[0][6];
        profileData[0][7] = excelData[0][7];

        return profileData;
    }

    @Test(dataProvider = "profile-data")
    public void updateProfileTest(String filePath, String name, String bio, String password, String re_password, String minPrice, String title, String url) {
        try{
            String domain = getDomain();
            Page page = getPage();
            UpdateProfilePage updatesProfilePage = new UpdateProfilePage(page,domain);
            updatesProfilePage.navigate();
            updatesProfilePage.updateProfile(filePath, name, bio, password , re_password, minPrice, title, url);
        }catch(PlaywrightException e){
            log.error("Error occurred during the test: {}", e.getMessage());
            assertFalse(true,e.getMessage());
        }catch(Exception e){
            log.error("An unexpected error occurred: {}", e.getMessage());
            assertFalse(true,e.getMessage());
        }
    }
}
