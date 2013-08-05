
package com.taobao.monitor.stat.db.po;
/**
 * 
 * @author xiaodu
 * @version 2010-4-6 ÏÂÎç04:09:13
 */
public class MonitorDetail {
	
	private int appId;
	
	private int keyId;
	
	private String valueData;
	
	private String collectTime;

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

	@Override
	public String toString() {
		String msg = "appId:"+appId+" keyId:"+keyId+" valueData:"+valueData+" collectTime:"+collectTime;
		return msg;
	}
	
	

}
