package com.stella.playwright.listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import lombok.extern.slf4j.Slf4j;

import static com.stella.playwright.base.BaseTest.takeScreenshot;

@Slf4j
public class StellaListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test success: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test Failed: " + result.getName());
        log.error("Error: " + result.getThrowable().getMessage());
        takeScreenshot(result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test Skipped: " + result.getName());
        takeScreenshot(result.getName());
    }
}
