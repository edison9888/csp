package com.taobao.monitor.alarm.network.lb;

import java.util.ArrayList;
import java.util.List;

public class LBRelateApp {
	private String lbName;
	private int hostCount;
	private String appName;
	private List<String> relateHostList = new ArrayList<String>();
	public String getLbName() {
		return lbName;
	}
	public void setLbName(String lbName) {
		this.lbName = lbName;
	}
	public int getHostCount() {
		return hostCount;
	}
	public void setHostCount(int hostCount) {
		this.hostCount = hostCount;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public List<String> getRelateHostList() {
		return relateHostList;
	}
	public void setRelateHostList(List<String> relateHostList) {
		this.relateHostList = relateHostList;
	}
	public LBRelateApp(String lbName, int hostCount, String appName,
			List<String> relateHostList) {
		super();
		this.lbName = lbName;
		this.hostCount = hostCount;
		this.appName = appName;
		this.relateHostList = relateHostList;
	}
	@Override
	public String toString() {
		return "LBRelateApp [lbName=" + lbName + ", hostCount=" + hostCount
				+ ", appName=" + appName + ", relateHostList=" + relateHostList
				+ "]";
	}
	

}
