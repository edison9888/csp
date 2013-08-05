package com.taobao.monitor.web.ao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.taobao.monitor.web.core.dao.impl.MonitorTairDao;
import com.taobao.monitor.web.tair.AllTairData;
import com.taobao.monitor.web.tair.TairInfoPo;
import com.taobao.monitor.web.tair.TairNamespacePo;
import com.taobao.monitor.web.tair.TairSumData;

/**
 * 
 * @author denghaichuan.pt
 * @version 2011-9-6
 */
public class MonitorTairAo {
	private static final Logger logger =  Logger.getLogger(MonitorTairAo.class);

	private static MonitorTairAo  ao = new MonitorTairAo();
	private MonitorTairDao dao = new MonitorTairDao();
	
	private MonitorTairAo(){
	}

	public static  MonitorTairAo getInstance(){
		return ao;
	}
	
	
	/**
	 * ��ѯӦ�ö�Ӧ��action������
	 * @param appId
	 * @param date
	 * @return
	 */
	public List<String> findGroupList(String date) {
		try {
			return dao.findGroupList(date);
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 * ��ѯ��Ӧ�����е�tair����Ϣ
	 * @param appId
	 * @param date
	 * @return
	 */
	public Map<String, Map<String, AllTairData>> findAppTairList(String opsName, String date) {
		
		// ��Ҫ���ص�map
		// map<actionType, map<groupName, ÿ����Ϣ>
		Map<String, Map<String, AllTairData>> tairMap = new TreeMap<String, Map<String, AllTairData>>();
		
		// ���ݿ��е�ÿ����¼
		List<TairInfoPo> tairList = new ArrayList<TairInfoPo>();
		tairList = dao.findAllTairInfo(opsName, date);
		
		// ���tairMap
		for (TairInfoPo po : tairList) {
			String actionType = po.getActionType();
			
			Map<String, AllTairData> allTairDateMap = tairMap.get(actionType);
			if (allTairDateMap == null) {
				allTairDateMap = new HashMap<String, AllTairData>();
				tairMap.put(actionType, allTairDateMap);
			}
			
			AllTairData allTairDate = allTairDateMap.get(po.getGroupName());
			if (allTairDate == null) {
				allTairDate = new AllTairData();
				allTairDateMap.put(po.getGroupName(), allTairDate);
				
			}
			
			allTairDate.addTairData(po);
		}
		
		// ת��AllTairData�е�����
		for(Map.Entry<String, Map<String, AllTairData>> entry : tairMap.entrySet()) {
			for (Map.Entry<String, AllTairData> entry1 : entry.getValue().entrySet()) {
				entry1.getValue().convertMap();
				entry1.getValue().createAllInfo();
			}
		}
		return tairMap;
	}
	
	/**
	 * 
	 * ����namespace����ȡtair���ݿ��е���Ϣ
	 * @return map<ip, ÿ������>
	 */
	public Map<String, List<TairInfoPo>> findTairInfoByNamespace(String appName, String groupName, String namespace, String date) {
		Map<String, List<TairInfoPo>> ipTairMap = new HashMap<String, List<TairInfoPo>>();
		
		List<TairInfoPo> tairList = dao.findTairInfoByNamespace(appName, groupName, namespace, date);
		for (TairInfoPo po : tairList) {
			List<TairInfoPo> ipTairList = ipTairMap.get(po.getHostIp());
			
			if (ipTairList == null) {
				ipTairList = new ArrayList<TairInfoPo>();
				ipTairMap.put(po.getHostIp(), ipTairList);
			}
			
			ipTairList.add(po);
		}
		
		return ipTairMap;
	}
	
	/**
	 * 
	 * ����actionType����ȡtair���ݿ��е���Ϣ
	 * @return
	 */
	public List<AllTairData> findTairInfoByAction(String actionType, String groupName, String date) {
		
		Map<String, AllTairData> appTairDataMap = new HashMap<String, AllTairData>();
		
		List<AllTairData> appTairDataList = new ArrayList<AllTairData>();
		
		List<TairInfoPo> tairList = dao.findTairInfoByAction(actionType, groupName, date);
		
		for (TairInfoPo po : tairList) {
			String appName = po.getAppName();
			AllTairData appTair = appTairDataMap.get(appName);
			if (appTair == null) {
				appTair = new AllTairData();
				appTair.setStr(appName);
				appTairDataMap.put(appName, appTair);
			}
			appTair.addData1(po.getData1());
			appTair.addData2(po.getData2());
		}
		
		for (Map.Entry<String, AllTairData> entry : appTairDataMap.entrySet()) {
			appTairDataList.add(entry.getValue());
		}
		
		Collections.sort(appTairDataList);
		
		if (appTairDataList.size() > 10) {
			appTairDataList = appTairDataList.subList(0, 9);
		}
		
		return appTairDataList;
	}
	
	/**
	 * ���ڲ���uic�ĵ���ǰ20  
	 * @param actionType
	 * @param date
	 * @return
	 */
	
	public Map<String, Long> findTairInfoTop(String actionType, String date, int top) {
		return dao.findTairInfoTop(actionType, date, top);
	}
	
	/**
	 * ��tair��groupNameΪ��λ�ҵ�����tair��Ӧ�����
	 * @param groupName
	 * @param date
	 * @return
	 */
	public Map<String, TairSumData> findTairProviderTopApp(String groupName, String date) {
		
		Map<String, TairSumData> appTairDataMap = new HashMap<String, TairSumData>();
		
		List<TairNamespacePo> tairNamespaceList = dao.findTairProviderSummaryList(groupName, date);
		
		for (TairNamespacePo po : tairNamespaceList) {
			String appName = po.getAppName();
			TairSumData appTairData = appTairDataMap.get(appName);
			if (appTairData == null) {
				appTairData = new TairSumData();
				appTairData.setAppName(appName);
				appTairDataMap.put(appName, appTairData);
			}
			
			appTairData.addAppCallSum(po.getCallSumNum());
			appTairData.calculateRushQps(po.getRushQps());
			appTairData.calculateRushRt(po.getRushRt());
			
			appTairData.appTairNamespaceList(po);
		}
		
		return appTairDataMap;
	}
	
	/**
	 * �����ĳӦ��һ��ʱ���ڵ�ֵ
	 * @version 2012-4-9
	 * @param groupName
	 * @param appName
	 * @param startDate
	 * @param endDate
	 * @return map<time, TairSumData>
	 */
	public Map<String, TairSumData> findTairProviderAppChart(String groupName, String appName, String startDate, String endDate) {
		Map<String, TairSumData> timeTairDateMap = new HashMap<String, TairSumData>();
		List<TairNamespacePo> tairNamespaceList = dao.findTairProviderChartByAppName(groupName, appName, startDate, endDate);
		for (TairNamespacePo po : tairNamespaceList) {
			String collctTime = po.getCollectTime();
			TairSumData appTairData = timeTairDateMap.get(collctTime);
			if (appTairData == null) {
				appTairData = new TairSumData();
				
				timeTairDateMap.put(collctTime, appTairData);
			}
			
			appTairData.addAppCallSum(po.getCallSumNum());
			appTairData.calculateRushQps(po.getRushQps());
			appTairData.calculateRushRt(po.getRushRt());
			
			appTairData.appTairNamespaceList(po);
		}
		return timeTairDateMap;
	}
}
