package com.taobao.csp.cost.po;

/**
 * 一些基础依赖的成本类
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-13
 */
public class SimpleCostPo {

	//依赖类型（TDDL/TFS/TAIR）
	private String dependType;
	//依赖名
	private String dependName;
	//调用次数
	private long callNum;
	//消费者成本
	private double consumerCost;
	
	public SimpleCostPo(String dependType){
		this.dependType=dependType;
	}
	
	public String getDependType() {
		return dependType;
	}
	public void setDependType(String dependType) {
		this.dependType = dependType;
	}
	public String getDependName() {
		return dependName;
	}
	public void setDependName(String dependName) {
		this.dependName = dependName;
	}
	public long getCallNum() {
		return callNum;
	}
	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}
	public double getConsumerCost() {
		return consumerCost;
	}
	public void setConsumerCost(double consumerCost) {
		this.consumerCost = consumerCost;
	}
	
	
}
