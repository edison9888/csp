package com.taobao.csp.day.tdod;

import java.io.Serializable;

public class TdodLogKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String appName;
	
	private String collectTime;
	
	public TdodLogKey() {
		
	}
	
	public TdodLogKey(String appName, String collectTime) {
		this.appName = appName;
		this.collectTime = collectTime;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
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
		if (!(object instanceof TdodLogKey)) return false;
		
		TdodLogKey po = (TdodLogKey)object;
		if (po.getAppName().equals(this.getAppName()) && po.getCollectTime().equals(this.getCollectTime())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + appName.hashCode();
		result = 37 * result + collectTime.hashCode();
		
		return result;
	}

}
