package com.taobao.www.entity;

import java.util.Date;

import com.taobao.www.base.entity.BaseAppMachine;

@SuppressWarnings("serial")
public class AppMachine extends BaseAppMachine implements java.io.Serializable {

	
	private String userName;
	
	private String password;
	
	private String preKinds;
	
	private String machineName;
	
	private String machineIp;
	
	
	public AppMachine(String macName) {
		super(macName);
	}
	public AppMachine( ) {
	}
	public AppMachine(int appId, String appName, String macName, String macOs,
			String macIp,String macState, Date createTime) {
		super(appId, appName, macName, macOs, macIp,macState,createTime);
	}

	public AppMachine(String userName, String password, String preKinds,String machineName) {
		this.userName = userName;
		this.password = password;
		this.preKinds = preKinds;
		this.machineName = machineName;
	}
	
	public AppMachine(String userName, String password, String preKinds,String machineName,String machineIp) {
		this.userName = userName;
		this.password = password;
		this.preKinds = preKinds;
		this.machineName = machineName;
		this.machineIp = machineIp;
	}
	
	
	public AppMachine(int id, int appId, String appName, String macName,
			String macOs, String macIp, String macState, int macTotle,
			Date createTime) {
		super(id, appId, appName, macName, macOs, macIp, macState, macTotle, createTime);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPreKinds() {
		return preKinds;
	}

	public void setPreKinds(String preKinds) {
		this.preKinds = preKinds;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getMachineIp() {
		return machineIp;
	}
	public void setMachineIp(String machineIp) {
		this.machineIp = machineIp;
	}
    
	 

	 
	 
}
