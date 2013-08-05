package com.taobao.csp.capacity.cost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.csp.capacity.constant.CostConstants;
import com.taobao.csp.capacity.dao.CspDependencyDao;
import com.taobao.csp.capacity.po.HsfConsumerSummaryPo;
import com.taobao.csp.capacity.po.HsfProviderSummaryPo;
import com.taobao.csp.capacity.util.SpringUtil;

public class HsfCostStrategy implements ICostStrategy {
	
	/***  detail  100000 ***/
	private Map<String, Long> appQps = new HashMap<String, Long>();
	
	private Map<String, Set<String>> appMachines = new HashMap<String, Set<String>>();
	
	/*** tradeplatform$detail, 1000  ***/
	private Map<String, Long> depCall = new HashMap<String, Long>();
	
	private CostType costType = CostType.HSF;

	@Override
	public void caculateCost() {
		CspDependencyDao cspDependencyDao = (CspDependencyDao)SpringUtil.getBean("dependencyDao");
		
		appQps.clear();
		List<HsfProviderSummaryPo> providerList = cspDependencyDao.getHsfProviderSummaryPos();
		for (HsfProviderSummaryPo po : providerList) {
			String appName = po.getProviderName();
			long callSum = po.getCallSum();
			appQps.put(appName, callSum);
			
			List<HsfConsumerSummaryPo> consumerList = cspDependencyDao.getHsfConsumerSummaryPos(appName);
			caculateDepCall(appName, consumerList);
			
		}
		
		appMachines = cspDependencyDao.getHsfProviderMachineNums();
	}
	
	@Override
	public Map<String, Long> getCallNum() {
		return appQps;
	}

	@Override
	public Map<String, Long> getCallDep() {
		return depCall;
	}
	
	@Override
	public int getMachineNum(String appName) {
		int num = 0;
		if (appMachines != null && appMachines.containsKey(appName) && appMachines.get(appName) != null) {
			num = appMachines.get(appName).size();
		}
		
		return num;
	}
	
	
	@Override
	public CostType getCostType() {
		return this.costType;
	}
	
	private void caculateDepCall(String appName, List<HsfConsumerSummaryPo> list) {
		for (HsfConsumerSummaryPo po : list) {
			String consumeName = po.getConsumeName();
			String prefix = appName + CostConstants.DEP_SEP + consumeName;
			depCall.put(prefix, po.getCallSum());
		}
	}
}
