/**
 * 
 */
package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2010-4-16 上午11:21:55
 */
/**
 * @author xiaodu
 * 
 */
public class MonitorBaseLineDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(MonitorBaseLineDao.class);


	/**
	 * 根据keyId 取得基线表中的 MS_MONITOR_DATA_BASELINE
	 * 
	 * @param keyId
	 * @param appName
	 * @return
	 */
	public Map<String, KeyValueBaseLinePo> findKeyBaseValueByDate(int appId, int keyId) {
		String sql = "select c.collect_time,c.BASELINE_DATA"
				+ " from MS_MONITOR_DATA_BASELINE c  where c.key_id =? and c.app_id=? ";
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		final Map<String, KeyValueBaseLinePo> timeMap = new HashMap<String, KeyValueBaseLinePo>();
		try {
			this.query(sql, new Object[] { keyId, appId }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String time = parseLogFormatDate.format(date);
					KeyValueBaseLinePo po = timeMap.get(time);
					if (po == null) {
						po = new KeyValueBaseLinePo();
						timeMap.put(time, po);
						po.setBaseLineValue(rs.getDouble("BASELINE_DATA"));
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return timeMap;
	}

	public void addMonitorDateBaseLine(KeyValueBaseLinePo po) {
		String sql = "insert into MS_MONITOR_DATA_BASELINE(KEY_ID,APP_ID,BASELINE_DATA,SAMEDAY_DATA,YESTERDAY_DATA,COLLECT_TIME,GMT_CREATE)"
				+ " values(?,?,?,?,?,?,NOW())";
		try {
			this.execute(sql, new Object[] { po.getKeyId(), po.getAppId(), po.getBaseLineValue(), po.getSameDayValue(),
					po.getYesterdayValue(), po.getCollectTime() });
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	/**
	 * 批量插入基线数据
	 * @param list
	 */
	public void addMonitorDateBaseLineByList(List<KeyValueBaseLinePo> list) {
		
		if(list.size() == 0) {
			return;
		}
		
		String sql = "insert into MS_MONITOR_DATA_BASELINE(KEY_ID,APP_ID,BASELINE_DATA,SAMEDAY_DATA,YESTERDAY_DATA,COLLECT_TIME,GMT_CREATE)"
				+ " values(?,?,?,?,?,?,NOW())";
		
		List<Object[]> paramsList = new ArrayList<Object[]>();
		for(KeyValueBaseLinePo po: list) {
			paramsList.add(new Object[] { po.getKeyId(), po.getAppId(), po.getBaseLineValue(), po.getSameDayValue(),
					po.getYesterdayValue(), po.getCollectTime() });
		}

		try {
			this.executeBatch(sql, paramsList);
		} catch (SQLException e) {
			logger.error("", e);
		}
		
	}	

	public void deleteAppKeyBaseLine(int appId, int keyId) {
		String sql = "delete from MS_MONITOR_DATA_BASELINE where app_id=? and key_id=?";

		try {
			this.execute(sql, new Object[] { appId, keyId });
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	/**
	 * 删除MS_MONITOR_DATA_BASELINE的所有记录
	 * 
	 */
	public void deleteAllBaseLine() {
		String sql = "delete from MS_MONITOR_DATA_BASELINE";
		try {
			this.execute(sql);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	/**
	 * 查询基线数据 根据时间点来获取
	 * 
	 * @param appId
	 * @param keyId
	 * @param dateList
	 * @return
	 */
	public Map<Date, KeyValueBaseLinePo> findKeyBaseValueByDate(int appId, int keyId, List<Date> dateList) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] times = new String[dateList.size()];
		for (int i = 0; i < dateList.size(); i++) {
			times[i] = sdf.format(dateList.get(i));
		}

		final Map<Date, KeyValueBaseLinePo> mapDateValue = new HashMap<Date, KeyValueBaseLinePo>();

		String sql = "select * from MS_MONITOR_DATA_BASELINE where app_id=? and key_id=? and collect_time in ("
				+ Utlitites.formatArray2Sqlin(times) + ")";

		try {
			this.query(sql, new Object[] { appId, keyId }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					KeyValueBaseLinePo po = mapDateValue.get(date);
					if (po == null) {
						po = new KeyValueBaseLinePo();
						mapDateValue.put(date, po);
						po.setBaseLineValue(rs.getDouble("BASELINE_DATA"));
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return mapDateValue;
	}

	/**
	 * 查询基线数据 根据时间点来获取
	 * 
	 * @param appId
	 * @param keyId
	 * @param dateList
	 * @return
	 */
	public KeyValueBaseLinePo findKeyBaseValueByDate(int appId, int keyId, Date date) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		final KeyValueBaseLinePo po = new KeyValueBaseLinePo();

		String sql = "select * from MS_MONITOR_DATA_BASELINE where app_id=? and key_id=? and collect_time =?";

		try {
			this.query(sql, new Object[] { appId,keyId , sdf.format(date)+":00" }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					po.setBaseLineValue(rs.getDouble("BASELINE_DATA"));
					po.setCollectTime(date);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getCollectTime()==null?null:po;
	}
	
	/**
	 * 根据传入的时间间隔查询基线表
	 * @param appId
	 * @param keyId
	 * @param beforeDate
	 * @param currentDate
	 * @return
	 */
	public List<KeyValueBaseLinePo> findKeyValuePoByinterval(int appId, int keyId, Date beforeDate, Date currentDate) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		
		final List<KeyValueBaseLinePo> list = new ArrayList<KeyValueBaseLinePo>();
		
		String sql = "select * from MS_MONITOR_DATA_BASELINE where app_id=? and key_id=? and collect_time between ? and ?";
		
		try {
			this.query(sql, new Object[] { appId,keyId , sdf.format(beforeDate)+":00",  sdf.format(currentDate)+":00"}, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					KeyValueBaseLinePo po = new KeyValueBaseLinePo();
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					po.setBaseLineValue(rs.getDouble("BASELINE_DATA"));
					po.setCollectTime(date);
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	/**
	 * 根据keyId 取得基线表中的 MS_MONITOR_DATA_BASELINE
	 * @param keyId
	 * @param appName
	 * @return
	 */
	public List<KeyValuePo> findKeyBaseValue(int appId, int keyId) {

		String sql = "select c.collect_time,c.BASELINE_DATA,k.key_value" +
		" from MS_MONITOR_DATA_BASELINE c,ms_monitor_key k " +
		" where k.key_id =? and c.key_id=k.key_id and c.app_id=? " ;
		final List<KeyValuePo> poList = new ArrayList<KeyValuePo>(); 
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		
		final Map<String, KeyValuePo> timeMap = new HashMap<String, KeyValuePo>();
		
		
		try {			
			this.query(sql, new Object[]{keyId, appId}, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
				
					String m_data = rs.getString("BASELINE_DATA") ;
					String key_name = rs.getString("key_value");
					Timestamp collectTime = rs.getTimestamp("collect_time") ; 
					Date date = new Date(collectTime.getTime());
					String time =parseLogFormatDate.format(date);					
					KeyValuePo po = timeMap.get(time);
					if(po==null){
						po = new KeyValuePo();
						timeMap.put(time, po);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);
						po.setKeyName(key_name);
						po.setValueStr(m_data);
					}					
				}
			}) ;
		} catch (Exception e) {
			logger.error("", e);
		}
		poList.addAll(timeMap.values());
		Collections.sort(poList);
		return poList ; 
	}

}
