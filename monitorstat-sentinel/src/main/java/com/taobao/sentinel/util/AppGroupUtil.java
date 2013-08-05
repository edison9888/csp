package com.taobao.sentinel.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class AppGroupUtil {
	
	public static Logger logger = Logger.getLogger(AppGroupUtil.class);
	
	private static Map<String, Map<String, List<String>>> groups = new HashMap<String, Map<String,List<String>>>();
	
	static {
		String resouceName = "app_group_info";
		if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().indexOf("window") > -1) {
			resouceName += "_windows";
		}
		resouceName += ".txt";
		
		InputStream inputStream = AppGroupUtil.class.getClassLoader().getResourceAsStream(resouceName);
		InputStreamReader inReader = new InputStreamReader(inputStream);
		BufferedReader bReader = new BufferedReader(inReader);
		
		String info;
		try {
			while((info = bReader.readLine()) != null) {
				String [] values = info.split("-");
				if (values == null || values.length != 3) continue;
				
				String appName = values[0];
				String groupName = values[1];
				String ip = values[2];
				
				Map<String, List<String>> map;
				if (groups.containsKey(appName)) {
					map = groups.get(appName);
				} else {
					map = new HashMap<String, List<String>>();
					groups.put(appName, map);
				}
				
				List<String> list;
				if (map.containsKey(groupName)) {
					list = map.get(groupName);
				} else {
					list = new ArrayList<String>();
					map.put(groupName, list);
				}
				
				list.add(ip);
				
			}
		} catch (IOException e) {
			logger.error("inteprete app groups error", e);
		}
		
	}
	
	
	public static Map<String, List<String>> getGroupInfo(String appName) {
		Map<String, List<String>> group = new HashMap<String, List<String>>();
		
		if (groups.get(appName) != null) {
			group = groups.get(appName);
		}
		
		
		return group;
	}

}
