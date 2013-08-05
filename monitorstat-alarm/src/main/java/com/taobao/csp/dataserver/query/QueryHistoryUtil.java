
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.query;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dao.hbase.base.HBaseUtil;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.key.DBMediaKeyCache;
import com.taobao.csp.dataserver.util.Util;

/**
 * @author xiaodu
 *
 * 下午4:08:20
 */
public class QueryHistoryUtil {

	private static final Logger logger = Logger.getLogger(QueryHistoryUtil.class);
	
	/**
	 * ===============================全部走到queryData=====================================
	 */
	
	/**
	 * 查询某个IP 对应这个key 历史数据量
	 * @param appName
	 * @param keyName
	 * @param ip
	 * @param propertyname
	 * @param day
	 * @return
	 */
	public static Map<Date,String> querySingleHost(String appName,String keyName,String ip,String propertyname,Date day){
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Date start = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		
		Date end = cal.getTime();
		
		String fullName = Util.combinHostKeyName(appName, keyName, ip);
		Integer id=DBMediaKeyCache.get().getKeyId(keyName);
		if(id==null){
			logger.warn(keyName+",id为null querySingleHost1"); 
			return new HashMap<Date, String>();
		}
		String fullIdName = Util.combinHostKeyName(appName, String.valueOf(id), ip);
		
		return queryData(HBaseUtil.getMD5String(fullName),HBaseUtil.getMD5String(fullIdName),
				propertyname,start,end);
	}
	
	
	/**
	 * 查询某个IP 对应这个key 历史数据量
	 * @param appName
	 * @param keyName
	 * @param ip
	 * @param propertyname
	 * @param day
	 * @return
	 */
	public static Map<Date,String> querySingleHost(String appName,String keyName,String ip,String propertyname,Date startDate,Date endDate){
		
		String fullName = Util.combinHostKeyName(appName, keyName, ip);
		Integer id=DBMediaKeyCache.get().getKeyId(keyName);
		if(id==null){
			logger.warn(keyName+",id为null querySingleHost2"); 
			return new HashMap<Date, String>();
		}
		String fullIdName = Util.combinHostKeyName(appName, String.valueOf(id), ip);
		
		return queryData(HBaseUtil.getMD5String(fullName),HBaseUtil.getMD5String(fullIdName),
				propertyname,startDate,endDate);
		
	}
	
	
	private static Map<Date,String> queryData(String preKey,String preIdKey,
			String propertyName,Date startDate,Date endDate){
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		
		long start = cal.getTimeInMillis();
		
		cal.setTime(endDate);
		
		long end = cal.getTimeInMillis();
		
		return doIpQuery( preKey, preIdKey,propertyName,start,end);
	}
	

	private static Map<Date,String> doIpQuery(String preKey,String preIdKey,String propertyName,long start,long end){
		Map<Date,String> tmp = new HashMap<Date, String>();
		
		Map<String,String> map=new HashMap<String, String>();
		
		if(start>Constants.QUERY_DEAD_TIME){
			//开始时间大，全按id查
			String rowkeyStart = preIdKey + Constants.S_SEPERATOR + start;
			String rowkeyEnd = preIdKey + Constants.S_SEPERATOR + end;
			
			map = HBaseUtil.queryValueMap(rowkeyStart, rowkeyEnd, propertyName, String.class);
			
			for(Map.Entry<String, String> entry:map.entrySet()){
				String time = entry.getKey().substring(preIdKey.length()+1);
				String value = entry.getValue();
				tmp.put(new Date(Long.valueOf(time)), value);
			}
			
		}else if(end<Constants.QUERY_DEAD_TIME){
			//结束时间小，全按string查
			String rowkeyStart = preKey + Constants.S_SEPERATOR + start;
			String rowkeyEnd = preKey + Constants.S_SEPERATOR + end;
			
			map = HBaseUtil.queryValueMap(rowkeyStart, rowkeyEnd, propertyName, String.class);
			
			for(Map.Entry<String, String> entry:map.entrySet()){
				String time = entry.getKey().substring(preKey.length()+1);
				String value = entry.getValue();
				tmp.put(new Date(Long.valueOf(time)), value);
			}
			
		}else{
			String rowkeyStart = preKey + Constants.S_SEPERATOR +start ;
			String rowkeyEnd = preKey + Constants.S_SEPERATOR + Constants.QUERY_DEAD_TIME;
			
			map = HBaseUtil.queryValueMap(rowkeyStart, rowkeyEnd, propertyName, String.class);

			for(Map.Entry<String, String> entry:map.entrySet()){
				String time = entry.getKey().substring(preKey.length()+1);
				String value = entry.getValue();
				tmp.put(new Date(Long.valueOf(time)), value);
			}
			
			rowkeyStart = preIdKey + Constants.S_SEPERATOR + Constants.QUERY_DEAD_TIME;
			rowkeyEnd = preIdKey + Constants.S_SEPERATOR + end;
	
			map=HBaseUtil.queryValueMap(rowkeyStart, rowkeyEnd, propertyName, String.class);

			for(Map.Entry<String, String> entry:map.entrySet()){
				String time = entry.getKey().substring(preIdKey.length()+1);
				String value = entry.getValue();
				tmp.put(new Date(Long.valueOf(time)), value);
			}
		}
		return tmp;
	}

