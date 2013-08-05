package com.taobao.csp.time.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.taobao.csp.dataserver.memcache.entry.DataEntry;

public class SortMapHelp {
	public static List<Map.Entry<String, Map<String, Object>>> sortNestedMapBySecondFloorKey(Map<String, Map<String, Object>> map){
		if(map == null)return null;
		List<Map.Entry<String, Map<String, Object>>> list = new ArrayList<Map.Entry<String, Map<String, Object>>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Map<String, Object>>>(){

			@Override
			public int compare(Entry<String, Map<String, Object>> arg0,
					Entry<String, Map<String, Object>> arg1) {
				String key1 = arg0.getKey();
				Long l1 = Long.parseLong(key1);
				String key2 = arg1.getKey();
				Long l2 = Long.parseLong(key2);
				return (int)(l2 - l1); 
			}});
		return list;
	}
	public static List<Map.Entry<String, Map<String, DataEntry>>> sortNestedMapBySecondFloorKeyValue(Map<String, Map<String, DataEntry>> map,final String keyName){
		if(map == null || keyName == null)return null;
		List<Map.Entry<String, Map<String, DataEntry>>> list = new ArrayList<Map.Entry<String, Map<String, DataEntry>>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Map<String, DataEntry>>>(){
			@Override
			public int compare(Entry<String, Map<String, DataEntry>> arg0,
					Entry<String, Map<String, DataEntry>> arg1) {
				if(arg1==null)return -1;
				if(arg0==null)return 1;
				String value1;
				try {
					value1 = arg0.getValue().get(keyName).getValue().toString();
				} catch (Exception e1) {
					return 1;
				}
				String value2;
				try {
					value2 = arg1.getValue().get(keyName).getValue().toString();
				} catch (Exception e) {
					return -1;
				}
				return value2.compareTo(value1);
			}});
		return list;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map.Entry<Object, Object>> sortMapByValue(Map map) {
		if(map == null)return null;
		List<Map.Entry<Object, Object>> info = new ArrayList<Map.Entry<Object, Object>>(map.entrySet());
		Collections.sort(info, new Comparator<Map.Entry<Object, Object>>() {
			public int compare(Map.Entry<Object, Object> obj1, Map.Entry<Object, Object> obj2) {
				if(obj2==null)return -1;
				if(obj1==null)return 1;
				return obj2.getValue().toString().compareTo(obj1.getValue().toString());
			}
		});
		return info;
	}
	@SuppressWarnings("rawtypes")
	public static TreeMap<Object, Object> sortMapByKey(Map map) {
		if(map == null)return null;
		TreeMap<Object, Object> mapVK = new TreeMap<Object, Object>(new Comparator<Object>() {
			public int compare(Object obj1, Object obj2) {
				String v1 = obj1.toString();
				String v2 = obj2.toString();
				int s = v2.compareTo(v1);
				return s;
			}
		});
		Set col = map.keySet();
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			mapVK.put(key, map.get(key));
		}
		return mapVK;
	}
}
