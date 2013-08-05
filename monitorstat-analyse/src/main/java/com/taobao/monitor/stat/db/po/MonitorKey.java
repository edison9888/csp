
package com.taobao.monitor.stat.db.po;
/**
 * 
 * @author xiaodu
 * @version 2010-4-6 ÏÂÎç03:59:39
 */
public class MonitorKey {
	private int id;
	private String keyName;
	
	private String defaultConfg;
	
	private String features;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getDefaultConfg() {
		return defaultConfg;
	}

	public void setDefaultConfg(String defaultConfg) {
		this.defaultConfg = defaultConfg;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

}
