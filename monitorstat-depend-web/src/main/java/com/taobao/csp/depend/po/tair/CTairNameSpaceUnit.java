package com.taobao.csp.depend.po.tair;

public class CTairNameSpaceUnit implements Comparable<CTairNameSpaceUnit>{
	
	public CTairNameSpaceUnit(String namespace) {
		this.namespace = namespace;
	}		
	
	private String namespace;
	
	
	//��˳��ֱ��¼ �������á������ʡ�����
	public long[] callNumberArray = new long[3];
	public long[] timeArray = new long[3];
	
	//��ʷ��¼ �������á������ʡ�����
	public long[] preCallNumberArray = new long[3];
	public long[] preTimeArray = new long[3];
	
	//������
	private double avgHit;
	private double preAvgHit;
	
	//���󳤶�
	private double avgLength;
	private double preAvgLength;
	
	//��ֵ
	private double avgRate;
	private double preAvgrate;
	
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
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
	 * namespace�������ƣ���С��������
	 */
	@Override
	public int compareTo(CTairNameSpaceUnit o) {
		return getNamespace().compareTo(o.getNamespace());
	}
}
