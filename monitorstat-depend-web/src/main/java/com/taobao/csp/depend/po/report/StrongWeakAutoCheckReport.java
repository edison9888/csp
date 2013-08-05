package com.taobao.csp.depend.po.report;

import java.util.HashMap;
import java.util.Map;

/**
 * ǿ������������ʾ����С��Ԫ
 * 
 * @author zhongting.zy
 * 
 */
public class StrongWeakAutoCheckReport implements
    Comparable<StrongWeakAutoCheckReport> {
  private String opsName; // ��ѡӦ��
  private String targetOpsName; // ������Ӧ��
  private String targetAppType; //Ӧ������

  private String expectDepIntensity;// Ԥ�ڵ�ǿ����ϵ

  // ���������
  private Map<String, String> curCheckResult = new HashMap<String, String>();
  private Map<String, String> preCheckResult = new HashMap<String, String>();

  private boolean isCheckConfig = false; // ���ű��Ƿ����ã�Ĭ�ϲ�����
  private Integer targetAppStatus = STATUS_ADD; // Ĭ��Ϊ���

  // -1��ʾ���٣�0��ʾ���䣬1��ʾ����
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
