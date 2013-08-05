
package com.taobao.monitor.other.tbsession;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2011-5-12 ÏÂÎç01:24:20
 */
public class TbSeesionLog {
	public String keyName;
	
	public long valueSum = 0;
	
	public long valueCount = 0;
	
	public int maxValueLen;
	
	public int minValueLen = Integer.MAX_VALUE;
	
	public double perSum = 0d;
	
	public double maxPer=0d;
	
	public int logType = 0;
	
	
	public Date collectTime;

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public long getValueSum() {
		return valueSum;
	}

	public void setValueSum(long valueSum) {
		this.valueSum = valueSum;
	}

	public long getValueCount() {
		return valueCount;
	}

	public void setValueCount(long valueCount) {
		this.valueCount = valueCount;
	}

	public int getMaxValueLen() {
		return maxValueLen;
	}

	public void setMaxValueLen(int maxValueLen) {
		this.maxValueLen = maxValueLen;
	}

	public int getMinValueLen() {
		return minValueLen;
	}

	public void setMinValueLen(int minValueLen) {
		this.minValueLen = minValueLen;
	}

	public double getPerSum() {
		return perSum;
	}

	public void setPerSum(double perSum) {
		this.perSum = perSum;
	}

	public double getMaxPer() {
		return maxPer;
	}

	public void setMaxPer(double maxPer) {
		this.maxPer = maxPer;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public int getLogType() {
		return logType;
	}

	public void setLogType(int logType) {
		this.logType = logType;
	}
	
	
	
	
	
}
