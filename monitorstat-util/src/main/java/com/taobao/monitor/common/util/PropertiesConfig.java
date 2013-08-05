
package com.taobao.monitor.common.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author xiaodu
 * @version 2010-4-2 ÏÂÎç05:20:23
 */
public class PropertiesConfig {
	
	private static String os = "liunx";
	
	static{
		Properties props = System.getProperties();
		Object systemname = props.get("os.name");
		if(systemname!=null){				
			String name = (String)systemname;
			if(name.toLowerCase().indexOf("window")>-1){
				os = "window";
			}				
		}
	}
	
	public static String getValue(String configname,String key){
		
		if("window".equals(os)){
			configname = configname+"_windows";
		}
		configname = configname+".properties";
		
		Properties prop = new Properties();
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configname));
			return prop.getProperty(key);
		} catch (IOException e) {
		}
		return null;
	}
}
