
package com.taobao.monitor.web.distinct;
/**
 * 
 * @author xiaodu
 * @version 2010-9-15 ÏÂÎç05:20:37
 */
public class DistinctInterface {
	
	private String keyName;
	
	private double count;
	
	private double responseTime;
	
	private double sameCount;
	
	private double sameResponseTime;

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}

	public double getSameCount() {
		return sameCount;
	}

	public void setSameCount(double sameCount) {
		this.sameCount = sameCount;
	}

	public double getSameResponseTime() {
		return sameResponseTime;
	}

	public void setSameResponseTime(double sameResponseTime) {
		this.sameResponseTime = sameResponseTime;
	}
	
	

}
