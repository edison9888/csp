package com.taobao.csp.depend.po.report;

import java.util.HashMap;
import java.util.Map;

/**
 * 强弱依赖报表显示的最小单元
 * 
 * @author zhongting.zy
 * 
 */
public class StrongWeakAutoCheckReport implements
    Comparable<StrongWeakAutoCheckReport> {
  private String opsName; // 所选应用
  private String targetOpsName; // 被检测的应用
  private String targetAppType; //应用类型

  private String expectDepIntensity;// 预期的强弱关系

  // 依赖检测结果
  private Map<String, String> curCheckResult = new HashMap<String, String>();
  private Map<String, String> preCheckResult = new HashMap<String, String>();

  private boolean isCheckConfig = false; // 检测脚本是否配置，默认不配置
  private Integer targetAppStatus = STATUS_ADD; // 默认为添加

  // -1表示减少，0表示不变，1表示新增
  public static final int STATUS_REDUCE = -1;
  public static final int STATUS_EQUAL = 0;
  public static final int STATUS_ADD = 1;

  public String getOpsName() {
    return opsName;
  }

  public void setOpsName(String opsName) {
    this.opsName = opsName;
  }

  public String getTargetOpsName() {
    return targetOpsName;
  }

  public void setTargetOpsName(String targetOpsName) {
    this.targetOpsName = targetOpsName;
  }

  public String getTargetAppType() {
    return targetAppType;
  }

  public void setTargetAppType(String targetAppType) {
    this.targetAppType = targetAppType;
  }

  public String getExpectDepIntensity() {
    return expectDepIntensity;
  }

  public void setExpectDepIntensity(String expectDepIntensity) {
    this.expectDepIntensity = expectDepIntensity;
  }

  public Map<String, String> getCurCheckResult() {
    return curCheckResult;
  }

  public void setCurCheckResult(Map<String, String> curCheckResult) {
    this.curCheckResult = curCheckResult;
  }

  public Map<String, String> getPreCheckResult() {
    return preCheckResult;
  }

  public void setPreCheckResult(Map<String, String> preCheckResult) {
    this.preCheckResult = preCheckResult;
  }

  public boolean isCheckConfig() {
    return isCheckConfig;
  }

  public void setCheckConfig(boolean isCheckConfig) {
    this.isCheckConfig = isCheckConfig;
  }

  public Integer getTargetAppStatus() {
    return targetAppStatus;
  }

  public void setTargetAppStatus(Integer targetAppStatus) {
    this.targetAppStatus = targetAppStatus;
  }

  @Override
  public int compareTo(StrongWeakAutoCheckReport o) {
    if (o.getTargetAppStatus().intValue() < this.targetAppStatus.intValue())
      return -1;
    else if (o.getTargetAppStatus().intValue() > this.targetAppStatus
        .intValue())
      return 1;
    return 0;
  }

}
