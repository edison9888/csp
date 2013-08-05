
package com.taobao.monitor.web.vo;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xiaodu
 * @version 2010-7-12 ÉÏÎç11:56:25
 */
public class AlarmRecordPo {
	
	private long id;
	
	private int appId;
	
	private int alarmkeyId;
	
	private String alarmKeyName;
	
	private Date collectTime;
	
	private String siteName;
	
	private int siteId;
	
	private String alarmValue;
	
	private Map<String,Integer> siteMap = new HashMap<String, Integer>();
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getAlarmkeyId() {
		return alarmkeyId;
	}

	public void setAlarmkeyId(int alarmkeyId) {
		this.alarmkeyId = alarmkeyId;
	}

	public String getAlarmKeyName() {
		return alarmKeyName;
	}

	public void setAlarmKeyName(String alarmKeyName) {
		this.alarmKeyName = alarmKeyName;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public String getAlarmValue() {
		return alarmValue;
	}

	public void setAlarmValue(String alarmValue) {
		this.alarmValue = alarmValue;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Map<String, Integer> getSiteMap() {
		return siteMap;
	}

	public void setSiteMap(Map<String, Integer> siteMap) {
		this.siteMap = siteMap;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	
	

}
