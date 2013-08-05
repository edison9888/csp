
package com.taobao.monitor.dependent.job;
/**
 * 
 * @author xiaodu
 * @version 2011-4-25 ÏÂÎç04:26:02
 */
public class NetstatInfo {
	
	private String localIp;
	
	private int localPort;
	
	private String foreignIp;

	private int foreignPort;
	
	private String foreignOpsName;
	
	private String foreignSiteName;

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getForeignIp() {
		return foreignIp;
	}

	public void setForeignIp(String foreignIp) {
		this.foreignIp = foreignIp;
	}

	public int getForeignPort() {
		return foreignPort;
	}

	public void setForeignPort(int foreignPort) {
		this.foreignPort = foreignPort;
	}

	public String getForeignOpsName() {
		return foreignOpsName;
	}

	public void setForeignOpsName(String foreignOpsName) {
		this.foreignOpsName = foreignOpsName;
	}

	public String getForeignSiteName() {
		return foreignSiteName;
	}

	public void setForeignSiteName(String foreignSiteName) {
		this.foreignSiteName = foreignSiteName;
	}

	
	

}
