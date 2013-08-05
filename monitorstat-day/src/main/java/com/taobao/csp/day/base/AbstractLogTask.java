package com.taobao.csp.day.base;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractLogTask implements Runnable {
	
	/*** ������ ***/
	private AbstractAnalyser analyser;
	
	/*** Ӧ���� ***/
	private final String appName;
	
	/*** �ɼ��Ļ�����Ϣ ***/
	private final HostInfo hostInfo;
	
	/*** �������� ***/
	private final DataType dataType;
	
	/*** ��ǰ�ɼ�����־·�� ***/
	private volatile String logPath;
	
	/*** һ�βɼ��Ĵ�С ***/
	private volatile long fetchSize;
	
	/*** ��ǰȡ��λ�� ***/
	private volatile long position = 0l;

	
	/*** �Ƿ���Ч ***/
	private volatile boolean valiable = true;
	
	/*** ��������б�־ ***/
	private volatile boolean run = false;
	
	/*** �ص� ***/
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
	 * �����ύʱ��identity������identity����Ӧspout
	 * 
	 * @return
	 */
	public abstract int filterIdentity();

	/***
	 * �����key,����Ψһ��
	 * 
	 * @return
	 */
	public abstract String identityKey();
	
	public abstract void run();

}
