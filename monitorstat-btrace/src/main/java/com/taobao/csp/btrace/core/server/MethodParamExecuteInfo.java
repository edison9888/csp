
package com.taobao.csp.btrace.core.server;
/**
 * 
 * @author xiaodu
 * @version 2011-9-7 обнГ01:37:51
 */
public class MethodParamExecuteInfo {
	
	private String[] paramValues;
	
	private long averageExecuteTime;
	

	public long getAverageExecuteTime() {
		return averageExecuteTime;
	}

	public void setAverageExecuteTime(long averageExecuteTime) {
		this.averageExecuteTime = averageExecuteTime;
	}

	public String[] getParamValues() {
		return paramValues;
	}

	public void setParamValues(String[] paramValues) {
		this.paramValues = paramValues;
	}
	
	
	
	
	

}
