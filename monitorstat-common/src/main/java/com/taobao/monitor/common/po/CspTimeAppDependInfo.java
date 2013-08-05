package com.taobao.monitor.common.po;


public class CspTimeAppDependInfo implements Comparable<CspTimeAppDependInfo>{
  
  private long id;
  private String appName;
  private String depAppName;
  private String sourceAppName;
  private String dependStatus;  //应用状态目前有3种，如上面static字段。
  private String dependtype; //依赖类型
  
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
  public String getDepAppName() {
    return depAppName;
  }
  public void setDepAppName(String depAppName) {
    this.depAppName = depAppName;
  }
  public String getSourceAppName() {
    return sourceAppName;
  }
  public void setSourceAppName(String sourceAppName) {
    this.sourceAppName = sourceAppName;
  }
  public String getDependStatus() {
    return dependStatus;
  }
  public void setDependStatus(String dependStatus) {
    this.dependStatus = dependStatus;
  }
  public String getDependtype() {
    return dependtype;
  }
  public void setDependtype(String dependtype) {
    this.dependtype = dependtype;
  }
  @Override
  public int compareTo(CspTimeAppDependInfo o) {
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
}
