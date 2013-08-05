package com.taobao.csp.hadoop.biz;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * access 日志格式的缓存
 * @author youji.zj
 *
 */
public class PvLogFormatCache {
	
	private static Map<String, PvLogFormatEnum> cache = new ConcurrentHashMap<String, PvLogFormatEnum>();
	
	public static PvLogFormatEnum getPvLogFormat(String appName, String url) {
		if (cache.containsKey(appName)) {
			return cache.get(appName);
		}
		
		if (url.indexOf("||") > -1) {
			cache.put(appName, PvLogFormatEnum.GALAXY);
			return PvLogFormatEnum.GALAXY;
		}
		
		if (url.split("\"").length == 4) {
			cache.put(appName, PvLogFormatEnum.LOGIN);
			return PvLogFormatEnum.LOGIN;
		}
		
		cache.put(appName, PvLogFormatEnum.NOMAL);
		return PvLogFormatEnum.NOMAL;
	}
}
