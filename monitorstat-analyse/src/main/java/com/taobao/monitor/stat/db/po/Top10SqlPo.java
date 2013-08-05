package com.taobao.monitor.stat.db.po;

/**
 * @author xiaoxie
 * @create time：2010-4-10 下午03:22:14
 * @description
 */
public class Top10SqlPo {
	/** 执行次数 */
	private long execDelta; 
	/** 单次逻辑读 */
	private long bufferGets;
	/** 单次物理读 */
	private long diskReads;
	/** 单次耗时 */
	private long elapsedTime;
	/** sql 文本*/
	private String sqlFullText;
	/** SQL执行的可的应用名称*/
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
