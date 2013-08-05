package com.taobao.monitor.time.ao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.monitor.time.po.AmlineFlash;

public class HistoryHSFQueryAo {
	private static final Logger logger =  Logger.getLogger(HistoryHSFQueryAo.class);
	private static HistoryHSFQueryAo ao = new HistoryHSFQueryAo();
	private HistoryHSFQueryAo(){
	}
	
	public static HistoryHSFQueryAo get(){
		return ao;
	}
	
	public String queryFlashData(String appName, String keyName){
		AmlineFlash am = QueryHistoryKeyByTime(appName, keyName);
		return am.getAmline();
	}
	
	private AmlineFlash QueryHistoryKeyByTime(String appName,String keyName){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar time = Calendar.getInstance();
		Date toDay = time.getTime();
		
		time.add(Calendar.DAY_OF_YEAR, -1);
		Date yesterDay = time.getTime();
		
		Map<Date,String> todayLineMap = QueryHistoryUtil.querySingle(appName, keyName, PropConstants.E_TIMES, toDay);
		Map<Date,String> yesterdayLineMap = QueryHistoryUtil.querySingle(appName, keyName, PropConstants.E_TIMES, yesterDay);
		
		AmlineFlash amFlash = new AmlineFlash();
		amFlash.addTitle("请求量"+"["+sdf.format(toDay)+"]");
		try{
			for(Map.Entry<Date,String> entry : todayLineMap.entrySet()){
				Date d = entry.getKey();
				double v = Double.parseDouble(entry.getValue());
				amFlash.addValue(d.getTime(), v);
			}
		} catch(Exception e){
			logger.warn("TodayLineMap ["+sdf.format(toDay)+"] has invalid MapValue", e);
		}
		
		amFlash.addTitle("请求量"+"["+sdf.format(yesterDay)+"]");
		try{
			for(Map.Entry<Date,String> entry : yesterdayLineMap.entrySet()){
				Date d = entry.getKey();
				double v = Double.parseDouble(entry.getValue());
				amFlash.addValue(d.getTime(), v);
			}
		} catch(Exception e){
			logger.warn("YesterdayLineMap ["+sdf.format(yesterDay)+"] has invalid MapValue", e);
		}
		return amFlash;
	}
}
