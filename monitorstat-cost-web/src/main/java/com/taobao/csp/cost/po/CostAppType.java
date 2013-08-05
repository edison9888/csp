package com.taobao.csp.cost.po;

import java.util.Date;

import com.taobao.csp.cost.service.day.CostType;

/**
 * 应用的各种依赖成本以及总成本
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-19
 */
public class CostAppType {

	//应用名
	private String appName;
	//类型（All|Direct_Cost）
	private String costType;
	//依赖名
	private String costName;
	//调用次数
	private long callNum;
	//依赖成本（直接依赖成本，或者总成本）
	private double dependCost;
	//收集时间
	private Date collectTime;
	//公司
	private String groupName="";
	//产品线
	private String line="";
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public long getCallNum() {
		return callNum;
	}
	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}
	
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public double getDependCost() {
		return dependCost;
	}
	public void setDependCost(double dependCost) {
		this.dependCost = dependCost;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
}
