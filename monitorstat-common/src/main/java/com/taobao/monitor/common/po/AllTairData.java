package com.taobao.monitor.common.po;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 描述tair中同一个actionTpye\groupName下所有信息
 * @author denghaichuan.pt
 * @version 2011-9-7
 */
public class AllTairData implements Comparable<AllTairData> {
	
	private String str;
	
	private long sumData1 = 0;
	
	private long sumData2 = 0;
	
	
	// 机房+分组的map  <siteName, List<每条记录>>
	private Map<String, List<SingleTairData>> siteListMap = new HashMap<String, List<SingleTairData>>();
	
	// 机房+分组的map  <siteName, map<Namespace, 每条记录>>
	private Map<String, Map<String, SingleTairData>> siteNamespaceMap = new HashMap<String, Map<String,SingleTairData>>();
	
	// 此分组对应的机房名字的list
	private List<String> siteNameList = new ArrayList<String>();
	
	// 机房统计数据的map  <siteName, Object>
	private Map<String, SingleTairData> siteDataMap = new HashMap<String, SingleTairData>();
	
	public AllTairData() {
		
	}
	
	/**
	 * 将数据库中的一条记录加入map中
	 * @param po
	 */
	public void addTairData(TairInfoPo po) {
		
		Map<String, SingleTairData> namespaceMap = siteNamespaceMap.get(po.getSiteName());
		if (namespaceMap == null) {
			namespaceMap = new HashMap<String, SingleTairData>();
			siteNamespaceMap.put(po.getSiteName(), namespaceMap);
		}
		
		SingleTairData namespaceData = namespaceMap.get(po.getNameSpace());
		if (namespaceData == null) {
			namespaceData = new SingleTairData();
			namespaceData.setNamespace(po.getNameSpace());
			namespaceMap.put(po.getNameSpace(), namespaceData);
		}
		
		namespaceData.addData1(po.getData1());
		namespaceData.addData2(po.getData2());
		
		this.sumData1 += po.getData1();
		this.sumData2 += po.getData2();
		
	}
	
	
	/**
	 * 将Map<String, Map<String, SingleTairData>> 类型的
	 * 转化为Map<String, List<SingleTairData>>，并排序
	 */
	public void convertMap() {
		
		// 转为list
		for (Map.Entry<String, Map<String, SingleTairData>> entry : siteNamespaceMap.entrySet()) {
			List<SingleTairData> list = siteListMap.get(entry.getKey());
			if (list == null) {
				list = new ArrayList<SingleTairData>();
				siteListMap.put(entry.getKey(), list);
			}
			for(Map.Entry<String, SingleTairData> entry2 : entry.getValue().entrySet()) {
				list.add(entry2.getValue());
			}
		}
		
		// 排序
		for (Map.Entry<String, List<SingleTairData>> entry : siteListMap.entrySet()) {
			
			Collections.sort(entry.getValue());
			
		}
	}
	
	/**
	 * 对数据siteDataMap和siteNameList的填充
	 */
	public void createAllInfo() {
		for (Map.Entry<String, List<SingleTairData>> entry : siteListMap.entrySet()) {
			String siteName = entry.getKey();
			siteNameList.add(siteName);
			
			List<SingleTairData> singleDataMap = entry.getValue();
			SingleTairData sumSiteData = new SingleTairData();
			siteDataMap.put(siteName, sumSiteData);
			for (SingleTairData singleData : singleDataMap) {
				sumSiteData.addData1(singleData.getData1());
				sumSiteData.addData2(singleData.getData2());
			}
		}
	}
	
	public int getMaxRow() {
		int i = 0;
		for (Map.Entry<String, List<SingleTairData>> entry : siteListMap.entrySet()) {
			List<SingleTairData> rowInfoList = entry.getValue();
			if (rowInfoList.size() > i) {
				i = rowInfoList.size();
			}
		}
		return i;
	}
	
	public long getSumData1() {
		return sumData1;
	}

	public void setSumData1(long sumData1) {
		this.sumData1 = sumData1;
	}

	public long getSumData2() {
		return sumData2;
	}

	public void setSumData2(long sumData2) {
		this.sumData2 = sumData2;
	}

	public void addData1(long data) {
		sumData1 += data;
	}
	
	public void addData2(long data) {
		sumData2 += data;
	}


	public Map<String, List<SingleTairData>> getSiteListMap() {
		return siteListMap;
	}

	public Map<String, Map<String, SingleTairData>> getSiteNamespaceMap() {
		return siteNamespaceMap;
	}

	public List<String> getSiteNameList() {
		return siteNameList;
	}

	public Map<String, SingleTairData> getSiteDataMap() {
		return siteDataMap;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	@Override
	public int compareTo(AllTairData o) {
		if (sumData2 > o.getSumData2()) {
			return -1;
		} else if (sumData2 == o.getSumData2()) {
			return 0;
		} else {
			return 1;
		}
		
	}
	
	

}
