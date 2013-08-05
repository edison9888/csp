package com.taobao.monitor.other.review;

public class AppHostPo {

	private String appName;
	
	private int hostAcount;// 机器总数
	
	private int success;// 通过验证数
	
	private int fail;// 未通过验证数
	
	private int monitorAcount;// 该应用被监控的主机数
	
	private int online;// 主机在线数
	
	private int offline;// 主机离线数

	public int getMonitorAcount() {
		return monitorAcount;
	}

	public void setMonitorAcount(int monitorAcount) {
		this.monitorAcount = monitorAcount;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getOffline() {
		return offline;
	}

	public void setOffline(int offline) {
		this.offline = offline;
	}

	public AppHostPo() {

	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getHostAcount() {
		return hostAcount;
	}

	public void setHostAcount(int hostAcount) {
		this.hostAcount = hostAcount;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}
}
