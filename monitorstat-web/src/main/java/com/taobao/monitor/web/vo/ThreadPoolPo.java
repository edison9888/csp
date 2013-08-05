
package com.taobao.monitor.web.vo;
/**
 * 
 * @author xiaodu
 * @version 2010-4-15 ионГ10:13:10
 */
public class ThreadPoolPo {
	
	private String maxThreads = "0";
	private String currentThreadCount = "0";
	public String getMaxThreads() {
		return maxThreads;
	}
	public void setMaxThreads(String maxThreads) {
		this.maxThreads = maxThreads;
	}
	public String getCurrentThreadCount() {
		return currentThreadCount;
	}
	public void setCurrentThreadCount(String currentThreadCount) {
		this.currentThreadCount = currentThreadCount;
	}
	

}
