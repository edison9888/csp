package com.taobao.monitor.alarm.compare;

import java.math.BigDecimal;
import java.util.Map;


public class MinituMonitorAlarm extends MonitorAlarm {

	@Override
	public boolean needAlarm(long nowMilliTime, int appId, int keyId, BigDecimal nowData) {
		// 基准时间为当前时间的两分钟之前
		Map<String, BigDecimal> mapData = getCompareData(nowMilliTime, appId, keyId, 0, -2, true);
		BigDecimal historyData = MonitorAlarmHelper.getAverageData(mapData);
		
		// 防止除0中断，线上环境应该不会出现
		if(historyData.intValue() == 0) {
			historyData = new BigDecimal("1");
		}
		
		boolean needAlarm = nowData.divide(historyData, 2, BigDecimal.ROUND_HALF_UP).compareTo(getAlarmRatio()) < 0 ? true : false;
		
		if (needAlarm) {
			setAlarmInfo("当前两分中内统计数据为:" + nowData + ",两分钟前的统计数据为:" + historyData + ". 比值低于预警比例:" + getAlarmRatio());
		}
		
		return needAlarm;
	}


}
