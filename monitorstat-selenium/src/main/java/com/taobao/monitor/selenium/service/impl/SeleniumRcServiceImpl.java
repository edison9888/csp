/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.taobao.monitor.selenium.dao.SeleniumRcDao;
import com.taobao.monitor.selenium.dao.model.SeleniumRc;
import com.taobao.monitor.selenium.service.SeleniumRcService;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-3 - 上午10:24:47
 * @version 1.0
 */
@Service
public class SeleniumRcServiceImpl implements SeleniumRcService {
	
	@Resource(name="seleniumRcDao")
	private SeleniumRcDao seleniumRcDao;
	

	/**
	 * @param id
	 * @return
	 * 2011-6-3 - 上午10:54:24
	 */
	@Override
	public SeleniumRc getSeleniumRcByPrimaryKey(int id) {
		return seleniumRcDao.getSeleniumRcByPrimaryKey(id);
	}

	/**
	 * @return
	 * 2011-6-3 - 下午02:52:57
	 */
	@Override
	public List<SeleniumRc> listRcServices() {
		return seleniumRcDao.listRcServices();
	}

	/**
	 * @param seleniumRc
	 * 2011-6-8 - 下午05:20:10
	 */
	@Override
	public void addSeleniumServer(SeleniumRc seleniumRc) {
		seleniumRcDao.addSeleniumServer(seleniumRc);
	}
	
	/**
	 * @param seleniumRc
	 * 2011-6-8 - 下午05:40:37
	 */
	@Override
	public void updateSeleniumServer(SeleniumRc seleniumRc) {
		seleniumRcDao.updateSeleniumServer(seleniumRc);
	}

	/**
	 * @param id
	 * 2011-6-9 - 下午03:36:37
	 */
	@Override
	public void deleteSeleniumServer(int id) {
		seleniumRcDao.deleteSeleniumServer(id);
	}
	
	/**
	 * @param id
	 * @return
	 * 2011-6-9 - 下午04:08:21
	 */
	@Override
	public Map<String ,String> getBrowsersByPrimaryKey(int id) {
		String browsers = seleniumRcDao.getBrowsersByPrimaryKey(id);
		Map<String ,String> browserMap = new HashMap<String ,String>();
		String[] browserArray = browsers.split(",");
		for(String browser : browserArray){
			//chrome *iexplore
			browserMap.put(browser, browser);
		}
		return browserMap;
	}
}
