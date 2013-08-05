package com.taobao.csp.depend.po.tair;

import java.util.HashMap;
import java.util.Map;

/**
 * ��namespaceչʾTairʱ��actiontypeά�ȵĴ洢��Ԫ
 * @author zhongting.zy
 */
public class CTairActionTotalUnit {
	
	public CTairActionTotalUnit(String actionShortName) {
		this.actionShortName = actionShortName;
	}
	
	//action type ������(��get��put��mget�ȣ�ȥ��exception�ʹ�/�Ĳ���)
	private String actionShortName;
	private long preCallNumber;
	
	//������
	private double avgHit;
	private double preAvgHit;
	
	//���󳤶�
	private double avgLength;
	private double preAvgLength;
	
	//��ֵ
	private double avgRate;
	private double preAvgrate;
	
	//��˳��ֱ��¼ �������á������ʡ�����
	public long[] callNumberArray = new long[3];
	public long[] timeArray = new long[3];
	
	public long[] preCallNumberArray = new long[3];
	public long[] preTimeArray = new long[3];	
	
	//map<machineroomName, CTairMachineRoomUnit>��������map
	final public Map<String, CTairMachineRoomUnit> machineMap = new HashMap<String, CTairMachineRoomUnit>();

	public String getActionShortName() {
		return actionShortName;
	}

	public void setActionShortName(String actionShortName) {
		this.actionShortName = actionShortName;
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
