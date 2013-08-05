package com.taobao.csp.depend.po.alarm;

import java.util.Date;

public class AlarmConfigPo {
	private Integer id = -1;
	private String appName;
	private String wangwangString;
	private String emailString;
	private Integer daysPre = 1;	//默认是对比昨天
	
	private Integer alarmMode;
	private Long lastSendTime;	//标记发送时间
	
	private Date collectDate;
	private Date GMTCreate;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getWangwangString() {
		return wangwangString;
	}
	public void setWangwangString(String wangwangString) {
		this.wangwangString = wangwangString;
	}
	public String getEmailString() {
		return emailString;
	}
	public void setEmailString(String emailString) {
		this.emailString = emailString;
	}
	public Long getLastSendTime() {
		return lastSendTime;
	}
	public void setLastSendTime(Long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
	public Date getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}
	public Date getGMTCreate() {
		return GMTCreate;
	}
	public void setGMTCreate(Date gMTCreate) {
		GMTCreate = gMTCreate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDaysPre() {
		return daysPre;
	}
	public void setDaysPre(Integer daysPre) {
		this.daysPre = daysPre;
	}
	public Integer getAlarmMode() {
		return alarmMode;
	}
	public void setAlarmMode(Integer alarmMode) {
		this.alarmMode = alarmMode;
	}
}
