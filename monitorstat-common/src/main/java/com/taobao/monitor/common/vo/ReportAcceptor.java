/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.common.vo;

import java.io.Serializable;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-12 - ����01:23:35
 * @version 1.0
 */
public class ReportAcceptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** ����ID */
	private int reportId;
	
	/** Ĭ��Ӧ��ID ���� �û��Զ������ */
	private String reportParam;
	
	/** ������ܵ�ַ */
	private String address;
	
	/** �����ַ���� */
	private String type;
	
	private ReportTemplate reportTemplate;
	
	private String selected;

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
	 * @return the reportParam
	 */
	public String getReportParam() {
		return reportParam;
	}

	/**
	 * @param reportParam the reportParam to set
	 */
	public void setReportParam(String reportParam) {
		this.reportParam = reportParam;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @return the reportTemplate
	 */
	public ReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	/**
	 * @param reportTemplate the reportTemplate to set
	 */
	public void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	/**
	 * @return the selected
	 */
	public String getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(String selected) {
		this.selected = selected;
	}
}
