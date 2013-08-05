package com.taobao.monitor.common.po;

import java.sql.Timestamp;

public class CspTimeKeyAlarmRecordPo {
	private String app_name;
	private String key_name;
	private String property_name;
	private String mode_name;
	private String key_scope;
	private Timestamp alarm_time;
	private String alarm_value;
	private String alarm_cause;
	private String ip;
	private String ftime;
	
	public String getFtime() {
		return ftime;
	}
	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getKey_name() {
		return key_name;
	}
	public void setKey_name(String key_name) {
		this.key_name = key_name;
	}
	public String getProperty_name() {
		return property_name;
	}
	public void setProperty_name(String property_name) {
		this.property_name = property_name;
	}
	public String getMode_name() {
		return mode_name;
	}
	public void setMode_name(String mode_name) {
		this.mode_name = mode_name;
	}
	public String getKey_scope() {
		return key_scope;
	}
	public void setKey_scope(String key_scope) {
		this.key_scope = key_scope;
	}
	public Timestamp getAlarm_time() {
		return alarm_time;
	}
	public void setAlarm_time(Timestamp alarm_time) {
		this.alarm_time = alarm_time;
	}
	public String getAlarm_value() {
		return alarm_value;
	}
	public void setAlarm_value(String alarm_value) {
		this.alarm_value = alarm_value;
	}
	public String getAlarm_cause() {
		return alarm_cause;
	}
	public void setAlarm_cause(String alarm_cause) {
		this.alarm_cause = alarm_cause;
	}
}
