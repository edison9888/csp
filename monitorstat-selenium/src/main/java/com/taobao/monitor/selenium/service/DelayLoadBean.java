/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.taobao.monitor.selenium.schedule.DeleteSeleniumTestData;
import com.taobao.monitor.selenium.schedule.SeleniumQuartzManager;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-14 - ����06:55:15
 * @version 1.0
 */
@Service
public class DelayLoadBean implements InitializingBean {
	
//	@Resource(name="useCaseResultServiceImpl")
//	private SeleniumUseCaseService ucResultService;
	/**
	 * ��ʼ���������Խ���������[����һ������]
	 * @throws Exception
	 * 2011-6-14 - ����06:55:15
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		//0 0/1 * * * ?
		SeleniumQuartzManager.addJob("delete_selenium_test_data",
				"0 0 4 * * ?", DeleteSeleniumTestData.class);
	}

}
