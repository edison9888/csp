package com.taobao.monitor.web.vo.beidou;

import java.util.Date;

/**
 * 保存从北斗的报警表获取的数据
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
		return "数据库组:" + alertGroup + "," +"数据库名称：" + alertSource;
	}

	public String getValueString(){
		return  "时间：" + checkTime + ":" + ",报警内容：" + alertMsg + ",报警类型：" + alertType; 
	}
}
