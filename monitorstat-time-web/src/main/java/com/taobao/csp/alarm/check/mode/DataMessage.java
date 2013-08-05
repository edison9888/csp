
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.check.mode;

import java.util.List;

import com.taobao.csp.dataserver.memcache.entry.DataEntry;

/**
 * @author xiaodu
 *
 * ÉÏÎç10:49:35
 */
public class DataMessage {
	
	private String appName;
	
	private String keyName;
	
	private String propertyName;
	
	private String ip;
	
	private List<DataEntry> dataList;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<DataEntry> getDataList() {
		return dataList;
	}

	public void setDataList(List<DataEntry> dataList) {
		this.dataList = dataList;
	}
	
	
	

}
