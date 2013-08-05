package com.taobao.monitor.common.db.impl.capacity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * 
 * @author xiaodu
 * @version 2010-9-2 上午09:18:05
 */
public class CspLoadRunDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(CspLoadRunDao.class);
	
	
	public CspLoadRunDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("csp_autoload"));
	}
	
	
	
	public AutoLoadType findLoadRunTypeByAppId(int appId) {
		
		
		
		String sql = "select loadrun_type from ms_monitor_loadrun_host where app_id=? ";
		try {
			String type = this.getString(sql, new Object[]{appId});
			if (type == null) {
				logger.warn("load tyoe is null");
				return null;
			}
			return AutoLoadType.valueOf(type);
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	
	/***
	 * 返回近期的压测qps
	 * 返回0代表近期未做过压测
	 * @param appId
	 * @return
	 */
	public double findRecentlyAppLoadRunQps(int appId){
		
		if(appId==8){
			System.out.println(8);
		}
		
		AutoLoadType type = findLoadRunTypeByAppId(appId);
		
		if(type == null){
			return 0;
		}
		double maxQps = 0;
		
		Date date = findRecentlyLoadDate(appId,type.getQpsKey());
		
		// date不为null，近期做个压测
		if(date !=null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			Date endDate = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			Date startDate = cal.getTime();
			
			List<LoadrunResult> list = findLoadrunResult(appId,type.getQpsKey(),startDate,endDate);
			
			if (type != AutoLoadType.apache && type != AutoLoadType.nginxProxy && type != AutoLoadType.apacheProxy) {
				for(LoadrunResult r:list){
					double qps = r.getValue();
					if (maxQps < qps) {
						maxQps = qps;
					}
				}
			} else {
				// apache与nginx类型的压测，与日志的不确定性有关系
				double maxApacheQps = 0;
				double maxTomcatQps = 0;
				for(LoadrunResult r:list){
					double qps = r.getValue();
					ResultKey key = r.getKey();
					if (key == ResultKey.Apache_Pv) {
						if (qps > maxApacheQps) {
							maxApacheQps = qps;
						}
					} else if (key == ResultKey.Tomcat_Pv) {
						if (qps > maxTomcatQps) {
							maxTomcatQps = qps;
						}
					}
				}
				
				if (type == AutoLoadType.apache) {
					maxQps = maxTomcatQps == 0 ? maxApacheQps : maxTomcatQps;
				} else {
					maxQps = maxApacheQps == 0 ? maxTomcatQps : maxApacheQps;
				}
			}
		}
		return maxQps;
		
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
		
		String sql = "select * from ms_monitor_loadrun_result where app_id=? and (loadrun_key=? or loadrun_key=?) and collect_time >=? and collect_time <=?";
		ResultKey orKey = key;
		if (key == ResultKey.Apache_Pv) {
			orKey = ResultKey.Tomcat_Pv;
		}
		if (key == ResultKey.Tomcat_Pv) {
			orKey = ResultKey.Apache_Pv;
		}
		
		final List<LoadrunResult> listLoad = new ArrayList<LoadrunResult>();
		
		try {
			this.query(sql, new Object[]{appId,key.name(),orKey.name(),start,end}, new SqlCallBack(){
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
	public Date findRecentlyLoadDate(Integer appId,ResultKey key){
		String sql = "select max(collect_time) from ms_monitor_loadrun_result where app_id=? and loadrun_key=?";
		try {
			return this.getDateValue(sql, new Object[]{appId,key.name()});
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
}
