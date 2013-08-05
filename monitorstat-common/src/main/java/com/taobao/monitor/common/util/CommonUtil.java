package com.taobao.monitor.common.util;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import com.taobao.monitor.common.po.HostPo;

/***
 * 通用的工具类
 * @author youji.zj
 *
 */
public class CommonUtil {
	
	/***
	 * 容量水位的标准
	 * @param appName
	 * @return
	 */
	public static int getCapacityStandard(String appName) {
		int standard = 40;
		int roomSize = 2;
		Map<String,List<HostPo>> hosts = CspCacheTBHostInfos.get().getHostMapByRoom(appName);
		if (hosts != null && hosts.keySet() != null) {
			roomSize = hosts.keySet().size();
		}
		
		if (roomSize == 1) {
			standard = 70;
		}
		
		if (roomSize == 3) {
			standard = 40;
		} 
		
		return standard;
	}
	
	public static <T> T[] getObjectArrayByJsonStr(Class<T> rootClass, String json) {
		JSONArray jsonArray = JSONArray.fromObject(json);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setArrayMode( JsonConfig.MODE_OBJECT_ARRAY );
		jsonConfig.setRootClass(rootClass);
		return (T[]) JSONSerializer.toJava( jsonArray, jsonConfig );    
	}

	/**
	 * 根据Json字符串，生成对象
	 * @param <T>
	 */
	public static <T> T getObjectByJsonStr(Class<T> rootClass, String json) {
		JSONObject jsonObj = JSONObject.fromObject(json);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(rootClass);
		return (T)JSONSerializer.toJava( jsonObj, jsonConfig );    
	}
	
	/***
	 * 容量水位的标准
	 * @param rooms
	 * @return
	 */
	public static int getCapacityStandard(int rooms) {
		int standard = 40;
		
		if (rooms == 1) {
			standard = 70;
		}
		
		if (rooms == 3) {
			standard = 40;
		} 
		
		return standard;
	}
	
	public static String combinAppNameAndGroupName(String appName, String groupName) {
		return appName + "`" + groupName;
	}
}
