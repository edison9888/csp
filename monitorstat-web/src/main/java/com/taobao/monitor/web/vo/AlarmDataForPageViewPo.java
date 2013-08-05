package com.taobao.monitor.web.vo;

import java.util.Date;

/**
 * ������ҳ����ʾ�ı���ָ��
 * @author baiyan
 * @author hongbing.ww
 * @since 2012-07-04
 */
public class AlarmDataForPageViewPo {
	/**
	 * �澯ָ��Ӧ������
	 */
	private String appName;
	/**
	 * �澯��ָ������
	 */
	private String keyName;
	/**
	 * �澯��
	 */
	private String propertyName;
	/**
	 * �ж�ģʽ
	 */
	private String modeName;
	/**
	 * ����������ip
	 */
	private String ipString;
	/**
	 * �澯��Χ
	 */
	private String keyScope;
	/**
	 * �澯ʱ��
	 */
	private Date alarmTime;
	/**
	 * �澯ֵ
	 */
	private String alarmValue;
	/**
	 * �澯ԭ��
	 */
	private String alarmCause;
	/**
	 * �澯����
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
