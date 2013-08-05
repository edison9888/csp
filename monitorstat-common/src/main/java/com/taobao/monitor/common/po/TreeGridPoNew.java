package com.taobao.monitor.common.po;

import java.util.HashMap;
import java.util.Map;

public class TreeGridPoNew {
  private long id;
  private String uuid;  
  private Map<String, Object> map = new HashMap<String, Object>();
  private TreeGridPoNew[] children = new TreeGridPoNew[0];
  private String state = "";  //默认打开，可以选择closed
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }
  public String getUuid() {
    return uuid;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
  public Map<String, Object> getMap() {
    return map;
  }
  public void setMap(Map<String, Object> map) {
    this.map = map;
  }
  public TreeGridPoNew[] getChildren() {
    return children;
  }
  public void setChildren(TreeGridPoNew[] children) {
    this.children = children;
  }
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }
  
}
