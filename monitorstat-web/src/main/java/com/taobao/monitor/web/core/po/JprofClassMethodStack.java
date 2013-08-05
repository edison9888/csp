
package com.taobao.monitor.web.core.po;
/**
 * 
 * @author xiaodu
 * @version 2010-8-12 ÏÂÎç03:47:49
 */
public class JprofClassMethodStack {
	
	private String appName;
	
	private String className;
	
	private String methodName;
	
	private int lineNum;
	
	private String rootParentClass;
	
	private String parentClass;
	
	private int stackNum;
	
	private String collectDay;
	
	private String md5;
	
	
	
	private long excuteNum;
	
	private double useTime;

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

	public String getRootParentClass() {
		return rootParentClass;
	}

	public void setRootParentClass(String rootParentClass) {
		this.rootParentClass = rootParentClass;
	}

	public String getParentClass() {
		return parentClass;
	}

	public void setParentClass(String parentClass) {
		this.parentClass = parentClass;
	}

	public String getCollectDay() {
		return collectDay;
	}

	public void setCollectDay(String collectDay) {
		this.collectDay = collectDay;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getStackNum() {
		return stackNum;
	}

	public void setStackNum(int stackNum) {
		this.stackNum = stackNum;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
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
