package com.taobao.csp.depend.po.tfs;

import java.util.HashMap;
import java.util.Map;

/**
 * TFS ��ʾ��ҳ���ϵ�table��po��
 * 
 * @author zhongting.zy
 * 
 */
public class TfsProviderWebPo {
  private String appName;
  // TFS���������任�����д��3��HashMap

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
