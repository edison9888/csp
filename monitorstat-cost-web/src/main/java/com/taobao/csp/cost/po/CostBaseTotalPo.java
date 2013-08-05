package com.taobao.csp.cost.po;

import java.util.Date;

import com.taobao.csp.cost.service.day.CostType;

/**
 * 一些基础依赖的总成本
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-19
 */
public class CostBaseTotalPo {

	//tairGroupName,dbName
	private String baseName;
	
	//类型（tair/db）
	private String costType;
	
	//调用次数
	private long callNum;
	
	//机器数
	private int machineNum;
	//单次成本
	private double perCost;
	//总成本
	private double totalCost;
	//收集时间
	private Date collectTime;
	
	public String getBaseName() {
		return baseName;
	}
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public long getCallNum() {
		return callNum;
	}
	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}
	public int getMachineNum() {
		return machineNum;
	}
	public void setMachineNum(int machineNum) {
		this.machineNum = machineNum;
	}
	public double getPerCost() {
		return perCost;
	}
	public void setPerCost(double perCost) {
		this.perCost = perCost;
	}
	
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	
	
	
}
