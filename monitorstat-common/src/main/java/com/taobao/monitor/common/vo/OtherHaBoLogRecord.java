
package com.taobao.monitor.common.vo;

import java.util.Date;

import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2010-6-3 ÏÂÎç01:25:08
 */
public class OtherHaBoLogRecord implements Comparable<OtherHaBoLogRecord>{
	
	private String typeName;
	
	private String keyName;
	
	private Date collectTime;
	
	private KeyValuePo exeCount;	
	private KeyValuePo averageExeTime;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public KeyValuePo getExeCount() {
		return exeCount;
	}

	public void setExeCount(KeyValuePo exeCount) {
		this.exeCount = exeCount;
	}

	public KeyValuePo getAverageExeTime() {
		return averageExeTime;
	}

	public void setAverageExeTime(KeyValuePo averageExeTime) {
		this.averageExeTime = averageExeTime;
	}

	
	public int compareTo(OtherHaBoLogRecord o) {
		
		byte[] h1 = this.getKeyName().getBytes();
		byte[] h2 = o.getKeyName().getBytes();
		
		
		int index = Math.min(h1.length, h2.length);
		
		for(int i=0;i<index;   i++){
			if(h1[i]   <   h2[i]){
				return   -1;
			}else if(h1[i]   >   h2[i]){
				return   1;
			}
			
		} 
		return   h1.length   -   h2.length;
	}

	
	
	
	

}
