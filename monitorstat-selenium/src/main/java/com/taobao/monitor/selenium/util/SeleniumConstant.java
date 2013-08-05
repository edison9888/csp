/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-3 - ����02:28:31
 * @version 1.0
 */
public final class SeleniumConstant {

	public final static String SELENIUM_UC_NAME = "csp_selenium_test_load_class=";
	
	/** selenium JobDetail����ǰ׺ */
	public final static String JOBDETAIL_PREFIX = "SELENIUM_JOBDETAIL";
	
	/** selenium trigger����ǰ׺ */
	public final static String TRIGGER_PREFIX = "SELENIUM_TRIGGER_";
	
	/** selenium���������� */
	public static String JOB_GROUP_NAME = "SELENIUM_JOB_GROUP_NAME";
	
	/** selenium������������ */
	public static String TRIGGER_GROUP_NAME = "SELENIUM_TRIGGER_GROUP_NAME";  
	
	/**
	 * ����Ѳ���澯����
	 * 
	 * @return
	 * 2011-7-12 - ����07:22:54
	 */
	public static Map<String, String> getSeleniumAlarmType(){
		Map<String, String> alarmType = new HashMap<String, String>();
		alarmType.put("ww", "����");
		alarmType.put("email", "�ʼ�");
		alarmType.put("sms", "����");
		return alarmType;
	}
}
