package com.taobao.csp.hadoop.job.gclog.gctrace;

import java.io.Serializable;

import com.taobao.csp.hadoop.job.gclog.utils.NumberSeq;

public class GCUnitHost implements Serializable {
	private static final long serialVersionUID = 1L;
	private String appName;
	private String gcActivityName;
	private NumberSeq numSeq;
	private String hostIp;
	
	 //按分钟为单位
	public final int arrayLength = 60*24;
	private double[] gcTimeArray = new double[arrayLength];
	private long[] gcArray = new long[arrayLength];
	private long[] memArray = new long[arrayLength];
	
	public NumberSeq getNumSeq() {
		return numSeq;
	}
	public void setNumSeq(NumberSeq numSeq) {
		this.numSeq = numSeq;
	}
	public double[] getGcTimeArray() {
		return gcTimeArray;
	}
	public void setGcTimeArray(double[] gcTimeArray) {
		this.gcTimeArray = gcTimeArray;
	}
	public long[] getGcArray() {
		return gcArray;
	}
	public void setGcArray(long[] gcArray) {
		this.gcArray = gcArray;
	}
	public long[] getMemArray() {
		return memArray;
	}
	public void setMemArray(long[] memArray) {
		this.memArray = memArray;
	}
	public int getArrayLength() {
		return arrayLength;
	}
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
}
