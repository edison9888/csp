package com.taobao.csp.loadrun.web.po;

import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.TBProductCache;


public class LoadrunAppInfoPo {
	
	private String productName;
	
	private String groupName;
	
	private String appName;
	
	private int appId;
	
	public LoadrunAppInfoPo(AppInfoPo appInfoPo) {
		this.appName = appInfoPo.getAppName();
		this.appId = appInfoPo.getAppId();
		this.productName = TBProductCache.getProductLineByAppName(appName).getDevelopGroup();
		this.groupName = TBProductCache.getProductLineByAppName(appName).getProductline();
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

}
