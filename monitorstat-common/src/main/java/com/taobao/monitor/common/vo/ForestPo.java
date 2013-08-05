
package com.taobao.monitor.common.vo;

import com.taobao.monitor.common.util.Utlitites;

/**
 * 
 * @author xiaodu
 * @version 2010-4-10 обнГ03:02:57
 */
public class ForestPo implements Comparable<ForestPo>{
	private Integer countkeyId;
	private Integer averageKeyId;
	private String methodName;
	
	private String type;
	
	private String exeCount;
	
	private String exeAverage;
	
	public String getHtmlMethodName(){
		return Utlitites.formatHtmlStr(this.methodName, 20);
	}

	
	public Integer getCountkeyId() {
		return countkeyId;
	}


	public void setCountkeyId(Integer countkeyId) {
		this.countkeyId = countkeyId;
	}


	public Integer getAverageKeyId() {
		return averageKeyId;
	}


	public void setAverageKeyId(Integer averageKeyId) {
		this.averageKeyId = averageKeyId;
	}


	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExeCount() {
		return exeCount;
	}

	public void setExeCount(String exeCount) {
		this.exeCount = exeCount;
	}

	public String getExeAverage() {
		return exeAverage;
	}

	public void setExeAverage(String exeAverage) {
		this.exeAverage = exeAverage;
	}
	private long exeCountNum;
	
	public long getExeCountNum() {
		return exeCountNum;
	}

	public void setExeCountNum(long exeCountNum) {
		this.exeCountNum = exeCountNum;
	}

	public int compareTo(ForestPo o) {
		
		if(o.exeCountNum==this.exeCountNum){
			return 0;
		}else if(o.exeCountNum>this.exeCountNum){
			return 1;
		}else if(o.exeCountNum<this.exeCountNum){
			return -1;
		}
		return 0;
	}
	

}
