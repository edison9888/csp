package com.taobao.www.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.taobao.www.base.entity.BaseAppConfig;

@SuppressWarnings("serial")
public class AppConfig extends BaseAppConfig implements java.io.Serializable {
	
	
	private String port ;
	
	private String reqTotle;
	
	private String exeTime;
	
	private String macIps;
	
	public AppConfig() {
		super();
	}

	public AppConfig(String appName, String preKinds, String preType,
			int preWay, String userName, String userPass, Date createTime) {
		super(appName, preKinds, preType, preWay, userName, userPass, createTime);
	}

	public AppConfig(int id, String appName, String preKinds, String preType,
			int preWay, String userName, String userPass, Date createTime) {
		super(id, appName, preKinds, preType, preWay, userName, userPass, createTime);
	}
	public String getCreateDateString() {
		SimpleDateFormat formate = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = getCreateTime();
		return formate.format(date);
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getReqTotle() {
		return reqTotle;
	}

	public void setReqTotle(String reqTotle) {
		this.reqTotle = reqTotle;
	}

	public String getExeTime() {
		return exeTime;
	}

	public void setExeTime(String exeTime) {
		this.exeTime = exeTime;
	}

	public String getMacIps() {
		return macIps;
	}

	public void setMacIps(String macIps) {
		this.macIps = macIps;
	}
	
}
