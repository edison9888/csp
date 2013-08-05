package com.taobao.csp.depend.po;

import java.util.Date;

public class HistoryGraphPo {
  private String opsName;
  
  private long callAllNum;
  
  private Date collectDate;

  public String getOpsName() {
    return opsName;
  }

  public void setOpsName(String opsName) {
    this.opsName = opsName;
  }

  public long getCallAllNum() {
    return callAllNum;
  }

  public void setCallAllNum(long callAllNum) {
    this.callAllNum = callAllNum;
  }

  public Date getCollectDate() {
    return collectDate;
  }

  public void setCollectDate(Date collectDate) {
    this.collectDate = collectDate;
  }
}
