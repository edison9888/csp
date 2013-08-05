
package com.taobao.monitor.baseline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.baseline.db.impl.MonitorDateDao;
import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;

/**
 * 
 * @author xiaodu
 * @version 2010-5-26 ÉÏÎç10:19:41
 */
public class DateBaseLine {
	
	private int cycle = 6;
	private MonitorDateDao monitorDateDao = null;
	
	
	
	public DateBaseLine(){
		monitorDateDao = new MonitorDateDao();
	}
	
	
	public void startup(){
		
		
		
		Map<String ,Map<String,KeyValueBaseLinePo>> appBaseLineMap = new HashMap<String, Map<String,KeyValueBaseLinePo>>();
		parseBaseLine(appBaseLineMap);
		parseSameDay(appBaseLineMap);
		parseYesterDay(appBaseLineMap);
		intoDbBaseTable(appBaseLineMap);
	}
	
	
	private void parseBaseLine(Map<String ,Map<String,KeyValueBaseLinePo>> appBaseLineMap){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar currentCalendar = Calendar.getInstance();
		try {
			currentCalendar.setTime(sdf.parse(sdf.format(new Date())));
		} catch (ParseException e) {
		}
		
		currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
		Date logDateTime = currentCalendar.getTime();
		
		for(int i=0;i<cycle;i++){
			currentCalendar.add(Calendar.DAY_OF_MONTH, -7);
			Date previousDate = currentCalendar.getTime();
			
			Map<String ,Map<String,KeyValueBaseLinePo>> appMap = monitorDateDao.findMonitorValueByDate(previousDate);
			
			for(Map.Entry<String, Map<String,KeyValueBaseLinePo>> appEntry:appMap.entrySet()){
				String appName = appEntry.getKey();
				Map<String,KeyValueBaseLinePo> keyMap = appEntry.getValue();
				Map<String,KeyValueBaseLinePo> keyValueMap = appBaseLineMap.get(appName);
				if(keyValueMap==null){
					keyValueMap = new HashMap<String, KeyValueBaseLinePo>();
					appBaseLineMap.put(appName, keyValueMap);					
				}
				
				for(Map.Entry<String, KeyValueBaseLinePo> keyEntry:keyMap.entrySet()){
					String keyName = keyEntry.getKey();
					KeyValueBaseLinePo po = keyEntry.getValue();					
					KeyValueBaseLinePo baselinePo = keyValueMap.get(keyName);
					if(baselinePo==null){
						baselinePo = new KeyValueBaseLinePo();
						keyValueMap.put(keyName, baselinePo);
						baselinePo.setAppId(po.getAppId());
						baselinePo.setKeyId(po.getKeyId());
						baselinePo.setCollectTime(logDateTime);
						baselinePo.setBaseLineValue(po.getValue());	
						baselinePo.setSumTimes(baselinePo.getSumTimes()+1);
					}else{
						baselinePo.setBaseLineValue(po.getValue()+baselinePo.getBaseLineValue());	
						baselinePo.setSumTimes(baselinePo.getSumTimes()+1);
					}
					
				}
			}
		}		
	}
	
	
	
	private void parseSameDay(Map<String ,Map<String,KeyValueBaseLinePo>> appBaseLineMap){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar currentCalendar = Calendar.getInstance();
		try {
			currentCalendar.setTime(sdf.parse(sdf.format(new Date())));
		} catch (ParseException e) {
		}
		
		currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
		Date logDateTime = currentCalendar.getTime();
		
		currentCalendar.add(Calendar.DAY_OF_MONTH, -7);
		Date previousDate = currentCalendar.getTime();
		
		Map<String ,Map<String,KeyValueBaseLinePo>> appMap = monitorDateDao.findMonitorValueByDate(previousDate);
		
		for(Map.Entry<String, Map<String,KeyValueBaseLinePo>> appEntry:appMap.entrySet()){
			String appName = appEntry.getKey();
			Map<String,KeyValueBaseLinePo> keyMap = appEntry.getValue();
			Map<String,KeyValueBaseLinePo> keyValueMap = appBaseLineMap.get(appName);
			if(keyValueMap==null){
				keyValueMap = new HashMap<String, KeyValueBaseLinePo>();
				appBaseLineMap.put(appName, keyValueMap);					
			}
			
			for(Map.Entry<String, KeyValueBaseLinePo> keyEntry:keyMap.entrySet()){
				String keyName = keyEntry.getKey();
				KeyValueBaseLinePo po = keyEntry.getValue();					
				KeyValueBaseLinePo baselinePo = keyValueMap.get(keyName);
				if(baselinePo==null){
					baselinePo = new KeyValueBaseLinePo();
					keyValueMap.put(keyName, baselinePo);
					baselinePo.setAppId(po.getAppId());
					baselinePo.setKeyId(po.getKeyId());
					baselinePo.setSameDayValue(po.getValue());
					baselinePo.setCollectTime(logDateTime);
				}else{
					baselinePo.setSameDayValue(po.getValue());	
				}				
			}
		}
	}
	
	private void parseYesterDay(Map<String ,Map<String,KeyValueBaseLinePo>> appBaseLineMap){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar currentCalendar = Calendar.getInstance();
		try {
			currentCalendar.setTime(sdf.parse(sdf.format(new Date())));
		} catch (ParseException e) {
		}
		
		currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
		Date logDateTime = currentCalendar.getTime();
		
		currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
		Date previousDate = currentCalendar.getTime();		
		
		Map<String ,Map<String,KeyValueBaseLinePo>> appMap = monitorDateDao.findMonitorValueByDate(previousDate);
		
		for(Map.Entry<String, Map<String,KeyValueBaseLinePo>> appEntry:appMap.entrySet()){
			String appName = appEntry.getKey();
			Map<String,KeyValueBaseLinePo> keyMap = appEntry.getValue();
			Map<String,KeyValueBaseLinePo> keyValueMap = appBaseLineMap.get(appName);
			if(keyValueMap==null){
				keyValueMap = new HashMap<String, KeyValueBaseLinePo>();
				appBaseLineMap.put(appName, keyValueMap);					
			}
			
			for(Map.Entry<String, KeyValueBaseLinePo> keyEntry:keyMap.entrySet()){
				String keyName = keyEntry.getKey();
				KeyValueBaseLinePo po = keyEntry.getValue();					
				KeyValueBaseLinePo baselinePo = keyValueMap.get(keyName);
				if(baselinePo==null){
					baselinePo = new KeyValueBaseLinePo();
					keyValueMap.put(keyName, baselinePo);
					baselinePo.setAppId(po.getAppId());
					baselinePo.setKeyId(po.getKeyId());
					baselinePo.setCollectTime(logDateTime);
					baselinePo.setYesterdayValue(po.getValue());	
				}else{
					baselinePo.setYesterdayValue(po.getValue());	
				}				
			}
		}
	}
	
	
	private void intoDbBaseTable(Map<String ,Map<String,KeyValueBaseLinePo>> appBaseLineMap){
		
		for(Map.Entry<String ,Map<String,KeyValueBaseLinePo>> appEntry:appBaseLineMap.entrySet()){
			
			Map<String,KeyValueBaseLinePo> keyMap = appEntry.getValue();
			
			for(Map.Entry<String, KeyValueBaseLinePo> poEntry:keyMap.entrySet()){
				
				KeyValueBaseLinePo po = poEntry.getValue();
				if(po.getSumTimes()>0){
					po.setBaseLineValue(po.getBaseLineValue()/po.getSumTimes());
				}				
				monitorDateDao.addMonitorDateBaseLine(po);
			}			
		}
		
		
		
	}
	
	
	

}
