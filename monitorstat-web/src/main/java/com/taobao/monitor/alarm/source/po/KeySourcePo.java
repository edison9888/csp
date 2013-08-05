package com.taobao.monitor.alarm.source.po;

public class KeySourcePo {
	private int id;
	
	private String keyName;
	
	private String keyId;

	private int appId;
	
	private String sourceAppName;
	
	private String sourceGroupName;
	
	private int sourceAppId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getSourceAppName() {
		return sourceAppName;
	}

	public void setSourceAppName(String sourceAppName) {
		this.sourceAppName = sourceAppName;
	}

	public String getSourceGroupName() {
		return sourceGroupName;
	}

	public void setSourceGroupName(String sourceGroupName) {
		this.sourceGroupName = sourceGroupName;
	}

	public int getSourceAppId() {
		return sourceAppId;
	}

	public void setSourceAppId(int sourceAppId) {
		this.sourceAppId = sourceAppId;
	}
	
	
}
