
package com.taobao.monitor.alarm.n.key;
/**
 * �澯��ֵ�����ж���Χ	
 * @author xiaodu
 * @version 2011-2-27 ����08:30:36
 */
public class RangeDefine {
	private double greaterthan;
	private double  lessthan;
	private double equalValue;
	private int startTime;		
	private int endTime;
	public double getGreaterthan() {
		return greaterthan;
	}
	public void setGreaterthan(double greaterthan) {
		this.greaterthan = greaterthan;
	}
	public double getLessthan() {
		return lessthan;
	}
	public void setLessthan(double lessthan) {
		this.lessthan = lessthan;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public double getEqualValue() {
	
		return equalValue;
	}
	public void setEqualValue(double equal) {
	
		this.equalValue = equal;
	}
}
