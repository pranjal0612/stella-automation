package com.stella.playwright.master;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.microsoft.playwright.PlaywrightException;
import com.stella.playwright.base.BaseTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class BeforeMasterSuiteTest {
	
	@Parameters({"filePath"})
	@Test
	public void beforeMasterSuite(String filePath) {
		try {
			BaseTest base = new BaseTest();
			base.setUpSuite(filePath);
			
			log.info("Before master suite");
		} catch (PlaywrightException e) {
			log.error("Playwright Error: " + e.getMessage());
			assertFalse(true);
		} catch (Exception e) {
			log.error("error while Before master suite:: ", e);
			assertFalse(true);
		}
	}

}
