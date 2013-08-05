package com.taobao.csp.day.apache;

import java.io.Serializable;

/***
 * apache special log µÄkeyÐÅÏ¢
 * @author youji.zj
 * 
 * @version 1.0 2012-11-12
 *
 */
public class ApacheSpecialLogKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String appName;
	
	private String requstUrl;
	
	private String httpCode;
	
	private String collectTime;
	
	public ApacheSpecialLogKey() {
		
	}
	
	public ApacheSpecialLogKey(String appName, String requstUrl, String httpCode,
			String collectTime) {
		this.appName = appName;
		this.requstUrl = requstUrl;
		this.httpCode = httpCode;
		this.collectTime = collectTime;
	}

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

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof ApacheSpecialLogKey)) return false;
		
		ApacheSpecialLogKey po = (ApacheSpecialLogKey)object;
		if (po.getAppName().equals(this.getAppName()) && po.getRequstUrl().equals(this.getRequstUrl()) && 
				po.getHttpCode().equals(this.getHttpCode()) && po.getCollectTime().equals(this.getCollectTime())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + appName.hashCode();
		result = 37 * result + requstUrl.hashCode();
		result = 37 * result + httpCode.hashCode();
		result = 37 * result + collectTime.hashCode();
		
		return result;
	}

}
