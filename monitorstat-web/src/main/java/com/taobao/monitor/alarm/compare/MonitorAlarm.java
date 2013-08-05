package com.taobao.monitor.alarm.compare;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.taobao.monitor.web.ao.MonitorTimeAo;

public abstract class MonitorAlarm {
	
	protected BigDecimal alarmRatio = new BigDecimal("0.6");
	
	protected String alarmInfo;
	
	public BigDecimal getAlarmRatio() {
		return alarmRatio;
	}


	public void setAlarmRatio(BigDecimal alarmRatio) {
		this.alarmRatio = alarmRatio;
	}


	public String getAlarmInfo() {
		return alarmInfo;
	}


	public void setAlarmInfo(String alarmInfo) {
		this.alarmInfo = alarmInfo;
	}


	public abstract boolean needAlarm(long nowMilliTime, int appId, int keyId, BigDecimal nowData);

	/***
	 * 取对比时间点前一分钟的统计数据
	 * @param nowInMillis
	 * @param appId
	 * @param keyId
	 * @param dayBefore
	 * @param minuteBefore
	 * @param isLimit
	 * @return
	 */
	protected Map<String, BigDecimal> getCompareData(long nowInMillis, int appId, int keyId, int dayBefore, int minuteBefore, boolean isLimit) {
		// 获取基准时间点
		Date basicDate = MonitorAlarmHelper.getPreviousDate(nowInMillis, dayBefore, minuteBefore);
		Calendar basicCalander = Calendar.getInstance();
		basicCalander.setTime(basicDate);
		long basicMillis = basicCalander.getTimeInMillis();
		
		// 获取统计信息的时间段,时间间隔为2分钟
		Date compareDataStart = MonitorAlarmHelper.getPreviousDate(basicMillis, 0, -2);
		Date compareDataEnd = basicDate;
		
		return MonitorTimeAo.get().findToalInRangeDate(appId, keyId, compareDataStart, compareDataEnd, isLimit);
	}
	
	
	
	
}
