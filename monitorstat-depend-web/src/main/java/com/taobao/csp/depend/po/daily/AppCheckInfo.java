package com.taobao.csp.depend.po.daily;

import java.util.Date;

public class AppCheckInfo {

	private String opsName;
	private int dependNum;
	private int checkDependAppNum;
	private String machineIp;
	private Date lastCheckTime;
	
	public String getOpsName() {
		return opsName;
	}


	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}


	public int getDependNum() {
		return dependNum;
	}


	public void setDependNum(int dependNum) {
		this.dependNum = dependNum;
	}


	public String getMachineIp() {
		return machineIp;
	}


	public void setMachineIp(String machineIp) {
		this.machineIp = machineIp;
	}


	public Date getLastCheckTime() {
		return lastCheckTime;
	}


	public void setLastCheckTime(Date lastCheckTime) {
		this.lastCheckTime = lastCheckTime;
	}


	public int getCheckDependAppNum() {
		return checkDependAppNum;
	}


	public void setCheckDependAppNum(int checkDependAppNum) {
		this.checkDependAppNum = checkDependAppNum;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
