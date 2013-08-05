package com.taobao.csp.time.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class Sort {
	private static Logger logger = Logger.getLogger(Sort.class);
	public static List<Object> listSort(List<Object> list ,final String name){
		Collections.sort(list, new Comparator<Object>(){

			@Override
			public int compare(Object o1, Object o2) {
				try {
					Field o1Field = o1.getClass().getDeclaredField(name);
					o1Field.setAccessible(true);
					Field o2Field = o2.getClass().getDeclaredField(name);
					o2Field.setAccessible(true);
					Object v1 = o1Field.get(o1);
					Object v2 = o2Field.get(o2);
					if(v1 instanceof String && v2 instanceof String){
						return ((String)v1).compareTo((String)v2);
					}
				} catch (Exception e) {
					return 0;
				}
				return 0;
			}});
		return list;
	}
	public static Map<String,Object> mapSort(Map<String,Object> map,final String name){
		Map<String,Object> retMap = new LinkedHashMap<String,Object>();
		List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String,Object>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Object>>(){

			@Override
			public int compare(Entry<String, Object> o1,
					Entry<String, Object> o2) {
				try {
					Field o1Field = o1.getClass().getDeclaredField(name);
					Field o2Field = o2.getClass().getDeclaredField(name);
					o1Field.setAccessible(true);
					o2Field.setAccessible(true);
					Object v1 = o1Field.get(o1);
					Object v2 = o2Field.get(o2);
					if(v1 instanceof Integer && v2 instanceof Integer){
						return (Integer)v2 - (Integer)v1;
					}
				} catch (Exception e) {
					return 0;
				}
				return 0;
			}
			
		});
		for(Map.Entry<String, Object> entry : list){
			retMap.put(entry.getKey(), entry.getValue());
		}
		return retMap;
	}
	public static List<Map<String,Object>> poListSort(List<Map<String,Object>> list ,final String name,Integer n){
		Collections.sort(list, new Comparator<Map<String,Object>>(){

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Object v1 =o1.get(name);
				Object v2 = o2.get(name);
				if(v1 instanceof Integer && v2 instanceof Integer){
					return (Integer)v2 - (Integer)v1;
				}
				if(v1 instanceof String && v2 instanceof String){
					return ((String)v1).compareTo((String)v2);
				}
				return 0;
			}});
		if(list.size()>n)list = list.subList(0, n);
		return list;
	}
	public static Map<String,Map<String,Object>> mapPoSort(Map<String,Map<String,Object>> map,final String name,Integer n){
		Map<String,Map<String,Object>> retMap = new LinkedHashMap<String,Map<String,Object>>();
		List<Map.Entry<String,Map<String,Object>>> list = new ArrayList<Map.Entry<String,Map<String,Object>>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String,Map<String,Object>>>(){

			@Override
			public int compare(Entry<String, Map<String, Object>> o1,
					Entry<String, Map<String, Object>> o2) {
				Object v1 = o1.getValue().get(name);
				Object v2 = o2.getValue().get(name);
				if(v1 instanceof Integer && v2 instanceof Integer){
					return (Integer)v2 - (Integer)v1;
				}
				return 0;
			}

			
		});
		Integer count = 0;
		for(Map.Entry<String, Map<String,Object>> entry : list){
			count++;
			if(count>n)break;
			retMap.put(entry.getKey(), entry.getValue());
		}
		return retMap;
	}
}
