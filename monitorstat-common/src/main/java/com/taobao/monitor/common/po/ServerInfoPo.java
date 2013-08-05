package com.taobao.monitor.common.po;

import java.util.List;

public class ServerInfoPo {

	private int serverId;
	
	private String serverName;
	
	private String serverIp;
	
	private String serverDesc;

	//存放相关联的app
	private List<AppInfoPo> appInfoPoList;
	
	private int serverStatus; // 0正常 1删除
	

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerDesc() {
		return serverDesc;
	}

	public void setServerDesc(String serverDesc) {
		this.serverDesc = serverDesc;
	}
	
	public List<AppInfoPo> getAppInfoPoList() {
		return appInfoPoList;
	}

	public void setAppInfoPoList(List<AppInfoPo> appInfoPoList) {
		this.appInfoPoList = appInfoPoList;
	}

	public int getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(int serverStatus) {
		this.serverStatus = serverStatus;
	}
	
	
}
