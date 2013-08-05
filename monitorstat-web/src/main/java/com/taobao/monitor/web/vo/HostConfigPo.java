package com.taobao.monitor.web.vo;

import java.util.Date;

public class HostConfigPo {
	private int appId;
	private String osVersion;
	private String platform;
	private String domainIp;
	private String cpu;
	private String memory;
	private String javaVersion;
	private String jbossVersion;
	private String apacheVersion;
	private String diskHome;
	private String uptime;
	private String jvmParameters;
	private Date updateTime;
	/**
	 * @return the appId
	 */
	public int getAppId() {
		return appId;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(int appId) {
		this.appId = appId;
	}
	/**
	 * @return the osVersion
	 */
	public String getOsVersion() {
		return osVersion;
	}
	/**
	 * @param osVersion the osVersion to set
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/**
	 * @return the domainIp
	 */
	public String getDomainIp() {
		return domainIp;
	}
	/**
	 * @param domainIp the domainIp to set
	 */
	public void setDomainIp(String domainIp) {
		this.domainIp = domainIp;
	}
	/**
	 * @return the cpu
	 */
	public String getCpu() {
		return cpu;
	}
	/**
	 * @param cpu the cpu to set
	 */
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	/**
	 * @return the memory
	 */
	public String getMemory() {
		return memory;
	}
	/**
	 * @param memory the memory to set
	 */
	public void setMemory(String memory) {
		this.memory = memory;
	}
	/**
	 * @return the javaVersion
	 */
	public String getJavaVersion() {
		return javaVersion;
	}
	/**
	 * @param javaVersion the javaVersion to set
	 */
	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}
	/**
	 * @return the jbossVersion
	 */
	public String getJbossVersion() {
		return jbossVersion;
	}
	/**
	 * @param jbossVersion the jbossVersion to set
	 */
	public void setJbossVersion(String jbossVersion) {
		this.jbossVersion = jbossVersion;
	}
	/**
	 * @return the apacheVersion
	 */
	public String getApacheVersion() {
		return apacheVersion;
	}
	/**
	 * @param apacheVersion the apacheVersion to set
	 */
	public void setApacheVersion(String apacheVersion) {
		this.apacheVersion = apacheVersion;
	}
	/**
	 * @return the diskHome
	 */
	public String getDiskHome() {
		return diskHome;
	}
	/**
	 * @param diskHome the diskHome to set
	 */
	public void setDiskHome(String diskHome) {
		this.diskHome = diskHome;
	}
	/**
	 * @return the uptime
	 */
	public String getUptime() {
		return uptime;
	}
	/**
	 * @param uptime the uptime to set
	 */
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	/**
	 * @return the jvmParameters
	 */
	public String getJvmParameters() {
		return jvmParameters;
	}
	/**
	 * @param jvmParameters the jvmParameters to set
	 */
	public void setJvmParameters(String jvmParameters) {
		this.jvmParameters = jvmParameters;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return
	 */
	@Override
	public String toString() {
		return "HostConfigPo [appId=" + appId + ", osVersion=" + osVersion + ", platform=" + platform + ", domainIp="
				+ domainIp + ", cpu=" + cpu + ", memory=" + memory + ", javaVersion=" + javaVersion + ", jbossVersion="
				+ jbossVersion + ", apacheVersion=" + apacheVersion + ", diskHome=" + diskHome + ", uptime=" + uptime
				+ ", jvmParameters=" + jvmParameters + ", updateTime=" + updateTime + "]";
	}
	
}
