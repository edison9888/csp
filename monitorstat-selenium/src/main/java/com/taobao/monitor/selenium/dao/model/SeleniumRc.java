/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.dao.model;

import java.io.Serializable;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-31 - 下午03:02:10
 * @version 1.0
 */
public class SeleniumRc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String seleniumRcIp;
	
	private String seleniumRcName;
	
	private int seleniumRcPort;
	
	private String operateSystemType;
	
	private String browsers; 
	
	/** 任务执行时间 */
	private String quartzCron;
	
	private UseCase useCase;
	
	private int jobState;
	
	/** job当前状态信息 */
	private String jobStateMsg;

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
	 * @return the seleniumRcName
	 */
	public String getSeleniumRcName() {
		return seleniumRcName;
	}

	/**
	 * @param seleniumRcName the seleniumRcName to set
	 */
	public void setSeleniumRcName(String seleniumRcName) {
		this.seleniumRcName = seleniumRcName;
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
	 * @return the useCase
	 */
	public UseCase getUseCase() {
		return useCase;
	}

	/**
	 * @param useCase the useCase to set
	 */
	public void setUseCase(UseCase useCase) {
		this.useCase = useCase;
	}

	/**
	 * @return the operateSystemType
	 */
	public String getOperateSystemType() {
		return operateSystemType;
	}

	/**
	 * @param operateSystemType the operateSystemType to set
	 */
	public void setOperateSystemType(String operateSystemType) {
		this.operateSystemType = operateSystemType;
	}

	/**
	 * @return the browsers
	 */
	public String getBrowsers() {
		return browsers;
	}

	/**
	 * @param browsers the browsers to set
	 */
	public void setBrowsers(String browsers) {
		this.browsers = browsers;
	}

	/**
	 * @return the quartzCron
	 */
	public String getQuartzCron() {
		return quartzCron;
	}

	/**
	 * @param quartzCron the quartzCron to set
	 */
	public void setQuartzCron(String quartzCron) {
		this.quartzCron = quartzCron;
	}

	/**
	 * @return the jobStateMsg
	 */
	public String getJobStateMsg() {
		return jobStateMsg;
	}

	/**
	 * @param jobStateMsg the jobStateMsg to set
	 */
	public void setJobStateMsg(String jobStateMsg) {
		this.jobStateMsg = jobStateMsg;
	}

	/**
	 * @return the jobState
	 */
	public int getJobState() {
		return jobState;
	}

	/**
	 * @param jobState the jobState to set
	 */
	public void setJobState(int jobState) {
		this.jobState = jobState;
	}
	
}
