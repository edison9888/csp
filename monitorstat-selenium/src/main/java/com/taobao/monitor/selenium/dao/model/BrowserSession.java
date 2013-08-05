/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.dao.model;

import java.io.Serializable;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-14 - 下午03:09:33
 * @version 1.0
 */
public class BrowserSession implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int useCaseId;
	private String browserSessionId;
	private String timeStamp;
	/** 被测试的主站域名 */
	private String baseUrl;
	
	private String seleniumRcIp;
	
	private int seleniumRcPort;
	
	private String browser;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the usecaseId
	 */
	public int getUseCaseId() {
		return useCaseId;
	}
	/**
	 * @param usecaseId the usecaseId to set
	 */
	public void setUseCaseId(int useCaseId) {
		this.useCaseId = useCaseId;
	}
	/**
	 * @return the browserSessionId
	 */
	public String getBrowserSessionId() {
		return browserSessionId;
	}
	/**
	 * @param browserSessionId the browserSessionId to set
	 */
	public void setBrowserSessionId(String browserSessionId) {
		this.browserSessionId = browserSessionId;
	}
	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}
	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	/**
	 * @return the seleniumRcIp
	 */
	public String getSeleniumRcIp() {
		return seleniumRcIp;
	}
	/**
	 * @param seleniumRcIp the seleniumRcIp to set
	 */
	public void setSeleniumRcIp(String seleniumRcIp) {
		this.seleniumRcIp = seleniumRcIp;
	}
	/**
	 * @return the seleniumRcPort
	 */
	public int getSeleniumRcPort() {
		return seleniumRcPort;
	}
	/**
	 * @param seleniumRcPort the seleniumRcPort to set
	 */
	public void setSeleniumRcPort(int seleniumRcPort) {
		this.seleniumRcPort = seleniumRcPort;
	}
	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}
	/**
	 * @param browser the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
}
