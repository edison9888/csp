package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.po.MonitorStatisticsDataPo;

public class MonitorStatisticsDataDao extends MysqlRouteBase {
	
	private static final Logger logger = Logger.getLogger(MonitorStatisticsDataDao.class);
	
	/***
	 * 从limit表收集统计表的数据对象，这个方法只会在收集数据的时候用到
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public List<MonitorStatisticsDataPo> findStatisticsLimitData(int appId, int keyId) {
		final List<MonitorStatisticsDataPo> statisticsData = new ArrayList<MonitorStatisticsDataPo>();
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT app_id, key_id, COUNT(site_id) site_count, collect_time, SUM(m_data) m_data FROM \n");
		sqlBuffer.append("(SELECT app_id, key_id, site_id, SUM(m_data)  m_data, collect_time FROM ms_monitor_data_limit \n");
		sqlBuffer.append(" where app_id=? AND key_id=? GROUP BY app_id,key_id,collect_time,site_id) temp \n");
		sqlBuffer.append("GROUP BY app_id,key_id,collect_time ");
		String sql = sqlBuffer.toString();
		
		try {
			this.query(sql, new Object[] { appId, keyId }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					MonitorStatisticsDataPo po = new MonitorStatisticsDataPo();
					int appId = rs.getInt("app_id");
					int keyId = rs.getInt("key_id");
					int siteCount = rs.getInt("site_count");
					double totalData = rs.getDouble("m_data");
					Date collectTime = rs.getTimestamp("collect_time");
					po.setAppId(appId);
					po.setKeyId(keyId);
					po.setSiteCount(siteCount);
					po.setTotalData(totalData);
					po.setCollectTime(collectTime);
					
					statisticsData.add(po);
					
				}},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("findStatisticsLimitData error",e);
		}
		
		return statisticsData;
	}
	
	/***
	 * 往统计条插入数据
	 * @param po
	 */
	public void addStatisticsTotalData(MonitorStatisticsDataPo po){
		String sql ="INSERT INTO ms_monitor_data_total (app_id,key_id,site_count,total_data,collect_time) VALUES (?,?,?,?,?)";
		
		try {
			this.execute(sql,  
					new Object[] { po.getAppId(), po.getKeyId(), po.getSiteCount(), po.getTotalData(), po.getCollectTime() }, 
					DbRouteManage.get().getDbRouteByTimeAppid(po.getAppId()));
		}  catch (Exception e) {
			logger.error("addMonitorStatisticsData error,", e);
		}
	}
	
	/***
	 * 删除统计表的数据
	 * @param appId
	 * @param keyId
	 * @param collectTime
	 */
	public void deleteDuplicateStatisticsData(int appId, int keyId, Date collectTime) {
		String sql ="DELETE FROM ms_monitor_data_total WHERE app_id=? AND key_id=? AND collect_time =? ";
		
		try {
			this.execute(sql,  
					new Object[] { appId, keyId, collectTime }, 
					DbRouteManage.get().getDbRouteByTimeAppid(appId));
		}  catch (Exception e) {
			logger.error("deleteDuplicateStatisticsData error,", e);
		}
	}
	
	/***
	 * 按时间查找统计表的数据
	 * @param appId
	 * @param keyId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<MonitorStatisticsDataPo> findStatisticsData(int appId, int keyId, Date beginDate, Date endDate){
		final List<MonitorStatisticsDataPo> statisticsData = new ArrayList<MonitorStatisticsDataPo>();
		
		String sql = "SELECT app_id,key_id,site_count,total_data,collect_time FROM ms_monitor_data_total WHERE " + 
			" app_id=? AND key_id=? AND collect_time>=? AND collect_time<? ";
		
		try {
			this.query(sql, new Object[] { appId, keyId, beginDate, endDate }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					MonitorStatisticsDataPo po = new MonitorStatisticsDataPo();
					int appId = rs.getInt("app_id");
					int keyId = rs.getInt("key_id");
					int siteCount = rs.getInt("site_count");
					double totalData = rs.getDouble("total_data");
					Date collectTime = rs.getTimestamp("collect_time");
					po.setAppId(appId);
					po.setKeyId(keyId);
					po.setSiteCount(siteCount);
					po.setTotalData(totalData);
					po.setCollectTime(collectTime);
					
					statisticsData.add(po);
					
				}},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("findStatisticsLimitData error",e);
		}
		
		return statisticsData;
	}

}
