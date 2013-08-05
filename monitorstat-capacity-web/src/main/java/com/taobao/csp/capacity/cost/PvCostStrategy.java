package com.taobao.csp.capacity.cost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.csp.capacity.dao.CspDependencyDao;
import com.taobao.csp.capacity.util.SpringUtil;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;

public class PvCostStrategy implements ICostStrategy {
	
	private Map<String,  Map<String, Long>> urlQps = new HashMap<String,  Map<String, Long>>();
	
	/*** sell$detail, 1000  ***/
	private Map<String, Long> depCall = new HashMap<String, Long>();
	
	private CostType costType = CostType.PV;

	@Override
	public void caculateCost() {
		CspDependencyDao cspDependencyDao = (CspDependencyDao)SpringUtil.getBean("dependencyDao");
		
		urlQps.clear();
		urlQps = cspDependencyDao.getPvRequestSummary();
//		for (Map.Entry<String,  Map<String, Long>> entry1 : urlQps.entrySet()) {
//			String appName = entry1.getKey();
//			
//			Map<String, Long> originUrlNum = cspDependencyDao.getPvOriginSummary(appName);
//			caculateDepCall(appName, originUrlNum);
//		}	
	}
	
	@Override
	public Map<String, Long> getCallNum() {
		Map<String, Long> map = new HashMap<String, Long>();
		
		for (Map.Entry<String,  Map<String, Long>> entry1 : urlQps.entrySet()) {
			String appName = entry1.getKey();
			Map<String, Long> urlCalls = entry1.getValue();
			
			long callNum = 0l;
			for (Map.Entry<String, Long> entry2 : urlCalls.entrySet()) {
				long num = entry2.getValue();
				callNum += num;
			}
			map.put(appName, callNum);
		}
		
		return map;
	}
	
	@Override
	public Map<String, Long> getCallDep() {
		return depCall;
	}
	
	@Override
	public int getMachineNum(String appName) {
		int num = 0;
		List<HostPo> machines = CspSyncOpsHostInfos.findOnlineHostByOpsName(appName);
		num = machines.size();
		
		return num;
	}
	
	@Override
	public CostType getCostType() {
		return this.costType;
	}
	
//	private void caculateDepCall(String appName, Map<String, Long>  originUrlNum) {
//		for (Map.Entry<String, Long> entry : originUrlNum.entrySet()) {
//			String orginUrl = entry.getKey();
//			Long callNum = entry.getValue();
//			
//			String depApp = findAppByUrl(orginUrl);
//			if ( depApp != null) {
//				String prefix = appName + CostConstants.DEP_SEP + depApp;
//				depCall.put(prefix, callNum);
//			}	
//		}
//	}
	
//	private String findAppByUrl(String url) {
//		for (Map.Entry<String,  Map<String, Long>> entry1 : urlQps.entrySet()) {
//			String appName = entry1.getKey();
//			Set<String> urls = entry1.getValue().keySet();
//			
//			if (urls != null && urls.contains(url)) {
//				return appName;
//			}
//		}
//		
//		return null;
//	}

}
