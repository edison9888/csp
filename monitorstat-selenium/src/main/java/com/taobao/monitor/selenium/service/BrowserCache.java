/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * ����selenium rc service������������Ϣ.
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-7 - ����03:03:16
 * @version 1.0
 */
public class BrowserCache extends SeleniumBeanService{

	private static final Logger logger = Logger.getLogger(BrowserCache.class);
	
	private static Map<Integer, String> browserMap = new ConcurrentHashMap<Integer, String>();
	
	static{
		synchronized (browserMap) {
			browserMap.put(1, "Internet Explorer");
			browserMap.put(2, "Chrome");
			browserMap.put(3, "Mozilla Firefox");
			browserMap.put(4, "Opera");
			browserMap.put(5, "Safari");
		}
	}
	
	/**
	 * ��ȡ���е����������
	 * 
	 * @return
	 * 2011-6-8 - ����03:56:44
	 */
	public static Map<Integer, String> getAllBrowser(){
		return browserMap;
	}
	
	/**
	 * ��ȡrc service��װ�����������
	 * 
	 * @param rcIds
	 * @return
	 * 2011-6-7 - ����03:23:30
	 */
	public static String getBrowserById(String browserIds){
		synchronized (browserMap) {
			String[] browserArr = null;
			if(browserIds != null && !browserIds.equals("")){
				String[] browserIdArr = browserIds.split(",");
				browserArr = new String[browserIdArr.length];
				for(int i = 0; i<browserIdArr.length ; i++){
					int browserId = Integer.valueOf(browserIdArr[i]);
					browserArr[i] = browserMap.get(browserId);
				}
			}
			if(browserArr == null)
				browserArr = new String[]{};
			StringBuffer buffer = new StringBuffer();
			for(String browser : browserArr){
				buffer.append(browser).append(",");
			}
			if(buffer.toString().indexOf(",") != -1)
				return buffer.toString().substring(0, 
						buffer.toString().lastIndexOf(","));
			else{
				return buffer.toString();
			}
		}
	}
}
