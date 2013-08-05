/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.dao.model;

import java.io.Serializable;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">Õ¶·É</a>
 * 2011-7-12 - ÏÂÎç08:17:06
 * @version 1.0
 */
public class SeleniumAlarmAcceptor implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long useCaseId;
	
	private String type;
	
	private String address;
	
	private long[] useCaseIdArr;

	/**
	 * @return the useCaseId
	 */
	public long getUseCaseId() {
		return useCaseId;
	}

	/**
	 * @param useCaseId the useCaseId to set
	 */
	public void setUseCaseId(long useCaseId) {
		this.useCaseId = useCaseId;
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
	 * @return the useCaseIdArr
	 */
	public long[] getUseCaseIdArr() {
		return useCaseIdArr;
	}

	/**
	 * @param useCaseIdArr the useCaseIdArr to set
	 */
	public void setUseCaseIdArr(long[] useCaseIdArr) {
		this.useCaseIdArr = useCaseIdArr;
	}

}
