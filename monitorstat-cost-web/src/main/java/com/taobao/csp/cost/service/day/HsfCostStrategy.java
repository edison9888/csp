package com.taobao.csp.cost.service.day;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.taobao.csp.cost.dao.CspHsfDependDao;
import com.taobao.csp.cost.po.HsfConsumerSummaryPo;
import com.taobao.csp.cost.util.CostConstants;

/**
 * HSF���ܳɱ�����
 */
public class HsfCostStrategy {
	
	@Resource(name = "cspHsfDependDao")
	private CspHsfDependDao cspHsfDependDao;
	Map<String, Long> appQps=new HashMap<String, Long>(4000);
	Map<String, Map<String,Long>> consumerProMap= new HashMap<String,Map<String,Long>>(4000);
	
	/**
	 * ���أ�
	 * 1.appQps <provider,callSum>					     �ṩ�ĵ��ô���
	 * 2.consumerProMap <consumer,<provider,callSum>> ����ÿ���Ĵ���
	 * @param date
	 */
	public void caculateCost(Date date) {
		appQps.clear();
		consumerProMap.clear();
	
		List<HsfConsumerSummaryPo> depCall = 
				cspHsfDependDao.getHsfConsumerSummaryPos(date);
		
		//����provider�ṩ���ܵ��ô���
		//�Լ�consumer���ø���provider�Ĵ���
		for (HsfConsumerSummaryPo po : depCall) {
			String consumerName = po.getConsumeName();
			String providerName = po.getProviderName();
			
			//<provider,callSum>app�Լ��ṩ�ĵ��ô���
			if(!appQps.containsKey(providerName)){
				appQps.put(providerName, po.getCallSum());
			}else{
				appQps.put(providerName, appQps.get(providerName)+ po.getCallSum());
			}
			
			//<consumer,<provider,callSum>>�Լ��ṩ�ĵ��ô���
			if(consumerProMap.containsKey(consumerName)){
				//providerName
				if(consumerProMap.get(consumerName).containsKey(providerName)){
					long preCount=consumerProMap.get(consumerName).get(providerName);
					consumerProMap.get(consumerName)
						.put(providerName, preCount+ po.getCallSum());
				}else{
					consumerProMap.get(consumerName).put(providerName, po.getCallSum());
				}
				
			}else{
				Map<String,Long> callProvider=new HashMap<String,Long>();
				callProvider.put(providerName, po.getCallSum());
				
				consumerProMap.put(consumerName, callProvider);
			}
		}
		
		
		
	}
	
	public Map<String, Long> getCallNum() {
		return appQps;
	}

	public Map<String, Map<String,Long>> getCallDep() {
		return consumerProMap;
	}
	
	
	public CostType getCostType() {
		return CostType.HSF;
	}
	
}
