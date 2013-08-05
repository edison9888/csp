package com.taobao.monitor.web.vo.beidou;

import java.util.Date;

/**
 * ����ӱ����ı������ȡ������
 * @author baiyan
 *
 */
public class BeidouAlarmDataPo {
	private Date checkTime;
	private String alertGroup;
	private String alertSource;
	private String alertMsg;
	private String alertType;
	private String alertClass;
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getAlertGroup() {
		return alertGroup;
	}
	public void setAlertGroup(String alertGroup) {
		this.alertGroup = alertGroup;
	}
	public String getAlertSource() {
		return alertSource;
	}
	public void setAlertSource(String alertSource) {
		this.alertSource = alertSource;
	}
	public String getAlertMsg() {
		return alertMsg;
	}
	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getAlertClass() {
		return alertClass;
	}
	public void setAlertClass(String alertClass) {
		this.alertClass = alertClass;
	}
	
	public String getKeyString(){
		return "���ݿ���:" + alertGroup + "," +"���ݿ����ƣ�" + alertSource;
	}

	public String getValueString(){
		return  "ʱ�䣺" + checkTime + ":" + ",�������ݣ�" + alertMsg + ",�������ͣ�" + alertType; 
	}
}
