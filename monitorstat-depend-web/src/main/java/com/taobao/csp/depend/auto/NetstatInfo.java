
package com.taobao.csp.depend.auto;
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



	@Override
	public String toString() {
		return "localIp="+localIp+":"+localPort+" foreignIp="+foreignIp+":"+foreignPort;
	}

	
	

}
