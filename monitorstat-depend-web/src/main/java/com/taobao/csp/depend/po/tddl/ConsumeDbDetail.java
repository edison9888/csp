package com.taobao.csp.depend.po.tddl;

import java.util.Date;
import java.util.HashMap;

public class ConsumeDbDetail implements Comparable<ConsumeDbDetail>{
	private String appName;
	private String dbName;
	private String sqlText;
	private String executeType;
	private long executeSum;
	private float timeAverage;
	private String appHostIp;
	private String appSiteName;
	private Date collect_time;
	
	//key-value: machineroomname-Long
	public HashMap<String, Long> siteMap = new HashMap<String, Long>();	
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getSqlText() {
		return sqlText;
	}
	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}
	public String getExecuteType() {
		return executeType;
	}
	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}
	public long getExecuteSum() {
		return executeSum;
	}
	public void setExecuteSum(long executeSum) {
		this.executeSum = executeSum;
	}
	public float getTimeAverage() {
		return timeAverage;
	}
	public void setTimeAverage(float timeAverage) {
		this.timeAverage = timeAverage;
	}
	public String getAppSiteName() {
		return appSiteName;
	}
	public void setAppSiteName(String appSiteName) {
		this.appSiteName = appSiteName;
	}
	public Date getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(Date collect_time) {
		this.collect_time = collect_time;
	}
	
	public String getAppHostIp() {
		return appHostIp;
	}
	public void setAppHostIp(String appHostIp) {
		this.appHostIp = appHostIp;
	}
	
	public HashMap<String, Long> getSiteMap() {
		return siteMap;
	}
	public void setSiteMap(HashMap<String, Long> siteMap) {
		this.siteMap = siteMap;
	}
	@Override
	public int compareTo(ConsumeDbDetail o) {
		if(o.getExecuteSum()<getExecuteSum()){
			return -1;
		}else if(o.getExecuteSum()>getExecuteSum()){
			return 1;
		}
		return 0;
	}
	
}
