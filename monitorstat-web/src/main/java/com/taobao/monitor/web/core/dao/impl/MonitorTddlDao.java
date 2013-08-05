package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.tddl.TddlPo;

/**
 * 对csp_app_db_distribe表进行操作的dao类
 * @author denghaichuan.pt
 * @version 2011-10-31
 */
public class MonitorTddlDao extends MysqlRouteBase  {
	
	private static final Logger logger = Logger.getLogger(MonitorTddlDao.class);
	
	public MonitorTddlDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
	
	/**
	 * 查询当前应用、当前时间所有的记录
	 * @param appId
	 * @param date
	 * @return
	 */
		
	public List<TddlPo> findTddlListByApp(String opsName, String date) {
		
		final List<TddlPo> tddlList = new ArrayList<TddlPo>();
		
		String sql = "select * from csp_app_consume_db_detail where app_name=? and collect_time=?";
		
		try {
			
			this.query(sql, new Object[]{opsName, date}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					TddlPo po = new TddlPo();
					po.setOpsName(rs.getString("app_name"));
					po.setDbName(rs.getString("db_name"));
					po.setSqlText(rs.getString("sql_text"));
					po.setExecuteType(rs.getString("execute_type"));
					po.setExecuteSum(rs.getLong("execute_sum"));
					po.setTimeAverage(rs.getDouble("time_average"));
					po.setHostIp(rs.getString("app_host_ip"));
					po.setHostSite(rs.getString("app_site_name"));
					po.setCollectTime(rs.getString("collect_time"));
					tddlList.add(po);
				}});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return tddlList;
	}
	
	/**
	 * 查询当前应用、当前时间所有的记录
	 * @param appId
	 * @param date
	 * @return
	 */
		
	public List<TddlPo> findTddlListByInfo(String opsName, String dbName, String type, String date) {
		
		final List<TddlPo> tddlList = new ArrayList<TddlPo>();
		
		String sql = "select * from csp_app_consume_db_detail where app_name=? and db_name=? and execute_type=? and collect_time=?";
		
		try {
			
			this.query(sql, new Object[]{opsName, dbName, type, date}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					TddlPo po = new TddlPo();
					po.setOpsName(rs.getString("app_name"));
					po.setDbName(rs.getString("db_name"));
					po.setSqlText(rs.getString("sql_text"));
					po.setExecuteType(rs.getString("execute_type"));
					po.setExecuteSum(rs.getLong("execute_sum"));
					po.setTimeAverage(rs.getDouble("time_average"));
					po.setHostIp(rs.getString("app_host_ip"));
					po.setHostSite(rs.getString("app_site_name"));
					po.setCollectTime(rs.getString("collect_time"));
					tddlList.add(po);
				}});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return tddlList;
	}

}
