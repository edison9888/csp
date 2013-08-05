
package com.taobao.monitor.web.vo;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2010-11-10 上午11:15:31
 */
public class UserAcceptInfo {
	
	private int appId;
	private int keyId;
	private int userId;
	private Date acceptDate;
	private String alarmType;
	private String alarmMsg;
	
	private String targetAim;//  目标信息 如果是手机 者是手机号   旺旺 则是旺旺号
	
	private String isSend;//是否发送
	
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getKeyId() {
		return keyId;
	}
	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getAcceptDate() {
		return acceptDate;
	}
	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmMsg() {
		return alarmMsg;
	}
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}
	public String getTargetAim() {
		return targetAim;
	}
	public void setTargetAim(String targetAim) {
		this.targetAim = targetAim;
	}
	public String getIsSend() {
		return isSend;
	}
	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
	
	
	

}
