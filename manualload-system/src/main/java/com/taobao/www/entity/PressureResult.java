package com.taobao.www.entity;

import java.util.Date;

import com.taobao.www.base.entity.BasePressureResult;

@SuppressWarnings("serial")
public class PressureResult extends BasePressureResult implements java.io.Serializable {

	private String appName;

	private String preDate;

	private String insertTime;

	public PressureResult() {
		super();
	}

	public PressureResult(Integer id, Integer requestTotle, Integer processCount, Integer bytesTotle,
			Integer timeTotle, Integer bytesConnection, Integer fetchesSec, Integer bytesSec, Integer connectAvg,
			Integer connectMax, Integer connectMin, Integer responseAvg, Integer responseMax, Integer responseMin,
			Integer badCount, String httpState_stateCount, Integer appId, String userName, Date createTime,
			String macName, String appName) {
		super(id, requestTotle, processCount, bytesTotle, timeTotle, bytesConnection, fetchesSec, bytesSec, connectAvg,
				connectMax, connectMin, responseAvg, responseMax, responseMin, badCount, httpState_stateCount, appId,
				userName, createTime, macName);
		this.appName = appName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPreDate() {
		return preDate;
	}

	public void setPreDate(String preDate) {
		this.preDate = preDate;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

}
