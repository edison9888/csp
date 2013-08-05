/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-25 - 上午09:12:53
 * @version 1.0
 */
public class LogDefaultSelenium extends DefaultSelenium implements
		LogSelenium {

	/**
	 * @param processor
	 */
	public LogDefaultSelenium(final CommandProcessor processor) {
		super(processor);
	}
	
	/**
	 * @param comment
	 * 2011-5-25 - 上午09:12:53
	 */
	@Override
	public void logComment(final String comment) {
		 commandProcessor.doCommand(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_COMMENT.getName(), new String[] {comment});
	}
    
	/**
	 * @param baseName
	 * 2011-5-25 - 上午09:12:53
	 */
	@Override
	public void logAutomaticScreenshot(String baseName) {
        commandProcessor.doCommand(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_AUTO_SCREENSHOT.getName(),
                new String[] {baseName});

	}
	
	/**
	 * @param assertionName
	 * @param assertionMessage
	 * @param assertionCondition
	 * 2011-5-25 - 上午09:12:53
	 */
	@Override
    public void logAssertion(final String assertionName, final String assertionMessage, 
    		final String assertionCondition) {
        commandProcessor.doCommand(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_ASSERTION.getName(), new String[] {
                assertionName, assertionMessage, assertionCondition});
    }

	/**
	 * @param description
	 * @param pathFile
	 * 2011-5-25 - 上午09:12:53
	 */
	@Override
    public void logResource(final String file, final String description) {
        commandProcessor.doCommand(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_RESOURCE.getName(), new String[] {
            file, description});
    }

}
