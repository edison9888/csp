/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import java.util.List;

import com.taobao.monitor.selenium.dao.model.SeleniumAlarmAcceptor;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-7-12 - ����08:32:24
 * @version 1.0
 */
public interface SeleniumAlarmAcceptorService {

	/**
	 * 
	 * 
	 * @param acceptors
	 * 2011-7-12 - ����08:32:53
	 */
	public void addAlarmAcceptor(long[] useCaseIdArr ,String[] addressArray, 
			String type);
	
	/**
	 * 
	 * 
	 * @param useCaseId
	 * @return
	 * 2011-7-12 - ����08:32:55
	 */
	public List<SeleniumAlarmAcceptor> getAlarmAcceptorByUcId(int useCaseId);
	
	public List<String> getAllAlarmAcceptor(String type);
	
	/**
	 * ���ݸ澯�����˻�ȡ�澯������
	 * 
	 * @param address
	 * @return
	 * 2011-7-12 - ����09:16:11
	 */
	public String getUseCaseIdsByAlarmAcceptor(String address);
	
	public void deleteSeleniumAlarmAcceptor(String addres);
}
