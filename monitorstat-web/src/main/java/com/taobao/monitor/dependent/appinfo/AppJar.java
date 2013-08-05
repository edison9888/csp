
package com.taobao.monitor.dependent.appinfo;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2011-5-3 ÏÂÎç03:20:08
 */
public class AppJar {
	
	private String appStatusId;
	
	private String jarName;
	
	private int jarSize;
	
	private String modifyTime;
	
	private String metaInfo;
	
	private Date collectTime;

	
	
	public String getAppStatusId() {
		return appStatusId;
	}

	public void setAppStatusId(String appStatusId) {
		this.appStatusId = appStatusId;
	}

	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	public int getJarSize() {
		return jarSize;
	}

	public void setJarSize(int jarSize) {
		this.jarSize = jarSize;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	
	

}
