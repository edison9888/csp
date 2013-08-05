package com.taobao.csp.day.po;

public class ApacheSpecialPo {
	
	private String appName;
	
	private String requstUrl;
	
	private String httpCode;
	
	private String collectTime;
	
	private long requstNum;
	
	private long rt;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getRequstUrl() {
		return requstUrl;
	}

	public void setRequstUrl(String requstUrl) {
		this.requstUrl = requstUrl;
	}

	public String getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(String httpCode) {
		this.httpCode = httpCode;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public long getRequstNum() {
		return requstNum;
	}

	public void setRequstNum(long requstNum) {
		this.requstNum = requstNum;
	}

	public long getRt() {
		return rt;
	}

	public void setRt(long rt) {
		this.rt = rt;
	}
}
