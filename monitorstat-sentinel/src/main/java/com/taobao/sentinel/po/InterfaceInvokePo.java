package com.taobao.sentinel.po;

public class InterfaceInvokePo {

private String id;
	
	private String appName;
	
	private String refApp;
	
	private String interfaceInfo;
	
	private int estimateQps;
	
	private String strong;
	
	private String remark;
	
	private String user;
	
	/*** not persist in database ***/
	private int number;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getRefApp() {
		return refApp;
	}

	public void setRefApp(String refApp) {
		this.refApp = refApp;
	}

	public String getInterfaceInfo() {
		return interfaceInfo;
	}

	public void setInterfaceInfo(String interfaceInfo) {
		this.interfaceInfo = interfaceInfo;
	}

	public int getEstimateQps() {
		return estimateQps;
	}

	public void setEstimateQps(int estimateQps) {
		this.estimateQps = estimateQps;
	}

	public String getStrong() {
		return strong;
	}

	public void setStrong(String strong) {
		this.strong = strong;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
