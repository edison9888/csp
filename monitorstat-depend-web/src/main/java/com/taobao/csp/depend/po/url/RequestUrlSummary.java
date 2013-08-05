package com.taobao.csp.depend.po.url;

import java.util.Date;
import java.util.HashMap;

/**
 * 
 * @author zhongting.zy
 * 
 */
public class RequestUrlSummary implements Comparable<RequestUrlSummary>{
  private String appName;
  private String requestUrl;
  private long requestNum;
  private int requestTime;

  private Date collect_time;

  private long preRequestUrlNum;

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getRequestUrl() {
    return requestUrl;
  }

  public void setRequestUrl(String requestUrl) {
    this.requestUrl = requestUrl;
  }

  public long getRequestNum() {
    return requestNum;
  }

  public void setRequestNum(long requestNum) {
    this.requestNum = requestNum;
  }

  public int getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(int requestTime) {
    this.requestTime = requestTime;
  }

  public Date getCollect_time() {
    return collect_time;
  }

  public void setCollect_time(Date collect_time) {
    this.collect_time = collect_time;
  }

  public long getPreRequestUrlNum() {
    return preRequestUrlNum;
  }

  public void setPreRequestUrlNum(long preRequestUrlNum) {
    this.preRequestUrlNum = preRequestUrlNum;
  }
  @Override
  public int compareTo(RequestUrlSummary o) {
    if(o.getRequestNum() < getRequestNum()){
      return -1;
    }else if(o.getRequestNum() > getRequestNum()){
      return 1;
    }			
    return 0;	
  }


}
