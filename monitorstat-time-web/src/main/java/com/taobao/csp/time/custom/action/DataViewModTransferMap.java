package com.taobao.csp.time.custom.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.dataserver.PropConstants;

public class DataViewModTransferMap {
	private Map<String,String> keyMap = new ConcurrentHashMap<String,String>();
	private static DataViewModTransferMap transferMap = new DataViewModTransferMap();
	private DataViewModTransferMap(){
		keyMap.put("/app/detail/custom/show.do?method=querySingleHostRealTime","主机级别最近十分钟数据");
		keyMap.put("/app/detail/custom/show.do?method=queryRecentlySingleHostRealTime","主机级别最近一分钟的数据");
		keyMap.put("/app/detail/custom/show.do?method=queryChildHostRealTime","主机级别最近十分钟子Key数据");
		keyMap.put("/app/detail/custom/show.do?method=queryRecentlyChildHostRealTime","主机级别最近一分钟子Key数据");
		keyMap.put("/app/detail/custom/show.do?method=querySingleRealTime","应用级别最近十分钟数据");
		keyMap.put("/app/detail/custom/show.do?method=querySingleRealTimeLine","应用级别最近十分钟数据,时间排序");
		keyMap.put("/app/detail/custom/show.do?method=queryRecentlySingleRealTime","应用级别最近一分钟数据");
		keyMap.put("/app/detail/custom/show.do?method=queryChildRealTime","应用级别最近十分钟子Key数据");
		keyMap.put("/app/detail/custom/show.do?method=queryRecentlyChildRealTime","应用级别最近一分钟子Key数据");
		keyMap.put("/app/detail/custom/show.do?method=queryHistory", "历史数据");
		keyMap.put("poListTable", "表格形式以主属性排序");
		keyMap.put("poListLine", "线形图展现");
		keyMap.put("poListTop10", "表格形式以主属性排序top10");
		keyMap.put("mapPoTable", "表格形式以主属性排序");
		keyMap.put("mapPoTop10", "表格形式以主属性排序top10");
		keyMap.put("mapPoListTable", "表格形式");
		keyMap.put("historyLine","线形图");
	}
	public  static DataViewModTransferMap getSingle(){
		return transferMap;
	}
	public String get(String key){
		return keyMap.get(key);
	}
}
