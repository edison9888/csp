package com.taobao.csp.day.tddl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.Log;

/***
 * tddl »’÷æ
 * @author youji.zj
 *
 */
public class TddlLog implements Log {
	
	public static Logger logger = Logger.getLogger(TddlLog.class);
	
	private String appName;
	
	private String dbFeature;
	
	private String dbName;
	
	private String dbIp;
	
	private String dbPort;
	
	private String sql;
	
	private long executeSum;
	
	private long executeTime;
	
	private int maxResp;
	private String maxRespTime;
	
	private int minResp;
	private String minRespTime;
	
	private String collectTime;
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDbFeature() {
		return dbFeature;
	}

	public void setDbFeature(String dbFeature) {
		this.dbFeature = dbFeature;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbIp() {
		return dbIp;
	}

	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public long getExecuteSum() {
		return executeSum;
	}

	public void setExecuteSum(long executeSum) {
		this.executeSum = executeSum;
	}

	public long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}

	public int getMaxResp() {
		return maxResp;
	}

	public void setMaxResp(int maxResp) {
		this.maxResp = maxResp;
	}

	public String getMaxRespTime() {
		return maxRespTime;
	}

	public void setMaxRespTime(String maxRespTime) {
		this.maxRespTime = maxRespTime;
	}

	public int getMinResp() {
		return minResp;
	}

	public void setMinResp(int minResp) {
		this.minResp = minResp;
	}

	public String getMinRespTime() {
		return minRespTime;
	}

	public void setMinRespTime(String minRespTime) {
		this.minRespTime = minRespTime;
	}
	
	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public static String[] getKeys() {
		String [] keys  = new String[13];
		keys[0] = "appName";
		keys[1] = "dbFeature";
		keys[2] = "dbName";
		keys[3] = "dbIp";
		keys[4] = "dbPort";
		keys[5] = "sql";
		keys[6] = "executeSum";
		keys[7] = "executeTime";
		keys[8] = "maxResp";
		keys[9] = "maxRespTime";
		keys[10] = "minResp";
		keys[11] = "minRespTime";
		keys[12] = "collectTime";
		
		return keys;
	}
	
	public static String[] getStaticsKeys() {
		String [] keys  = new String[6];
		keys[0] = "appName";
		keys[1] = "dbFeature";
		keys[2] = "minResp";
		keys[3] = "minRespTime";
		keys[4] = "maxResp";
		keys[5] = "maxRespTime";
		
		return keys;
	}
	
	public static List<String> getGroupFields() {
		List<String> groupFields = new ArrayList<String>(); 
		groupFields.add("dbIp");
		
		return groupFields;
	}

	@Override
	public Object[] getValues() {
		Object [] objects  = new Object[13];
		objects[0] = appName;
		objects[1] = dbFeature;
		objects[2] = dbName;
		objects[3] = dbIp;
		objects[4] = dbPort;
		objects[5] = sql;
		objects[6] = executeSum;
		objects[7] = executeTime;
		objects[8] = maxResp;
		objects[9] = maxRespTime;
		objects[10] = minResp;
		objects[11] = minRespTime;
		objects[12] = collectTime;
		
		return objects;
	}

	@Override
	public Date getDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
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
