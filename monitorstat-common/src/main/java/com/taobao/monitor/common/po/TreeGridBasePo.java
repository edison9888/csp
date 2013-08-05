package com.taobao.monitor.common.po;

public class TreeGridBasePo {
  private long id;
  private String keyName;
  private String appName;
  private String rate;
  private String acturalRate;
  
  //补充一些熟悉到po中
  private TreeGridAddBasePo addedPo = new TreeGridAddBasePo();  
  
  public TreeGridAddBasePo getAddedPo() {
    return addedPo;
  }
  public void setAddedPo(TreeGridAddBasePo addedPo) {
    this.addedPo = addedPo;
  }
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }
  public String getKeyName() {
    return keyName;
  }
  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }
  public String getRate() {
    return rate;
  }
  public void setRate(String rate) {
    this.rate = rate;
  }
  public String getActuralRate() {
    return acturalRate;
  }
  public void setActuralRate(String acturalRate) {
    this.acturalRate = acturalRate;
  }
  public String getAppName() {
    return appName;
  }
  public void setAppName(String appName) {
    this.appName = appName;
  }
  
}
