package com.taobao.monitor.common.po;

/**
 * database和app的关联实体
 * @author wuhaiqian.pt
 *
 */
public class DatabaseAppRelPo {

	private int databaseId;
	
	private int appId;
	
	private String appType;



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

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}
}