	/**
	 * 查询某个key的一天的数据
	 * @param appName
	 * @param keyName
	 * @param day
	 * @param c
	 * @return
	 */
	public static Map<Date,String> querySingle(String appName,String keyName,String propertyname,Date startDate,Date endDate){
		String fullName = Util.combinAppKeyName(appName, keyName);
		Integer id=DBMediaKeyCache.get().getKeyId(keyName);
		if(id==null){
			logger.warn(keyName+",id为null querySingle"); 
			return new HashMap<Date, String>();
		}
		String fullIdName = Util.combinHostKeyName(appName, String.valueOf(id));
		
		return queryData(HBaseUtil.getMD5String(fullName),HBaseUtil.getMD5String(fullIdName),
				propertyname,startDate,endDate);
		
	}

	/**
	 * 查询某个key的一天的数据
	 * @param appName
	 * @param keyName
	 * @param day
	 * @param c
	 * @return
	 */
	public static  Map<Date,String> querySingle(String appName,String keyName,String propertyname,Date day){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Date start = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		
		Date end = cal.getTime();
		
		
		return querySingle(appName,keyName,propertyname,start,end);
	}
	
	/**
	 * =============================app级别=============================
	 */

	
	public static Map<Date,Map<String,String>> queryMultiProperty(String appName,String keyName,String ip,
			String[] properties,Date day){
		
		
		Map<Date,Map<String,String>> tmp = new HashMap<Date, Map<String,String>>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Date start = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		
		Date end = cal.getTime();
		
		String fullName=Util.combinAppKeyName(appName, keyName);
		Integer id=DBMediaKeyCache.get().getKeyId(keyName);
		if(id==null){
			logger.warn(keyName+",id为null querySingleHost2"); 
			return tmp;
		}
		String fullIdNameString=Util.combinHostKeyName(appName, String.valueOf(id));
		
		if(StringUtils.isNotBlank(ip)){
			fullName=Util.combinAppKeyName(fullName, ip);
			fullIdNameString=Util.combinAppKeyName(fullIdNameString, ip);
		}
		
		String fullKey = HBaseUtil.getMD5String(fullName);
		String fullIdName = HBaseUtil.getMD5String(fullIdNameString);
		
		logger.warn("s,"+start.getTime()+"e,"+end.getTime()+"k,"+keyName
				+"id,"+id+","+ip);
		
		Map<String,Map<String,String>> map =new HashMap<String, Map<String,String>>();
		if(start.getTime()>Constants.QUERY_DEAD_TIME){
			//开始时间大，全按id查
			String rowkeyStart = fullIdName + Constants.S_SEPERATOR + start.getTime();
			String rowkeyEnd = fullIdName + Constants.S_SEPERATOR + end.getTime();
			
			map =HBaseUtil.queryValueMap(rowkeyStart, rowkeyEnd, properties);
			
			for(Map.Entry<String,Map<String,String>> entry:map.entrySet()){
				String preKey = entry.getKey();
				String time = preKey.substring(fullIdName.length()+1);
				Map<String,String> value = entry.getValue();
				tmp.put(new Date(Long.valueOf(time)), value);
			}
			logger.warn("come here1,"+map.size());
		}else if(end.getTime()<Constants.QUERY_DEAD_TIME){
			//结束时间小，全按string查
			String rowkeyStart = fullKey + Constants.S_SEPERATOR + start.getTime();
			String rowkeyEnd = fullKey + Constants.S_SEPERATOR + end.getTime();
			
			map = HBaseUtil.queryValueMap(rowkeyStart, rowkeyEnd, properties);

			for(Map.Entry<String,Map<String,String>> entry:map.entrySet()){
				String preKey = entry.getKey();
				String time = preKey.substring(fullKey.length()+1);
				Map<String,String> value = entry.getValue();
				tmp.put(new Date(Long.valueOf(time)), value);
			}
			logger.warn("come here2,"+map.size());
		}else{
			String rowkeyStart = fullKey + Constants.S_SEPERATOR +start.getTime() ;
			String rowkeyEnd = fullKey + Constants.S_SEPERATOR + Constants.QUERY_DEAD_TIME;
			
			map =HBaseUtil.queryValueMap(rowkeyStart, rowkeyEnd, properties);

			for(Map.Entry<String,Map<String,String>> entry:map.entrySet()){
				String preKey = entry.getKey();
				String time = preKey.substring(fullKey.length()+1);
				Map<String,String> value = entry.getValue();
				tmp.put(new Date(Long.valueOf(time)), value);
			}
			
			logger.warn("come here3,"+map.size());
			rowkeyStart = fullIdName + Constants.S_SEPERATOR + Constants.QUERY_DEAD_TIME;
			rowkeyEnd = fullIdName + Constants.S_SEPERATOR + end.getTime();
	
			map=HBaseUtil.queryValueMap(rowkeyStart, rowkeyEnd, properties);
			for(Map.Entry<String,Map<String,String>> entry:map.entrySet()){
				String preKey = entry.getKey();
				String time = preKey.substring(fullIdName.length()+1);
				Map<String,String> value = entry.getValue();
				tmp.put(new Date(Long.valueOf(time)), value);
			}

			logger.warn("come here4,"+map.size());
		}
		
		return tmp;
	}
	
	
	/**
	 *	============================查询单个值================================= 
	 */
	
