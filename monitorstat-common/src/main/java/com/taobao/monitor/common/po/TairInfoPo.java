package com.taobao.monitor.common.po;
/**
 * 
 * @author denghaichuan.pt
 * @version 2011-9-6
 */

public class TairInfoPo {
	
	private String appName;

	private String actionType;
	
	private String siteName;
	
	private String nameSpace;
	
	private String groupName;
	
	private String hostIp;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	private long Data1;
	
	private long Data2;

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public long getData1() {
		return Data1;
	}

	public void setData1(long data1) {
		Data1 = data1;
	}

	public long getData2() {
		return Data2;
	}

	public void setData2(long data2) {
		Data2 = data2;
	}
	
	
	
	
	
	
	
}
