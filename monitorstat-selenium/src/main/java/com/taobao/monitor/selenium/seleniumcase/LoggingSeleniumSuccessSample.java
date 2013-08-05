/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.seleniumcase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.taobao.monitor.selenium.util.log.DatabaseResultsFormatter;
import com.taobao.monitor.selenium.util.log.LogCommandProcessor;
import com.taobao.monitor.selenium.util.log.LogDefaultSelenium;
import com.taobao.monitor.selenium.util.log.LogResultsFormatter;
import com.taobao.monitor.selenium.util.log.LogSelenium;
import com.taobao.monitor.selenium.util.log.LogUtils;
import com.thoughtworks.selenium.HttpCommandProcessor;

public class LoggingSeleniumSuccessSample {
	
	protected LogSelenium selenium;

	private BufferedWriter loggingWriter;
	
	private static final String RESULT_FILE_ENCODING = "UTF-8";
	
	private static final String DEFAULT_TIMEOUT = "10";//ms
	
	private static final String OPENQA_URL = "https://www.google.com/";
	
	private static final String SCREENSHOT_PATH = "screenshots";
	
	private final String RESULTS_BASE_PATH = "target" + File.separator + "loggingResults";
	
	private String resultsPath = new File(RESULTS_BASE_PATH).getAbsolutePath();
	
	private String screenshotsResultsPath = new File(RESULTS_BASE_PATH + File.separator + SCREENSHOT_PATH).getAbsolutePath();
	
	@Before
	public void setUp() {
		if (!new File(screenshotsResultsPath).exists()) {
			new File(screenshotsResultsPath).mkdirs();
		}
		// to keep every result use a timestamped filename like this
		// final String resultHtmlFileName = resultsPath
		// + File.separator
		// + "autorunResult"
		// + LoggingUtils.timeStampForFileName()
		// + ".html";
		final String resultHtmlFileName = resultsPath + File.separator + "sampleResultSuccess.html";
		System.err.println("resultHtmlFileName=" + resultHtmlFileName);
		
		loggingWriter = LogUtils.createWriter(resultHtmlFileName, LoggingSeleniumSuccessSample.RESULT_FILE_ENCODING, true);
		
		LogResultsFormatter htmlFormatter = new DatabaseResultsFormatter();
		htmlFormatter.setScreenShotBaseUri(LoggingSeleniumSuccessSample.SCREENSHOT_PATH + "/"); // has to be "/" as this is a URI
		htmlFormatter.setAutomaticScreenshotPath(screenshotsResultsPath);
		LogCommandProcessor myProcessor = new LogCommandProcessor(new HttpCommandProcessor("localhost", 4444, "*chrome",
		OPENQA_URL), htmlFormatter);
		myProcessor.setExcludedCommands(new String[] {});
		selenium = new LogDefaultSelenium(myProcessor);
		selenium.start();
		selenium.windowMaximize();
	}
	
	@After
	public void tearDown() {
		selenium.stop();
		//selenium.close();
		try {
			if (null != loggingWriter) {
				loggingWriter.close();
			}
		} catch (IOException e) {
		// do nothing
		}
	}
	
	@Test
	public void loggingSeleniumSuccessSample() throws InterruptedException, ParseException {
		selenium.setContext("loggingSeleniumSuccessSample()");
		//selenium.waitForPageToLoad("10");
		selenium.open("/accounts/ServiceLogin?service=mail&passive=true&rm=false&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F%3Fui%3Dhtml%26zy%3Dl&bsv=llya694le36z&ss=1&scc=1&ltmpl=default&ltmplcache=2&hl=zh-CN&from=logout");
		selenium.type("Email", "zhanfei.tm");
		selenium.type("Passwd", "19860522");
		selenium.click("signIn");
		selenium.waitForPageToLoad("300000");
	 
//		 selenium.open(OPENQA_URL);
//		 
//		 selenium.captureScreenshot(screenshotsResultsPath
//		 + File.separator
//		 + "openQaHomePage_"
//		 + LoggingUtils.timeStampForFileName()
//		 + ".png");
//		 
//		 // Note the special assertEquals from logging-selenium needs
//		 // a selenium instance and a description string
//		 // selenium instance is needed in order to be able to log exceptions
//		 // Get rid of this is to be available in V >2.0
//		 assertEquals("Expected page title not found", "OpenQA: Home", selenium.getTitle(), selenium);
//		 
//		 selenium.click("link=Selenium RC");
//		 selenium.waitForPageToLoad(DEFAULT_TIMEOUT);
//		 
//		 assertEquals("Expected page title not found", "Selenium RC: About", selenium.getTitle(), selenium);
//		 assertTrue("Expected text 'Supported Platforms' not found", selenium.isTextPresent("Supported Platforms"), selenium);
//		 
//		 selenium.click("link=Tutorial");
//		 selenium.waitForPageToLoad(DEFAULT_TIMEOUT);
//		 assertEquals("Expected page title not found", "Selenium RC: Tutorial", selenium.getTitle(), selenium);
		 
		 selenium.captureScreenshot(screenshotsResultsPath
		 + File.separator
		 + "openQaTutorialPage_"
		 + LogUtils.timeStampForFileName()
		 + ".png");
	 }
 }

