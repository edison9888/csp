
package com.taobao.monitor.web.core.po;
/**
 * 
 * @author xiaodu
 * @version 2010-8-10 ÏÂÎç04:10:10
 */
public class JprofHost {
	
	private int id;
	
	
	private String appName;
	
	private String hostIp;
	
	private String hostUser;
	
	private String hostPasswd;
	
	private String filePath;
	
	private int runType;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getHostUser() {
		return hostUser;
	}

	public void setHostUser(String hostUser) {
		this.hostUser = hostUser;
	}

	public String getHostPasswd() {
		return hostPasswd;
	}

	public void setHostPasswd(String hostPasswd) {
		this.hostPasswd = hostPasswd;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getRunType() {
		return runType;
	}

	public void setRunType(int runType) {
		this.runType = runType;
	}
	
	

}
