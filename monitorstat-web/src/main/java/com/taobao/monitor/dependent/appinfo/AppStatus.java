
package com.taobao.monitor.dependent.appinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2011-5-3 ÏÂÎç03:34:04
 */
public class AppStatus {
	
	private String id;
	
	
	
	private String hostIp;
	private String appName;
	private String hostSite;
	
	private String httpdStartTime;
	
	private String jbossStartTime;
	
	private String webInfo;
	
	private String webxInfo;
	
	
	private List<AppJar> jarList = new ArrayList<AppJar>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getHostSite() {
		return hostSite;
	}

	public void setHostSite(String hostSite) {
		this.hostSite = hostSite;
	}

	public String getHttpdStartTime() {
		return httpdStartTime;
	}

	public void setHttpdStartTime(String httpdStartTime) {
		this.httpdStartTime = httpdStartTime;
	}

	public String getJbossStartTime() {
		return jbossStartTime;
	}

	public void setJbossStartTime(String jbossStartTime) {
		this.jbossStartTime = jbossStartTime;
	}

	public String getWebInfo() {
		return webInfo;
	}

	public void setWebInfo(String webInfo) {
		this.webInfo = webInfo;
	}

	public String getWebxInfo() {
		return webxInfo;
	}

	public void setWebxInfo(String webxInfo) {
		this.webxInfo = webxInfo;
	}

	public List<AppJar> getJarList() {
		return jarList;
	}

	public void setJarList(List<AppJar> jarList) {
		this.jarList = jarList;
	}
	
	
	
	

}
