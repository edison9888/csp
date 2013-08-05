
/**
 * monitorstat-time-web
 */
package com.taobao.monitor.time.po;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaodu
 *
 * 下午2:06:35
 */
public class SortEntry<T> implements Comparable<SortEntry<T>>{
	
	private String appName; //应用名称
	
	private String fullKeyName; //这个key的完整名称
	
	private String keyName; //展示展示用的名称，会将父key的名称截掉
	
	private String ip; //应用ip
	
	private double sortValue;
	//pv-refer url对应的应用名
	private String referAppName;
	
	private Map<String,T> objectMap = new HashMap<String, T>();
	

	public String getReferAppName() {
		return referAppName;
	}

	public void setReferAppName(String referAppName) {
		this.referAppName = referAppName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}


	public double getSortValue() {
		return sortValue;
	}

	public void setSortValue(double sortValue) {
		this.sortValue = sortValue;
	}


	public Map<String, T> getObjectMap() {
		return objectMap;
	}

	public void setObjectMap(Map<String, T> objectMap) {
		this.objectMap = objectMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SortEntry<T> o) {
		
		if(o.getSortValue() >this.getSortValue()){
			return 1;
		}else if(o.getSortValue() <this.getSortValue()){
			return -1;
		}
		return 0;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getFullKeyName() {
		return fullKeyName;
	}

	public void setFullKeyName(String fullKeyName) {
		this.fullKeyName = fullKeyName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
