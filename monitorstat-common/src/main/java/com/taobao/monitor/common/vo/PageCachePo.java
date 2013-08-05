
package com.taobao.monitor.common.vo;
/**
 * 
 * @author xiaodu
 * @version 2010-4-10 обнГ03:07:11
 */
public class PageCachePo implements Comparable<PageCachePo>{
	
	private Integer countkeyId;
	private Integer averageKeyId;
	
	
	private String pageCacheName;
	
	private String exeCount;
	
	private String exeAverage;

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

	public String getPageCacheName() {
		return pageCacheName;
	}

	public void setPageCacheName(String pageCacheName) {
		this.pageCacheName = pageCacheName;
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

	public int compareTo(PageCachePo o) {
		
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
