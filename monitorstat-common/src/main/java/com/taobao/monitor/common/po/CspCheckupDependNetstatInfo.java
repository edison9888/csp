package com.taobao.monitor.common.po;

import java.util.Date;

public class CspCheckupDependNetstatInfo {
  private String appName;
  private String ip;
  private int port;
  private String dependApp;
  private String dependIp;
  private int dependport;
  private Date gmt_create;
  public String getAppName() {
    return appName;
  }
  public void setAppName(String appName) {
    this.appName = appName;
  }
  public String getIp() {
    return ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
  public int getPort() {
    return port;
  }
  public void setPort(int port) {
    this.port = port;
  }
  public String getDependApp() {
    return dependApp;
  }
  public void setDependApp(String dependApp) {
    this.dependApp = dependApp;
  }
  public String getDependIp() {
    return dependIp;
  }
  public void setDependIp(String dependIp) {
    this.dependIp = dependIp;
  }
  public int getDependport() {
    return dependport;
  }
  public void setDependport(int dependport) {
    this.dependport = dependport;
  }
  public Date getGmt_create() {
    return gmt_create;
  }
  public void setGmt_create(Date gmt_create) {
    this.gmt_create = gmt_create;
  }

}
