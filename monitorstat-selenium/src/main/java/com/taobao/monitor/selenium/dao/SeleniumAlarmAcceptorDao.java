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
import com.taobao.monitor.selenium.dao.model.SeleniumAlarmAcceptor;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-7-12 - 下午08:21:29
 * @version 1.0
 */
@Repository
public class SeleniumAlarmAcceptorDao extends MysqlRouteBase {
	
	private static final Logger logger = Logger.getLogger(SeleniumRcDao.class);

	/**
	 * 初始化数据源
	 */
	public SeleniumAlarmAcceptorDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Monitor_Script"));
	}
	
	/**
	 * 交易巡警告警接收人批量入库
	 * @author 斩飞
	 * @param acceptors
	 * 2011-7-6 - 下午03:37:31
	 */
	public void addAlarmAcceptor(List<SeleniumAlarmAcceptor> acceptors){
		String sql = "INSERT INTO csp_selenium_alarm_acceptor(usecase_id,TYPE,address) " +
				" VALUES(?,?,?)";
		List<Object[]> tmp = new ArrayList<Object[]>();
		for(SeleniumAlarmAcceptor acceptor:acceptors){
			Object[] o = new Object[] {
					acceptor.getUseCaseId(), 
					acceptor.getType(),
					acceptor.getAddress()
					};
			tmp.add(o);
		}
		try {
			this.executeBatch(sql, tmp, DbRouteManage.get().getDbRouteByRouteId("Monitor_Script"));
		} catch (Exception e) {
			logger.error("addAlarmAcceptor 出错",e);
		}
	}
	
	/**
	 * 获取交易巡警告警接收人列表
	 * @author 斩飞
	 * @param reportId
	 * @return
	 * 2011-5-15 - 下午02:23:00
	 */
	public List<SeleniumAlarmAcceptor> getAlarmAcceptorByUseCaseId(long useCaseId){
		String sql ="SELECT a.address, a.TYPE FROM csp_selenium_alarm_acceptor " +
				" a WHERE a.usecase_id=?";
		final List<SeleniumAlarmAcceptor> list = new ArrayList<SeleniumAlarmAcceptor>();
		try {
			this.query(sql, new Object[]{useCaseId}, new SqlCallBack() {	
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					SeleniumAlarmAcceptor acceptor = new SeleniumAlarmAcceptor();
					acceptor.setAddress(rs.getString("address"));
					acceptor.setType(rs.getString("type"));
					list.add(acceptor);
				}
			});
		} catch (Exception e) {
			logger.error("getAlarmAcceptorByReportId出错", e);
		}
		return list;
	}
	
	/**
	 * 获取所有巡警告警接收人列表
	 * @author 斩飞
	 * @param reportId
	 * @return
	 * 2011-5-15 - 下午02:23:00
	 */
	public List<String> getAllAlarmAcceptor(String type){
		String sql ="SELECT distinct a.address, a.TYPE FROM csp_selenium_alarm_acceptor a" +
				" where a.TYPE=?";
		final List<String> list = new ArrayList<String>();
		try {
			this.query(sql, new Object[]{type}, new SqlCallBack() {	
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					list.add(rs.getString("address"));
				}
			});
		} catch (Exception e) {
			logger.error("getAllAlarmAcceptor出错", e);
		}
		return list;
	}
	
	/**
	 * 删除加以巡警对应接收人信息
	 * @author 斩飞
	 * @param appId
	 * @param addres
	 * 2011-5-12 - 下午03:39:19
	 */
	public void deleteAlarmAcceptor(long useCaseId, String addres, String type){
		String sql = "DELETE FROM csp_selenium_alarm_acceptor WHERE " +
				"usecase_id =? AND type=?";
		try {
			this.execute(sql, new Object[]{useCaseId, type});
		} catch (SQLException e) {
			logger.error("deleteAlarmAcceptorByUcIdAndTypeAndAddress 出错",e);
		}
	}
	
	public 	List<Long> getUseCaseIdsByAlarmAcceptor(String address){
		String sql = "SELECT a.usecase_id FROM csp_selenium_alarm_acceptor a WHERE a.address in("+address+")";
		final List<Long> list = new ArrayList<Long>();
		try {
			this.query(sql, new SqlCallBack() {	
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					list.add(rs.getLong("usecase_id"));
				}
			});
		} catch (Exception e) {
			logger.error("getUseCaseIdsByAlarmAcceptor出错", e);
		}
		return list;
	}
	
	public void deleteSeleniumAlarmAcceptor(String addres){
		String sql = "DELETE FROM csp_selenium_alarm_acceptor WHERE " +
				" address IN("+addres+")";
		try {
			this.execute(sql);
		} catch (SQLException e) {
			logger.error("deleteSeleniumAlarmAcceptor 出错",e);
		}
	}
}
