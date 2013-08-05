/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import java.util.List;

import com.taobao.monitor.selenium.dao.model.SeleniumAlarmAcceptor;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-7-12 - 下午08:32:24
 * @version 1.0
 */
public interface SeleniumAlarmAcceptorService {

	/**
	 * 
	 * 
	 * @param acceptors
	 * 2011-7-12 - 下午08:32:53
	 */
	public void addAlarmAcceptor(long[] useCaseIdArr ,String[] addressArray, 
			String type);
	
	/**
	 * 
	 * 
	 * @param useCaseId
	 * @return
	 * 2011-7-12 - 下午08:32:55
	 */
	public List<SeleniumAlarmAcceptor> getAlarmAcceptorByUcId(int useCaseId);
	
	public List<String> getAllAlarmAcceptor(String type);
	
	/**
	 * 根据告警接收人获取告警的用例
	 * 
	 * @param address
	 * @return
	 * 2011-7-12 - 下午09:16:11
	 */
	public String getUseCaseIdsByAlarmAcceptor(String address);
	
	public void deleteSeleniumAlarmAcceptor(String addres);
}
