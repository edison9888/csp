
package com.taobao.monitor.web.rating;
/**
 * 
 * @author xiaodu
 * @version 2010-8-18 下午05:48:06
 */
public class RatingIndicatorValue {
	private int id;
	private int appId;
	private String indicatorKey;
	private double indicatorValue;
	private int collectDay;
	private int indicatorWeight;//当前的权重
	private String IndicatorThresholdValue; //当前的评分区间
	private int keyId;
	private String keyUnit;

	
	
	
	
	public double getIndicatorRating() {
		
		RatingCompute r = new RatingCompute();
		
		
		return r.compute(this);
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public double getIndicatorValue() {
		return indicatorValue;
	}

	public void setIndicatorValue(double indicatorValue) {
		this.indicatorValue = indicatorValue;
	}

	public int getCollectDay() {
		return collectDay;
	}

	public void setCollectDay(int collectDay) {
		this.collectDay = collectDay;
	}

	public int getIndicatorWeight() {
		return indicatorWeight;
	}

	public void setIndicatorWeight(int indicatorWeight) {
		this.indicatorWeight = indicatorWeight;
	}

	public String getIndicatorThresholdValue() {
		return IndicatorThresholdValue;
	}

	public void setIndicatorThresholdValue(String indicatorThresholdValue) {
		IndicatorThresholdValue = indicatorThresholdValue;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getIndicatorKey() {
		return indicatorKey;
	}

	public void setIndicatorKey(String indicatorKey) {
		this.indicatorKey = indicatorKey;
	}



	public int getKeyId() {
		return keyId;
	}



	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}



	public String getKeyUnit() {
		return keyUnit;
	}



	public void setKeyUnit(String keyUnit) {
		this.keyUnit = keyUnit;
	}
	
	
	
	

}
