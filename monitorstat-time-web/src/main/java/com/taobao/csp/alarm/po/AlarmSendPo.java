
package com.taobao.csp.alarm.po;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2011-4-26 ÉÏÎç11:25:48
 */
public class AlarmSendPo {
	
	private String targetAim;
	
	private int appId;
	
	private String alarmMsg;
	
	private Date acceptTime;
	
	private String alarmType;

	public String getTargetAim() {
		return targetAim;
	}

	public void setTargetAim(String targetAim) {
		this.targetAim = targetAim;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAlarmMsg() {
		return alarmMsg;
	}

	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

}
