package com.taobao.csp.depend.po.tair;

import java.util.HashMap;
import java.util.Map;

/**
 * 按actiontype展示Tair时，machine维度的存储单元
 * @author zhongting.zy
 */
public class CTairMachineRoomUnit {
	
	public CTairMachineRoomUnit(String machineRoomName) {
		this.machineRoomName = machineRoomName;
	}	
	
	//机房名称
	private String machineRoomName;
	private long preCallNumber;
	
	//按顺序分别记录 正常调用、命中率、长度
	public long[] callNumberArray = new long[3];
	public long[] timeArray = new long[3];
	
	public long[] preCallNumberArray = new long[3];
	public long[] preTimeArray = new long[3];	
	
	//命中率
	private double avgHit;
	private double preAvgHit;
	
	//请求长度
	private double avgLength;
	private double preAvgLength;
	
	//均值
	private double avgRate;
	private double preAvgrate;
	
	//map<namespace, CTairNameSpaceUnit>namespace数据列表，按照namespace排序
	final public Map<String, CTairNameSpaceUnit> namespaceMap = new HashMap<String, CTairNameSpaceUnit>();

	public String getMachineRoomName() {
		return machineRoomName;
	}

	public void setMachineRoomName(String machineRoomName) {
		this.machineRoomName = machineRoomName;
	}

	public long getPreCallNumber() {
		return preCallNumber;
	}

	public void setPreCallNumber(long preCallNumber) {
		this.preCallNumber = preCallNumber;
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
}
