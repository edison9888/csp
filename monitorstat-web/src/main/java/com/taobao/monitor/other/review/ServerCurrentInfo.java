package com.taobao.monitor.other.review;

import com.taobao.monitor.common.po.ServerInfoPo;

public class ServerCurrentInfo {

	private String usedSpace;// ��ʹ�ÿռ�ٷֱ�
	
	private String allSpace;// ���̿ռ�
	
	private int dayAppCount;// �ձ���ص�app����
	
	private int timeAppcount;// ʵ����ص�app����
	
	private int mysqlCount;// mysql���ݿ������
	
	private String serverIp;
	
	private String serverName;

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getUsedSpace() {
		return usedSpace;
	}

	public void setUsedSpace(String usedSpace) {
		this.usedSpace = usedSpace;
	}

	public String getAllSpace() {
		return allSpace;
	}

	public void setAllSpace(String allSpace) {
		this.allSpace = allSpace;
	}

	public int getDayAppCount() {
		return dayAppCount;
	}

	public void setDayAppCount(int dayAppCount) {
		this.dayAppCount = dayAppCount;
	}

	public int getTimeAppcount() {
		return timeAppcount;
	}

	public void setTimeAppcount(int timeAppcount) {
		this.timeAppcount = timeAppcount;
	}

	public int getMysqlCount() {
		return mysqlCount;
	}

	public void setMysqlCount(int mysqlCount) {
		this.mysqlCount = mysqlCount;
	}

	public String getStorage() {
		return usedSpace;
	}

	public void setStorage(String usedSpace) {
		this.usedSpace = usedSpace;
	}

}
