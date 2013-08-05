package com.taobao.monitor.common.po;

public class CspTimeKeyDependInfo implements Comparable<CspTimeKeyDependInfo>{
  private long id;
  private String appName;
  private String keyName;
  private String dependAppName;
  private String dependKeyName;
  private String sourceAppName;
  private String sourceKeyName;
  private String dependStatus;  //标记手动配置项是否被自动检测出来
  private float rate;
  
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
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
  public String getDependAppName() {
    return dependAppName;
  }
  public void setDependAppName(String dependAppName) {
    this.dependAppName = dependAppName;
  }
  public String getDependKeyName() {
    return dependKeyName;
  }
  public void setDependKeyName(String dependKeyName) {
    this.dependKeyName = dependKeyName;
  }
  public String getSourceAppName() {
    return sourceAppName;
  }
  public void setSourceAppName(String sourceAppName) {
    this.sourceAppName = sourceAppName;
  }
  public String getSourceKeyName() {
    return sourceKeyName;
  }
  public void setSourceKeyName(String sourceKeyName) {
    this.sourceKeyName = sourceKeyName;
  }
  public String getDependStatus() {
    return dependStatus;
  }
  public void setDependStatus(String dependStatus) {
    this.dependStatus = dependStatus;
  }
  @Override
  public int compareTo(CspTimeKeyDependInfo o) {
    try {
      Integer a = Integer.parseInt(this.dependStatus);
      Integer b = Integer.parseInt(o.getDependStatus());
      if (a >= b) {
        return -1;
      } else {
        return 1;
      }
    } catch (Exception e) {
    }
    return 0;
  }
public float getRate() {
	return rate;
}
public void setRate(float rate) {
	this.rate = rate;
}
  
  
}
