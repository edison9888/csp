
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.common.ao.capacity.CspLoadRunAo;

/**
 * @author xiaodu
 *
 * ÏÂÎç8:30:00
 */
public class CapacityCache {
	
	private static CapacityCache cache = new CapacityCache();
	private CapacityCache(){}
	
	public static CapacityCache get(){
		return cache;
	}
	
	private Map<Integer,EntryCapacity> map = new HashMap<Integer,EntryCapacity>();
	
	public synchronized double getAppCapacity(int  appId){
		EntryCapacity ec = 	map.get(appId);
		if(ec == null){
			reset(appId);
		}else{
			Date d = ec.date;
			Date cur = new Date();
			if(d.getDay() != cur.getDay()){
				reset(appId);
			}
		}
		return map.get(appId).value;
	}
	
	
	public void reset(int  appId){
		double d = 0;
		try{
			d = CspLoadRunAo.get().findRecentlyAppLoadRunQps(appId);
		}catch (Exception e1) {
		}
		
		EntryCapacity ec  = new EntryCapacity();
		ec.date = new Date();
		ec.value = d;
		map.put(appId, ec);
	}
	
	
	
	private class EntryCapacity{
		private Date date;
		
		private double value;
		
	}

}
