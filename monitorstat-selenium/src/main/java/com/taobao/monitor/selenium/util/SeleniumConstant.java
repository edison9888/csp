/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-3 - 下午02:28:31
 * @version 1.0
 */
public final class SeleniumConstant {

	public final static String SELENIUM_UC_NAME = "csp_selenium_test_load_class=";
	
	/** selenium JobDetail名称前缀 */
	public final static String JOBDETAIL_PREFIX = "SELENIUM_JOBDETAIL";
	
	/** selenium trigger名称前缀 */
	public final static String TRIGGER_PREFIX = "SELENIUM_TRIGGER_";
	
	/** selenium任务组名字 */
	public static String JOB_GROUP_NAME = "SELENIUM_JOB_GROUP_NAME";
	
	/** selenium触发器组名字 */
	public static String TRIGGER_GROUP_NAME = "SELENIUM_TRIGGER_GROUP_NAME";  
	
	/**
	 * 交易巡警告警类型
	 * 
	 * @return
	 * 2011-7-12 - 下午07:22:54
	 */
	public static Map<String, String> getSeleniumAlarmType(){
		Map<String, String> alarmType = new HashMap<String, String>();
		alarmType.put("ww", "旺旺");
		alarmType.put("email", "邮件");
		alarmType.put("sms", "短信");
		return alarmType;
	}
}
