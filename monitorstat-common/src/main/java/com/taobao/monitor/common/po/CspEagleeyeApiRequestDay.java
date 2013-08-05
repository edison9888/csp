package com.taobao.monitor.common.po;

import java.util.Date;

public class CspEagleeyeApiRequestDay {
	private String appName;
	private Date collectTime;
	private String responseContent;
	private String apiType;
	private String version;
	
	private String sourcekey;	//appname or url or hsf full key
	
	public String getSourcekey() {
		return sourcekey;
	}
	public void setSourcekey(String sourcekey) {
		this.sourcekey = sourcekey;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public String getResponseContent() {
		return responseContent;
	}
	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
	public String getApiType() {
		return apiType;
	}
	public void setApiType(String apiType) {
		this.apiType = apiType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
