
package com.taobao.csp.alarm.transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import com.taobao.csp.alarm.po.AlarmContext;


/**
 * 
 * @author xiaodu
 * @version 2011-2-12 下午01:16:25
 */
public abstract class Transfer implements Runnable {

	private Thread thread;

	private Map<String, Queue<AlarmContext>> needSendMap = new ConcurrentHashMap<String, Queue<AlarmContext>>();



	/**
	 * 
	 * @param key 发送 号码
	 * @param targetData
	 */
	public void doTranser(String key,AlarmContext targetData) {

		Queue<AlarmContext> queue = needSendMap.get(key);
		if(queue==null){
			queue = new ConcurrentLinkedQueue<AlarmContext>();
			needSendMap.put(key, queue);			
		}
		queue.add(targetData);

		synchronized (needSendMap) {
			needSendMap.notifyAll();
		}
	}

	/**
	 * 将需要发送的信息格式化
	 * @param targetData
	 * @return
	 */
	protected abstract String formatTranser(String targetSend,List<AlarmContext> targetData);


	public Transfer(String name){
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.setName("Thread - TranserWork-"+name);
		thread.start();		
	}



	public void run(){
		while(true){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

			for(Map.Entry<String, Queue<AlarmContext>> entry:needSendMap.entrySet()){
				String key = entry.getKey();
				Queue<AlarmContext> queue = entry.getValue();

				Map<Integer,Map<String,Map<String,List<AlarmContext>>>> appKeyPropertyMap = new ConcurrentHashMap<Integer, Map<String,Map<String,List<AlarmContext>>>>();

				AlarmContext targetData = null;
				while((targetData = queue.poll())!=null){

					Map<String,Map<String,List<AlarmContext>>> keyPropertyMap = appKeyPropertyMap.get(targetData.getAppId());
					if(keyPropertyMap==null){
						keyPropertyMap = new HashMap<String,Map<String, List<AlarmContext>>>();
						appKeyPropertyMap.put(targetData.getAppId(), keyPropertyMap);
					}
					Map<String, List<AlarmContext>> propertyMap = keyPropertyMap.get(targetData.getKeyName());
					if(propertyMap == null){
						propertyMap = new HashMap<String,List<AlarmContext>>();
						keyPropertyMap.put(targetData.getKeyName(), propertyMap);
					}
					List<AlarmContext> dataList = propertyMap.get(targetData.getProperty());
					if(dataList==null){
						dataList = new ArrayList<AlarmContext>();
						propertyMap.put(targetData.getProperty(), dataList);
					}
					dataList.add(targetData);				
				}

				for(Map.Entry<Integer,Map<String,Map<String,List<AlarmContext>>>> tmp:appKeyPropertyMap.entrySet()){
					for(Map.Entry<String,Map<String,List<AlarmContext>>> tmpEntry:tmp.getValue().entrySet()){
						for(Map.Entry<String, List<AlarmContext>> tmpEntry2:tmpEntry.getValue().entrySet()){
							formatTranser(key,tmpEntry2.getValue());
						}
					}
				}

			}

			synchronized (needSendMap) {
				try {
					needSendMap.wait();
				} catch (InterruptedException e) {
				}
			}
		}

	}
}
