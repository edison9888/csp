package com.taobao.csp.day.tddl;

import java.io.Serializable;

public class TDDL implements Serializable {
	private static final long serialVersionUID = 5261330324535896003L;
	
	public long exeCount;
	public long respTime;
	
	public int maxResp;
	public int minResp;
	public String maxRespDate;
	public String minRespDate;
	
	public long getExeCount() {
		return exeCount;
	}
	public void setExeCount(long exeCount) {
		this.exeCount = exeCount;
	}
	public long getRespTime() {
		return respTime;
	}
	public void setRespTime(long respTime) {
		this.respTime = respTime;
	}
	public int getMaxResp() {
		return maxResp;
	}
	public void setMaxResp(int maxResp) {
		this.maxResp = maxResp;
	}
	public int getMinResp() {
		return minResp;
	}
	public void setMinResp(int minResp) {
		this.minResp = minResp;
	}
	public String getMaxRespDate() {
		return maxRespDate;
	}
	public void setMaxRespDate(String maxRespDate) {
		this.maxRespDate = maxRespDate;
	}
	public String getMinRespDate() {
		return minRespDate;
	}
	public void setMinRespDate(String minRespDate) {
		this.minRespDate = minRespDate;
	}
}
