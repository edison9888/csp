/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.csp.config.po;

import java.io.Serializable;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-11 - ����08:20:46
 * @version 1.0
 */
public class ReportTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	
	/** ����ID */
	private int reportId;
	
	/** �������� */
	private String reportName;
	
	/** ������� */
	private String alias;
	
	/** ���������� sms|ww|email */
	private String type;
	
	/** �������ͱ����url��ַ */
	private String path;
	
	/** ������quartz cron���ʽ */
	private String quartzCron;
    
	/** �Ƿ��޸ı����͵�Quartz Cron���ʽ */
	private String qcUpdate;
	
	/** job״̬ */
	private int jobState;
	
	private String jobStateMsg;
	
	/** �������ͣ����ݷ�Ӧ��| �û��Զ��� */
	private int reportType; 
	
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
	 * @return the reportId
	 */
	public int getReportId() {
		return reportId;
	}

	/**
	 * @param reportId the reportId to set
	 */
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the reportType
	 */
	public int getReportType() {
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
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
	 * @return the qcUpdate
	 */
	public String getQcUpdate() {
		return qcUpdate;
	}

	/**
	 * @param qcUpdate the qcUpdate to set
	 */
	public void setQcUpdate(String qcUpdate) {
		this.qcUpdate = qcUpdate;
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
	
}
