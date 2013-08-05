
package com.taobao.csp.loadrun.core;

import java.util.Date;

import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.ResultKey;

/**
 * 
 * @author xiaodu
 * @version 2011-6-24 ÏÂÎç02:12:22
 */
public class LoadrunResult {
	
	private int appId;
	
	private String loadId;
	
	private String targetIp;
	
	private AutoLoadType loadrunType;
	
	private String controlFeature;
	
	private ResultKey key;
	
	private double value;
	
	private int loadrunOrder;
	
	private Date collectTime;

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public ResultKey getKey() {
		return key;
	}

	public void setKey(ResultKey key) {
		this.key = key;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	

	public AutoLoadType getLoadrunType() {
		return loadrunType;
	}

	public void setLoadrunType(AutoLoadType loadrunType) {
		this.loadrunType = loadrunType;
	}

	public String getControlFeature() {
		return controlFeature;
	}

	public void setControlFeature(String controlFeature) {
		this.controlFeature = controlFeature;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public int getLoadrunOrder() {
		return loadrunOrder;
	}

	public void setLoadrunOrder(int loadrunOrder) {
		this.loadrunOrder = loadrunOrder;
	}

	public String getLoadId() {
		return loadId;
	}

	public void setLoadId(String loadId) {
		this.loadId = loadId;
	}
	
	
	
	

}
