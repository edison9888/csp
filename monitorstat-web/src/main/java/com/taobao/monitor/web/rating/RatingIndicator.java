
package com.taobao.monitor.web.rating;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2010-8-18 ÏÂÎç02:15:11
 */
public class RatingIndicator {
		
	private int id;
	
	private int appId;
	
	private int keyId;
	
	private String indicatorKeyName;
	
	private String keyUnit;
	
	//private IndicatorEnum indicatorKey;
	
	private int indicatorWeight;
	
	private String IndicatorThresholdValue;
	
	private Date gmtModified;
	
	private Date gmtCreate;
	
	private int status;
	
	private int rushHour_start;
	
	private int rushHour_end;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	

//	public IndicatorEnum getIndicatorKey() {
//		return indicatorKey;
//	}
//
//	public void setIndicatorKey(IndicatorEnum indicatorKey) {
//		this.indicatorKey = indicatorKey;
//	}

	

	public int getIndicatorWeight() {
		return indicatorWeight;
	}

	public void setIndicatorWeight(int indicatorWeight) {
		this.indicatorWeight = indicatorWeight;
	}

	public String getIndicatorThresholdValue() {
		return IndicatorThresholdValue;
	}

	public void setIndicatorThresholdValue(String indicatorThresholdValue) {
		IndicatorThresholdValue = indicatorThresholdValue;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRushHour_start() {
		return rushHour_start;
	}

	public void setRushHour_start(int rushHour_start) {
		this.rushHour_start = rushHour_start;
	}

	public int getRushHour_end() {
		return rushHour_end;
	}

	public void setRushHour_end(int rushHour_end) {
		this.rushHour_end = rushHour_end;
	}

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public String getIndicatorKeyName() {
		return indicatorKeyName;
	}

	public void setIndicatorKeyName(String indicatorKeyName) {
		this.indicatorKeyName = indicatorKeyName;
	}

	public String getKeyUnit() {
		return keyUnit;
	}

	public void setKeyUnit(String keyUnit) {
		this.keyUnit = keyUnit;
	}
	
	
}
