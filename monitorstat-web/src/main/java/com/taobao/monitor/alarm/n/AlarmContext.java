
package com.taobao.monitor.alarm.n;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.alarm.n.key.KeyJudgeEnum;

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
	
	private int siteId;
	
	private String siteName;
	
	private int keyType;
	
	private Map<Date,String> valueMap = null;
	
	private int continuousAlarmTimes;
	
	private Date recentlyDate;
	
	private String recentlyValue;
	
	private String baseLineValue;
	
	private String rangeMessage;
	
	
	private String scriptMessage;
	
	private String relationMessage;
	


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


	public int getSiteId() {
		return siteId;
	}


	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public int getKeyType() {
		return keyType;
	}


	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}


	public Map<Date, String> getValueMap() {
		return valueMap;
	}


	public void setValueMap(Map<Date, String> valueMap) {
		this.valueMap = valueMap;
	}


	public int getContinuousAlarmTimes() {
		return continuousAlarmTimes;
	}


	public void setContinuousAlarmTimes(int continuousAlarmTimes) {
		this.continuousAlarmTimes = continuousAlarmTimes;
	}



	public String getBaseLineValue() {
		return baseLineValue;
	}


	public void setBaseLineValue(String baseLineValue) {
		this.baseLineValue = baseLineValue;
	}


	public Date getRecentlyDate() {
		return recentlyDate;
	}


	public void setRecentlyDate(Date recentlyDate) {
		this.recentlyDate = recentlyDate;
	}


	public String getRecentlyValue() {
		return recentlyValue;
	}


	public void setRecentlyValue(String recentlyValue) {
		this.recentlyValue = recentlyValue;
	}


	public String getRangeMessage() {
		return rangeMessage;
	}


	public void setRangeMessage(String rangeMessage) {
		this.rangeMessage = rangeMessage;
	}


	public String getScriptMessage() {
		return scriptMessage;
	}


	public void setScriptMessage(String scriptMessage) {
		this.scriptMessage = scriptMessage;
	}


	public String getRelationMessage() {
		return relationMessage;
	}


	public void setRelationMessage(String relationMessage) {
		this.relationMessage = relationMessage;
	}


	
	
		
	
	

}
