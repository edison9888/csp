package com.taobao.monitor.common.po;

import java.util.Date;

public class ReportInvokeDataPo {
	
	private String appName;
	
	private String invokeType;
	
	private String resouceName;
	
	private long count;
	
	private double time;
	
	private Date collectDate;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getInvokeType() {
		return invokeType;
	}

	public void setInvokeType(String invokeType) {
		this.invokeType = invokeType;
	}

	public String getResouceName() {
		return resouceName;
	}

	public void setResouceName(String resouceName) {
		this.resouceName = resouceName;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public Date getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}
	
	@Override
	public String toString() {
		return appName + ":" + invokeType + ":" + resouceName + ":" + count + ":" + time + ":" + collectDate;
	}

}
