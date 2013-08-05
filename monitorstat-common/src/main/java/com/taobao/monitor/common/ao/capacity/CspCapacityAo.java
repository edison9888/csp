package com.taobao.monitor.common.ao.capacity;

import java.util.Map;

import com.taobao.monitor.common.db.impl.capacity.CspCapacityDao;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityRankingPo;

public class CspCapacityAo {
	
	private static  CspCapacityAo ao = new CspCapacityAo();
	
	private CspCapacityDao dao = new CspCapacityDao();
	
	public static CspCapacityAo get() {
		return ao;
	}
	
	public String findCapacityGroups(int appId) {
		return dao.findCapacityGroups(appId);
	}
	
	public CapacityRankingPo findCapacityLatestRankingPo(int appId) {
		return dao.findCapacityLatestRankingPo(appId);
	}
	
	public Map<String, String> findLatestCapacityCostByApp(String appName) {
		return dao.findLatestCapacityCostByApp(appName);
	}
}
