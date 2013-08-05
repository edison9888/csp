package com.taobao.csp.day.gc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.Log;

/***
 * 小手指日志
 * @author youji.zj
 * @time 2012-12-06
 *
 */
public class GcLog implements Log {
	
	public static Logger logger = Logger.getLogger(GcLog.class);
	
	private String appName;
	
	private long count;
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}


	public static String[] getKeys() {
		String [] keys  = new String[2];
		keys[0] = "appName";
		keys[1] = "count";
		
		return keys;
	}
	
	
	public static List<String> getGroupFields() {
		List<String> groupFields = new ArrayList<String>(); 
		groupFields.add("appName");
		
		return groupFields;
	}

	@Override
	public Object[] getValues() {
		Object [] objects  = new Object[2];
		objects[0] = appName;
		objects[1] = count;
		
		return objects;
	}

	@Override
	public Date getDate() {
		return null;
	}
}
