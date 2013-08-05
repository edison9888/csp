package com.taobao.monitor.alarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.web.ao.MonitorTimeAo;
import com.taobao.monitor.web.vo.AlarmDataPo;

public class AlarmHelper {
	
	/***
	 * 从原有数据中提取报警策略为自动的数据
	 * @param origin
	 * @return
	 */
	public static List<AlarmDataPo> transferToUnionSite(List<AlarmDataPo> origin) {
		
		Map<String, AlarmDataPo> unionMap = new HashMap<String, AlarmDataPo>();
		List<AlarmDataPo> current = new ArrayList<AlarmDataPo>();
		
		for (AlarmDataPo alarmPo : origin) {
			if (alarmPo.getAlarmType() != null && alarmPo.getAlarmType().trim().equals("3")) {
				String key = alarmPo.getAppId() + "_" + alarmPo.getKeyId();
				if (!unionMap.containsKey(key)) {
					// site无关 后面有用到site 先取第一台的site防止后续出问题
					alarmPo.setSiteId(-1);
					alarmPo.setSite("");
					// 该报警策略用不上limitDataMap
					alarmPo.setLimitDataMap(null);
					unionMap.put(key, alarmPo);
				}
				
			}
		}
		
		current.addAll(unionMap.values());
		return current;
		
	}
	
	public static Date getLatestDateInLimite(int appId, int keyId) {
		return MonitorTimeAo.get().findLatestDateInLimit(appId, keyId);
	}
	
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
