
package com.taobao.monitor.common.vo;

import com.taobao.monitor.common.util.Utlitites;

/**
 * 
 * @author xiaodu
 * @version 2010-4-10 обнГ03:21:22
 */
public class TairClientPo implements Comparable<TairClientPo>{
	private Integer countkeyId;
	private Integer averageKeyId;
	private String methodName;
	
	private String exeCount;
	
	private String averageExe;
	
	private String averageExeBaseLine;
	
	private String exeCountBaseLine;
	
	public String getAverageExeBaseLine() {
		return averageExeBaseLine;
	}

	public void setAverageExeBaseLine(String averageExeBaseLine) {
		this.averageExeBaseLine = averageExeBaseLine;
	}

	public String getExeCountBaseLine() {
		return exeCountBaseLine;
	}

	public void setExeCountBaseLine(String exeCountBaseLine) {
		this.exeCountBaseLine = exeCountBaseLine;
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

	public String getHtmlMethodName(){
		return Utlitites.formatHtmlStr(this.methodName, 20);
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getExeCount() {
		return exeCount;
	}

	public void setExeCount(String exeCount) {
		this.exeCount = exeCount;
	}

	public String getAverageExe() {
		return averageExe;
	}

	public void setAverageExe(String averageExe) {
		this.averageExe = averageExe;
	}
	
	
	private long exeCountNum;
	
	public long getExeCountNum() {
		return exeCountNum;
	}

	public void setExeCountNum(long exeCountNum) {
		this.exeCountNum = exeCountNum;
	}

	public int compareTo(TairClientPo o) {
		
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
