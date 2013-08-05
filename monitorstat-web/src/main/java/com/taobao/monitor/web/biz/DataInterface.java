package com.taobao.monitor.web.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.web.ao.MonitorDayAo;
import com.taobao.monitor.web.vo.MonitorVo;

public class DataInterface {
	
	
	public static String getReuqestUrl(String appName, String colllectTime) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		AppInfoPo appInfo = AppInfoAo.get().getAppInfoByAppName(appName);
		
		if (appInfo == null) {
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		try {
			Map<Integer, MonitorVo> map = MonitorDayAo.get().findMonitorCountMapByDate(appInfo.getAppDayId(), colllectTime);
			
			MonitorVo vo = map.get(appInfo.getAppDayId());
			
			if(vo.getApachePv()!=null){
				Map<String, Long> reqUrlPvMap = new HashMap<String, Long>();
				if(vo.getReqUrlPvMap()!= null)
					reqUrlPvMap = vo.getReqUrlPvMap();
				
				Comparator<Map.Entry<String, Long>> compare = new Comparator<Map.Entry<String, Long>>(){
					public int compare(Map.Entry<String, Long> e1,Map.Entry<String, Long> e2){
						return (int)(e2.getValue() - e1.getValue());
					}
				};
				
				ArrayList<Map.Entry<String, Long>>reqUrlPvList = new ArrayList<Map.Entry<String, Long>>(reqUrlPvMap.entrySet());
				Collections.sort(reqUrlPvList,compare);
				
				for(Integer i=0; i<reqUrlPvList.size();i++){
						Map.Entry<String, Long> entry = reqUrlPvList.get(i);
						String key = entry.getKey();
						String value = entry.getValue().toString();
						
						jsonMap.put(key, value);
				}
			}
		} catch (Exception e) {
			return JSONObject.fromObject(jsonMap).toString();
		}
		
		return JSONObject.fromObject(jsonMap).toString();
	}

}
