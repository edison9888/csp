package com.taobao.csp.cost.service.day;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.taobao.csp.cost.dao.CspWebDependDao;

/**
 * 计算应用被请求的PV数
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-19
 */
public class PvCostStrategy {

	@Resource(name = "cspWebDependDao")
	private CspWebDependDao cspWebDependDao;
	
	//<app,num>
	public Map<String, Long> caculateCost(Date date) {
		
		Map<String, Long> depCall = new HashMap<String, Long>();
		
		//<app,[[url,num],[url,num]]>
		Map<String,  Map<String, Long>>  urlQps = cspWebDependDao.getPvRequestSummary(date);
		for (Map.Entry<String,  Map<String, Long>> entry1 : urlQps.entrySet()) {
			String appName = entry1.getKey();
			
			for(Map.Entry<String, Long> urlNum:entry1.getValue().entrySet()){
				if(depCall.containsKey(appName)){
					long preNum=depCall.get(appName);
					depCall.put(appName, preNum+urlNum.getValue());
				}else{
					depCall.put(appName, urlNum.getValue());
				}
			}
		}	
		return depCall;
	}
	
	public CostType getCostType() {
		return CostType.PV;
	}
	

}
