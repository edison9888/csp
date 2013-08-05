package com.taobao.monitor.common.po;

/**
 * CSP_APP_DEPEND_APP±íµÄPO
 * 
 * @author zhongting.zy
 * 
 */
public class CspAppDepAppPo {
  private String opsName;
  private String depOpsName;
  private String depAppType;
  private String collectTime;

  public String getOpsName() {
    return opsName;
  }

  public void setOpsName(String opsName) {
    this.opsName = opsName;
  }

  public String getDepOpsName() {
    return depOpsName;
  }

  public void setDepOpsName(String depOpsName) {
    this.depOpsName = depOpsName;
  }

  public String getDepAppType() {
    return depAppType;
  }

  public void setDepAppType(String depAppType) {
    this.depAppType = depAppType;
  }

  public String getCollectTime() {
    return collectTime;
  }

  public void setCollectTime(String collectTime) {
    this.collectTime = collectTime;
  }
}
