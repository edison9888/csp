
package com.taobao.monitor.alarm.n;


import java.util.Date;
import java.util.Map;

import com.taobao.monitor.alarm.n.event.EventService;
import com.taobao.monitor.alarm.n.extra.ExtraMessageService;
import com.taobao.monitor.alarm.n.filter.FilterService;
import com.taobao.monitor.alarm.n.key.KeyService;
import com.taobao.monitor.alarm.n.user.UserService;
import com.taobao.monitor.web.vo.AlarmDataPo;

/**
 * 
 * @author xiaodu
 * @version 2011-2-25 上午10:20:29
 */
public class ServiceFacade {
	
	private static ServiceFacade facade = new ServiceFacade();
	
	private ServiceFacade(){}
	
	public static ServiceFacade get(){
		return facade;
	}
	
	
	public AlarmContext lookup(AlarmDataPo po){
		
		try{
		
			AlarmContext context = createContext(po);
			
			if(context == null){
				return null;
			}
			if(!KeyService.get().lookup(context)){
				return null;
			}
			
			if(!FilterService.get().lookup(context)){
				return null;
			}
			
			if(!EventService.get().lookup(context)){
				return null;
			}		

			if(!ExtraMessageService.get().lookup(context)){
				return null;
			}
			
			if(!UserService.get().lookup(context)){
				return null;
			}
			//经过前面的过滤此处才是有真的报警消息
			return context;
		}catch (Exception e) {
			return null;
		}
	}
	
	
	
	
	private AlarmContext createContext(AlarmDataPo po){
//		
		AlarmContext context = new AlarmContext();
		context.setAppId(po.getAppId());
		context.setKeyId(Integer.parseInt(po.getKeyId()));
		
		context.setAppName(po.getAppName());
		context.setKeyName(po.getKeyName());
		
		context.setKeyType(po.getKeyType());
		context.setSiteId(po.getSiteId());
		context.setSiteName(po.getSite());
		context.setValueMap(po.getLimitDataMap());
		
		// 该策略po.getLimitDataMap()为null,将在keyservice里面设置
		if (po.getAlarmType() != null && po.getAlarmType().trim().equals("3")) {
			return context;
		}
		
		Date maxDate = getMaxDate(po.getLimitDataMap());
		if(maxDate == null){
			return null;
		}
		context.setRecentlyDate(maxDate);
		context.setRecentlyValue(po.getLimitDataMap().get(maxDate));
//		
		return context;		
	}
	
	
	/**
	 * 将告警值 按照时间降序 排序列表
	 * @param dataMap
	 * @return
	 */
	protected Date getMaxDate(Map<Date, String> dataMap){
		
		Date maxDate = null;
		
		for(Date date:dataMap.keySet()){
			if(maxDate ==null){
				maxDate = date;
			}else{
				if(date.getTime() > maxDate.getTime()){
					maxDate = date;
				}
			}
		}
		return maxDate;
	}


}
