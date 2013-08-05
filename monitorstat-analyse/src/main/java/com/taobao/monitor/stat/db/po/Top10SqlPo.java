package com.taobao.monitor.stat.db.po;

/**
 * @author xiaoxie
 * @create time��2010-4-10 ����03:22:14
 * @description
 */
public class Top10SqlPo {
	/** ִ�д��� */
	private long execDelta; 
	/** �����߼��� */
	private long bufferGets;
	/** ��������� */
	private long diskReads;
	/** ���κ�ʱ */
	private long elapsedTime;
	/** sql �ı�*/
	private String sqlFullText;
	/** SQLִ�еĿɵ�Ӧ������*/
	private String appName;
	
	public long getExecDelta() {
		return execDelta;
	}

	public void setExecDelta(long execDelta) {
		this.execDelta = execDelta;
	}

	public long getBufferGets() {
		return bufferGets;
	}

	public void setBufferGets(long bufferGets) {
		this.bufferGets = bufferGets;
	}

	public long getDiskReads() {
		return diskReads;
	}

	public void setDiskReads(long diskReads) {
		this.diskReads = diskReads;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getSqlFullText() {
		return sqlFullText;
	}

	public void setSqlFullText(String sqlFullText) {
		this.sqlFullText = sqlFullText;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
