/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.selenium.dao.model.BrowserSession;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-14 - ����03:07:01
 * @version 1.0
 */
@Repository
public class BrowserSessionDao extends MysqlRouteBase {
	
	private static final Logger logger = Logger.getLogger(UseCaseDao.class);

	/**
	 * 
	 */
	public BrowserSessionDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Monitor_Script"));
	}
	
	/**
	 * �����������������Ϣ
	 * 
	 * @param useCase
	 * 2011-5-30 - ����01:37:27
	 */
	public void addBrowserSession(BrowserSession browserSession){
		String sql = "INSERT INTO csp_selenium_browser_session(usecase_id, selenium_rc_ip, " +
				" selenium_rc_port, browser_session_id, time_stamp)VALUES(?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{
					browserSession.getUseCaseId(),
					browserSession.getSeleniumRcIp(),
					browserSession.getSeleniumRcPort(),
					browserSession.getBrowserSessionId(),
					browserSession.getTimeStamp()
					});
		} catch (SQLException e) {
			logger.error("addBrowserSession ���� "+e);
		}
	}
	
	/**
	 * ɾ��gc�������������
	 * 
	 * @param browserSession
	 * 2011-6-14 - ����03:14:55
	 */
	public void deleteBrowserSession(int id){
		String sql = "DELETE FROM csp_selenium_browser_session WHERE id=?";
		try {
			this.execute(sql, new Object[]{id});
		} catch (SQLException e) {
			logger.error("deleteBrowserSession[by id] ���� "+e);
		}
	}
	
	/**
	 * ɾ��gc�������������
	 * 
	 * @param id
	 * 2011-6-14 - ����03:14:55
	 */
	public void deleteBrowserSession(BrowserSession browserSession){
		String sql = "DELETE FROM csp_selenium_browser_session WHERE usecase_id=? " +
				" AND selenium_rc_ip=? AND selenium_rc_port=?";
		try {
			this.execute(sql, new Object[]{
					browserSession.getUseCaseId(),
					browserSession.getSeleniumRcIp(),
					browserSession.getSeleniumRcPort(),
					});
		} catch (SQLException e) {
			logger.error("deleteBrowserSession[by browserSession] ���� "+e);
		}
	}
	
	/**
	 * ��ѯ���е�δ�رյ������session��Ϣ
	 * 
	 * @return
	 * 2011-6-3 - ����04:11:33
	 */
	public List<BrowserSession> queryAllBrowserSession(){
		String sql = "SELECT id, usecase_id, browser_session_id, time_stamp " +
				" FROM csp_selenium_browser_session ";
		final List<BrowserSession> list = new ArrayList<BrowserSession>();
		try {
			this.query(sql, new SqlCallBack() {		
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					BrowserSession bs = new BrowserSession();
					bs.setId(rs.getInt("id"));
					bs.setUseCaseId(rs.getInt("usecase_id"));
					bs.setBrowserSessionId(rs.getString("browser_session_id"));
					bs.setTimeStamp(rs.getString("time_stamp"));
					list.add(bs);
				}
			});
		} catch (Exception e) {
			logger.error("queryAllBrowserSession ���� "+e);
		}
		return list;
	}
	
//	/**
//	 * ��������ID��ȡ�����������Ϣ
//	 * 
//	 * @param ucId
//	 * @return
//	 * 2011-6-14 - ����05:31:27
//	 */
//	public BrowserSession getBrowserSessionBySUcId(int ucId){
//		
//	}
	
}
