package com.taobao.www.base.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public abstract class BaseAppMachine implements Serializable {
	
	private int id;
	
	private int appId;
	
	private String appName;
	
	private String macName;
	 
	private String macOs;
	
	private String macIp;
	
	private String macState;
	
	private int   macTotle;
	
	private Date createTime;

	public BaseAppMachine() {
		super();
	}
	
	public BaseAppMachine(String macName) {
		this.macName = macName;
	}

	public BaseAppMachine(int appId, String appName, String macName, String macOs,
			Date createTime) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.macName = macName;
		this.macOs = macOs;
		this.createTime = createTime;
	}
	
	public BaseAppMachine(String appName, String macName,
			String macOs, Date createTime) {
		super();
		this.appName = appName;
		this.macName = macName;
		this.macOs = macOs;
		this.createTime = createTime;
	}
	
	public BaseAppMachine(int id, int appId, String appName, String macName,
			String macOs, String macIp, String macState, int macTotle,
			Date createTime) {
		super();
		this.id = id;
		this.appId = appId;
		this.appName = appName;
		this.macName = macName;
		this.macOs = macOs;
		this.macIp = macIp;
		this.macState = macState;
		this.macTotle = macTotle;
		this.createTime = createTime;
	}
	
	

	public BaseAppMachine(String macName, String macIp) {
		super();
		this.macName = macName;
		this.macIp = macIp;
	}

	public BaseAppMachine(int appId, String appName, String macName,
			String macOs, String macIp,String macState, Date createTime) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.macName = macName;
		this.macOs = macOs;
		this.macIp = macIp;
		this.macState = macState;
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMacName() {
		return macName;
	}

	public void setMacName(String macName) {
		this.macName = macName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMacOs() {
		return macOs;
	}

	public void setMacOs(String macOs) {
		this.macOs = macOs;
	}

	public String getMacIp() {
		return macIp;
	}

	public void setMacIp(String macIp) {
		this.macIp = macIp;
	}

	public String getMacState() {
		return macState;
	}

	public void setMacState(String macState) {
		this.macState = macState;
	}

	public int getMacTotle() {
		return macTotle;
	}

	public void setMacTotle(int macTotle) {
		this.macTotle = macTotle;
	}

 
	
	

}
