package com.taobao.csp.depend.po.url;

import java.util.Date;

/**
 * 
 * @author zhongting.zy
 * 
 */
public class UrlOriginSummary implements Comparable<UrlOriginSummary>{
	private String appName;
	private String originUrl;
	private long originUrlNum;
	private Date collect_time;
	
	private long preOriginUrlNum;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getOriginUrl() {
		return originUrl;
	}
	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
	}
	public long getOriginUrlNum() {
		return originUrlNum;
	}
	public void setOriginUrlNum(long originUrlNum) {
		this.originUrlNum = originUrlNum;
	}
	
	
	public Date getCollect_time() {
    return collect_time;
  }
  public void setCollect_time(Date collect_time) {
    this.collect_time = collect_time;
  }
  public long getPreOriginUrlNum() {
    return preOriginUrlNum;
  }
  public void setPreOriginUrlNum(long preOriginUrlNum) {
    this.preOriginUrlNum = preOriginUrlNum;
  }
  @Override
  public int compareTo(UrlOriginSummary o) {
    if(o.getOriginUrlNum() < getOriginUrlNum()){
      return -1;
    }else if(o.getOriginUrlNum() > getOriginUrlNum()){
      return 1;
    }			
    return 0;	
  }
	
	
}
