/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.dao.model;

import java.io.Serializable;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-26 - ����04:01:48
 * @version 1.0
 */
public class UseCase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int useCaseId;
	
	private int useCaseTextId;
	/** ������������selenium rc������key */
	private int rcId;
	
	private Integer[] rcIdArray;
	
	/** �������� */
	private String useCaseName;
	
	/** �������� */
	
	private String useCaseAlias;
	
	/** �����Ե���վ���� */
	private String baseUrl;
	
	/** ����ʹ������� */
	private String browser;
	
	/** �������� */
	private String useCaseType;

	/** �����汾 */
	private Long useCaseVersion;
	
	/** �����汾 */
	private String useCaseSource;
	private String param;	//����str1
	private String paramStr;	//����str2

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
