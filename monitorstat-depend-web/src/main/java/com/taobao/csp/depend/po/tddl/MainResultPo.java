package com.taobao.csp.depend.po.tddl;

public class MainResultPo {
  private boolean status;
  private String msg;
  private TablePo[] data;
  public boolean isStatus() {
    return status;
  }
  public void setStatus(boolean status) {
    this.status = status;
  }
  public String getMsg() {
    return msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }
  public TablePo[] getData() {
    return data;
  }
  public void setData(TablePo[] data) {
    this.data = data;
  }
}
