package com.taobao.csp.time.web.po;

import java.sql.Timestamp;

public class BeiDouAlarmRecordPo {
	private String dbName;
	private String groupName;
	private String hostName;
	private String ip;
	private String alarmMsg;
	private String alarmType;
	private String alarmClass;
	private Timestamp time;
	
	
	
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmClass() {
		return alarmClass;
	}
	public void setAlarmClass(String alarmClass) {
		this.alarmClass = alarmClass;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getAlarmMsg() {
		return alarmMsg;
	}
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}
}
