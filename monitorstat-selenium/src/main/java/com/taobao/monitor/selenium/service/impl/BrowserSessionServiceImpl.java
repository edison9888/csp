///*
// * Taobao.com Inc.
// * Copyright (c) 2010-2011 All Rights Reserved.
// */
//package com.taobao.monitor.selenium.service.impl;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//
//import com.taobao.monitor.selenium.dao.BrowserSessionDao;
//import com.taobao.monitor.selenium.dao.model.BrowserSession;
//import com.taobao.monitor.selenium.service.BrowserSessionService;
//
///**
// * .
// * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
// * 2011-6-14 - 下午03:22:40
// * @version 1.0
// */
//@Service
//public class BrowserSessionServiceImpl implements BrowserSessionService {
//
//	@Resource(name="browserSessionDao")
//	private BrowserSessionDao browserSessionDao;
//	
//	/**
//	 * @param browserSession
//	 * 2011-6-14 - 下午03:22:40
//	 */
//	@Override
//	public void addBrowserSession(BrowserSession browserSession) {
//		browserSessionDao.addBrowserSession(browserSession);
//	}
//
//	/**
//	 * @param id
//	 * 2011-6-15 - 上午10:39:34
//	 */
//	@Override
//	public void deleteBrowserSession(int id) {
//		browserSessionDao.deleteBrowserSession(id);
//	}
//	
//	/**
//	 * @param id
//	 * 2011-6-14 - 下午03:22:40
//	 */
//	@Override
//	public void deleteBrowserSession(BrowserSession browserSession) {
//		browserSessionDao.deleteBrowserSession(browserSession);
//	}
//
//	/**
//	 * @return
//	 * 2011-6-14 - 下午03:22:40
//	 */
//	@Override
//	public List<BrowserSession> queryAllBrowserSession() {
//		return browserSessionDao.queryAllBrowserSession();
//	}
//
////	/**
////	 * @param ucId
////	 * @return
////	 * 2011-6-14 - 下午05:32:23
////	 */
////	@Override
////	public BrowserSession getBrowserSessionBySUcId(int ucId) {
////		return browserSessionDao.getBrowserSessionBySUcId(ucId);
////	}
//
//}
