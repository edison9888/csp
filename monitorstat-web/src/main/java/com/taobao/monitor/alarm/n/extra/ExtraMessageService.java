
package com.taobao.monitor.alarm.n.extra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.Service;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.ao.MonitorTimeAo;

/**
 * key ������key��һ�����������һ��key �澯����Ҫͬʱ��ʾ��֮������key��һ�������һ����Ϣ
 * @author xiaodu
 * @version 2011-3-4 ����11:01:06
 */
public class ExtraMessageService implements Service{
	
	private Map<Integer,String> map = new HashMap<Integer, String>();//��ǰ������������
	
	private ExtraMessageService(){
		init();
	}
	
	private static ExtraMessageService s = new ExtraMessageService();
	
	public static ExtraMessageService get(){
		return s;
	}
	

	@Override
	public void init() {
		map.put(175,"PV");// pv
		map.put(3113,"CPU");//cpu 
		map.put(944,"LOAD");//load 
		map.put(7621,"jvm�ڴ�");//jvm 
		map.put(176,"apache��Ӧʱ��");//rest
		
	}

	@Override
	public boolean lookup(AlarmContext context) {
		// ��̨ͳ�Ƶ������ֱ��return
		if (context.getSiteId() == -1) {
			return true;
		}
		
		int appId = context.getAppId();		
		int siteId = context.getSiteId();
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<Integer,String> k:map.entrySet()){
			int keyid = k.getKey();
			if(keyid == 176){
				Map<Date,Double> map = MonitorTimeAo.get().findKeyLimitRecentlyData(appId, keyid, siteId);
				if(map.size() >1){
					List<Date> dateList = new ArrayList<Date>();
					dateList.addAll(map.keySet());
					Collections.sort(dateList);				
					Date date = dateList.get(1);				
					Double d = map.get(date);	
					sb.append("�ϴ�Rest:"+Arith.div(d, 1000, 1)).append(" ");
				}
			}else if(keyid == 175){
				Map<Date,Double> map = MonitorTimeAo.get().findKeyLimitRecentlyData(appId, keyid, siteId);
				if(map.size() >1){
					List<Date> dateList = new ArrayList<Date>();
					dateList.addAll(map.keySet());
					Collections.sort(dateList);				
					Date date = dateList.get(1);				
					Double d = map.get(date);	
					sb.append("�ϴ�PV:"+d).append(" ");
				}
			}else{
				Map<Date,Double> map = MonitorTimeAo.get().findKeyLimitRecentlyData(appId, keyid, siteId);
				if(map.size() >0){
					List<Date> dateList = new ArrayList<Date>();
					dateList.addAll(map.keySet());
					Collections.sort(dateList);				
					Date date = dateList.get(0);				
					Double d = map.get(date);	
					sb.append(k.getValue()+":"+d).append(" ");
				}
			}
			
		}
		if(sb.length() >0){
			context.setRelationMessage(sb.toString());
		}
						
		return true;
	}

	@Override
	public void register(Object t) {
		
	}

	@Override
	public void unregister(Object t) {
		
	}
	
	

}
