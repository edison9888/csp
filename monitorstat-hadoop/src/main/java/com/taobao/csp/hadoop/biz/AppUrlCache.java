package com.taobao.csp.hadoop.biz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class AppUrlCache {
	
	private static Logger logger = Logger.getLogger(AppUrlCache.class);
	
	private static Map<String, Set<String>> cache = new HashMap<String, Set<String>>();
	
	static {
		try {
			InputStream inputStream = AppUrlCache.class.getClassLoader().getResourceAsStream("appurl");
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.trim().startsWith("#") || StringUtils.isBlank(line)) {
					continue;
				}
				
				String [] values = line.split("\t");
				String appName = values[0];
				String url = values[1];
				
				Set<String> urlL = cache.get(appName);
				if (urlL == null) {
					urlL = new HashSet<String>();
					cache.put(appName, urlL);
				}
				
				urlL.add(url);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static boolean contains(String appName, String url) {
		boolean contain = false;
		
		Set<String> urlS = cache.get(appName);
		if (urlS != null && urlS.contains(url)) {
			contain = true;
		}
		
		return contain;
	}
	
	public static String urlRelateApp(String url) {
		String app = null;
		
		for (Map.Entry<String, Set<String>> entry : cache.entrySet()) {
			String appName = entry.getKey();
			Set<String> appUrlS = entry.getValue();
			
			if (appUrlS.contains(url)) {
				app = appName;
				break;
			}
		}
		
		return app;
	}
}
