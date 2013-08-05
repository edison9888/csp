package com.taobao.monitor.common.po;

public class TreeGridData extends TreeGridBasePo {
//  private String iconCls = "icon-ok";
//  private String state = "closed";
  private String sourceUrl;
  
  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  private TreeGridBasePo[] children = new TreeGridBasePo[0];

  public TreeGridBasePo[] getChildren() {
    return children;
  }

  public void setChildren(TreeGridBasePo[] children) {
    this.children = children;
  } 
}
