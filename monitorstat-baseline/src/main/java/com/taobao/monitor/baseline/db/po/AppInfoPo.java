
package com.taobao.monitor.baseline.db.po;
/**
 * 
 * @author xiaodu
 * @version 2010-5-17 ионГ10:28:38
 */
public class AppInfoPo implements Comparable<AppInfoPo>{
	
	private int appId;
	
	private String appName;
	
	private int sortIndex;
	
	private String feature;

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public int compareTo(AppInfoPo o) {
		
		if(this.getSortIndex()>o.getSortIndex()){
			return 1;
		}else if(this.getSortIndex()==o.getSortIndex()){
			return 0;
		}else if(this.getSortIndex()<o.getSortIndex()){
			return -1;
		}		
		return 0;
	}
	
}
