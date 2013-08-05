package com.taobao.monitor.common.po;
/**
 * 北斗DBA手动维护的分组表
 * @author zhongting.zy
 *
 */
public class DbHostGroup {
  private String groupName;
  private String memberName;
  private String ownerName;
  private String comment;
  public String getGroupName() {
    return groupName;
  }
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }
  public String getMemberName() {
    return memberName;
  }
  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }
  public String getOwnerName() {
    return ownerName;
  }
  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }

}
