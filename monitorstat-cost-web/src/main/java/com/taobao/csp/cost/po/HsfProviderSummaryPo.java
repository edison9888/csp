package com.taobao.csp.cost.po;

import java.util.Date;

public class HsfProviderSummaryPo {
	
	private String providerName;
	
	private String providerGroup;
	
	private float rushTimeQps;
	
	private float rushTimeRt;
	
	private long callSum;
	
	private String roomFeature;
	
	private Date collectTime;

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderGroup() {
		return providerGroup;
	}

	public void setProviderGroup(String providerGroup) {
		this.providerGroup = providerGroup;
	}

	public float getRushTimeQps() {
		return rushTimeQps;
	}

	public void setRushTimeQps(float rushTimeQps) {
		this.rushTimeQps = rushTimeQps;
	}

	public float getRushTimeRt() {
		return rushTimeRt;
	}

	public void setRushTimeRt(float rushTimeRt) {
		this.rushTimeRt = rushTimeRt;
	}

	public long getCallSum() {
		return callSum;
	}

	public void setCallSum(long callSum) {
		this.callSum = callSum;
	}

	public String getRoomFeature() {
		return roomFeature;
	}

	public void setRoomFeature(String roomFeature) {
		this.roomFeature = roomFeature;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

}
