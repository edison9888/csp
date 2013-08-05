/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.adapter.impl;

import com.taobao.monitor.selenium.adapter.CaseAdapter;
import com.taobao.monitor.selenium.util.log.LogSelenium;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-26 - 下午03:30:37
 * @version 1.0
 */
public class DefaultCaseAdapter implements CaseAdapter {

	/**
	 * 不做任何操作
	 * 
	 * 2011-5-30 - 上午10:55:55
	 */
	@Override
	public void execute(LogSelenium selenium) {
		selenium.stop();
	}

}
