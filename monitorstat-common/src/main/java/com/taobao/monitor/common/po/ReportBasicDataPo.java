package com.taobao.monitor.common.po;

import java.util.Date;

public class ReportBasicDataPo {
	
	private String appName;
	
	private String machineInfo = "0/0";
	
	private double load;
	
	private long pv;
	
	private int uv;
	
	private double qps;
	
	private double rt;
	
	private double capacityLevel;
	
	private double singleCost;
	
	private int fullGcCount;
	
	private double fullGcTime;
	
	private int youngGcCount;
	
	private double youngGcTime;
	
	private Date collectDate;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMachineInfo() {
		return machineInfo;
	}

	public void setMachineInfo(String machineInfo) {
		this.machineInfo = machineInfo;
	}

	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = load;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public double getQps() {
		return qps;
	}

	public void setQps(double qps) {
		this.qps = qps;
	}

	public double getRt() {
		return rt;
	}

	public void setRt(double rt) {
		this.rt = rt;
	}

	public double getCapacityLevel() {
		return capacityLevel;
	}

	public void setCapacityLevel(double capacityLevel) {
		this.capacityLevel = capacityLevel;
	}

	public double getSingleCost() {
		return singleCost;
	}

	public void setSingleCost(double singleCost) {
		this.singleCost = singleCost;
	}

	public int getFullGcCount() {
		return fullGcCount;
	}

	public void setFullGcCount(int fullGcCount) {
		this.fullGcCount = fullGcCount;
	}

	public double getFullGcTime() {
		return fullGcTime;
	}

	public void setFullGcTime(double fullGcTime) {
		this.fullGcTime = fullGcTime;
	}

	public int getYoungGcCount() {
		return youngGcCount;
	}

	public void setYoungGcCount(int youngGcCount) {
		this.youngGcCount = youngGcCount;
	}

	public double getYoungGcTime() {
		return youngGcTime;
	}

	public void setYoungGcTime(double youngGcTime) {
		this.youngGcTime = youngGcTime;
	}

	public Date getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}
	
	@Override
	public String toString() {
		return appName + ":" + machineInfo + ":" + load + ":" + pv + ":" + uv + ":" + qps + ":" + rt + ":"
			+ capacityLevel + ":" + singleCost + ":" + fullGcCount + ":" + fullGcTime + ":" + youngGcCount + ":" + 
				youngGcTime + ":" + collectDate;
	}

}
