
package com.taobao.monitor.common.vo;

import com.taobao.monitor.common.util.Utlitites;

/**
 * 
 * @author xiaodu
 * @version 2010-4-10 обнГ02:58:34
 */
public class SearchEnginePo implements Comparable<SearchEnginePo>{
	
	private Integer countkeyId;
	private Integer averageKeyId;
	private String url;
	
	private String type;
	
	private String exeCount;
	
	private String exeAverage;
	
	private String exeCountBaseLine;
	
	private String exeAverageBaseLine;
	
	public String getExeCountBaseLine() {
		return exeCountBaseLine;
	}


	public void setExeCountBaseLine(String exeCountBaseLine) {
		this.exeCountBaseLine = exeCountBaseLine;
	}


	public String getExeAverageBaseLine() {
		return exeAverageBaseLine;
	}


	public void setExeAverageBaseLine(String exeAverageBaseLine) {
		this.exeAverageBaseLine = exeAverageBaseLine;
	}


	public String getHtmlUrl(){
		return Utlitites.formatHtmlStr(this.url, 40);
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


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public int compareTo(SearchEnginePo o) {
		
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
