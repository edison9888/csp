/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import java.util.List;
import java.util.Map;

import com.taobao.monitor.selenium.dao.model.SeleniumRc;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-3 - ����09:46:06
 * @version 1.0
 */
public interface SeleniumRcService {
	
	/**
	 * ����Selenium server����
	 * 
	 * @param seleniumRc
	 * 2011-6-3 - ����05:39:26
	 */
	public void addSeleniumServer(SeleniumRc seleniumRc);
	
	/**
	 * ����Selenium server����
	 * 
	 * @param seleniumRc
	 * 2011-6-8 - ����05:39:26
	 */
	public void updateSeleniumServer(SeleniumRc seleniumRc);

	/**
	 * ����������ȡ����������Selenium rc��Ϣ
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-6-3 - ����10:26:26
	 */
	public SeleniumRc getSeleniumRcByPrimaryKey(int id);
	
	/**
	 * selenium rc service �б���Ϣ
	 * 
	 * @return
	 * 2011-6-3 - ����02:52:32
	 */
	public List<SeleniumRc> listRcServices(); 
	
	/**
	 * ��������ɾ��ָ����selenium server
	 * 
	 * @param id
	 * 2011-6-9 - ����03:36:12
	 */
	public void deleteSeleniumServer(int id);
	
	/**
	 * ����������ȡrc֧�ֵ����������
	 * 
	 * @param id
	 * @return
	 * 2011-6-9 - ����04:07:50
	 */
	public Map<String ,String> getBrowsersByPrimaryKey(int id);
}
