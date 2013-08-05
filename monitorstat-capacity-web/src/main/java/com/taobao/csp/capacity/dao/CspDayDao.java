
package com.taobao.csp.capacity.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.capacity.po.PvcountPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2011-9-19 下午05:00:40
 */
public class CspDayDao extends MysqlRouteBase{
	
	public CspDayDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_DAY"));
	}
	
	private static final Logger logger =  Logger.getLogger(CspDayDao.class);
	
	
	
	private static String formatSet2Sqlin(Collection<Integer> args) {

		if (args != null && args.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Integer id:args) {
				sb.append("'" + id + "'").append(",");				
			}
			return sb.substring(0, sb.length()-1);
		}
		return "-1";
	}
	
	
	
	
	/**
	 * 查询统计表中时间段内的数据
	 * @param appId
	 * @param keyId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<KeyValuePo> findMonitorCountByDate(int appId,int keyId,Date startDate,Date endDate){
		
		logger.info("sql MonitorDayDao:findMonitorCountByDate");
		
		final List<KeyValuePo> keyvalueList = new ArrayList<KeyValuePo>();
		
		String sql = "select * from ms_monitor_count where app_id = ? and key_id = ? and collect_time >= ? and collect_time <=?";
		try {
			this.query(sql, new Object[]{appId,keyId,startDate,endDate}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					KeyValuePo po = new KeyValuePo();
					po.setAppId(rs.getInt("app_id"));
					po.setKeyId(rs.getInt("key_id"));
					Timestamp time = rs.getTimestamp("collect_time");
					po.setCollectTime(new Date(time.getTime()));
					po.setValueStr(rs.getString("m_data"));
					keyvalueList.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		//
		return keyvalueList;
	}
	
	
	/**
	 * 获取最近30天内容最大的QPS
	 * @param appId
	 * @param ids
	 * @param date
	 * @return
	 */
	public Map<Integer,Double> findKeyValueFromCountMonthMax(int appId,Collection<Integer> ids,Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Date endDate = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -30);
		Date startDate = cal.getTime();
		
		String sql = "select c.key_id,c.m_data from ms_monitor_count c where c.collect_time >= ? and c.collect_time <= ? " +
				" and c.app_id = ? and c.key_id in ("+formatSet2Sqlin(ids)+")";
		
		Object[] objs = new Object[]{startDate, endDate, appId};
		
		final Map<Integer,Double> map = new HashMap<Integer, Double>();
		
		try {
			this.query(sql, objs, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					String mData = rs.getString("m_data");	
					int key_id = rs.getInt("key_id");
					Double d = map.get(key_id);
					if(d != null){
						if(Double.parseDouble(mData) > d){
							map.put(key_id, Double.parseDouble(mData));
						}
					}else{
						map.put(key_id, Double.parseDouble(mData));
					}
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}
	
	
	public Map<Integer,Double> findKeyValueFromCountByDay(int appId,Collection<Integer> ids,Date date){
		
		
		String sql = "select c.key_id,c.m_data from ms_monitor_count c where DATE_FORMAT(c.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and c.app_id = ? and c.key_id in ("+formatSet2Sqlin(ids)+")";
		Object[] objs = new Object[]{date, appId};
		
		final Map<Integer,Double> map = new HashMap<Integer, Double>();
		
		try {
			this.query(sql, objs, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					String mData = rs.getString("m_data");	
					int key_id = rs.getInt("key_id");
					Double d = map.get(key_id);
					if(d != null){
						if(Double.parseDouble(mData) > d){
							map.put(key_id, Double.parseDouble(mData));
						}
					}else{
						map.put(key_id, Double.parseDouble(mData));
					}
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}
	
	public Map<Integer,String> findDataFromCountByDay(int appId,Collection<Integer> ids,Date date){
		String sql = "select c.key_id,c.m_data from ms_monitor_count c where DATE_FORMAT(c.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and c.app_id = ? and c.key_id in ("+formatSet2Sqlin(ids)+")";
		Object[] objs = new Object[]{date, appId};
		
		final Map<Integer,String> map = new HashMap<Integer,String>();
		
		try {
			this.query(sql, objs, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					String mData = rs.getString("m_data");	
					int key_id = rs.getInt("key_id");
					map.put(key_id, mData);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}
	
	
	/**
	 * 
	 * @param keyId
	 * @param appId
	 * @param date
	 * @return
	 */
	public KeyValuePo findKeyValueFromCountByDay(int keyId,int appId,Date date){
		
		
		
		logger.info("sql MonitorDayDao:findKeyValueFromCountByDate");
		
		String sql = "select c.app_id,c.m_data from ms_monitor_count c where  DATE_FORMAT(c.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")  and c.app_id = ? and c.key_id=?";
		
		Object[] objs = new Object[]{date,appId,keyId};
		
		final KeyValuePo po = new KeyValuePo();
		
		try {
			this.query(sql, objs, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					po.setAppId(appId);
					String mData = rs.getString("m_data");	
					if(po.getValueStr()!=null){
						if(Double.parseDouble(po.getValueStr()) <Double.parseDouble(mData)){
							po.setValueStr(mData);
						}
					}else{
						po.setValueStr(mData);
					}
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getAppId()==null?null:po;
	}
	
	/**
	 * 
	 * @param keyId
	 * @param appId
	 * @param date
	 * @return  如果出现多次将会是最后一个
	 */
	public KeyValuePo findKeyValueFromCountByDayWithStringValue(int keyId,int appId,Date date){
		
		logger.info("sql MonitorDayDao:findKeyValueFromCountByDate");
		
		String sql = "select c.app_id,c.m_data from ms_monitor_count c where  DATE_FORMAT(c.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")  and c.app_id = ? and c.key_id=?";
		
		Object[] objs = new Object[]{date,appId,keyId};
		
		final KeyValuePo po = new KeyValuePo();
		
		try {
			this.query(sql, objs, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					po.setAppId(appId);
					String mData = rs.getString("m_data");
					po.setValueStr(mData);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getAppId()==null?null:po;
	}
	
	/**
	 * 获取最近30天，最大的值
	 * @param keyId
	 * @param appId
	 * @param date
	 * @return
	 */
	public KeyValuePo findMaxValueFromCountRecentlyDay(int keyId,int appId,Date date){
		
		logger.info("sql MonitorDayDao:findKeyValueFromCountByDate");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		
		Date endDate = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -30);
		Date startDate = cal.getTime();
		
		String sql = "select c.app_id,c.m_data from ms_monitor_count c where c.collect_time >= ? and c.collect_time <= ?  and c.app_id = ? and c.key_id=?";
		
		Object[] objs = new Object[]{startDate,endDate,appId,keyId};
		
		final KeyValuePo po = new KeyValuePo();
		
		try {
			this.query(sql, objs, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					po.setAppId(appId);
					String mData = rs.getString("m_data");	
					if(po.getValueStr()!=null){
						if(Double.parseDouble(po.getValueStr()) <Double.parseDouble(mData)){
							po.setValueStr(mData);
						}
					}else{
						po.setValueStr(mData);
					}
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getAppId()==null?null:po;
	}
	
	public Map<Date,Double> findAllHSF(int appDayId){
		
		String sql = "SELECT sum(m_data) as m,collect_time FROM ms_monitor_count d " +
		"WHERE d.app_id=? AND d.key_id IN " +
		"(SELECT key_id FROM ms_monitor_key WHERE key_value LIKE 'IN_HSF-ProviderDetail\\_%\\_COUNTTIMES')  group by collect_time";
		
		
		final Map<Date,Double> dataMap = new HashMap<Date, Double>();
		try {
			this.query(sql, new Object[]{appDayId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Date d = rs.getTimestamp("collect_time");
					if(d != null){
						Double m = rs.getDouble("m");
						dataMap.put(d, m);
					}
				}});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dataMap;
	}


}
