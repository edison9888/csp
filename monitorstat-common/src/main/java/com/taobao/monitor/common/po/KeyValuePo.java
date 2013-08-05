
package com.taobao.monitor.common.po;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.Utlitites;

/**
 * 
 * @author xiaodu
 * @version 2010-4-16 ÉÏÎç11:37:33
 */
public class KeyValuePo implements Comparable<KeyValuePo>{
	
	private String appName;
	private Integer appId;	
	private String keyName;
	private Integer keyId;		
	private String valueStr;	
	private Date collectTime;
	private String collectTimeStr;
	private Integer siteId;
	
	private double backupSum;
	//
	private Map<Integer,Double> siteValueMap = new HashMap<Integer, Double>();
	
	
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
				siteValueMap.put(siteId, Arith.div(Arith.add(v, value), 2,2));
			}else{
				siteValueMap.put(siteId, Arith.div(Arith.add(v, value), 2,2));
			}			
		}
	}
	


	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getCollectTimeStr() {
		return collectTimeStr;
	}

	public void setCollectTimeStr(String collectTimeStr) {
		this.collectTimeStr = collectTimeStr;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Integer getKeyId() {
		return keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}

	public String getValueStr() {
		if(siteValueMap.size()==0){
			if(valueStr==null){
				return "0";
			}
			return valueStr;
		}else{
			double v = 0;
			for(Map.Entry<Integer, Double> entry:siteValueMap.entrySet()){
				v=Arith.add(v, entry.getValue());
			}
			return Arith.div(v, siteValueMap.size(),2)+"";
		}
	}
	
	
	public double getMaxSiteValue(){
		
		if(siteValueMap.size()==0){
			return 0;
		}else{
			double maxValue=0;
			for(Map.Entry<Integer, Double> entry:siteValueMap.entrySet()){
				if(maxValue<entry.getValue()){
					maxValue = entry.getValue();
				}
			}
			return maxValue;
		}
	}
	

	public void setValueStr(String valueStr) {
		siteValueMap.clear();
		this.valueStr = valueStr;
	}

	
	public int compareTo(KeyValuePo o) {
		
		if(collectTime.before(o.getCollectTime())){
			return 1;
		}else if(collectTime.equals(o.getCollectTime())){
			return 0;
		}else if(collectTime.after(o.getCollectTime())){
			return -1;
		}
		
		
		return 0;
	}

	public Map<Integer, Double> getSiteValueMap() {
		return siteValueMap;
	}

	public void setSiteValueMap(Map<Integer, Double> siteValueMap) {
		this.siteValueMap = siteValueMap;
	}



	public double getBackupSum() {
		return backupSum;
	}



	public void setBackupSum(double backupSum) {
		this.backupSum = backupSum;
	}
	
	
	
	

}
