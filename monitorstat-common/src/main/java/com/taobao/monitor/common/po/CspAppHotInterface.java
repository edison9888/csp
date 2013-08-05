package com.taobao.monitor.common.po;

import java.util.Date;

public class CspAppHotInterface {
  private String appType;
  private String appName;
  private String keyName;
  private long callNum;
  private Date collect_time;
  public String getAppType() {
    return appType;
  }
  public void setAppType(String appType) {
    this.appType = appType;
  }
  public String getAppName() {
    return appName;
  }
  public void setAppName(String appName) {
    this.appName = appName;
  }
  public String getKeyName() {
    return keyName;
  }
  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }
  public long getCallNum() {
    return callNum;
  }
  public void setCallNum(long callNum) {
    this.callNum = callNum;
  }
  public Date getCollect_time() {
    return collect_time;
  }
  public void setCollect_time(Date collect_time) {
    this.collect_time = collect_time;
  }

}
