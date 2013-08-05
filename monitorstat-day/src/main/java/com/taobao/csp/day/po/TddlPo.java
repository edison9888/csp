package com.taobao.csp.day.po;

/***
 * tddl po¿‡
 * @author youji.zj
 * 
 * @version 1.0 2012-09-06
 *
 */
public class TddlPo {
	private String appName;
	private String dbFeature;
	private String dbName;
	private String dbIp;
	private String dbPort;
	private String sqlText;
	private String type = "1";
	
	private long exeCount;
	private float respTime;
	
	private int maxResp;
	private int minResp = 1000;
	private String maxRespDate;
	private String minRespDate;

	private String appHostIp = "127.0.0.1";
	private String appHostSite = "CM0";
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
	
	public String getSqlText() {
		return sqlText;
	}
	
	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public long getExeCount() {
		return exeCount;
	}
	
	public void setExeCount(long exeCount) {
		this.exeCount = exeCount;
	}
	
	public float getRespTime() {
		return respTime;
	}
	
	public void setRespTime(float respTime) {
		this.respTime = respTime;
	}
	
	public int getMaxResp() {
		return maxResp;
	}
	
	public void setMaxResp(int maxResp) {
		this.maxResp = maxResp;
	}
	
	public int getMinResp() {
		return minResp;
	}
	
	public void setMinResp(int minResp) {
		this.minResp = minResp;
	}
	
	public String getMaxRespDate() {
		return maxRespDate;
	}
	
	public void setMaxRespDate(String maxRespDate) {
		this.maxRespDate = maxRespDate;
	}
	
	public String getMinRespDate() {
		return minRespDate;
	}
	
	public void setMinRespDate(String minRespDate) {
		this.minRespDate = minRespDate;
	}
	
	public String getAppHostIp() {
		return appHostIp;
	}
	
	public void setAppHostIp(String appHostIp) {
		this.appHostIp = appHostIp;
	}
	
	public String getAppHostSite() {
		return appHostSite;
	}
	
	public void setAppHostSite(String appHostSite) {
		this.appHostSite = appHostSite;
	}
	
	public String getCollectTime() {
		return collectTime;
	}
	
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	
	
}

