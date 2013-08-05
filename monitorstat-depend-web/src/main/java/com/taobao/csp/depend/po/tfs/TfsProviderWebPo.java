package com.taobao.csp.depend.po.tfs;

import java.util.HashMap;
import java.util.Map;

/**
 * TFS 显示在页面上的table的po类
 * 
 * @author zhongting.zy
 * 
 */
public class TfsProviderWebPo {
  private String appName;
  // TFS操作不常变换，因此写死3个HashMap

  // <typename,<column,value>
  public Map<String, Map<String, String>> norMap = new HashMap<String, Map<String, String>>();
  public Map<String, Map<String, String>> rushMap = new HashMap<String, Map<String, String>>();

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }
}
