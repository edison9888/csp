
package com.taobao.monitor.dependent.po;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2011-4-22 ÏÂÎç02:03:48
 */
public class AppDependentRelationPo {
	
	private String selfSite;
	
	private String selfIp;
	private String selfOpsName;
	
	private String dependentOpsName;
	
	private String dependentIp;
	
	private int dependentPort;
	
	private String dependentSite;
	
	private String dependentType;
	
	private Date collectTime;

	public String getSelfOpsName() {
		return selfOpsName;
	}

	public void setSelfOpsName(String selfOpsName) {
		this.selfOpsName = selfOpsName;
	}

	public String getDependentOpsName() {
		return dependentOpsName;
	}

	public void setDependentOpsName(String dependentOpsName) {
		this.dependentOpsName = dependentOpsName;
	}

	public String getDependentType() {
		return dependentType;
	}

	public void setDependentType(String dependentType) {
		this.dependentType = dependentType;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public String getDependentIp() {
		return dependentIp;
	}

	public void setDependentIp(String dependentIp) {
		this.dependentIp = dependentIp;
	}

	public int getDependentPort() {
		return dependentPort;
	}

	public void setDependentPort(int dependentPort) {
		this.dependentPort = dependentPort;
	}

	public String getDependentSite() {
		return dependentSite;
	}

	public void setDependentSite(String dependentSite) {
		this.dependentSite = dependentSite;
	}

	public String getSelfSite() {
		return selfSite;
	}

	public void setSelfSite(String selfSite) {
		this.selfSite = selfSite;
	}

	public String getSelfIp() {
		return selfIp;
	}

	public void setSelfIp(String selfIp) {
		this.selfIp = selfIp;
	}
	
	
	
	

}
