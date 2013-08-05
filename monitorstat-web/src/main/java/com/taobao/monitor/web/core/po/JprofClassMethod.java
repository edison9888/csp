
package com.taobao.monitor.web.core.po;
/**
 * 
 * @author xiaodu
 * @version 2010-8-10 ÏÂÎç04:12:45
 */
public class JprofClassMethod {
	
	private int id;
	
	private String appName;
	
	private String className;
	
	private String methodName;
	
	private int lineNum;
	
	private long excuteNum;
	
	private double useTime;
	
	private String collectDay;
	
	
	

	public String getCollectDay() {
		return collectDay;
	}

	public void setCollectDay(String collectDay) {
		this.collectDay = collectDay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public long getExcuteNum() {
		return excuteNum;
	}

	public void setExcuteNum(long excuteNum) {
		this.excuteNum = excuteNum;
	}

	public double getUseTime() {
		return useTime;
	}

	public void setUseTime(double useTime) {
		this.useTime = useTime;
	}

	

}
