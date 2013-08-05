/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用例失败数据信息记录、用于告警、数据只针对当前内存数据.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-14 - 下午03:29:28
 * @version 1.0
 */
public final class AlarmCache {

	/**
	 * 用例失败信息记录
	 * key：用例ID
	 * value：时间戳队列(保存五分钟内的执行失败的时间)
	 */
	private static Map<Integer, List<Long>> alarmMap = 
		new ConcurrentHashMap<Integer, List<Long>>();
	
	/**
	 * 获取用例失败数据信息
	 * @return
	 * 2011-6-14 - 下午03:37:10
	 */
	public static Map<Integer, List<Long>> getAlarmMap(){
		synchronized (alarmMap) {
			return alarmMap;
		}
	}
	
	/**
	 * 新增用例失败数据信息
	 * 
	 * @param browserSession
	 * 2011-6-14 - 下午03:38:56
	 */
	public static void addAlarm(Integer key, List value){
		synchronized (alarmMap) {
			alarmMap.put(key, value);
		}
	}
	
	/**
	 * 获取用例失败数据信息
	 * 
	 * @param browserSession
	 * 2011-6-14 - 下午03:38:56
	 */
	public static boolean whetherAlarm(int key){
		synchronized (alarmMap) {
			List<Long> value = alarmMap.get(key);
			if(value != null){
				//首先删除过期的失败数据(5分钟之内的数据才作为告警参考值)
				long currentTime = new Date().getTime();
				Iterator<Long> iterator = value.iterator(); 
				while(iterator.hasNext()) {  
					Long timeStap = iterator.next(); 
					//3分钟内同一用例执行失败次数>=3就告警
					if((currentTime-timeStap)>=3*60*1000){
						timeStap = null;
						iterator.remove(); 
					}
				}
				value.add(currentTime);
				if(value.size()>=3){//告警
					alarmMap.remove(key);
					return true;
				}else{
					return false;
				}
			}else{//第一次失败
				List list = new ArrayList();
				list.add(new Date().getTime());
				alarmMap.put(key, list);
				return false;
			}
		}
	}
	
	/**
	 * 删除用例失败数据信息
	 * 
	 * @param browserSession
	 * 2011-6-14 - 下午03:38:56
	 */
	public static void deleteAlarm(String key){
		synchronized (alarmMap) {
			alarmMap.remove(key);
		}
	}
}
