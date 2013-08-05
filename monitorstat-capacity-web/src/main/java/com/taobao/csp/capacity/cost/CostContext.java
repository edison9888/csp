package com.taobao.csp.capacity.cost;

import java.util.Map;

public class CostContext {
	
	ICostStrategy strategy;

	public CostContext(ICostStrategy strategy) {
		this.strategy = strategy;
	}
	
	public void caculateCost() {
		strategy.caculateCost();
	}
	
	public Map<String, Long> getCallNum() {
		return strategy.getCallNum();
	}
	
	public Map<String, Long> getCallDep() {
		return strategy.getCallDep();
	}
	
	public int getMachineNum(String name) {
		return strategy.getMachineNum(name);
	}
	
	public CostType getCostType() {
		return strategy.getCostType();
	}
	
}
