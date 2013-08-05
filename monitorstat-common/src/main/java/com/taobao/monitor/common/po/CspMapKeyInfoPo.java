package com.taobao.monitor.common.po;

import java.util.Date;

public class CspMapKeyInfoPo {
	private long id;
	private String keyname;
	private String appname;
	private int keyLevel;
	private double rate;
	private int controlType;
	private String updateBy;
	private Date updateTime;
	private int isBlack;
	private int keyStatus;
	
	private String dataFrom = "";	//·ÇÊý¾Ý¿â×Ö¶Î
	
	public String getDataFrom() {
		return dataFrom;
	}
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getKeyname() {
		return keyname;
	}
	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public int getKeyLevel() {
		return keyLevel;
	}
	public void setKeyLevel(int keyLevel) {
		this.keyLevel = keyLevel;
	}
	public int getControlType() {
		return controlType;
	}
	public void setControlType(int controlType) {
		this.controlType = controlType;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getIsBlack() {
		return isBlack;
	}
	public void setIsBlack(int isBlack) {
		this.isBlack = isBlack;
	}
	public int getKeyStatus() {
		return keyStatus;
	}
	public void setKeyStatus(int keyStatus) {
		this.keyStatus = keyStatus;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
}
