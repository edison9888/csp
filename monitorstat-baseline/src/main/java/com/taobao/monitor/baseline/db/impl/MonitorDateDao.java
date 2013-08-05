
package com.taobao.monitor.baseline.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * 操作日报 库表的类
 * @author xiaodu
 * @version 2010-5-26 上午09:28:32
 */
public class MonitorDateDao extends MysqlRouteBase{
	
	private static final Logger logger =  Logger.getLogger(MonitorDateDao.class);

	public MonitorDateDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Main_DAY"));
	}
	
	/**
	 * 通过收集日期 取得日报库中的 ms_monitor_count 中的数据
	 * @param collectDate
	 * @return key 为keyName 
	 */
	public Map<String ,Map<String,KeyValueBaseLinePo>> findMonitorValueByDate(Date collectDate){
		
		String sql = "select a.app_id,a.app_name,k.key_id,k.key_value,c.m_data,c.collect_time" + 
        " from ms_monitor_count c,ms_monitor_key k,ms_monitor_app a " +
        " where a.app_id = c.app_id and k.key_id = c.key_id " +
        " and c.collect_time = ? ";
		
		final Map<String ,Map<String,KeyValueBaseLinePo>> appMap = new HashMap<String, Map<String,KeyValueBaseLinePo>>();
		
		try {
			this.query(sql,new Object[]{collectDate}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					
					String keyName = rs.getString("key_value");
					String appName = rs.getString("app_name");
					double m_data = rs.getDouble("m_data");
					int appId = rs.getInt("app_id");
					int keyId = rs.getInt("key_id");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					
					Map<String,KeyValueBaseLinePo> keyValueMap = appMap.get(appName);
					if(keyValueMap==null){
						keyValueMap = new HashMap<String, KeyValueBaseLinePo>();
						appMap.put(appName, keyValueMap);
					}
					
					KeyValueBaseLinePo po = keyValueMap.get(keyName);
					if(po==null){
						po = new KeyValueBaseLinePo();
						po.setAppName(appName);
						po.setAppId(appId);
						po.setKeyId(keyId);
						po.setValue(m_data);
						po.setCollectTime(new Date(collectTime.getTime()));						
						keyValueMap.put(keyName, po);
					}else{
						logger.info("怎么会存在相同的key:"+keyName);
					}
					
					
				}});
		} catch (Exception e) {
			logger.debug("",e);
		}
		return appMap;
	}
	
	
	/**
	 * 往基线表里插入数据
	 * @param po
	 */
	public void addMonitorDateBaseLine(KeyValueBaseLinePo po){
		String sql = "insert into MS_MONITOR_COUNT_BASELINE(KEY_ID,APP_ID,BASELINE_DATA,SAMEDAY_DATA,YESTERDAY_DATA,COLLECT_TIME,GMT_CREATE)" +
		" values(?,?,?,?,?,?,NOW())";

		try {
			this.execute(sql, new Object[]{po.getKeyId(),po.getAppId(),po.getBaseLineValue(),po.getSameDayValue(),po.getYesterdayValue(),po.getCollectTime()});
		} catch (SQLException e) {
			logger.error("", e);
		}
		
	}
	
	

}
