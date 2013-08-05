package com.taobao.csp.day.tdod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.Log;

public class TdodLog implements Log {
	
	public static Logger logger = Logger.getLogger(TdodLog.class);
	
	private String appName;
	
	private int blockCount;
	
	private String collectTime;
	

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getBlockCount() {
		return blockCount;
	}

	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
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
		keys[1] = "blockCount";
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
		objects[1] = blockCount;
		objects[2] = collectTime;
		
		return objects;
	}

	@Override
	public Date getDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
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
