package com.stella.playwright.base;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasePage {

    protected static Page page;
    protected String domain;


    public BasePage(Page page, String domain) {
        this.page = page;
        this.domain = domain;
    }

    public String getCurrentUrl() {
        if (page != null) {
            String currentUrl = page.url();
            log.info("Current page URL: {}", currentUrl);
            return currentUrl;
        }
        log.error("Page is null. Cannot retrieve the current URL.");
        return null;
    }

    public Page getPage(){
        return page;
    }
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public static void maximizeWindow(Page page) {
        if (page != null) {
            int screenWidth = 1920;
            int screenHeight = 1080;
            page.setViewportSize(screenWidth, screenHeight);
            page.evaluate("window.resizeTo(" + screenWidth + ", " + screenHeight + ");");
            log.info("Browser window has been resized to cover the screen.");
        } else {
            log.error("Page is null. Cannot maximize the browser window.");
        }
    }



}

