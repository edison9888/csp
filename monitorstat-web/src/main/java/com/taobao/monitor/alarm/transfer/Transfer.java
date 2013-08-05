
package com.taobao.monitor.alarm.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.taobao.monitor.alarm.n.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-12 下午01:16:25
 */
public abstract class Transfer implements Runnable{
	
	
	
	private Thread thread;
	
	private Map<String, Queue<AlarmContext>> needSendMap = new ConcurrentHashMap<String, Queue<AlarmContext>>();
	
	
	protected abstract boolean isRecentlySend(String key,AlarmContext targetData);
	
	
	/**
	 * 
	 * @param key 发送 号码
	 * @param targetData
	 */
	public synchronized void doTranser(String key,AlarmContext targetData) {
		
		if(isRecentlySend(key,targetData)){
			return ;
		}
		
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
				
				Map<Integer,Map<Integer,List<AlarmContext>>> appKeyMap = new ConcurrentHashMap<Integer, Map<Integer,List<AlarmContext>>>();
				
				AlarmContext targetData = null;
				while((targetData = queue.poll())!=null){
					
					Map<Integer,List<AlarmContext>> keyMap = appKeyMap.get(targetData.getAppId());
					if(keyMap==null){
						keyMap = new HashMap<Integer, List<AlarmContext>>();
						appKeyMap.put(targetData.getAppId(), keyMap);
					}
					List<AlarmContext> dataList = keyMap.get(targetData.getKeyId());
					if(dataList==null){
						dataList = new ArrayList<AlarmContext>();
						keyMap.put(targetData.getKeyId(), dataList);
					}
					dataList.add(targetData);				
				}
				
				for(Map.Entry<Integer,Map<Integer,List<AlarmContext>>> tmp:appKeyMap.entrySet()){
					for(Map.Entry<Integer,List<AlarmContext>> tmpEntry:tmp.getValue().entrySet()){
						formatTranser(key,tmpEntry.getValue());
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
