package com.taobao.csp.day.tddl;

/***
 * 最大响应时间与最小响应时间的value
 * 
 * @author youji.zj
 * @version 1.0 2012-09-06
 *
 */
public class TddlResponseValue {
	
	private int response;
	
	private String responseTime;
	
	public TddlResponseValue(int response, String responseTime) {
		this.response = response;
		this.responseTime = responseTime;
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
}
