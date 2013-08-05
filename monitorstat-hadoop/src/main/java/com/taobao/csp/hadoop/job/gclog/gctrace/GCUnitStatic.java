package com.taobao.csp.hadoop.job.gclog.gctrace;

import java.io.Serializable;

import com.taobao.csp.hadoop.job.gclog.utils.NumberSeq;

public class GCUnitStatic implements Serializable {
	private static final long serialVersionUID = 1L;
	private String appName;
	private String gcActivityName;
	private NumberSeq numSeq;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getGcActivityName() {
		return gcActivityName;
	}
	public void setGcActivityName(String gcActivityName) {
		this.gcActivityName = gcActivityName;
	}
	public NumberSeq getNumSeq() {
		return numSeq;
	}
	public void setNumSeq(NumberSeq numSeq) {
		this.numSeq = numSeq;
	}
}
