package com.taobao.csp.time.cache;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.query.QueryHistoryUtil;

public class HistoryCache {
	private static final Logger logger = Logger.getLogger(TimeCache.class);
	private static Integer CACHE_SIZE = 100;
	private Map<String,Map<Date,String>> historyMap = Collections.synchronizedMap(new LruCache<String, Map<Date,String>>(CACHE_SIZE));
	private HistoryCache(){
	}
	private static HistoryCache historyCache = new HistoryCache();
	public static HistoryCache get(){
		return historyCache;
	}
	public Map<Date,String> getHistoryCache(String appName,String keyName,String property,Date date){
		String key = appName+":"+keyName+":"+property+":"+date;
		Map<Date,String> map = historyMap.get(key);
		if(map == null){
			map =  QueryHistoryUtil.querySingle(appName,keyName,property,date);
			historyMap.put(key, map);
		}
		return map;
	}
	private class LruCache<A, B> extends LinkedHashMap<A, B> {
		private static final long serialVersionUID = -1786317775350866713L;
		private final int maxEntries;

	    public LruCache(final int maxEntries) {
	        super(maxEntries + 1, 1.0f, true);
	        this.maxEntries = maxEntries;
	    }
	    @Override
	    protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
	        return super.size() > maxEntries;
	    }
	}
}
