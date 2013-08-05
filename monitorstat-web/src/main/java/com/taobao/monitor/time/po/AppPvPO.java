package com.taobao.monitor.time.po;

import java.util.Date;

public class AppPvPO {
	private String appName;
	private Integer pvCount;
	private String urlName;
	private Date pvTime;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Integer getPvCount() {
		return pvCount;
	}
	public void setPvCount(Integer pvCount) {
		this.pvCount = pvCount;
	}
	public String getUrlName() {
		return urlName;
	}
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	public Date getPvTime() {
		return pvTime;
	}
	public void setPvTime(Date pvTime) {
		this.pvTime = pvTime;
	}
}
