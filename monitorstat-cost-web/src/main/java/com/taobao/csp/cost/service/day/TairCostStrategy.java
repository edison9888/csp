package com.taobao.csp.cost.service.day;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.taobao.csp.cost.dao.CspTairDependDao;

/**
 * TAIR的总成本计算
 */
public class TairCostStrategy implements ICostStrategy {
	
	@Resource(name = "cspTairDependDao")
	private CspTairDependDao cspTairDependDao;
	
	private Map<String, Long> groupQps = new HashMap<String, Long>();
	
	private Map<String, Set<String>> groupMachines = new HashMap<String, Set<String>>();
	
	/*** group1$detail, 1000 ***/
	private Map<String, Long> depCall = new HashMap<String, Long>();

	/**
	 * 返回结果：
	 * 1.groupName-->被调用总次数
	 * 2.groupName$app--->调用次数
	 * 3.groupName的机器数
	 */
	@Override
	public void caculateCost(Date date,int tableId) {
		groupQps.clear();
		//<groupName,callSum>
		groupQps = cspTairDependDao.getTairProviderSummary(date);
		
		//<groupName$app,callSum>
		depCall.clear();
		for (String group : groupQps.keySet()) {
			Map<String, Long> dep = cspTairDependDao.getTairDepSummary(group,date);
			depCall.putAll(dep);
		}
		//<groupName,[ip,ip,ip]>
		groupMachines = cspTairDependDao.getTairMachines(date);
	}
	
	@Override
	public Map<String, Long> getCallNum() {
		return groupQps;
	}

	@Override
	public Map<String, Long> getCallDep() {
		return depCall;
	}
	
	@Override
	public int getMachineNum(String groupName) {
		int num = 0;
		if (groupMachines != null && groupMachines.containsKey(groupName) 
				&& groupMachines.get(groupName) != null) {
			num = groupMachines.get(groupName).size();
		}
		
		return num;
	}
	
	@Override
	public CostType getCostType() {
		return CostType.TAIR;
	}

	@Override
	public Set<String> getMachines(String groupName) {
		if (groupMachines != null 
				&& groupMachines.get(groupName) != null) {
			return groupMachines.get(groupName);
		}
		return null;
	}

}
