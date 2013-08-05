
package com.taobao.monitor.web.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.web.ao.MonitorDayAo;
import com.taobao.monitor.web.weekreport.InterfaceWaveManage;

/**
 * 
 * @author xiaodu
 * @version 2011-2-14 ÏÂÎç01:23:06
 */
public class WeekReportCache {
	
	private static WeekReportCache cache = new WeekReportCache();
	
	public static WeekReportCache get(){
		return cache;
	}
	
	Map<Integer ,InterfaceWaveManage> map = new HashMap<Integer, InterfaceWaveManage>();
	
	
	public InterfaceWaveManage getInterfaceWaveManage(Integer appId,String appName,Date date){
		
		InterfaceWaveManage i = map.get(appId);
		if(i==null){
			i = new InterfaceWaveManage(date,appName,appId);
			map.put(appId, i);
		}
		
		if(!i.currentDay.equals(date)){
			i = new InterfaceWaveManage(date,appName,appId);
			map.put(appId, i);
		}
		return i;
	}
	
	
	public void reset(){
		Date c = new Date();
		List<AppInfoPo> appList = MonitorDayAo.get().findAllApp();
		
		for(AppInfoPo po:appList){
			getInterfaceWaveManage(po.getAppId(),po.getAppName(),new Date(c.getYear(),c.getMonth(),c.getDay()));
			
			
		}
		
		
	}

}
