package com.taobao.monitor.web.vo;

import java.util.ArrayList;
import java.util.List;



public class AlarmDescPo {
	private List<Long> alarmIdList = new ArrayList<Long>();
	
	
	private int appId;
	private String alarmReason;
	private String alarmDesc;
	
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getAlarmReason() {
		return alarmReason;
	}
	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}
	public String getAlarmDesc() {
		return alarmDesc;
	}
	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}
	public List<Long> getAlarmIdList() {
		return alarmIdList;
	}
	public void setAlarmIdList(List<Long> alarmIdList) {
		this.alarmIdList = alarmIdList;
	}
	

}
