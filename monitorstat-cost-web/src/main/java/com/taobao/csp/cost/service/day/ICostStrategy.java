package com.taobao.csp.cost.service.day;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 总成本计算接口
 * 
 */
public interface ICostStrategy {
	
	public void caculateCost(Date date,int tableId);
	
	public Map<String, Long> getCallNum();
	
	public Map<String, Long> getCallDep();
	
	public int getMachineNum(String name);
	
	/**
	 * 获得机器列表
	 * 
	 * @param name
	 * @return
	 */
	public Set<String> getMachines(String name);
	
	public CostType getCostType();
}
