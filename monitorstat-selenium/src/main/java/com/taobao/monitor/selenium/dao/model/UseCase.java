/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.dao.model;

import java.io.Serializable;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-26 - 下午04:01:48
 * @version 1.0
 */
public class UseCase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int useCaseId;
	
	private int useCaseTextId;
	/** 测试用例运行selenium rc服务器key */
	private int rcId;
	
	private Integer[] rcIdArray;
	
	/** 用例名称 */
	private String useCaseName;
	
	/** 用例别名 */
	
	private String useCaseAlias;
	
	/** 被测试的主站域名 */
	private String baseUrl;
	
	/** 运行使用浏览器 */
	private String browser;
	
	/** 用例类型 */
	private String useCaseType;

	/** 用例版本 */
	private Long useCaseVersion;
	
	/** 用例版本 */
	private String useCaseSource;
	private String param;	//备用str1
	private String paramStr;	//备用str2

	private SeleniumRc rc;
	/**
	 * @return the useCaseId
	 */
	public int getUseCaseId() {
		return useCaseId;
	}
	/**
	 * @param useCaseId the useCaseId to set
	 */
	public void setUseCaseId(int useCaseId) {
		this.useCaseId = useCaseId;
	}
	/**
	 * @return the useCaseName
	 */
	public String getUseCaseName() {
		return useCaseName;
	}
	/**
	 * @param useCaseName the useCaseName to set
	 */
	public void setUseCaseName(String useCaseName) {
		this.useCaseName = useCaseName;
	}
	/**
	 * @return the useCaseType
	 */
	public String getUseCaseType() {
		return useCaseType;
	}
	/**
	 * @param useCaseType the useCaseType to set
	 */
	public void setUseCaseType(String useCaseType) {
		this.useCaseType = useCaseType;
	}
	/**
	 * @return the useCaseVersion
	 */
	public Long getUseCaseVersion() {
		return useCaseVersion;
	}
	/**
	 * @param useCaseVersion the useCaseVersion to set
	 */
	public void setUseCaseVersion(Long useCaseVersion) {
		this.useCaseVersion = useCaseVersion;
	}
	/**
	 * @return the param
	 */
	public String getParam() {
		return param;
	}
	/**
	 * @param param the param to set
	 */
	public void setParam(String param) {
		this.param = param;
	}
	/**
	 * @return the paramStr
	 */
	public String getParamStr() {
		return paramStr;
	}
	/**
	 * @param paramStr the paramStr to set
	 */
	public void setParamStr(String paramStr) {
		this.paramStr = paramStr;
	}
	/**
	 * @return the useCaseSource
	 */
	public String getUseCaseSource() {
		return useCaseSource;
	}
	/**
	 * @param useCaseSource the useCaseSource to set
	 */
	public void setUseCaseSource(String useCaseSource) {
		this.useCaseSource = useCaseSource;
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
	 * @return the useCaseTextId
	 */
	public int getUseCaseTextId() {
		return useCaseTextId;
	}
	/**
	 * @param useCaseTextId the useCaseTextId to set
	 */
	public void setUseCaseTextId(int useCaseTextId) {
		this.useCaseTextId = useCaseTextId;
	}
	/**
	 * @return the rcId
	 */
	public int getRcId() {
		return rcId;
	}
	/**
	 * @param rcId the rcId to set
	 */
	public void setRcId(int rcId) {
		this.rcId = rcId;
	}
	/**
	 * @return the useCaseAlias
	 */
	public String getUseCaseAlias() {
		return useCaseAlias;
	}
	/**
	 * @param useCaseAlias the useCaseAlias to set
	 */
	public void setUseCaseAlias(String useCaseAlias) {
		this.useCaseAlias = useCaseAlias;
	}
	/**
	 * @return the rc
	 */
	public SeleniumRc getRc() {
		return rc;
	}
	/**
	 * @param rc the rc to set
	 */
	public void setRc(SeleniumRc rc) {
		this.rc = rc;
	}
	/**
	 * @return the rcIdArray
	 */
	public Integer[] getRcIdArray() {
		return rcIdArray;
	}
	/**
	 * @param rcIdArray the rcIdArray to set
	 */
	public void setRcIdArray(Integer[] rcIdArray) {
		this.rcIdArray = rcIdArray;
	}
	
}
