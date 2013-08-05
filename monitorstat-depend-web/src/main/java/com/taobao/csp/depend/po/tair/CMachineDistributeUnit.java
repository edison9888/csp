package com.taobao.csp.depend.po.tair;

import com.taobao.csp.depend.util.ConstantParameters;

/**
 * 纪录机器分布的PO
 * @author zhongting.zy
 *
 */
public class CMachineDistributeUnit implements Comparable<CMachineDistributeUnit>{
	public CMachineDistributeUnit(String ip) {
		this.ip = ip;
	}
	
	private String ip;	//主键
	private String siteName;	//机房
	//命中率
	private double avgHit;
	private double preAvgHit;
	
	//请求长度
	private double avgLength;
	private double preAvgLength;
	
	//均值
	private double avgRate;
	private double preAvgrate;
	
	//按顺序分别记录 正常调用、命中率、长度
	public long[] callNumberArray = new long[3];
	public long[] timeArray = new long[3];
	
	//历史调用
	public long[] preCallNumberArray = new long[3];
	public long[] preTimeArray = new long[3];
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public double getAvgHit() {
		return avgHit;
	}
	public void setAvgHit(double avgHit) {
		this.avgHit = avgHit;
	}
	public double getPreAvgHit() {
		return preAvgHit;
	}
	public void setPreAvgHit(double preAvgHit) {
		this.preAvgHit = preAvgHit;
	}
	public double getAvgLength() {
		return avgLength;
	}
	public void setAvgLength(double avgLength) {
		this.avgLength = avgLength;
	}
	public double getPreAvgLength() {
		return preAvgLength;
	}
	public void setPreAvgLength(double preAvgLength) {
		this.preAvgLength = preAvgLength;
	}
	public double getAvgRate() {
		return avgRate;
	}
	public void setAvgRate(double avgRate) {
		this.avgRate = avgRate;
	}
	public double getPreAvgrate() {
		return preAvgrate;
	}
	public void setPreAvgrate(double preAvgrate) {
		this.preAvgrate = preAvgrate;
	}
	public long[] getCallNumberArray() {
		return callNumberArray;
	}
	public void setCallNumberArray(long[] callNumberArray) {
		this.callNumberArray = callNumberArray;
	}
	public long[] getTimeArray() {
		return timeArray;
	}
	public void setTimeArray(long[] timeArray) {
		this.timeArray = timeArray;
	}
	
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public long[] getPreCallNumberArray() {
		return preCallNumberArray;
	}
	public void setPreCallNumberArray(long[] preCallNumberArray) {
		this.preCallNumberArray = preCallNumberArray;
	}
	public long[] getPreTimeArray() {
		return preTimeArray;
	}
	public void setPreTimeArray(long[] preTimeArray) {
		this.preTimeArray = preTimeArray;
	}
	/**
	 * 按照调用总量排序
	 */
	@Override
	public int compareTo(CMachineDistributeUnit o) {
		if(getCallNumberArray()[ConstantParameters.INT_TYPE_NOR] > o.getCallNumberArray()[ConstantParameters.INT_TYPE_NOR]) {
			return 1;
		} else if(getCallNumberArray()[ConstantParameters.INT_TYPE_NOR] > o.getCallNumberArray()[ConstantParameters.INT_TYPE_NOR]) {
			return -1;
		}
		return 0;
	}
	
}
