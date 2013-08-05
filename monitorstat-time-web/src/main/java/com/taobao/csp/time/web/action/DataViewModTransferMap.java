package com.taobao.csp.time.web.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.time.util.Urls;

public class DataViewModTransferMap {
	private Map<String,String> keyMap = new ConcurrentHashMap<String,String>();
	private static DataViewModTransferMap transferMap = new DataViewModTransferMap();
	private DataViewModTransferMap(){
		keyMap.put(Urls.querySingleHostRealTime,"主机级别最近十分钟数据");
		keyMap.put(Urls.queryRecentlySingleHostRealTime,"主机级别最近一分钟的数据");
		keyMap.put(Urls.queryChildHostRealTime,"主机级别最近十分钟子Key数据");
		keyMap.put(Urls.queryRecentlyChildHostRealTime,"主机级别最近一分钟子Key数据");
		keyMap.put(Urls.querySingleRealTime,"应用级别最近十分钟数据");
		keyMap.put(Urls.queryCustomerBusinessLog,"应用级别最近十分钟业务日志数据");
		keyMap.put(Urls.querySingleRealTimeLine,"应用级别最近十分钟数据,时间排序");
		keyMap.put(Urls.queryRecentlySingleRealTime,"应用级别最近一分钟数据");
		keyMap.put(Urls.queryChildRealTime,"应用级别最近十分钟子Key数据");
		keyMap.put(Urls.queryRecentlyChildRealTime,"应用级别最近一分钟子Key数据");
		
		keyMap.put(Urls.queryHistory, "历史数据");
		keyMap.put(Urls.poListTable, "表格形式以主属性排序");
		keyMap.put(Urls.poListLine, "线形图展现");
		keyMap.put(Urls.poListTop10, "表格形式以主属性排序top10");
		keyMap.put(Urls.mapPoTable, "表格形式以主属性排序");
		keyMap.put(Urls.mapPoTop10, "表格形式以主属性排序top10");
		keyMap.put(Urls.mapPoListTable, "表格形式");
		keyMap.put(Urls.historyLine,"线形图");
		keyMap.put(Urls.mapPoListTable2, "表格形式");
	}
	public  static DataViewModTransferMap getSingle(){
		return transferMap;
	}
	public String get(String key){
		return keyMap.get(key);
	}
}
