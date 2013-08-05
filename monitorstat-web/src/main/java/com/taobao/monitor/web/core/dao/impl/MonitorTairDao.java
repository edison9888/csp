package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.tair.AllTairData;
import com.taobao.monitor.web.tair.TairInfoPo;
import com.taobao.monitor.web.tair.TairNamespacePo;

/**
 * 
 * @author denghaichuan.pt
 * @version 2011-9-6
 */

public class MonitorTairDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(MonitorTairDao.class);
	
	public MonitorTairDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
	
	/**
	 * 查询应用对应的action的种类
	 * @param appId
	 * @param date
	 * @return
	 */
	
	public List<String> findGroupList(String date) {
		String sql = "select distinct tair_group_name from csp_tair_provide_app_summary where collect_time=?";
		
		final List<String> groupList = new ArrayList<String>();

		try {
			this.query(sql, new Object[]{date}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					groupList.add(rs.getString("tair_group_name"));
				}});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		
		return groupList;
	}
	
	/**
	 * 获取全部的tair信息
	 * @param appId
	 * @param date
	 * @return
	 */
	
	public List<TairInfoPo> findAllTairInfo(String appName, String date) {
		String sql = "select * from csp_tair_provider_app_detail where app_name=? and collect_time=?";
		
		final List<TairInfoPo> tairList = new ArrayList<TairInfoPo>();
		try {
			
			this.query(sql, new Object[]{appName, date}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					TairInfoPo po = new TairInfoPo();
					po.setActionType(rs.getString("action_type"));
					po.setSiteName(rs.getString("tair_site_name"));
					po.setNameSpace(rs.getString("namespace"));
					po.setGroupName(rs.getString("tair_group_name"));
					po.setData1(rs.getLong("invoking_num"));
					po.setData2(rs.getLong("invoking_time"));
					tairList.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return tairList;
	}
	
	/**
	 * 
	 * 根据namespace来获取tair数据库中的信息
	 * @return
	 */
	public List<TairInfoPo> findTairInfoByNamespace(String appName, String groupName, String namespace, String date) {
		String sql = "select * from csp_tair_provider_app_detail where app_name=? and tair_group_name=? and namespace=? and collect_time=?";
		
		final List<TairInfoPo> tairList = new ArrayList<TairInfoPo>();
		try {
			
			this.query(sql, new Object[]{appName, groupName, namespace, date}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					TairInfoPo po = new TairInfoPo();
					po.setActionType(rs.getString("action_type"));
					po.setSiteName(rs.getString("tair_site_name"));
					po.setNameSpace(rs.getString("namespace"));
					po.setGroupName(rs.getString("tair_group_name"));
					po.setHostIp(rs.getString("tair_host_ip").substring(0, rs.getString("tair_host_ip").indexOf(":")));
					po.setData1(rs.getLong("invoking_num"));
					po.setData2(rs.getLong("invoking_time"));
					tairList.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return tairList;
	}
	
	/**
	 * 
	 * 根据actionType来获取tair数据库中的信息
	 * @return
	 */
	public List<TairInfoPo> findTairInfoByAction(String actionType, String groupName, String date) {
		String sql = "select * from csp_tair_provider_app_detail where action_type=? and tair_group_name=? and collect_time=?";
		
		final List<TairInfoPo> tairList = new ArrayList<TairInfoPo>();
		try {
			
			this.query(sql, new Object[]{actionType, groupName, date}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					TairInfoPo po = new TairInfoPo();
					po.setAppName(rs.getString("app_name"));
					po.setActionType(rs.getString("action_type"));
					po.setSiteName(rs.getString("tair_site_name"));
					po.setNameSpace(rs.getString("namespace"));
					po.setGroupName(rs.getString("tair_group_name"));
					po.setHostIp(rs.getString("tair_host_ip").substring(0, rs.getString("tair_host_ip").indexOf(":")));
					po.setData1(rs.getLong("invoking_num"));
					po.setData2(rs.getLong("invoking_time"));
					tairList.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return tairList;
	}
	
	
	/**
	 * 用于查找uic的调用前20 
	 * @param actionType
	 * @param date
	 * @return
	 */
	
	public Map<String, Long> findTairInfoTop(String actionType, String date, int top) {
		
		String sql = "SELECT app_name,SUM(invoking_num) FROM  csp_tair_provider_app_detail WHERE collect_time=? " +
				"AND action_type=? AND tair_host_ip IN ('172.23.165.9:5191','172.23.184.12:5191','172.23.165.19:5191'" +
				",'172.23.164.19:5191','172.23.164.29:5191','172.23.201.29:5191','172.23.173.23:5191','172.23.184.11:5191" +
				" ','172.23.183.36:5191','172.23.176.48:5191','172.23.176.23:5191','172.23.183.12:5191','172.23.164.39:5191','" +
				"172.23.163.9:5191','172.23.179.23:5191','172.23.174.23:5191','172.23.172.48:5191','172.23.164.10:5191'," +
				"'172.23.175.24:5191','172.23.179.24:5191','172.24.158.15:5191','172.24.66.133:5191','172.24.66.122:5191','172.24.66.109:5191'" +
				" ,'172.24.158.12:5191','172.24.158.14:5191','172.24.158.17:5191','172.24.66.110:5191','172.24.66.116:5191'" +
				",'172.24.66.120:5191','172.24.66.107:5191','172.24.66.117:5191','172.24.158.7:5191','172.24.66.137:5191'" +
				",'172.24.66.118:5191','172.24.158.1:5191','172.24.66.108:5191','172.24.142.24:5191','172.24.66.121:5191'" +
				",'172.24.66.129:5191') GROUP BY app_name ORDER BY SUM(invoking_num) DESC limit ?;";
		
		final Map<String, Long> dataMap = new HashMap<String, Long>();
		
		try {
			
			this.query(sql, new Object[]{date, actionType, top}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					dataMap.put(rs.getString(1), rs.getLong(2));
				}});
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return dataMap;
	}
	
	
	/**
	 * 已分组形式查找tair的调用情况
	 * @param groupName
	 * @param date
	 * @return
	 */
	public List<TairNamespacePo> findTairProviderSummaryList(String groupName, String date) {
		
		String sql = "select * from csp_tair_provide_app_summary where tair_group_name=? and collect_time=?";
		
		final List<TairNamespacePo> tairList = new ArrayList<TairNamespacePo>();
		try {
			
			this.query(sql, new Object[]{groupName, date}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					TairNamespacePo po = new TairNamespacePo();
					po.setAppName(rs.getString("app_name"));
					po.setTairGroupName(rs.getString("tair_group_name"));
					po.setNamespace(rs.getString("namespace"));
					po.setRushQps(rs.getDouble("rush_time_qps"));
					po.setRushRt(rs.getDouble("rush_time_rt"));
					po.setCallSumNum(rs.getLong("invoking_all_num"));
					po.createSiteMap(rs.getString("room_feature"));
					tairList.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return tairList;
	}
	
	public List<TairNamespacePo> findTairProviderChartByAppName(String groupName, String appName, String startDate, String endDate) {
		String sql = "select * from csp_tair_provide_app_summary where tair_group_name=? and app_name=? and collect_time between ? and ?";
		final List<TairNamespacePo> tairList = new ArrayList<TairNamespacePo>();
		try {
			
			this.query(sql, new Object[]{groupName, appName, startDate, endDate}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					TairNamespacePo po = new TairNamespacePo();
					po.setAppName(rs.getString("app_name"));
					po.setTairGroupName(rs.getString("tair_group_name"));
					po.setNamespace(rs.getString("namespace"));
					po.setRushQps(rs.getDouble("rush_time_qps"));
					po.setRushRt(rs.getDouble("rush_time_rt"));
					po.setCallSumNum(rs.getLong("invoking_all_num"));
					po.createSiteMap(rs.getString("room_feature"));
					po.setCollectTime(rs.getString("collect_time"));
					tairList.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		return tairList;
	}
}
