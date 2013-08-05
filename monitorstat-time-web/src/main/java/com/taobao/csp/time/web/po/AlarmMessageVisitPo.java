package com.taobao.csp.time.web.po;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AlarmMessageVisitPo {
	String appName;
	String keyName;
	String propertyName;
	Date alarmTime;
	Integer visitCount;
	Set<String> visitors = new HashSet<String>();
	
	public Set<String> getVisitors() {
		return visitors;
	}
	public void setVisitors(Set<String> visitors) {
		this.visitors = visitors;
	}
	public void addVisitor(String visitor){
		visitors.add(visitor);
	}
	public void deleteVisitor(String visitor){
		visitors.remove(visitor);
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Date getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}
	public Integer getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}
}
