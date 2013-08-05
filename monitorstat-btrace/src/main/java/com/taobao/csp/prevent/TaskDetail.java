package com.taobao.csp.prevent;

public class TaskDetail {
	
	public static String INJECT_EXCEPTION = "exception";
	public static String INJECT_DELAY = "delay";
	
	private String taskId;
	
	private String remoteAddr;
	
	private String remotePort;
	
	private String injectType;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}

	public String getInjectType() {
		return injectType;
	}

	public void setInjectType(String injectType) {
		this.injectType = injectType;
	}
	
	

}
