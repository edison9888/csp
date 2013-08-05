package com.taobao.monitor.alarm.source.change;

public class ChangeFreePo {
	private String id;
	private String title;
	private String changeType;
	private String userName;
	private String phone;
	private String mobilePhone;
	private String startTime;
	private String endTime;
	private String systemName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	@Override
	public String toString() {
		return "ChangeFreePo [id=" + id + ", title=" + title + ", changeType="
				+ changeType + ", userName=" + userName + ", phone=" + phone
				+ ", mobilePhone=" + mobilePhone + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", systemName=" + systemName + "]";
	}
	

}
