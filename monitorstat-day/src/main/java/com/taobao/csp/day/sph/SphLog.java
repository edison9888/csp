package com.taobao.csp.day.sph;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.Log;

/***
 * sph »’÷æ
 * @author youji.zj
 *
 */
public class SphLog implements Log {
	
	public static Logger logger = Logger.getLogger(SphLog.class);
	
	private String appName;
	
	private String ip;
	
	private String blockKey;
	
	private String action;
	
	private int blockCount;
	
	private String collectTime;
	

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getBlockKey() {
		return blockKey;
	}

	public void setBlockKey(String blockKey) {
		this.blockKey = blockKey;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
		String [] keys  = new String[6];
		keys[0] = "appName";
		keys[1] = "ip";
		keys[2] = "blockKey";
		keys[3] = "action";
		keys[4] = "blockCount";
		keys[5] = "collectTime";
		
		return keys;
	}
	
	
	public static List<String> getGroupFields() {
		List<String> groupFields = new ArrayList<String>(); 
		groupFields.add("ip");
		
		return groupFields;
	}

	@Override
	public Object[] getValues() {
		Object [] objects  = new Object[6];
		objects[0] = appName;
		objects[1] = ip;
		objects[2] = blockKey;
		objects[3] = action;
		objects[4] = blockCount;
		objects[5] = collectTime;
		
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
