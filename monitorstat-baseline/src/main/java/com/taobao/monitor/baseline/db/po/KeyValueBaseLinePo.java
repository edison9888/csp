
package com.taobao.monitor.baseline.db.po;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.Utlitites;

/**
 * 
 * @author xiaodu
 * @version 2010-5-26 ÉÏÎç09:29:17
 */
public class KeyValueBaseLinePo {
	
	private int appId;
	private int keyId;
	private int siteId;
	private String appName;
	private String keyName;
	private String siteName;
	private double value;	
	private double baseLineValue;
	private double sameDayValue;
	private double yesterdayValue;
	private Date collectTime;
	
	private Map<Integer,Double> siteValueMap = new HashMap<Integer, Double>();
	
	public Map<Integer, Double> getSiteValueMap() {
		return siteValueMap;
	}
	public void setSiteValueMap(Map<Integer, Double> siteValueMap) {
		this.siteValueMap = siteValueMap;
	}
	
	
	
	
	
	public void putValue(Integer siteId,Double value){		
		if(value==0){
			return ;
		}		
		Double v = siteValueMap.get(siteId);		
		if(v==null){
			siteValueMap.put(siteId, value);
		}else{
			if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
				siteValueMap.put(siteId, v+value);
			}else if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){				
				siteValueMap.put(siteId, (v+value)/2);
			}
			
		}
	}
	
	private int sumTimes;
	
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
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
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
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	public double getBaseLineValue() {
		return baseLineValue;
	}
	public void setBaseLineValue(double baseLineValue) {
		this.baseLineValue = baseLineValue;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public int getSumTimes() {
		return sumTimes;
	}
	public void setSumTimes(int sumTimes) {
		this.sumTimes = sumTimes;
	}
	public double getValue() {
		if(siteValueMap.size()==0){			
			return value;
		}else{
			double v = 0;
			for(Map.Entry<Integer, Double> entry:siteValueMap.entrySet()){
				v+=entry.getValue();
			}
			return v/siteValueMap.size();
		}
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getSameDayValue() {
		return sameDayValue;
	}
	public void setSameDayValue(double sameDayValue) {
		this.sameDayValue = sameDayValue;
	}
	public double getYesterdayValue() {
		return yesterdayValue;
	}
	public void setYesterdayValue(double yesterdayValue) {
		this.yesterdayValue = yesterdayValue;
	}
	
	

}
