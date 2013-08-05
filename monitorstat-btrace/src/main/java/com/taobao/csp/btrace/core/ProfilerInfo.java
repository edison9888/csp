
package com.taobao.csp.btrace.core;

import java.io.Serializable;

/**
 * 
 * @author xiaodu
 * @version 2011-8-22 ÉÏÎç11:06:56
 */
public class ProfilerInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7260044360242013679L;

	private long threadId;
	
	private int methodId;
	
	private long startTime;
	
	private long endTime;
	
	private int methodStackNum;
	
	private String returnValue;
	
	private String[] paramValues;
	
	private MethodInfo methodInfo;
	
	

	public MethodInfo getMethodInfo() {
		return methodInfo;
	}

	public void setMethodInfo(MethodInfo methodInfo) {
		this.methodInfo = methodInfo;
	}

	public int getMethodId() {
		return methodId;
	}

	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getMethodStackNum() {
		return methodStackNum;
	}

	public void setMethodStackNum(int methodStackNum) {
		this.methodStackNum = methodStackNum;
	}

	public String[] getParamValues() {
		return paramValues;
	}

	public void setParamValues(String[] paramValues) {
		this.paramValues = paramValues;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("threadId:").append(threadId).append(",");
		sb.append("methodId:").append(methodId).append(",");
		sb.append("methodStackNum:").append(methodStackNum).append(",");
		sb.append("startTime:").append(startTime).append(",");
		sb.append("endTime:").append(endTime).append(",");
		sb.append("returnValue:").append(returnValue).append(",");
		sb.append("paramValues:[");
		if(paramValues !=null){
			for(String f:paramValues){
				sb.append(f);
			}
		}
		sb.append("]]");
		return sb.toString();
	}
	
	

}
