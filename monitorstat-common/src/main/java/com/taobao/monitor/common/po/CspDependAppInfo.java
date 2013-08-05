package com.taobao.monitor.common.po;

/*
 * csp_app_id_map 表的PO FIXME 这张表会改名称的
 */
public class CspDependAppInfo {
  private long id;
  private String appname;
  private String lastupdate_time;
  private String appType; // 不常变化，直接写为名称
  private String keyname;

  public CspDependAppInfo(String configType) {
    this.configType = configType;
  }

  private String configType; // APP or KEY

  public String getConfigType() {
    return configType;
  }

  public void setConfigType(String configType) {
    this.configType = configType;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAppname() {
    return appname;
  }

  public void setAppname(String appname) {
    this.appname = appname;
  }

  public String getLastupdate_time() {
    return lastupdate_time;
  }

  public void setLastupdate_time(String lastupdate_time) {
    this.lastupdate_time = lastupdate_time;
  }

  public String getAppType() {
    return appType;
  }

  public void setAppType(String appType) {
    this.appType = appType;
  }

  public String getKeyname() {
    return keyname;
  }

  public void setKeyname(String keyname) {
    this.keyname = keyname;
  }
}
