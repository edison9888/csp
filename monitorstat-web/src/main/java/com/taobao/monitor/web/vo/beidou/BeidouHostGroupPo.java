package com.taobao.monitor.web.vo.beidou;

/**
 * 保存group和db hostName的对应关系
 * @author baiyan
 *
 */
public class BeidouHostGroupPo {
	private String groupName;
	
	private String memberName;
	
	private String ownerName;

	public static String dbConstants = "db";
	
	public String getGroupName() {
		return groupName ;
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
	

}
