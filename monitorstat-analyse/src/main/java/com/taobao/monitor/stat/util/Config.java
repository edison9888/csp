
package com.taobao.monitor.stat.util;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 
 * @author xiaodu
 * @version 2010-4-2 ÏÂÎç05:20:23
 */
public class Config {
	
	
private static ResourceBundle bundle;
	
	static{
		try{
			String propertyname= "relation";
			Properties props = System.getProperties();
			Object systemname = props.get("os.name");
			if(systemname!=null){				
				String name = (String)systemname;
				if(name.toLowerCase().indexOf("window")>-1){
					propertyname = "relation_windows";
				}				
			}
			bundle = ResourceBundle.getBundle(propertyname);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	public static String getValue(String key){
		return bundle.getString(key);
	}
	
}
