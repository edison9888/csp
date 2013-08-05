package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.HttpLoadLogType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.core.po.LoadRunHost;

/**
 * 
 * @author xiaodu
 * @version 2010-9-2 上午09:18:05
 */
public class MonitorLoadRunDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(MonitorLoadRunDao.class);
	
	
	public MonitorLoadRunDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("csp_autoload"));
	}

	private void fillValue(LoadRunHost loadRunHost,ResultSet rs) throws SQLException{
		loadRunHost.setAppId(rs.getInt("app_id"));
		loadRunHost.setHostIp(rs.getString("host_ip"));
		loadRunHost.setStartTime(rs.getString("start_time"));
		loadRunHost.setLoadAuto(rs.getInt("load_auto"));
		
		
		
		loadRunHost.setLoadType(AutoLoadType.valueOf(rs.getString("loadrun_type")));
		
		
		
		loadRunHost.setLimitFeature(rs.getString("limit_feature"));
		loadRunHost.setLoadFeature(rs.getString("load_feature"));
		loadRunHost.setConfigFeature(rs.getString("config_feature"));
		loadRunHost.setWangwangs(rs.getString("wangwangs"));
		loadRunHost.setUserName(rs.getString("user_name"));
		loadRunHost.setPassword(rs.getString("user_password"));
		loadRunHost.setJkConfigPath(rs.getString("jk_config_path"));
		loadRunHost.setApachectlPath(rs.getString("apachectl_path"));
		loadRunHost.setApache_default_config(rs.getString("apache_config_default"));
		loadRunHost.setApache_split_config(rs.getString("apache_config_split"));
		loadRunHost.setHttploadpath(rs.getString("http_load_path"));
		loadRunHost.setHttploadProxy(rs.getString("http_load_proxy"));
		loadRunHost.setHttploadlogtype(HttpLoadLogType.valueOf(rs.getString("http_load_log_type")));
		loadRunHost.setHttploadloglog(rs.getString("http_load_log"));
	}

	
	public List<LoadRunHost> findAllLoadRunHost() {
		String sql = "select * from ms_monitor_loadrun_host ";

		final List<LoadRunHost> list = new ArrayList<LoadRunHost>();

		try {
			this.query(sql, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					
					LoadRunHost loadRunHost = new LoadRunHost();
					fillValue(loadRunHost,rs);
					list.add(loadRunHost);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;

	}
	
	/**
	 * 根据appid取得指定LoadRunHost
	 * 
	 * @return
	 */
	public LoadRunHost findLoadRunHostByAppId(int appId) {
		String sql = "select * from ms_monitor_loadrun_host where app_id=? ";
		final LoadRunHost loadRunHost = new LoadRunHost();
		try {
			this.query(sql, new Object[]{appId},new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					
					fillValue(loadRunHost,rs);

				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return loadRunHost;
		
	}
	
	public List<LoadrunResult> findLoadrunResult(int appId,Date collectTime){
		
		String sql = "select * from ms_monitor_loadrun_result where app_id=? and DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		final List<LoadrunResult> listLoad = new ArrayList<LoadrunResult>();
		
		try {
			this.query(sql, new Object[]{appId,collectTime}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					LoadrunResult result = new LoadrunResult();
					result.setAppId(rs.getInt("app_id"));
					result.setCollectTime(rs.getDate("collect_time"));
					result.setControlFeature(rs.getString("control_feature"));
					result.setKey(ResultKey.valueOf(rs.getString("loadrun_key")));
					result.setLoadrunOrder(rs.getInt("loadrun_order"));
					result.setLoadrunType(AutoLoadType.valueOf(rs.getString("loadrun_type")));
					result.setTargetIp(rs.getString("target_ip"));
					result.setValue(rs.getDouble("loadrun_value"));
					result.setLoadId(rs.getString("loadrun_id"));
					listLoad.add(result);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return listLoad;
	}
	
	/**
	 * 
	 * @param appId
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<LoadrunResult> findLoadrunResult(int appId,ResultKey key,Date start,Date end){
		
		String sql = "select * from ms_monitor_loadrun_result where app_id=? and loadrun_key=? and collect_time >=? and collect_time <=?";
		
		final List<LoadrunResult> listLoad = new ArrayList<LoadrunResult>();
		
		try {
			this.query(sql, new Object[]{appId,key.name(),start,end}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					LoadrunResult result = new LoadrunResult();
					result.setAppId(rs.getInt("app_id"));
					result.setCollectTime(rs.getDate("collect_time"));
					result.setControlFeature(rs.getString("control_feature"));
					result.setKey(ResultKey.valueOf(rs.getString("loadrun_key")));
					result.setLoadrunOrder(rs.getInt("loadrun_order"));
					result.setLoadrunType(AutoLoadType.valueOf(rs.getString("loadrun_type")));
					result.setTargetIp(rs.getString("target_ip"));
					result.setValue(rs.getDouble("loadrun_value"));
					result.setLoadId(rs.getString("loadrun_id"));
					listLoad.add(result);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return listLoad;
	}
	
	/**
	 * 取得在某个时间的 key的数据
	 * @param appId
	 * @param key
	 * @param collectTime
	 * @return
	 */
	public List<LoadrunResult> findLoadrunResult(int appId,ResultKey key,Date collectTime){
		
		String sql = "select * from ms_monitor_loadrun_result where app_id=? and loadrun_key=? and DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		final List<LoadrunResult> listLoad = new ArrayList<LoadrunResult>();
		
		try {
			this.query(sql, new Object[]{appId,key.name(),collectTime}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					LoadrunResult result = new LoadrunResult();
					result.setAppId(rs.getInt("app_id"));
					result.setCollectTime(rs.getDate("collect_time"));
					result.setControlFeature(rs.getString("control_feature"));
					result.setKey(ResultKey.valueOf(rs.getString("loadrun_key")));
					result.setLoadrunOrder(rs.getInt("loadrun_order"));
					result.setLoadrunType(AutoLoadType.valueOf(rs.getString("loadrun_type")));
					result.setTargetIp(rs.getString("target_ip"));
					result.setValue(rs.getDouble("loadrun_value"));
					result.setLoadId(rs.getString("loadrun_id"));
					listLoad.add(result);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return listLoad;
	}
	
	
	
	/**
	 * 取得最近一个压测时间
	 * @param appId
	 * @return
	 */
	public Date findRecentlyLoadDate(Integer appId){
		String sql = "select max(collect_time) from ms_monitor_loadrun_result where app_id=?";
		try {
			return this.getDateValue(sql, new Object[]{appId});
		} catch (SQLException e) {
			logger.error("", e);
		}
		return null;
	}
}
