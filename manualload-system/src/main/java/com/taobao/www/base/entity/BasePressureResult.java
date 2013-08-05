package com.taobao.www.base.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class BasePressureResult implements Serializable {
	
	private Integer id;
	
	private Integer requestTotle;
	
	private Integer processCount;
	
	private Integer bytesTotle;
	
	private Integer timeTotle;
	
	private Integer bytesConnection;
	
	private Integer fetchesSec;
	
	private Integer bytesSec;
	
	private Integer connectAvg;
	
	private Integer connectMax;
	
	private Integer connectMin;
	
	private Integer responseAvg;
	
	private Integer responseMax;
	
	private Integer responseMin;
	
	private Integer badCount;
	
	private String httpState_stateCount;
	
	private Integer appId;
	
	private String userName;
	
	private Date createTime;
	
	private String macName;
	
	public BasePressureResult() {
		super();
	}
 
	public BasePressureResult(Integer id,Integer requestTotle, Integer processCount,
			Integer bytesTotle, Integer timeTotle, Integer bytesConnection,
			Integer fetchesSec, Integer bytesSec, Integer connectAvg,
			Integer connectMax, Integer connectMin, Integer responseAvg,
			Integer responseMax, Integer responseMin, Integer badCount,
			String httpState_stateCount, Integer appId, String userName,
			Date createTime, String macName) {
		super();
		this.id = id;
		this.requestTotle = requestTotle;
		this.processCount = processCount;
		this.bytesTotle = bytesTotle;
		this.timeTotle = timeTotle;
		this.bytesConnection = bytesConnection;
		this.fetchesSec = fetchesSec;
		this.bytesSec = bytesSec;
		this.connectAvg = connectAvg;
		this.connectMax = connectMax;
		this.connectMin = connectMin;
		this.responseAvg = responseAvg;
		this.responseMax = responseMax;
		this.responseMin = responseMin;
		this.badCount = badCount;
		this.httpState_stateCount = httpState_stateCount;
		this.appId = appId;
		this.userName = userName;
		this.createTime = createTime;
		this.macName = macName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRequestTotle() {
		return requestTotle;
	}

	public void setRequestTotle(Integer requestTotle) {
		this.requestTotle = requestTotle;
	}

	public Integer getProcessCount() {
		return processCount;
	}

	public void setProcessCount(Integer processCount) {
		this.processCount = processCount;
	}

	public Integer getBytesTotle() {
		return bytesTotle;
	}

	public void setBytesTotle(Integer bytesTotle) {
		this.bytesTotle = bytesTotle;
	}

	public Integer getTimeTotle() {
		return timeTotle;
	}

	public void setTimeTotle(Integer timeTotle) {
		this.timeTotle = timeTotle;
	}

	public Integer getBytesConnection() {
		return bytesConnection;
	}

	public void setBytesConnection(Integer bytesConnection) {
		this.bytesConnection = bytesConnection;
	}

	public Integer getFetchesSec() {
		return fetchesSec;
	}

	public void setFetchesSec(Integer fetchesSec) {
		this.fetchesSec = fetchesSec;
	}

	public Integer getBytesSec() {
		return bytesSec;
	}

	public void setBytesSec(Integer bytesSec) {
		this.bytesSec = bytesSec;
	}

	public Integer getConnectAvg() {
		return connectAvg;
	}

	public void setConnectAvg(Integer connectAvg) {
		this.connectAvg = connectAvg;
	}

	public Integer getConnectMax() {
		return connectMax;
	}

	public void setConnectMax(Integer connectMax) {
		this.connectMax = connectMax;
	}

	public Integer getConnectMin() {
		return connectMin;
	}

	public void setConnectMin(Integer connectMin) {
		this.connectMin = connectMin;
	}

	public Integer getResponseAvg() {
		return responseAvg;
	}

	public void setResponseAvg(Integer responseAvg) {
		this.responseAvg = responseAvg;
	}

	public Integer getResponseMax() {
		return responseMax;
	}

	public void setResponseMax(Integer responseMax) {
		this.responseMax = responseMax;
	}

	public Integer getResponseMin() {
		return responseMin;
	}

	public void setResponseMin(Integer responseMin) {
		this.responseMin = responseMin;
	}

	public Integer getBadCount() {
		return badCount;
	}

	public void setBadCount(Integer badCount) {
		this.badCount = badCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getHttpState_stateCount() {
		return httpState_stateCount;
	}

	public void setHttpState_stateCount(String httpState_stateCount) {
		this.httpState_stateCount = httpState_stateCount;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMacName() {
		return macName;
	}
	public void setMacName(String macName) {
		this.macName = macName;
	}
	
	
      
}
