package com.stella.playwright.master;

import org.testng.annotations.Test;
import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import static org.testng.Assert.fail;

@Slf4j
class AfterMasterSuiteTest {

	@Test
	public void afterMasterSuite() {
		try {
			BaseTest base = new BaseTest();
			base.setUpAfterSuite();
			log.info("After master suite executed successfully");
		} catch (PlaywrightException e) {
			log.error("Playwright Error: {}", e.getMessage(), e);
			fail("Test failed due to PlaywrightException: " + e.getMessage());
		} catch (Exception e) {
			log.error("Error during After master suite:: ", e);
			fail("Test failed due to an unexpected exception.");
		}
	}
}
