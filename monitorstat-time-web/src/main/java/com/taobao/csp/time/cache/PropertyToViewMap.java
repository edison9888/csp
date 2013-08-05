package com.taobao.csp.time.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.PropConstants;

public class PropertyToViewMap {
	public static Logger logger = Logger.getLogger(PropertyToViewMap.class);
	private static PropertyToViewMap single = new PropertyToViewMap();
	private Map<String,String> propertyToViewMap = new HashMap<String,String>();
	private PropertyToViewMap(){
		propertyToViewMap.put(PropConstants.E_TIMES, "流量");
		propertyToViewMap.put(PropConstants.C_TIME, "响应时间");
		propertyToViewMap.put(PropConstants.NOTIFY_C_RA_S, "发送成功次数");
		propertyToViewMap.put(PropConstants.NOTIFY_C_RA_S_RT, "发送成功耗时");
	}
	public static PropertyToViewMap get(){
		return single; 
	}
	public String get(String key){
		return propertyToViewMap.get(key) == null?key:propertyToViewMap.get(key) ;
	}
}
