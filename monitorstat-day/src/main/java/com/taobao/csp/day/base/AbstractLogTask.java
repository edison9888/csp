package com.taobao.csp.day.base;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractLogTask implements Runnable {
	
	/*** 分析器 ***/
	private AbstractAnalyser analyser;
	
	/*** 应用名 ***/
	private final String appName;
	
	/*** 采集的机器信息 ***/
	private final HostInfo hostInfo;
	
	/*** 数据类型 ***/
	private final DataType dataType;
	
	/*** 当前采集的日志路径 ***/
	private volatile String logPath;
	
	/*** 一次采集的大小 ***/
	private volatile long fetchSize;
	
	/*** 当前取的位置 ***/
	private volatile long position = 0l;

	
	/*** 是否有效 ***/
	private volatile boolean valiable = true;
	
	/*** 任务的运行标志 ***/
	private volatile boolean run = false;
	
	/*** 回调 ***/
	private List<FetcherListener> listeners = new ArrayList<FetcherListener>();
	
	public AbstractAnalyser getAnalyser() {
		return analyser;
	}

	public void setAnalyser(AbstractAnalyser analyser) {
		this.analyser = analyser;
	}

	public String getAppName() {
		return appName;
	}
	
	public HostInfo getHostInfo() {
		return hostInfo;
	}

	public DataType getDataType() {
		return dataType;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public long getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(long fetchSize) {
		this.fetchSize = fetchSize;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public boolean isValiable() {
		return valiable;
	}

	public void setValiable(boolean valiable) {
		this.valiable = valiable;
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}
	
	public List<FetcherListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<FetcherListener> listeners) {
		this.listeners = listeners;
	}

	public AbstractLogTask(AbstractAnalyser analyser, String appName, HostInfo hostInfo, DataType dataType, String logPath, long fetchSize) {
		this.analyser = analyser;
		this.appName = appName;
		this.hostInfo = hostInfo;
		this.dataType = dataType;
		this.logPath = logPath;
		this.fetchSize = fetchSize;
		
	}

	/***
	 * 任务提交时的identity，根据identity来对应spout
	 * 
	 * @return
	 */
	public abstract int filterIdentity();

	/***
	 * 任务的key,具有唯一性
	 * 
	 * @return
	 */
	public abstract String identityKey();
	
	public abstract void run();

}
