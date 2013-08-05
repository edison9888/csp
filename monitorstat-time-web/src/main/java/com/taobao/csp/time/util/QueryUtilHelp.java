package com.taobao.csp.time.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.query.QueryUtil;

public class QueryUtilHelp {
	private static Logger logger = Logger.getLogger(QueryUtilHelp.class);
	public static Map<String, Map<String, Map<String, Object>>> queryChildHostRealTime(String appName,String keyFather,List<String> ipList){
		try {
			Map<String,Map<String,Map<String,Object>>> retMap = new HashMap<String, Map<String, Map<String, Object>>>();
			for(String ip : ipList){
				Map<String,Map<String,Map<String,Object>>> tempMap = QueryUtil.queryChildHostRealTime(appName, keyFather, ip);
				retMap.putAll(tempMap);
			}
			return retMap;
		} catch (Exception e) {
			logger.info(e);
			return null;
		}
	}
	public static Map<String,List<Object>> queryChildHostRealTime(String appName,String keyFather,String ip,Object obj){
		Map<String,List<Object>> retMap = new HashMap<String,List<Object>>();
		try{
			Class classType = obj.getClass();
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildHostRealTime(appName, keyFather, ip);
			for(Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()){
				if(entry.getValue()==null||entry.getValue().size()==0)continue;
				List<Object> list = new ArrayList<Object>();
				for(Map.Entry<String, Map<String,Object>> entry2 : entry.getValue().entrySet()){
					Object object = classType.newInstance();
					object = TransformMapToPo.transform(entry2.getKey(),entry2.getValue(),object);
					list.add(object);
				}
				retMap.put(entry.getKey(), list);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return retMap;
	}
	public static Map<String,List<Object>> queryGrandsonHostRealTime(String appName,String keyFather,String ip,Object obj){
		Map<String,List<Object>> retMap = new HashMap<String,List<Object>>();
		try{
			Class classType = obj.getClass();
			List<String> ipList = new ArrayList<String>();
			ipList.add(ip);
			Map<String, Map<String, Map<String, Object>>> map = QueryUtilHelp.queryGrandsonRealTimeByHost(appName, keyFather, ipList);
			for(Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()){
				if(entry.getValue()==null||entry.getValue().size()==0)continue;
				List<Object> list = new ArrayList<Object>();
				for(Map.Entry<String, Map<String,Object>> entry2 : entry.getValue().entrySet()){
					Object object = classType.newInstance();
					object = TransformMapToPo.transform(entry2.getKey(),entry2.getValue(),object);
					list.add(object);
				}
				retMap.put(entry.getKey(), list);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return retMap;
	}
	public static Map<String,List<Object>> queryChildHostRealTime(String appName,String keyFather,List<String> ipList,Object obj){
		Map<String,List<Object>> retMap = new HashMap<String,List<Object>>();
		try{
			Class classType = obj.getClass();
			Map<String, Map<String, Map<String, Object>>> map = QueryUtilHelp.queryChildHostRealTime(appName, keyFather, ipList);
			for(Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()){
				if(entry.getValue()==null||entry.getValue().size()==0)continue;
				List<Object> list = new ArrayList<Object>();
				for(Map.Entry<String, Map<String,Object>> entry2 : entry.getValue().entrySet()){
					Object object = classType.newInstance();
					object = TransformMapToPo.transform(entry2.getKey(),entry2.getValue(),object);
					list.add(object);
				}
				retMap.put(entry.getKey(), list);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return retMap;
	}
	public static Map<String,Map<String,DataEntry>> queryRecentlyGrandsonRealTime(String appName,String keyName){
		try {
			Map<String, Map<String, DataEntry>> map = QueryUtil.queryRecentlyChildRealTime(appName, keyName);
			Map<String,Map<String,DataEntry>> retMap = new HashMap<String,Map<String,DataEntry>>();
			List<String> keys = new ArrayList<String>();
			for(Map.Entry<String, Map<String,DataEntry>> entry : map.entrySet()){
				String key = entry.getKey();
				Integer index = key.indexOf(Constants.S_SEPERATOR);
				key = key.substring(index+1,key.length());
				keys.add(key);
			}
			for(String key : keys){
				Map<String, Map<String, DataEntry>> map2 = QueryUtil.queryRecentlyChildRealTime(appName, key);
				retMap.putAll(map2);
			}
			return retMap;
		} catch (Exception e) {
			logger.info(e);
			return null;
		}
	}
	public static Map<String, Map<String, Map<String, Object>>> queryGrandsonRealTime(String appName,String keyName){
		try {
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime(appName, keyName);
			Map<String, Map<String, Map<String, Object>>> retMap = new HashMap<String, Map<String, Map<String, Object>>>();
			List<String> keys = new ArrayList<String>();
			for(Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()){
				String key = entry.getKey();
				Integer index = key.indexOf(Constants.S_SEPERATOR);
				key = key.substring(index+1,key.length());
				logger.info("log key"+entry.getKey());
				keys.add(key);
			}
			for(String key : keys){
				Map<String, Map<String, Map<String, Object>>> map2 = QueryUtil.queryChildRealTime(appName, key);
				for(Map.Entry<String, Map<String, Map<String, Object>>> entry : map2.entrySet()){
					logger.info("log key"+entry.getKey());
				}
				retMap.putAll(map2);
			}
			return retMap;
		} catch (Exception e) {
			logger.info(e);
			return null;
		}
	}
	public static Map<String, List<Object>> queryGrandsonRealTime(String appName,String keyName,Object object){
		Map<String,List<Object>> retMap = new HashMap<String,List<Object>>();
		try{
			Class classType = object.getClass();
			Map<String, Map<String, Map<String, Object>>> map = QueryUtilHelp.queryGrandsonRealTime(appName, keyName);
			for(Map.Entry<String, Map<String,Map<String,Object>>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				List<Object> list = new ArrayList<Object>();
				for(Map.Entry<String, Map<String,Object>> entry1 : entry.getValue().entrySet()){
					Object obj = classType.newInstance();
					obj = TransformMapToPo.transform(entry1.getKey(), entry1.getValue(), obj);
					list.add(obj);
				}
				retMap.put(entry.getKey(), list);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return retMap;
	}
	public static Map<String,Map<String,Map<String,Object>>> queryGrandsonRealTimeByHost(String appName,String keyName,List<String> ipList){
		Map<String,Map<String,Map<String,Object>>> retMap = new HashMap<String,Map<String,Map<String,Object>>>();
		try {
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime(appName, keyName);
			Set<String> childKeys = new HashSet<String>();
			for(Map.Entry<String, Map<String,Map<String,Object>>> entry : map.entrySet()){
				String key = entry.getKey().substring(entry.getKey().indexOf(Constants.S_SEPERATOR)+1,entry.getKey().length());
				childKeys.add(key);
			}
			for(String key : childKeys){
				Map<String, Map<String, Map<String, Object>>> map2 = QueryUtilHelp.queryChildHostRealTime(appName, key, ipList);
				for(Map.Entry<String, Map<String, Map<String, Object>>> entry : map2.entrySet()){
					if(entry.getValue()!=null){
						retMap.put(entry.getKey(),entry.getValue());
					}
				}
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return retMap;
	}
	public static Map<String, Map<String, DataEntry>> queryRecentlyChildHostRealTime(String appName,String keyFather,List<String> ipList){
		try {
			Map<String, Map<String, DataEntry>>  map = QueryUtil.queryRecentlyChildRealTime(appName,keyFather);
			List<String> keys = new ArrayList<String>();
			for(Map.Entry<String, Map<String, DataEntry>> entry : map.entrySet()){
				String keyChild = entry.getKey();
				for(String ip : ipList){
					String key = keyChild+Constants.S_SEPERATOR+ip;
					Integer index = key.indexOf(Constants.S_SEPERATOR);
					key = key.substring(index+1,key.length());
					keys.add(key);
				}
			}
			Map<String, Map<String, DataEntry>> retMap = QueryUtil.queryRecentlyMultiRealTime(appName, keys);
			return retMap;
		} catch (Exception e) {
			logger.info(e);
			return null;
		}
	}
	public static Map<String, Map<String, Map<String, Object>>> queryChildHostRealTime(String appName,String keyFather,String ip){
		List<String> ipList = new ArrayList<String>();
		ipList.add(ip);
		Map<String, Map<String, Map<String, Object>>> map = queryChildHostRealTime(appName,keyFather,ipList);
		return map;
	}
	public static Map<String, Map<String, DataEntry>> queryRecentlyChildHostRealTime(String appName,String keyFather,String ip){
		Map<String, Map<String, DataEntry>> map = queryRecentlyChildHostRealTime(appName,keyFather, ip);
		return map;
	}
	public static Map<String,List<Object>> queryHostRealTime(String appName,String keyName,Object obj,List<String> ipList){
		Map<String,List<Object>> retMap = new HashMap<String,List<Object>>();
		if(appName==null||keyName==null||obj==null||ipList==null){
			return retMap;
		}
		try {
			Class classType = obj.getClass();
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryHostRealTime(appName, keyName, ipList);
			for(Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()){
				String key = entry.getKey();
				Map<String,Map<String,Object>> valueMap = entry.getValue();
				List<Object> valueList = new ArrayList<Object>();
				if(valueMap!=null&&valueMap.size()!=0){
					for(Map.Entry<String, Map<String,Object>> valueEntry : valueMap.entrySet()){
						Object object = classType.newInstance();
						object = TransformMapToPo.transform(valueEntry.getKey(), valueEntry.getValue(), object);
						valueList.add(object);
					}
				}else{
					continue;
				}
				retMap.put(key, valueList);
			}
		} catch (Exception e) {
			logger.info(e);
		}
		return retMap;
	}
	public static Object queryRecentlyRealTime(String appName,String keyName,Object obj){
		try{
			Map<String, DataEntry> map = QueryUtil.queryRecentlySingleRealTime(appName, keyName);
			obj = TransformMapToPo.transformDataEntry(map, obj);
		}catch(Exception e){
			logger.info(e);
		}
		return obj;
	}
	public static Map<String,Object> queryRecentlyChildRealTime(String appName,String keyName,Object obj){
		Map<String,Object> retMap = new HashMap<String,Object>();
		Class classType = obj.getClass();
		try{
			Map<String, Map<String, DataEntry>> map = QueryUtil.queryRecentlyChildRealTime(appName, keyName);
			logger.info(appName+keyName+map);
			if(map == null)return retMap;
			for(Map.Entry<String, Map<String, DataEntry>> entry : map.entrySet()){
				if(entry.getValue()==null||entry.getValue().size()==0)continue;
				Map<String,DataEntry> valueMap = entry.getValue();
				Object object = classType.newInstance();
				object = TransformMapToPo.transformDataEntry(valueMap, object);
				retMap.put(entry.getKey(), object);
			}
		}catch(Exception e){
			logger.info(e);
		}
		return retMap;
	}
	public static Map<String,Object> queryRecentlyHostRealTime(String appName,String keyName,List<String> ipList,Object obj){
		Map<String,Object> retMap = new HashMap<String,Object>();
		if(appName==null||keyName==null||obj==null){
			return retMap;
		}
		try{
			Class classType = obj.getClass();
			Map<String, Map<String, DataEntry>> map = QueryUtil.queryRecentlyHostRealTime(appName, keyName, ipList);
			if(map == null)return retMap;
			Long start = System.currentTimeMillis();
			for(Map.Entry<String, Map<String, DataEntry>> entry : map.entrySet()){
				Map<String,DataEntry> poMap = entry.getValue();
				if(poMap!=null&&poMap.size()!=0){
					Object po = classType.newInstance();
					po = TransformMapToPo.transformDataEntry(poMap, po);
					String key = entry.getKey();
					retMap.put(key, po);
				}
			}
			Long end = System.currentTimeMillis();
			logger.info("∑¥…‰–‘ƒ‹"+(end - start));
		}catch(Exception e){
			logger.info(e);
		}
		return retMap;
	}
	public static List<Object> querySingleRealTime(String appName,String keyName,Object obj){
		List<Object> list = new ArrayList<Object>();
		if(appName==null||keyName==null||obj==null){
			return list;
		}
		try{
			Class classType = obj.getClass();
			Map<String, Map<String, Object>> map =  QueryUtil.querySingleRealTime(appName, keyName);
			if(map == null)return list;
			for(Map.Entry<String, Map<String,Object>> entry : map.entrySet()){
				Object po = classType.newInstance();
				po = TransformMapToPo.transform(entry.getKey(), entry.getValue(), po);
				list.add(po);
			}
		}catch(Exception e){
			logger.info(e);
		}
		return list;
	}
	public static Map<String,List<Object>> queryChildRealTime(String appName,String keyName,Object obj){
		Map<String,List<Object>> retMap = new HashMap<String,List<Object>>();
		if(appName==null||keyName==null||obj==null){
			return retMap;
		}
		try{
			Class classType = obj.getClass();
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime(appName, keyName);
			if(map == null)return retMap;
			for(Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				String key = entry.getKey();
				List<Object> list = new ArrayList<Object>();
				for(Map.Entry<String, Map<String,Object>> entry2 : entry.getValue().entrySet()){
					Object po = classType.newInstance();
					po = TransformMapToPo.transform(entry2.getKey(), entry2.getValue(), po);
					list.add(po);
				}
				retMap.put(key, list);
			}
		}catch(Exception e){
			logger.info(e);
		}
		return retMap;
	}
}
