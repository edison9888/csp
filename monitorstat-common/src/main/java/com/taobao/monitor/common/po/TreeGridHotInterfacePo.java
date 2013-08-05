package com.taobao.monitor.common.po;

import java.util.HashMap;
import java.util.Map;

public class TreeGridHotInterfacePo extends TreeGridAddBasePo {
  //time,callnum
  private Map map = new HashMap();

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }
  
}
