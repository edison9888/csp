package com.taobao.monitor.alarm.compare;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MonitorAlarmHelper {
	
	public static Date getPreviousDate(long nowInMillis, int dayBefore, int miniteBefore) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(nowInMillis));
		if (dayBefore != 0) {
			calendar.add(Calendar.DAY_OF_MONTH, dayBefore);
		}
        if (miniteBefore != 0) {
    		calendar.add(Calendar.MINUTE, miniteBefore);
        }
		return calendar.getTime();
	}
	
	public static BigDecimal getAverageData(Map<String, BigDecimal> mapData) {
		BigDecimal total = new BigDecimal("0");
		int count = 0;
		if (mapData == null || mapData.size() == 0) {
			return total;
		}
		
		for(String key : mapData.keySet()) {
			BigDecimal value = mapData.get(key);
			total = total.add(value);
			count++;
		}
		
		return total.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);	
	}
	
}
