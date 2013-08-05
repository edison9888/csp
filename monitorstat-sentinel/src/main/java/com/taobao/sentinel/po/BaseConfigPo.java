package com.taobao.sentinel.po;


public class BaseConfigPo {
	
	private String id;
	
	private String appName;
	
	private String refAppName;
	
	private String user;
	
	private String version;
	
	private int state;
	
	/*** not persist in database ***/
	private int number;
	
	private boolean outOfDate;
	
	private String stateInfo;
	
	private String opositeStateInfo;
	
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	
	public  String getStateInfo() {
		if (state == 0) {
			stateInfo = "禁用";
		} else {
			stateInfo = "启用";
		}
		
		return stateInfo;
	}
	
	public void setOpositeStateInfo(String opositeStateInfo) {
		this.opositeStateInfo = opositeStateInfo;
	}
	
	public String getOpositeStateInfo() {
		if (state == 0) {
			opositeStateInfo = "启用";
		} else {
			opositeStateInfo = "禁用";
		}
		
		return opositeStateInfo;
	}

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

	public String getRefAppName() {
		return refAppName;
	}

	public void setRefAppName(String refAppName) {
		this.refAppName = refAppName;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isOutOfDate() {
		return outOfDate;
	}

	public void setOutOfDate(boolean outOfDate) {
		this.outOfDate = outOfDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	

}
