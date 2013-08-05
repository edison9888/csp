
package com.taobao.monitor.common.po;
/**
 * 
 * @author xiaodu
 * @version 2010-4-6 ÏÂÎç03:59:39
 */
public class MonitorSite {
	private int id;
	private String siteName;
	
	private String defaultConfg;
	
	private String features;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
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
