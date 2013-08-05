package com.taobao.monitor.web.po;

import java.util.Date;

public class MonitorStatisticsDataPo {

	private int appId;
	private int keyId;
	private int siteCount;
	private double totalData;
	private Date collectTime;
	
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getKeyId() {
		return keyId;
	}
	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}
	public int getSiteCount() {
		return siteCount;
	}
	public void setSiteCount(int siteCount) {
		this.siteCount = siteCount;
	}
	public double getTotalData() {
		return totalData;
	}
	public void setTotalData(double totalData) {
		this.totalData = totalData;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	
	
}
