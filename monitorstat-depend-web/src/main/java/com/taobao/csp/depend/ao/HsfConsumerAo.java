package com.taobao.csp.depend.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.dao.CspAppHsfDependConsumeDao;
import com.taobao.csp.depend.po.hsf.AppConsumerSummary;

public class HsfConsumerAo {
	private static final Logger logger =  Logger.getLogger(HsfConsumerAo.class);
	
	private static HsfConsumerAo ao = new HsfConsumerAo();
	private HsfConsumerAo(){}
	public static HsfConsumerAo get() {
		return ao;
	}
	
	private CspAppHsfDependConsumeDao dao = new CspAppHsfDependConsumeDao();

	/*************************获取csp_app_dep_hsf_consume_summary相关的信息*******************************************************/

	public List<AppConsumerSummary> getHsfSummaryInfoListMeDepend(String collect_time, String appName){
		return dao.getHsfSummaryInfoListMeDepend(collect_time, appName);
	}
	
	public List<AppConsumerSummary> getHsfSummaryInfoListDependMe(String collect_time, String appName){
		return dao.getHsfSummaryInfoListMeDepend(collect_time, appName);
	}
	
	public Map<String,AppConsumerSummary> changeListToMap(List<AppConsumerSummary> list) {
		Map<String,AppConsumerSummary> map = new HashMap<String,AppConsumerSummary>();
		if(list == null || list.size() == 0)
			return map;
		for(AppConsumerSummary info : list) {
			map.put(info.getOpsName(), info);
		}
		return map;
	}
}
