package com.taobao.monitor.common.po;

/**
 * Server和app的关联实体
 * @author wuhaiqian.pt
 *
 */
public class ServerAppRelPo {

	private int serverId;
	
	private int appId;
	
	private String appType;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}
}
