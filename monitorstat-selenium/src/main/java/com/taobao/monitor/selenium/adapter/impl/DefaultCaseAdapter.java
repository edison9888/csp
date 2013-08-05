/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.adapter.impl;

import com.taobao.monitor.selenium.adapter.CaseAdapter;
import com.taobao.monitor.selenium.util.log.LogSelenium;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-26 - ����03:30:37
 * @version 1.0
 */
public class DefaultCaseAdapter implements CaseAdapter {

	/**
	 * �����κβ���
	 * 
	 * 2011-5-30 - ����10:55:55
	 */
	@Override
	public void execute(LogSelenium selenium) {
		selenium.stop();
	}

}
