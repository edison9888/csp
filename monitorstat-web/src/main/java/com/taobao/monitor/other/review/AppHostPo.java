package com.taobao.monitor.other.review;

public class AppHostPo {

	private String appName;
	
	private int hostAcount;// ��������
	
	private int success;// ͨ����֤��
	
	private int fail;// δͨ����֤��
	
	private int monitorAcount;// ��Ӧ�ñ���ص�������
	
	private int online;// ����������
	
	private int offline;// ����������

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
