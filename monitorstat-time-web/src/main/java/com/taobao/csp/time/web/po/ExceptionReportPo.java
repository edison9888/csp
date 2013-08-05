/**
 * 
 */
package com.taobao.csp.time.web.po;

/**
 *@author wb-lixing
 *2012-5-30 下午03:56:48 
 */
public class ExceptionReportPo {
	private int max;
	private int total;
	private int average;
	private String appName;
	//出现异常最多的时间点
	private String maxPointTime;
	
	private String keyName;
	
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getAverage() {
		return average;
	}
	public void setAverage(int average) {
		this.average = average;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getMaxPointTime() {
		return maxPointTime;
	}
	public void setMaxPointTime(String maxPointTime) {
		this.maxPointTime = maxPointTime;
	}
	
	
}
