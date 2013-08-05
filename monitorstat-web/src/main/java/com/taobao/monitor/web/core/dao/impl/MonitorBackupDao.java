package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.util.TableNameConverUtil;
import com.taobao.monitor.web.cache.KeyCache;

public class MonitorBackupDao extends MysqlRouteBase{
	
	private static final Logger logger = Logger.getLogger(MonitorBackupDao.class);

	
	public MonitorBackupDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_backup"));
	}
	
	/**
	 * 添加备份数据
	 * @param time
	 * @param app
	 * @param key
	 * @param value
	 */
	public void addMonitorData(String time, long app, long key, long value) {
		try {
			String tableTime = time.replaceAll("-", "").substring(4, 8);
			String sql = "insert into MS_MONITOR_BACKUP_" + tableTime
					+ "(APP_ID,KEY_ID,M_DATA,COLLECT_TIME) values(?,?,?,?)";
			this.execute(sql, new Object[] { app, key, value, time });
		} catch (Exception e) {
			logger.error("addMonitorData 出错,", e);
		}
	}
	
	/**
	 * 传入KeyValuePo的map遍历插入数据库
	 * @param map
	 */
	public void addMonitorData(Map<String, KeyValuePo> map) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			for(Object key : map.keySet()) {
				
				Date date = map.get(key).getCollectTime();
				String timeStr = sdf1.format(map.get(key).getCollectTime());
				String tableTime = sdf.format(date).replaceAll("-", "").substring(4, 8);
				String sql = "insert into MS_MONITOR_BACKUP_" + tableTime
				+ "(APP_ID,KEY_ID,M_DATA,COLLECT_TIME) values(?,?,?,?)";
				this.execute(sql, new Object[] { map.get(key).getAppId(), map.get(key).getKeyId(), map.get(key).getBackupSum(), timeStr });
			}
		} catch (Exception e) {
			logger.error("addMonitorData 出错,", e);
		}
		
	}
	
	/**
	 * 查询时间内的 数据，按照监控点分割
	 * 
	 * @param keyId
	 * @param appName
	 * @param start
	 * @param end
	 * @return Map<site,List<KeyValuePo>>
	 */
	public Map<String, List<KeyValuePo>> findKeyValueSiteByDate(int appId,int keyId,  java.util.Date start,
			java.util.Date end) {

		String sql = "select c.app_id,c.key_id,c.m_data,c.collect_time from "
				+ getTableName(start) + " c where c.key_id =? and c.app_id=? and c.collect_time between ? and ?";

		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		final List<KeyValuePo> list = new ArrayList<KeyValuePo>();
		Map<String, List<KeyValuePo>> map = new HashMap<String, List<KeyValuePo>>();

		try {
			final String key_name = KeyCache.get().getKey(keyId).getKeyName();
			
			this.query(sql, new Object[] { keyId, appId, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					int keyId = rs.getInt("key_id");
					String m_data = rs.getString("m_data");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String time = parseLogFormatDate.format(date);
					KeyValuePo po = new KeyValuePo();
					po.setCollectTime(date);
					po.setCollectTimeStr(time);
					//Map<Integer, Double> siteMap = po.getSiteValueMap();
					
					Map<Integer, Double> m = new HashMap<Integer,Double>();
					m.put(1, Double.parseDouble(m_data));
					po.setSiteValueMap(m);
					po.setKeyName(key_name);		
					po.setAppId(appId);
					po.setKeyId(keyId);
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		map.put("历史数据", list);

		return map;
	}

	
	public String getTableName(Date date) {
		return TableNameConverUtil.formatBackupTableName(date);
	}
	
}
