
package com.taobao.csp.depend.po;

import java.util.Date;

import com.taobao.csp.depend.util.ConstantParameters;

public class AppDepApp {

	private String opsName;

	private String selfAppType;

	private String dependOpsName;

	private String dependAppType;

	private CheckupDependConfig config = new CheckupDependConfig(); 

	private Date collectTime;

	private String existStatus = ConstantParameters.CONTROST_SUB;

	private int portInfo;

	private long callnum;

	private double rate;


	public CheckupDependConfig getConfig() {
		return config;
	}

	public void setConfig(CheckupDependConfig config) {
		this.config = config;
	}

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public String getDependOpsName() {
		return dependOpsName;
	}

	public void setDependOpsName(String dependOpsName) {
		this.dependOpsName = dependOpsName;
	}

	public String getDependAppType() {
		return dependAppType;
	}

	public void setDependAppType(String dependAppType) {
		this.dependAppType = dependAppType;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public String getExistStatus() {
		return existStatus;
	}

	public void setExistStatus(String existStatus) {
		this.existStatus = existStatus;
	}

	public int getPortInfo() {
		return portInfo;
	}

	public void setPortInfo(int portInfo) {
		this.portInfo = portInfo;
	}

	public String getSelfAppType() {
		return selfAppType;
	}

	public void setSelfAppType(String selfAppType) {
		this.selfAppType = selfAppType;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AppDepApp){
			AppDepApp app = (AppDepApp)obj;
			if(opsName!=null&&dependOpsName!=null&&opsName.equals(app.getOpsName())
					&&dependOpsName.equals(app.getDependOpsName())){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		String tmp = opsName+"_"+dependOpsName;
		return tmp.hashCode();
	}

	public long getCallnum() {
		return callnum;
	}

	public void setCallnum(long callnum) {
		this.callnum = callnum;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

}
