
package com.taobao.jprof;
/**
 * 
 * @author xiaodu
 * @version 2010-6-23 ÏÂÎç04:25:23
 */
public class JprofMethodInfo {
	
	private String mClassName;
	private String mMethodName;
	private String mFileName;
	private int mLineNum;
	
	
	public String toString(){
		return mClassName+":"+mMethodName+":"+mLineNum;
	}
	
	public String getMClassName() {
		return mClassName;
	}
	public void setMClassName(String className) {
		mClassName = className;
	}
	public String getMMethodName() {
		return mMethodName;
	}
	public void setMMethodName(String methodName) {
		mMethodName = methodName;
	}
	public String getMFileName() {
		return mFileName;
	}
	public void setMFileName(String fileName) {
		mFileName = fileName;
	}
	public int getMLineNum() {
		return mLineNum;
	}
	public void setMLineNum(int lineNum) {
		mLineNum = lineNum;
	}
	
	
	

}
