package com.taobao.monitor.common.po;

public class HostTmpPo {

	private int hostId;
	
	private String hostIp;
	
	private String hostSite;//�����ĸ�����
	
	private String hostName;
	
	//��һλ ��ʾֻ���� limit ���ڶ�λ��ʾ����data
	private String savedata;
	
	private int status;	//'0 ���� 1��ʾɾ��'

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getSavedata() {
		return savedata;
	}

	public void setSavedata(String savedata) {
		this.savedata = savedata;
	}

	public String getHostSite() {
		return hostSite;
	}

	public void setHostSite(String hostSite) {
		this.hostSite = hostSite;
	}

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
