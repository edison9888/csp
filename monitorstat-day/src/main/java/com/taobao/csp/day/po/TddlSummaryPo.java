package com.taobao.csp.day.po;

/***
 * »ã×ÜÊý¾Ýpo
 * 
 * @author youji.zj
 * @version 1.0
 *
 */
public class TddlSummaryPo {
	
	private String appName;
	private String dbFeature;
	private String dbName;
	private String dbIp;
	private String dbPort;
	
	private long exeCount;
	private float respTime;
	
	private int maxResp;
	private int minResp = 1000;
	private String maxRespDate;
	private String minRespDate;
	
	private String roomFeature;
	private String collectDate;
	
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
	
	public String getRoomFeature() {
		return roomFeature;
	}
	
	public void setRoomFeature(String roomFeature) {
		this.roomFeature = roomFeature;
	}
	
	public String getCollectDate() {
		return collectDate;
	}
	
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}
}
