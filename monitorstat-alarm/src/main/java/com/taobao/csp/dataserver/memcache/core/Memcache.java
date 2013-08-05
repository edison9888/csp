package com.taobao.csp.dataserver.memcache.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.key.ChildKeyCacheImpl;
import com.taobao.csp.dataserver.memcache.entry.Item;

public class Memcache  {
	private static final Logger logger = Logger.getLogger(Memcache.class);

	private Map<String, Item> itemMap = new ConcurrentHashMap<String, Item>();
	
	public static AtomicInteger count = new AtomicInteger(0);

	private Thread thread = null;

	public Memcache() {
		thread = new Thread() {
			public void run() {
				while (true) {
					try {
						TimeUnit.MINUTES.sleep(15);
					} catch (InterruptedException e) {
						logger.debug("tigger exception", e);
					}
					
					 logger.info("run clean Memcache ...");
					
					freeMemory();
					
					ChildKeyCacheImpl.clear();
					
					 logger.info("run clean ChildKeyCacheImpl ...");
				}
			}
		};
		thread.setName("Memcache - Clean");
		thread.start();
	}

	public synchronized void put(String key, Item item) {
		itemMap.put(key, item);
		
		count.incrementAndGet();

	}

	public Item get(String key) {
		return itemMap.get(key);
	}

	public void remove(String keyName) {
		itemMap.remove(keyName);
	}
	public void freeMemory() {
		List<String> removeList = new ArrayList<String>();

		for (Map.Entry<String, Item> entry : itemMap.entrySet()) {
			long time = entry.getValue().getRecentlyTime();
			if (System.currentTimeMillis() - time > 60 * 1000 * 20) {
				removeList.add(entry.getKey());
			}
		}
		
		logger.info("run clean Memcache ..."+removeList.size());
		for(String key:removeList){
			itemMap.remove(key);
			count.decrementAndGet();
		}
	}
	
	public void clear(){
		itemMap.clear();
		ChildKeyCacheImpl.clear();
	}

}
