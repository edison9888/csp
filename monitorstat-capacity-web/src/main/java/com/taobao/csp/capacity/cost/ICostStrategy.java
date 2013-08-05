package com.taobao.csp.capacity.cost;

import java.util.Map;

public interface ICostStrategy {
	
	public void caculateCost();
	
	public Map<String, Long> getCallNum();
	
	public Map<String, Long> getCallDep();
	
	public int getMachineNum(String name);
	
	public CostType getCostType();
}
