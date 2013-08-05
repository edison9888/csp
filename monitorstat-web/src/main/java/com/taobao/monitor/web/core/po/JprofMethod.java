
package com.taobao.monitor.web.core.po;
/**
 * 
 * @author xiaodu
 * @version 2010-8-10 ÏÂÎç04:13:40
 */
public class JprofMethod {
	
	private int id;
	
	private int appId;
	
	private int classId;
	
	private String methodName;
	
	private int lineNum;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
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
	
	

}
