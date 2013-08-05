/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.adapter;

import com.taobao.monitor.selenium.util.log.LogSelenium;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-26 - 下午03:29:46
 * @version 1.0
 */
public interface CaseAdapter {
	
	/**
	 * 执行测试用例
	 * 2011-5-30 - 上午10:55:34
	 */
	public void execute(LogSelenium selenium) throws Exception;
}
