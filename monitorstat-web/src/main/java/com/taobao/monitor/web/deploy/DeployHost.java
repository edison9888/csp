
package com.taobao.monitor.web.deploy;

import java.util.List;

import com.taobao.monitor.common.po.AppInfoPo;

/**
 * 
 * @author xiaodu
 * @version 2010-5-21 ÏÂÎç02:16:18
 */
public class DeployHost {
	
	private String ip;
	
	private String userName ;
	
	private String password;
	
	private List<AppInfoPo> appList;
	
	
	private String jarpath;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<AppInfoPo> getAppList() {
		return appList;
	}

	public void setAppList(List<AppInfoPo> appList) {
		this.appList = appList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJarpath() {
		return jarpath;
	}

	public void setJarpath(String jarpath) {
		this.jarpath = jarpath;
	} 
	
	
	
	
	

}
