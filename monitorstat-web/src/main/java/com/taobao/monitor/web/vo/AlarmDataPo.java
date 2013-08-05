
package com.taobao.monitor.web.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.util.Constants;

/**
 * 
 * @author xiaodu
 * @version 2010-5-11 下午05:22:20
 */
public class AlarmDataPo {
	
	private String id;
	
	private String appName;
	
	private Integer appId;
	
	private String keyName;
	
	private String keyId;
	
	private Integer siteId;
	
	private String alarmDefine;
	
	private String site;
	
	private String alarmAim;
	
	private String alarmFeature;	
	
	private String alarmType;//<option value="1">阀值</option>	<option value="2">百分比波动</option>	
	
	private String dataDesc;
	
	private Date maxTime;	
	
	private double maxValue;
	
	private int keyType;//key的重要程度  1 普通 2 重要 3 紧急
	
	private Map<Date,String> limitDataMap = new HashMap<Date, String>();
	
	
	public void putDataValue(Date date,String value){
		String v = this.getLimitDataMap().get(date);
		if (v == null) {
			this.getLimitDataMap().put(date, value);
		} else {
			if (this.getKeyName().indexOf(Constants.COUNT_TIMES_FLAG) > -1) {//求和
				
				this.getLimitDataMap().put(date, (Double.parseDouble(value) + Double.parseDouble(v))+"");
			} else if (this.getKeyName().indexOf(Constants.AVERAGE_USERTIMES_FLAG) > -1) {//计算平均值
				this.getLimitDataMap().put(date, (Double.parseDouble(value) + Double.parseDouble(v)) / 2 +"");
			}else{
				this.getLimitDataMap().put(date, value);
			}
		}
	}
	
	
	public String getDataDesc() {
		return dataDesc;
	}

	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Date getAlarmMaxTime(){
		List<Date> list = new ArrayList<Date>();
		list.addAll(limitDataMap.keySet());
		Collections.sort(list,new Comparator<Date>(){
			
			public int compare(Date o1, Date o2) {
				long thisTime = o1.getTime();
				long anotherTime = o2.getTime();
				return (thisTime<anotherTime ? 1 : (thisTime==anotherTime ? 0 : -1));
			}});
		return list.get(0);
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

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	

	public String getAlarmDefine() {
		return alarmDefine;
	}

	public void setAlarmDefine(String alarmDefine) {
		this.alarmDefine = alarmDefine;
	}

	public Map<Date, String> getLimitDataMap() {
		return limitDataMap;
	}

	public void setLimitDataMap(Map<Date, String> limitDataMap) {
		this.limitDataMap = limitDataMap;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getAlarmAim() {
		return alarmAim;
	}

	public void setAlarmAim(String alarmAim) {
		this.alarmAim = alarmAim;
	}

	public String getAlarmFeature() {
		return alarmFeature;
	}

	public void setAlarmFeature(String alarmFeature) {
		this.alarmFeature = alarmFeature;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public int getKeyType() {
		return keyType;
	}

	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}
	
	
	

}
