package com.stella.playwright.base;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseTest {

    private static Playwright playwright;
    private static Browser browser;
    private static Page page;
    private static String domain;
    private static String filePath;

    public Playwright getPlaywright() {
        playwright = Playwright.create();
        return playwright;
    }

    public Browser getBrowser(String browserName, boolean isHeadless) {
        Playwright playwright = getPlaywright();

        Browser browser = null;
        try {
            if (browserName.equals("chrome"))
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
            else if (browserName.equals("msedge"))
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));
            else if (browserName.equals("firefox"))
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setChannel("firefox").setHeadless(false));
            else browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        } catch (Exception e) {
            log.error("Failed to launch browser", e);
        }
        return browser;
    }

    public Page getPage(Browser browser, String url) {
        if (browser == null || isEmpty(url)) return null;
        try {
            BrowserContext browserContext = browser.newContext();
            browserContext.clearCookies();
            browserContext.clearCookies();
            Page page = browserContext.newPage();
            page.setDefaultTimeout(60000);
            page.navigate(url);
            log.info("successfully navigated to {}", url);
            return page;
        } catch (Exception e) {
            log.error("Failed to create page or navigate to URL", e);
            return null;
        }
    }

    public void close() {
        try {
            if (page != null) {
                page.close();
            }
            if (browser != null) {
                browser.close();
            }
            if (playwright != null) {
                playwright.close();
            }
        } catch (Exception e) {
            log.error("Error while closing resources", e);
        }
    }

    //	@BeforeSuite
    public void setUpSuite(String filePath) throws IOException {
        setFilePath(filePath);
        Properties prop = new Properties();

        FileInputStream fis = new FileInputStream("./src/test/resources/config/preSuite.properties");
        prop.load(fis);

        String browser = prop.getProperty("com.stella.browser");
        String domain = prop.getProperty("com.stella.domain");
        String isHeadlessValue = prop.getProperty("com.stella.isHeadless");
        boolean isHeadless = StringUtils.equalsIgnoreCase(isHeadlessValue, "true") ? true : false;
        log.info("before suite {}", domain);

        Browser browserObj = getBrowser(browser, isHeadless);
        page = getPage(browserObj, domain);
        setDomain(domain);
    }

    public void setUpAfterSuite() {
        log.info("after suite");
        close();
    }

    public static Page getPage() {
        return page;
    }

    public static void setDomain(String domain) {
        BaseTest.domain = domain;
    }

    public static String getDomain() {
        return domain;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        BaseTest.filePath = filePath;
    }

    public static String takeScreenshot(String testName) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String timestamp = LocalDateTime.now().format(formatter);

            String path = System.getProperty("user.home") + "/screenshot/" + testName + "_" + timestamp + ".png";

            java.nio.file.Path screenshotDir = Paths.get(System.getProperty("user.home") + "/screenshot/");
            if (!exists(screenshotDir)) {
                createDirectories(screenshotDir);
            }

            byte[] buffer = getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
            String base64Path = Base64.getEncoder().encodeToString(buffer);

            log.info("Screenshot taken successfully at: " + path);
            return base64Path;
        } catch (Exception e) {
            log.error("Failed to take screenshot", e);
            return null;
        }
    }


}