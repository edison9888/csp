package com.taobao.csp.depend.po.tfs;

/**
 * Csp_Tfs_Provider_Detail±íµÄPoÀà
 * 
 * @author zhongting.zy
 * 
 */
public class CspTfsProviderDetailPo {
  private String appName;
  private String operType;
  private long operTimes;
  private long operSize;
  private long operRt;
  private long operSucc;
  private float cacheHitRatio;

  private long rushOperTimes;
  private long rushOperSize;
  private long rushOperRt;
  private long rushOperSucc;
  private float rushcacheHitRatio;

  private String collectTime;

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getOperType() {
    return operType;
  }

  public void setOperType(String operType) {
    this.operType = operType;
  }

  public long getOperTimes() {
    return operTimes;
  }

  public void setOperTimes(long operTimes) {
    this.operTimes = operTimes;
  }

  public long getOperSize() {
    return operSize;
  }

  public void setOperSize(long operSize) {
    this.operSize = operSize;
  }

  public long getOperRt() {
    return operRt;
  }

  public void setOperRt(long operRt) {
    this.operRt = operRt;
  }

  public long getOperSucc() {
    return operSucc;
  }

  public void setOperSucc(long operSucc) {
    this.operSucc = operSucc;
  }

  public float getCacheHitRatio() {
    return cacheHitRatio;
  }

  public void setCacheHitRatio(float cacheHitRatio) {
    this.cacheHitRatio = cacheHitRatio;
  }

  public long getRushOperTimes() {
    return rushOperTimes;
  }

  public void setRushOperTimes(long rushOperTimes) {
    this.rushOperTimes = rushOperTimes;
  }

  public long getRushOperSize() {
    return rushOperSize;
  }

  public void setRushOperSize(long rushOperSize) {
    this.rushOperSize = rushOperSize;
  }

  public long getRushOperRt() {
    return rushOperRt;
  }

  public void setRushOperRt(long rushOperRt) {
    this.rushOperRt = rushOperRt;
  }

  public long getRushOperSucc() {
    return rushOperSucc;
  }

  public void setRushOperSucc(long rushOperSucc) {
    this.rushOperSucc = rushOperSucc;
  }

  public float getRushcacheHitRatio() {
    return rushcacheHitRatio;
  }

  public void setRushcacheHitRatio(float rushcacheHitRatio) {
    this.rushcacheHitRatio = rushcacheHitRatio;
  }

  public String getCollectTime() {
    return collectTime;
  }

  public void setCollectTime(String collectTime) {
    this.collectTime = collectTime;
  }
}
