package com.taobao.www.base.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public abstract class BaseAppConfig implements Serializable {
	
	private int id;
	
	private String appName;
	
	private String preKinds;
	
	private String preType;
	
	private int preWay;
	
	private int prePort;
	
	private String userName;
	
	private String userPass;
	
	private Date createTime;
	
	public BaseAppConfig() {
		super();
	}

	public BaseAppConfig(int id, String appName, String preKinds,
			String preType, int preWay, String userName, String userPass,
			Date createTime) {
		super();
		this.id = id;
		this.appName = appName;
		this.preKinds = preKinds;
		this.preType = preType;
		this.preWay = preWay;
		this.userName = userName;
		this.userPass = userPass;
		this.createTime = createTime;
	}

	public BaseAppConfig(String appName, String preKinds, String preType,
			int preWay, String userName, String userPass, Date createTime) {
		super();
		this.appName = appName;
		this.preKinds = preKinds;
		this.preType = preType;
		this.preWay = preWay;
		this.userName = userName;
		this.userPass = userPass;
		this.createTime = createTime;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPreKinds() {
		return preKinds;
	}

	public void setPreKinds(String preKinds) {
		this.preKinds = preKinds;
	}

	public String getPreType() {
		return preType;
	}

	public void setPreType(String preType) {
		this.preType = preType;
	}

	public int getPreWay() {
		return preWay;
	}

	public void setPreWay(int preWay) {
		this.preWay = preWay;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getPrePort() {
		return prePort;
	}

	public void setPrePort(int prePort) {
		this.prePort = prePort;
	}
	
	

}
