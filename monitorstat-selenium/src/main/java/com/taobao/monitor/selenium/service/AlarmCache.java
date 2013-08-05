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
 * ����ʧ��������Ϣ��¼�����ڸ澯������ֻ��Ե�ǰ�ڴ�����.
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-14 - ����03:29:28
 * @version 1.0
 */
public final class AlarmCache {

	/**
	 * ����ʧ����Ϣ��¼
	 * key������ID
	 * value��ʱ�������(����������ڵ�ִ��ʧ�ܵ�ʱ��)
	 */
	private static Map<Integer, List<Long>> alarmMap = 
		new ConcurrentHashMap<Integer, List<Long>>();
	
	/**
	 * ��ȡ����ʧ��������Ϣ
	 * @return
	 * 2011-6-14 - ����03:37:10
	 */
	public static Map<Integer, List<Long>> getAlarmMap(){
		synchronized (alarmMap) {
			return alarmMap;
		}
	}
	
	/**
	 * ��������ʧ��������Ϣ
	 * 
	 * @param browserSession
	 * 2011-6-14 - ����03:38:56
	 */
	public static void addAlarm(Integer key, List value){
		synchronized (alarmMap) {
			alarmMap.put(key, value);
		}
	}
	
	/**
	 * ��ȡ����ʧ��������Ϣ
	 * 
	 * @param browserSession
	 * 2011-6-14 - ����03:38:56
	 */
	public static boolean whetherAlarm(int key){
		synchronized (alarmMap) {
			List<Long> value = alarmMap.get(key);
			if(value != null){
				//����ɾ�����ڵ�ʧ������(5����֮�ڵ����ݲ���Ϊ�澯�ο�ֵ)
				long currentTime = new Date().getTime();
				Iterator<Long> iterator = value.iterator(); 
				while(iterator.hasNext()) {  
					Long timeStap = iterator.next(); 
					//3������ͬһ����ִ��ʧ�ܴ���>=3�͸澯
					if((currentTime-timeStap)>=3*60*1000){
						timeStap = null;
						iterator.remove(); 
					}
				}
				value.add(currentTime);
				if(value.size()>=3){//�澯
					alarmMap.remove(key);
					return true;
				}else{
					return false;
				}
			}else{//��һ��ʧ��
				List list = new ArrayList();
				list.add(new Date().getTime());
				alarmMap.put(key, list);
				return false;
			}
		}
	}
	
	/**
	 * ɾ������ʧ��������Ϣ
	 * 
	 * @param browserSession
	 * 2011-6-14 - ����03:38:56
	 */
	public static void deleteAlarm(String key){
		synchronized (alarmMap) {
			alarmMap.remove(key);
		}
	}
}
