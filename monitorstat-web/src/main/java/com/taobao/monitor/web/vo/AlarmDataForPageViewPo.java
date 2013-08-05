package com.taobao.monitor.web.vo;

import java.util.Date;

/**
 * 用于在页面显示的报警指标
 * @author baiyan
 * @author hongbing.ww
 * @since 2012-07-04
 */
public class AlarmDataForPageViewPo {
	/**
	 * 告警指标应用名称
	 */
	private String appName;
	/**
	 * 告警的指标名称
	 */
	private String keyName;
	/**
	 * 告警点
	 */
	private String propertyName;
	/**
	 * 判断模式
	 */
	private String modeName;
	/**
	 * 报警服务器ip
	 */
	private String ipString;
	/**
	 * 告警范围
	 */
	private String keyScope;
	/**
	 * 告警时间
	 */
	private Date alarmTime;
	/**
	 * 告警值
	 */
	private String alarmValue;
	/**
	 * 告警原因
	 */
	private String alarmCause;
	/**
	 * 告警级别
	 */
	private int level;
	
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
	public String getModeName() {
		return modeName;
	}
	public void setModeName(String modeName) {
		this.modeName = modeName;
	}
	public String getIpString() {
		return ipString;
	}
	public void setIpString(String ipString) {
		this.ipString = ipString;
	}
	public String getKeyScope() {
		return keyScope;
	}
	public void setKeyScope(String keyScope) {
		this.keyScope = keyScope;
	}
	public Date getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getAlarmValue() {
		return alarmValue;
	}
	public void setAlarmValue(String alarmValue) {
		this.alarmValue = alarmValue;
	}
	public String getAlarmCause() {
		return alarmCause;
	}
	public void setAlarmCause(String alarmCause) {
		this.alarmCause = alarmCause;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
