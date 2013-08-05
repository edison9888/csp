package com.taobao.monitor.common.po;

public class TreeGridAlarmPo extends TreeGridAddBasePo {
  private int alarmCount;
  private double callCount; 
  private double consumeCount; 
  private String rate = "0.0%";
  
  public int getAlarmCount() {
    return alarmCount;
  }

  public void setAlarmCount(int alarmCount) {
    this.alarmCount = alarmCount;
  }

  public double getCallCount() {
    return callCount;
  }

  public void setCallCount(double callCount) {
    this.callCount = callCount;
  }

  public double getConsumeCount() {
    return consumeCount;
  }

  public void setConsumeCount(double consumeCount) {
    this.consumeCount = consumeCount;
  }

  public String getRate() {
    return rate;
  }

  public void setRate(String rate) {
    this.rate = rate;
  }
}
