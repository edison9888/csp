package com.taobao.csp.day.pinkie;

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
public class PinkieLog implements Log {
	
	public static Logger logger = Logger.getLogger(PinkieLog.class);
	
	private String appName;
	
	private long count;
	
	private String collectTime;
	
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

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public static String[] getKeys() {
		String [] keys  = new String[3];
		keys[0] = "appName";
		keys[1] = "count";
		keys[2] = "collectTime";
		
		return keys;
	}
	
	
	public static List<String> getGroupFields() {
		List<String> groupFields = new ArrayList<String>(); 
		groupFields.add("appName");
		
		return groupFields;
	}

	@Override
	public Object[] getValues() {
		Object [] objects  = new Object[3];
		objects[0] = appName;
		objects[1] = count;
		objects[2] = collectTime;
		
		return objects;
	}

	@Override
	public Date getDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HH:mm");
		Date date = null;
		try {
			date = sf.parse(this.getCollectTime());
		} catch (ParseException e) {
			logger.error(e);
			date = Calendar.getInstance().getTime();
		}
		
		return date;
	}
}
