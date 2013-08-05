
package com.taobao.csp.alarm.po;

import com.taobao.csp.dataserver.memcache.entry.DataEntry;


/**
 * key信息的上下文内容，在传递过程中用于接收赋予的信息
 * @author xiaodu
 * @version 2011-2-25 上午10:13:45
 */
public class AlarmContext {
	
	/**
	 * 当前key的数据判断后的状态
	 */
	private KeyJudgeEnum keyJudge;
	
	private int appId;
	
	private String appName;
	
	private int keyId;
	
	private String keyName;
	
	private String property;
	
	private String description;
	
	private String ip;
	
	private long time;
	
	private Object value;
	
	private String keyLevel;
	
	private int continuousAlarmTimes;
	
	private String rangeMessage;
	
	private String modeName;
	
	private String keyScope;
	
	private String keyAlias; // key的别名
	

	public String getModeName() {
		return modeName;
	}


	public void setModeName(String modeName) {
		this.modeName = modeName;
	}


	public String getKeyScope() {
		return keyScope;
	}


	public void setKeyScope(String keyScope) {
		this.keyScope = keyScope;
	}


	public String getRangeMessage() {
		return rangeMessage;
	}


	public void setRangeMessage(String rangeMessage) {
		this.rangeMessage = rangeMessage;
	}


	public int getContinuousAlarmTimes() {
		return continuousAlarmTimes;
	}


	public void setContinuousAlarmTimes(int continuousAlarmTimes) {
		this.continuousAlarmTimes = continuousAlarmTimes;
	}

	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getProperty() {
		return property;
	}


	public void setProperty(String property) {
		this.property = property;
	}


	public KeyJudgeEnum getKeyJudge() {
		return keyJudge;
	}


	public void setKeyJudge(KeyJudgeEnum keyJudge) {
		this.keyJudge = keyJudge;
	}


	public int getAppId() {
		return appId;
	}


	public void setAppId(int appId) {
		this.appId = appId;
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public int getKeyId() {
		return keyId;
	}


	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}


	public String getKeyName() {
		return keyName;
	}


	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}


	public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
	}


	public Object getValue() {
		return value;
	}


	public void setValue(Object value) {
		this.value = value;
	}


	public String getKeyLevel() {
		return keyLevel;
	}


	public void setKeyLevel(String keyLevel) {
		this.keyLevel = keyLevel;
	}


	public String getKeyAlias() {
		return keyAlias;
	}


	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}
	
}
