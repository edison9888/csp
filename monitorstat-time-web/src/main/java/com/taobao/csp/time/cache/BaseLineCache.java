package com.taobao.csp.time.cache;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.csp.alarm.baseline.AppBaseLineProcessor;
import com.taobao.csp.alarm.baseline.BaseLineProcessor;
import com.taobao.csp.alarm.baseline.HostBaseLineProcessor;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

public class BaseLineCache {
	private static final Logger logger = Logger.getLogger(TimeCache.class);

	// map<String,Map<hh:mm,values>>
	private Map<String, Map<String, Double>> baseLineCacheMap = new ConcurrentHashMap<String, Map<String, Double>>();

	private Calendar cachetime;

	private static BaseLineCache baselineCache = new BaseLineCache();

	private BaseLineCache() {
		cachetime = Calendar.getInstance();
	}

	public static BaseLineCache get() {
		return baselineCache;
	}

	public void clean() {
		baseLineCacheMap.clear();
	}

	/**
	 * 查询基线内容，过滤HSF信息
	 * @param appName
	 * @param keyName
	 * @param property
	 * @return
	 */
	public Map<String, Double> getBaseLine(String appName, String keyName, String property) {
		if(keyName.indexOf("HSF")>=0){
			return new HashMap<String, Double>();
		} 
		return getBaseLineInner(appName, keyName, property);
	}
	
	/**
	 * 查询有分组信息的应用的基线,不区分机房，不过滤HSF信息。appName格式举例: ump`G1
	 * @param appName
	 * @param keyName
	 * @param property
	 * @return
	 */
	public Map<String, Double> getBaseLineForGroup(String appName, String keyName, String property) {
		return getBaseLineInner(appName, keyName, property);
	}
	
	private Map<String, Double> getBaseLineInner(String appName, String keyName, String property) {
		String key = appName + ":" + keyName + ":" + property;
		Map<String, Double> map = baseLineCacheMap.get(key);
		if (map == null || !isTheSameDay()) {
			try {
				BaseLineProcessor processor = new AppBaseLineProcessor(appName, keyName, property, new Date());
				map = processor.getBaseLine();
				if (map != null && map.size() != 0) {
					baseLineCacheMap.put(key, map);
				}
			} catch (Exception e) {
				logger.info(e);
			}
		}
		if(map == null){
			return new HashMap<String, Double>();
		}
		return map;
	}

	public String getScaleKeyWithAppName(String fullKeyName, String property, String ftime, double value) {
		int firstSeperator = fullKeyName.indexOf(Constants.S_SEPERATOR);

		String appName = fullKeyName.substring(0, firstSeperator);
		String keyName = fullKeyName.substring(firstSeperator + 1);

		return getScale(appName, keyName, property, ftime, value);
	}

	public String getScaleKeyWithAppNameIP(String fullKeyName, String property, String ftime, double value) {
		int firstSeperator = fullKeyName.indexOf(Constants.S_SEPERATOR);
		int lastSeperator = fullKeyName.lastIndexOf(Constants.S_SEPERATOR);
		String appName = fullKeyName.substring(0, firstSeperator);
		String keyName = fullKeyName.substring(firstSeperator + 1, lastSeperator);
		String ip = fullKeyName.substring(lastSeperator + 1);
		return getScale(appName, keyName, property, ftime, value, ip);

	}

	public String getScale(String appName, String keyName, String property, String ftime, double value) {
		Map<String, Double> mapScale = getBaseLine(appName, keyName, property);
		String scale = "";
		if (mapScale != null && mapScale.get(ftime) != null) {
			scale = DataUtil.scale(value, mapScale.get(ftime));
		}
		return scale;
	}

	public String getScale(String appName, String keyName, String property, String ftime, double value, String ip) {
		Map<String, Double> mapScale = getBaseLine(appName, keyName, property, ip);
		String scale = "";
		if (mapScale != null && mapScale.get(ftime) != null) {
			scale = DataUtil.scale(value, mapScale.get(ftime));
		}
		return scale;
	}

	/**
	 * 通过机房获取
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param keyName
	 * @param property
	 * @param ftime
	 * @param value
	 * @param siteName
	 * @return TODO
	 */
	public String getScaleBySite(String appName, String keyName, String property, String ftime, double value, String siteName) {
		Map<String, Double> mapScale = getBaseLineBySite(appName, keyName, property, siteName);
		String scale = "";
		if (mapScale != null && mapScale.get(ftime) != null) {
			scale = DataUtil.scale(value, mapScale.get(ftime));
		}
		return scale;
	}

	public Map<String, Double> getBaseLine(String appName, String keyName, String property, String ip) {
		String cm = CspCacheTBHostInfos.get().getHostInfoByIp(ip).getHostSite();
		return getBaseLineBySite(appName, keyName, property, cm);
	}

	/**
	 * 取得机器基线通过机房名称
	 * 
	 * @author xiaodu
	 * @param appName
	 * @param keyName
	 * @param property
	 * @param siteName
	 *            机房名称
	 * @return TODO
	 */
	public Map<String, Double> getBaseLineBySite(String appName, String keyName, String property, String siteName) {
		String key = appName + ":" + keyName + ":" + property + ":" + siteName;
		Map<String, Double> map = baseLineCacheMap.get(key);
		
//		if(keyName.indexOf("HSF")>=0){
//			return new HashMap<String, Double>();
//		}
		
		if (map == null || !isTheSameDay()) {
			try {
				BaseLineProcessor processor = new HostBaseLineProcessor(siteName.toUpperCase(), appName, keyName, property, new Date());
				map = processor.getBaseLine();
				if (map != null && map.size() != 0) {
					baseLineCacheMap.put(key, map);
				}
			} catch (Exception e) {
				logger.info(e);
			}
		}
		
		if(map == null){
			return new HashMap<String, Double>();
		}
		
		return map;
	}

	private boolean isTheSameDay() {
		Calendar c1 = Calendar.getInstance();

		if ((c1.get(Calendar.YEAR) == cachetime.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == cachetime.get(Calendar.MONTH))
				&& (c1.get(Calendar.DAY_OF_MONTH) == cachetime.get(Calendar.DAY_OF_MONTH))) {
			return true;
		} else {
			cachetime = c1;
			return false;
		}

	}
}
