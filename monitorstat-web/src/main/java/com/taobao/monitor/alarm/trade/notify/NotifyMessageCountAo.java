package com.taobao.monitor.alarm.trade.notify;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.time.util.TimeUtil;
import com.taobao.util.CollectionUtil;

public class NotifyMessageCountAo {
	private static final Logger logger = Logger.getLogger(NotifyMessageCountAo.class);
	
	private static NotifyMessageCountAo ao = new NotifyMessageCountAo();
	public static NotifyMessageCountAo get(){
		return ao;
	}
	
	
	public void checkNotifyMesageCountRecPro(){
		Map<Date, Integer> tradeplatformMap = new HashMap<Date, Integer>();
		String appNameTp = "tradeplatform";
		String keyTp = "tp交易相关总量`P2-Notify";
		
		Map<Date, Integer> tradelogsMap = new HashMap<Date, Integer>();
		String appNameTradelogs = "tradelogs";
		String keyTradelogs = "TotalProcessor"; 
		
		try {
			//获取tp发出的消息总量
			Map<String, Map<String, Object>> cspQueryTpMap = QueryUtil.querySingleRealTime(appNameTp, keyTp);
			for (Map.Entry<String, Map<String, Object>> entry : cspQueryTpMap
					.entrySet()) {
				if (entry.getValue() == null) 	continue;
				Map<String, Object> tMap = entry.getValue();
				tradeplatformMap.put(new Date(Long.parseLong(entry.getKey())), (Integer) tMap.get("success"));
			}
			if(tradeplatformMap.size() ==0){
				return;
			}
			//获取tradelogs接收的消息总量
			Map<String, Map<String, Object>> cspQueryTradelogsMap = QueryUtil.querySingleRealTime(appNameTradelogs, keyTradelogs);
			for (Map.Entry<String, Map<String, Object>> entry : cspQueryTradelogsMap
							.entrySet()) {
				if (entry.getValue() == null) continue;
				Map<String, Object> tMap = entry.getValue();
				tradelogsMap.put(new Date(Long.parseLong(entry.getKey())), (Integer) tMap.get("count"));
			}
			if(tradelogsMap.size() ==0){
				return;
			}
			
			List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
			
			for(Map.Entry<Date, Integer> entry:tradeplatformMap.entrySet()){
				Date dateFromTp = entry.getKey();
				Date dateFromTradelogsNearTp = TimeUtil.getTheMostNearTime(dateFromTp,
						tradelogsMap.keySet());
				if(dateFromTradelogsNearTp == null) continue;
				int countFromTradelogs = tradelogsMap.get(dateFromTradelogsNearTp).intValue();
				logger.warn("compare notify message count,dateFromTp=" + dateFromTp +",countFromTp=" + entry.getValue() + "," +
						"dateFromTradelogsNearTp=" + dateFromTradelogsNearTp + ",countFromTradelogs=" + countFromTradelogs);
				//比较比例，tp消息的发出总量与tradelogs的接收量，相差不大于20%
				float proportion = BigDecimal.valueOf(Math.abs(entry.getValue() - countFromTradelogs)).
				divide(BigDecimal.valueOf(entry.getValue().intValue()),0,BigDecimal.ROUND_HALF_UP).floatValue();
				if(proportion > 0.2f){
					//插入一条告警记录
					CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
					po.setMode_name("阀值");
					po.setKey_scope("APP");
					po.setApp_name("tradeplatform");
					po.setKey_name("tp的notify消息总量");
					po.setProperty_name("count");
					po.setAlarm_cause("tp发出消息超过接收消息的120%");
					po.setAlarm_time(new Timestamp(dateFromTp.getTime()));
					po.setAlarm_value("tp=" + entry.getValue() + ",logs=" + countFromTradelogs);
					po.setIp("172.24.168.111");  //随意写的一台tp机器的ip
					list.add(po);
				}
			}
			if(CollectionUtil.isNotEmpty(list)){
				CspTimeKeyAlarmRecordAo.get().insert(list);
			}
					
			}catch (Exception e) {
				logger.error("checkNotifyMesageCountRecPro 分析出错",e);
			}
		}
	

}