	public static String queryCellBySingleKey(String appName,String keyName,String property, Date day){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		String fullKey = "";
		
		if(cal.getTime().getTime()<Constants.QUERY_DEAD_TIME){
			fullKey = HBaseUtil.getMD5String(Util.combinAppKeyName(appName, keyName));
		}else{
			Integer id=DBMediaKeyCache.get().getKeyId(keyName);
			if(id==null){
				logger.warn(keyName+",id为null querySingleHost2"); 
				return null;
			}
			fullKey = HBaseUtil.getMD5String(Util.combinAppKeyName(appName, String.valueOf(id)));
		}
		String rowkey = fullKey + Constants.S_SEPERATOR + cal.getTime().getTime();
		
		return HBaseUtil.queryCellValue(rowkey, property, String.class);
	}
	
	public static Map<String,String> queryMultiPropertyBySingleKey(String appName,String keyName,String[] properties,Date day){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		String fullKey = "";
		
		if(cal.getTime().getTime()<Constants.QUERY_DEAD_TIME){
			fullKey = HBaseUtil.getMD5String(Util.combinAppKeyName(appName, keyName));
		}else{
			Integer id=DBMediaKeyCache.get().getKeyId(keyName);
			if(id==null){
				logger.warn(keyName+",id为null querySingleHost2"); 
				return null;
			}
			fullKey = HBaseUtil.getMD5String(Util.combinAppKeyName(appName, String.valueOf(id)));
		}
		String rowkey = fullKey + Constants.S_SEPERATOR + cal.getTime().getTime();
		
		
		Map<String,String> map = HBaseUtil.queryCellValue(rowkey, properties);
		return map;
	}
	
	public static void main(String[] args){
		
//		Map<Date,Map<String,String>> map = QueryHistoryUtil.queryMultiProperty("shopsystem", "PV", new String[]{"E-times","C-time","P-size","C-200","C-302","c-other"},new Date());
//		for(Map.Entry<Date,Map<String,String>> entry:map.entrySet()){
//			System.out.println(entry.getKey()+""+entry.getValue());
//		}
		
		Date d=new Date(1354273837657L);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		
		System.out.println(cal.get(Calendar.YEAR));
		System.out.println(cal.get(Calendar.MONDAY));
		System.out.println(cal.get(Calendar.DAY_OF_MONTH));
		
		cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+1);
		System.out.println(cal.getTime()+","+cal.getTime().getTime());
	}
	

}
