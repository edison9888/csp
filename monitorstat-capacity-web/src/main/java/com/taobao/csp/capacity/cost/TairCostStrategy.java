package com.taobao.csp.capacity.cost;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.taobao.csp.capacity.dao.CspDependencyDao;
import com.taobao.csp.capacity.util.SpringUtil;

public class TairCostStrategy implements ICostStrategy {
	
	private Map<String, Long> groupQps = new HashMap<String, Long>();
	
	private Map<String, Set<String>> groupMachines = new HashMap<String, Set<String>>();
	
	/*** group1$detail, 1000 ***/
	private Map<String, Long> depCall = new HashMap<String, Long>();
	
	private CostType costType = CostType.TAIR;

	@Override
	public void caculateCost() {
		CspDependencyDao cspDependencyDao = (CspDependencyDao)SpringUtil.getBean("dependencyDao");
		
		groupQps.clear();
		groupQps = cspDependencyDao.getTairProviderSummary();
		
		depCall.clear();
		for (String group : groupQps.keySet()) {
			Map<String, Long> dep = cspDependencyDao.getTairDepSummary(group);
			depCall.putAll(dep);
		}
		
		groupMachines = cspDependencyDao.getTairMachines();
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
		if (groupMachines != null && groupMachines.containsKey(groupName) && groupMachines.get(groupName) != null) {
			num = groupMachines.get(groupName).size();
		}
		
		return num;
	}
	
	@Override
	public CostType getCostType() {
		return this.costType;
	}

}
