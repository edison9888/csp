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
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-3 - 上午09:46:06
 * @version 1.0
 */
public interface SeleniumRcService {
	
	/**
	 * 新增Selenium server配置
	 * 
	 * @param seleniumRc
	 * 2011-6-3 - 下午05:39:26
	 */
	public void addSeleniumServer(SeleniumRc seleniumRc);
	
	/**
	 * 更新Selenium server配置
	 * 
	 * @param seleniumRc
	 * 2011-6-8 - 下午05:39:26
	 */
	public void updateSeleniumServer(SeleniumRc seleniumRc);

	/**
	 * 根据主键获取运行用例的Selenium rc信息
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-6-3 - 上午10:26:26
	 */
	public SeleniumRc getSeleniumRcByPrimaryKey(int id);
	
	/**
	 * selenium rc service 列表信息
	 * 
	 * @return
	 * 2011-6-3 - 下午02:52:32
	 */
	public List<SeleniumRc> listRcServices(); 
	
	/**
	 * 根据主键删除指定的selenium server
	 * 
	 * @param id
	 * 2011-6-9 - 下午03:36:12
	 */
	public void deleteSeleniumServer(int id);
	
	/**
	 * 根据主键获取rc支持的浏览器类型
	 * 
	 * @param id
	 * @return
	 * 2011-6-9 - 下午04:07:50
	 */
	public Map<String ,String> getBrowsersByPrimaryKey(int id);
}
