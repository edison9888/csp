package com.taobao.csp.day.sph;

import java.io.Serializable;

public class SphLogKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String appName;
	
	private String ip;
	
	private String blockKey;
	
	private String action;
	
	private String collectTime;
	
	public SphLogKey() {
		
	}
	
	public SphLogKey(String appName, String ip, String blockKey, String action,
			String collectTime) {
		this.appName = appName;
		this.ip = ip;
		this.blockKey = blockKey;
		this.action = action;
		this.collectTime = collectTime;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getBlockKey() {
		return blockKey;
	}

	public void setBlockKey(String blockKey) {
		this.blockKey = blockKey;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
		if (!(object instanceof SphLogKey)) return false;
		
		SphLogKey po = (SphLogKey)object;
		if (po.getAppName().equals(this.getAppName()) && po.getIp().equals(this.getIp()) && 
				po.getBlockKey().equals(this.getBlockKey()) && 
				po.getAction().equals(this.getAction()) && po.getCollectTime().equals(this.getCollectTime())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + appName.hashCode();
		result = 37 * result + ip.hashCode();
		result = 37 * result + blockKey.hashCode();
		result = 37 * result + action.hashCode();
		result = 37 * result + collectTime.hashCode();
		
		return result;
	}

}
