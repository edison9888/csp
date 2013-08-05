
package com.taobao.monitor.web.cache;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.monitor.web.ao.MonitorTimeAo;

/**
 * 
 * @author xiaodu
 * @version 2010-9-29 ÏÂÎç04:43:26
 */
public class CacheKeyValue {
	
	private CacheKeyValue(){}
	private static CacheKeyValue c = new CacheKeyValue();
	public static CacheKeyValue get(){
		return c;
	}
	private Map<String,Double> keyvalueCache = new ConcurrentHashMap<String, Double>();
	/**
	 * 
	 * @param appid
	 * @param keyId
	 * @param day yyyy-MM-dd
	 * @return
	 */
	public double getKeyAverageCahceValue(int appid,int keyId, Date startDate,Date endDate){
		String key = appid+"-"+keyId+"-"+startDate.getTime()+"-"+endDate.getTime();
		Double v = keyvalueCache.get(key);
		if(v != null){
			return v;
		}else{
			synchronized (keyvalueCache) {
				v = keyvalueCache.get(key);
				if(v == null){
					v = MonitorTimeAo.get().findKeyTimeRangeAverageValue( appid, keyId, startDate,endDate);
					keyvalueCache.put(key, v);
				}
			}			
			return v;
		}
	}

}
