/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.dao.model;

import java.io.Serializable;
import java.util.List;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-29 - 上午11:24:29
 * @version 1.0
 */
public class UseCaseTestStat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2627135963083284125L;

	/** 运行总次数 */
	private int count;
	
	/** 记录隶属于UC_ID */
	private int useCaseId;
	
	private String useCaseAlias;
	
    private long commandsProcessed = 0;

    private long failedCommands = 0;

    private long verificationsProcessed = 0;
    
    /** 测试总次数 */
    private int total;
    
    /** 测试用例运行成功次数 */
    private int successTimes;
    
    private long failedTimes;
    
    /** 用例运行成功率 */
    private String successRates;

    private String startDate;

    private String endDate;
    
    private long duration;
    
    private String durationStr;
    
    //查询条件
    private List<Integer> ucIds;
    
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
	 * @return the commandsProcessed
	 */
	public long getCommandsProcessed() {
		return commandsProcessed;
	}

	/**
	 * @param commandsProcessed the commandsProcessed to set
	 */
	public void setCommandsProcessed(long commandsProcessed) {
		this.commandsProcessed = commandsProcessed;
	}

	/**
	 * @return the failedCommands
	 */
	public long getFailedCommands() {
		return failedCommands;
	}

	/**
	 * @param failedCommands the failedCommands to set
	 */
	public void setFailedCommands(long failedCommands) {
		this.failedCommands = failedCommands;
	}

	/**
	 * @return the verificationsProcessed
	 */
	public long getVerificationsProcessed() {
		return verificationsProcessed;
	}

	/**
	 * @param verificationsProcessed the verificationsProcessed to set
	 */
	public void setVerificationsProcessed(long verificationsProcessed) {
		this.verificationsProcessed = verificationsProcessed;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the successTimes
	 */
	public int getSuccessTimes() {
		return successTimes;
	}

	/**
	 * @param successTimes the successTimes to set
	 */
	public void setSuccessTimes(int successTimes) {
		this.successTimes = successTimes;
	}

	/**
	 * @return the successRates
	 */
	public String getSuccessRates() {
		return successRates;
	}

	/**
	 * @param successRates the successRates to set
	 */
	public void setSuccessRates(String successRates) {
		this.successRates = successRates;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the durationStr
	 */
	public String getDurationStr() {
		return durationStr;
	}

	/**
	 * @param durationStr the durationStr to set
	 */
	public void setDurationStr(String durationStr) {
		this.durationStr = durationStr;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the ucIds
	 */
	public List<Integer> getUcIds() {
		return ucIds;
	}

	/**
	 * @param ucIds the ucIds to set
	 */
	public void setUcIds(List<Integer> ucIds) {
		this.ucIds = ucIds;
	}

	/**
	 * @return the failedTimes
	 */
	public long getFailedTimes() {
		return failedTimes;
	}

	/**
	 * @param failedTimes the failedTimes to set
	 */
	public void setFailedTimes(long failedTimes) {
		this.failedTimes = failedTimes;
	}
}
