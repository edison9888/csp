
package com.taobao.sentinel.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesConfig {
	
	public static Logger logger = Logger.getLogger(PropertiesConfig.class);
	
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
			logger.error("get property error", e);
		}
		return null;
	}
}
