
package com.taobao.monitor.common.po;
/**
 * 
 * @author xiaodu
 * @version 2010-4-6 ÏÂÎç04:09:13
 */
public class MonitorDetail {
	
	private int appId;	
	private int keyId;	
	private int siteId;	
	private String valueData;	
	private String collectTime; //yyyy-MM-dd HH:mm	
	private String monitorKeyType;	
	private String monitorKeyFlag;
	
	private String monitorDesc;
	
	private String keyName;

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

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

	public String getValueData() {
		return valueData;
	}

	public void setValueData(String valueData) {
		this.valueData = valueData;
	}
	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getMonitorKeyType() {
		return monitorKeyType;
	}

	public void setMonitorKeyType(String monitorKeyType) {
		this.monitorKeyType = monitorKeyType;
	}

	public String getMonitorKeyFlag() {
		return monitorKeyFlag;
	}

	public void setMonitorKeyFlag(String monitorKeyFlag) {
		this.monitorKeyFlag = monitorKeyFlag;
	}

	public String getMonitorDesc() {
		return monitorDesc;
	}

	public void setMonitorDesc(String monitorDesc) {
		this.monitorDesc = monitorDesc;
	}

	
	public String toString() {
		String msg = "appId:"+appId+"siteId:"+siteId+" keyId:"+keyId+" valueData:"+valueData+" collectTime:"+collectTime;
		return msg;
	}

	
	
	

}
