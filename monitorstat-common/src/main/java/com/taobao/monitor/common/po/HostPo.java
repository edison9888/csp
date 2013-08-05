package com.taobao.monitor.common.po;

import java.io.Serializable;

public class HostPo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8979861220575300113L;

	/***非持久信息**/
	private int hostId;
	
	private int appId;
	//第一位 表示只保存 limit ，第二位表示保存data
	private String savedata;
	
	private String userName = "nobody"; //默认
	
	private String userPassword = "look";//默认
	
	
	/***持久信息 CSP_APP_HOST_INFO_SYNC 表**/
	private String opsName;
	
	private String hostIp;
	
	private String hostSite;//属于哪个机房
	
	private String hostName;//机器名称
	
	private String nodeGroup;//机器分组名称
	
	private String rack;//机柜信息
	
	private String hdrs_chassis;
	
	private String hostType; //机器类型，是否是虚拟机  vm 表示虚拟机
	
	private String description;
	
	private String vmparent;
	
	private String state;
	
	private String manifest;

	private int cpsVersion;
	
	public int getCpsVersion() {
		return cpsVersion;
	}

	public void setCpsVersion(int cpsVersion) {
		this.cpsVersion = cpsVersion;
	}

	/**
	 * 是否虚拟机
	 * @return true表示虚拟机
	 */
	public boolean isVirtualHost(){
		return !vmparent.equals("");
	}
	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getSavedata() {
		return savedata;
	}

	public void setSavedata(String savedata) {
		this.savedata = savedata;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getHostSite() {
		return hostSite;
	}

	public void setHostSite(String hostSite) {
		this.hostSite = hostSite;
	}

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public String getHostType() {
		return hostType;
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

	public String getNodeGroup() {
		return nodeGroup;
	}

	public void setNodeGroup(String nodeGroup) {
		this.nodeGroup = nodeGroup;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getHdrs_chassis() {
		return hdrs_chassis;
	}

	public void setHdrs_chassis(String hdrs_chassis) {
		this.hdrs_chassis = hdrs_chassis;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVmparent() {
		return vmparent;
	}

	public void setVmparent(String vmparent) {
		this.vmparent = vmparent;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getManifest() {
		return manifest;
	}

	public void setManifest(String manifest) {
		this.manifest = manifest;
	}
	
	
}
