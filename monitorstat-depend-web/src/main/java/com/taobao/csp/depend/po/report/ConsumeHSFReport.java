package com.taobao.csp.depend.po.report;

public class ConsumeHSFReport implements Comparable<ConsumeHSFReport> {

	private String appName;
	private String opsName;
	private long bizExceptionNum;
	private long exceptionNum;
	private long callNumber;
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}



	public String getOpsName() {
		return opsName;
	}



	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}



	public long getBizExceptionNum() {
		return bizExceptionNum;
	}



	public void setBizExceptionNum(long bizExceptionNum) {
		this.bizExceptionNum = bizExceptionNum;
	}



	public long getExceptionNum() {
		return exceptionNum;
	}



	public void setExceptionNum(long exceptionNum) {
		this.exceptionNum = exceptionNum;
	}



	public long getCallNumber() {
		return callNumber;
	}



	public void setCallNumber(long callNumber) {
		this.callNumber = callNumber;
	}

	@Override
	public int compareTo(ConsumeHSFReport o) {
		if(o.getCallNumber() >getCallNumber()){
			return 1;
		}else if(o.getCallNumber() ==getCallNumber()){
			return 0;
		}else{
			return -1;
		}
	}
}
