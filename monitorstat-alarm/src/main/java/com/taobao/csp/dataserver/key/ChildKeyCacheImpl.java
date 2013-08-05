/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaodu
 * 
 *         ÏÂÎç3:04:07
 */
public class ChildKeyCacheImpl {

	private static Map<String, Set<String>> childMap = new ConcurrentHashMap<String, Set<String>>();

	public static void putKeyChildren(String parentKeyName, String childKeyName) {
		
		Set<String> set = childMap.get(parentKeyName);
		if (set == null) {
			synchronized (childMap) {
				set = childMap.get(parentKeyName);
				if(set ==null){
					set =Collections.synchronizedSet(new HashSet<String>()) ;
					childMap.put(parentKeyName, set);
				}
			}
		}
		if (!set.contains(childKeyName)) {
			set.add(childKeyName);
		}
	}
	
	public static List<String> getKeyChildren(String parentKeyName) {

		List<String> list = new ArrayList<String>();

		Set<String> set = childMap.get(parentKeyName);
		if (set != null) {
			list.addAll(set);
		}
		return list;
	}

	public static void clear() {
		childMap.clear();
	}

}
