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
import com.taobao.monitor.selenium.dao.model.SeleniumRc;
import com.taobao.monitor.selenium.schedule.SeleniumQuartzManager;
import com.taobao.monitor.selenium.service.BrowserCache;
import com.taobao.monitor.selenium.util.SeleniumConstant;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-2 - 下午05:43:03
 * @version 1.0
 */
@Repository
public class SeleniumRcDao extends MysqlRouteBase {
	
	private static final Logger logger = Logger.getLogger(SeleniumRcDao.class);

	/**
	 * 初始化数据源
	 */
	public SeleniumRcDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Monitor_Script"));
	}
	
	/**
	 * 新增Selenium server
	 * 
	 * @param seleniumRc
	 * 2011-6-8 - 下午05:20:28
	 */
	public void addSeleniumServer(SeleniumRc seleniumRc){
		String sql = "INSERT INTO csp_selenium_rc(selenium_rc_name, selenium_rc_ip, selenium_rc_port, " +
				" operate_system_type, browser_ids, quartz_cron)VALUES(?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{
					seleniumRc.getSeleniumRcName(),
					seleniumRc.getSeleniumRcIp(),
					seleniumRc.getSeleniumRcPort(),
					seleniumRc.getOperateSystemType(),
					seleniumRc.getBrowsers(),
					seleniumRc.getQuartzCron()
			});
		} catch (SQLException e) {
			logger.error("addSeleniumServer 出错！", e);
		}
	}
	
	/**
	 * 更新Selenium server配置
	 * 
	 * @param seleniumRc
	 * 2011-6-8 - 下午05:39:26
	 */
	public void updateSeleniumServer(SeleniumRc seleniumRc){
		String sql = "UPDATE csp_selenium_rc SET selenium_rc_name=?, selenium_rc_ip=?, selenium_rc_port=?," +
				" operate_system_type=?,browser_ids=?,quartz_cron=? WHERE id=?";
		try {
			this.execute(sql, new Object[]{
					seleniumRc.getSeleniumRcName(),
					seleniumRc.getSeleniumRcIp(),
					seleniumRc.getSeleniumRcPort(),
					seleniumRc.getOperateSystemType(),
					seleniumRc.getBrowsers(),
					seleniumRc.getQuartzCron(),
					seleniumRc.getId()
			});
		} catch (SQLException e) {
			logger.error("updateSeleniumServer 出错！", e);
		}
	}
	
	/**
	 * 根据主键获取运行用例的Selenium rc信息
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-6-3 - 上午10:26:26
	 */
	public SeleniumRc getSeleniumRcByPrimaryKey(final int id){
		String sql = "SELECT r.browser_ids, r.operate_system_type, r.quartz_cron, r.selenium_rc_name, " +
				" r.selenium_rc_ip, r.selenium_rc_port FROM csp_selenium_rc r WHERE r.id=?";
		final SeleniumRc seleniumRc = new SeleniumRc();
		try {
			this.query(sql, new Object[]{id}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					seleniumRc.setBrowsers(rs.getString("browser_ids"));
					seleniumRc.setId(id);
					seleniumRc.setOperateSystemType(rs.getString("operate_system_type"));
					seleniumRc.setQuartzCron(rs.getString("quartz_cron"));
					seleniumRc.setSeleniumRcName(rs.getString("selenium_rc_name"));
					seleniumRc.setSeleniumRcIp(rs.getString("selenium_rc_ip"));
					seleniumRc.setSeleniumRcPort(rs.getInt("selenium_rc_port"));
				}
				
			});
		} catch (Exception e) {
			logger.error("getSeleniumRcByPrimaryKey 出错！", e);
		}
		return seleniumRc;
	}
	
	/**
	 * selenium rc 列表
	 * 
	 * @return
	 * 2011-6-7 - 下午02:53:36
	 */
	public List<SeleniumRc> listRcServices() {
		String sql = "SELECT rc.id, rc.browser_ids, rc.operate_system_type,rc.selenium_rc_ip," +
				" rc.quartz_cron, rc.selenium_rc_name, rc.selenium_rc_port FROM csp_selenium_rc rc ";
		final List<SeleniumRc> list = new ArrayList<SeleniumRc>();
		try {
			this.query(sql, new SqlCallBack() {		
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					SeleniumRc rc = new SeleniumRc();
					rc.setId(rs.getInt("id"));
					rc.setSeleniumRcIp(rs.getString("selenium_rc_ip"));
					rc.setSeleniumRcName(rs.getString("selenium_rc_name"));
					rc.setSeleniumRcPort(rs.getInt("selenium_rc_port"));
					rc.setOperateSystemType(rs.getString("operate_system_type"));
					rc.setQuartzCron(rs.getString("quartz_cron"));
					rc.setBrowsers(BrowserCache.getBrowserById(rs.getString("browser_ids")));
					rc.setJobState(SeleniumQuartzManager.getTaskState(SeleniumConstant.TRIGGER_PREFIX+rc.getId()));
					rc.setJobStateMsg(SeleniumQuartzManager.getTaskStateMsg(SeleniumConstant.TRIGGER_PREFIX+rc.getId()));
					list.add(rc);
				}
			});
		} catch (Exception e) {
			logger.error("listRcServices 出错！", e);
		}
		return list;
	}
	
	/**
	 * 根据主键删除指定的selenium server
	 * 
	 * @param id
	 * 2011-6-9 - 下午03:36:58
	 */
	public void deleteSeleniumServer(int id){
		String sql = "DELETE FROM csp_selenium_rc WHERE id=?";
		try {
			this.execute(sql, new Object[]{id});
		} catch (SQLException e) {
			logger.error("deleteSeleniumServer 出错！", e);
		}
	}
	
	/**
	 * 根据主键获取rc支持的浏览器类型
	 * 
	 * @param id
	 * @return
	 * 2011-6-9 - 下午04:07:50
	 */
	public String getBrowsersByPrimaryKey(int id){
		String sql = "SELECT rc.browser_ids FROM csp_selenium_rc rc WHERE rc.id=?";
		final StringBuffer buf = new StringBuffer();
		try {
			this.query(sql, new Object[]{id}, new SqlCallBack() {		
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					buf.append(BrowserCache.getBrowserById(rs.getString("browser_ids")));
				}
			});
		} catch (Exception e) {
			logger.error("getBrowsersByPrimaryKey 出错！", e);
		}
		return buf.toString();
	}
}
