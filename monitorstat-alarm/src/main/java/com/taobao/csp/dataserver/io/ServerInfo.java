
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;

import org.apache.commons.lang.StringUtils;

/**
 * @author xiaodu
 *
 * ÏÂÎç4:18:09
 */
public class ServerInfo implements Comparable<ServerInfo>{
	
	private String serverInfo;
	
	private String ip;
	
	private int port;
	
	public ServerInfo(String ipInfo){
		this.serverInfo = ipInfo;
		try{
			String[] tmp = StringUtils.split(ipInfo,":");
			this.ip = tmp[0];
			this.port = Integer.parseInt(tmp[1]);
		}catch (Exception e) {
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public int hashCode() {
		return ("["+serverInfo+"]").hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ServerInfo){
			ServerInfo tmp = (ServerInfo)obj;
			
			if(tmp.toString().equals(this.toString())){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return ("["+serverInfo+"]");
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}

	@Override
	public int compareTo(ServerInfo o) {
		return this.serverInfo.compareTo(o.getServerInfo());
	}
	
	

}
