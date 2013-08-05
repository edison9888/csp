package com.taobao.monitor.common.po;

/**
 * 用来存储实时监控之外的应用信息
 * @author zhongting.zy
 *
 */
public class CspTimeExtraAppInfo {
  private String extraAppName;
  private String extraAppDesc;
  private String extraAppType;
  
  public String getExtraAppName() {
    return extraAppName;
  }
  public void setExtraAppName(String extraAppName) {
    this.extraAppName = extraAppName;
  }
  public String getExtraAppDesc() {
    return extraAppDesc;
  }
  public void setExtraAppDesc(String extraAppDesc) {
    this.extraAppDesc = extraAppDesc;
  }
  public String getExtraAppType() {
    return extraAppType;
  }
  public void setExtraAppType(String extraAppType) {
    this.extraAppType = extraAppType;
  }
}
