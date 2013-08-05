///*
// * Taobao.com Inc.
// * Copyright (c) 2010-2011 All Rights Reserved.
// */
//package com.taobao.monitor.selenium.service;
//
//import java.util.List;
//
//import com.taobao.monitor.selenium.dao.model.BrowserSession;
//
///**
// * .
// * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
// * 2011-6-14 - 下午03:21:45
// * @version 1.0
// */
//public interface BrowserSessionService{
//
//	/**
//	 * 新增打开浏览器窗口信息
//	 * 
//	 * @param useCase
//	 * 2011-5-30 - 下午01:37:27
//	 */
//	public void addBrowserSession(BrowserSession browserSession);
//	
//	/**
//	 * 删除gc掉的浏览器窗口
//	 * 
//	 * @param browserSession
//	 * 2011-6-14 - 下午03:14:55
//	 */
//	public void deleteBrowserSession(int id);
//	
//	/**
//	 * 删除gc掉的浏览器窗口
//	 * 
//	 * @param browserSession
//	 * 2011-6-14 - 下午03:14:55
//	 */
//	public void deleteBrowserSession(BrowserSession browserSession);
//	
//	/**
//	 * 查询所有的未关闭的浏览器session信息
//	 * 
//	 * @return
//	 * 2011-6-3 - 下午04:11:33
//	 */
//	public List<BrowserSession> queryAllBrowserSession();
//	
////	/**
////	 * 根据用例ID获取启动浏览器信息
////	 * 
////	 * @param ucId
////	 * @return
////	 * 2011-6-14 - 下午05:31:27
////	 */
////	public BrowserSession getBrowserSessionBySUcId(int ucId);
//}
