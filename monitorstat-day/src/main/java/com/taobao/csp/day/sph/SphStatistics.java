package com.taobao.csp.day.sph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

import backtype.storm.utils.Utils;

import com.taobao.csp.day.ao.ReportContent;
import com.taobao.csp.day.ao.ReportContentInterface;
import com.taobao.csp.day.po.SphPo;

public class SphStatistics {
	
	public static Logger logger = Logger.getLogger(SphStatistics.class);
	
	private static SphStatistics sphStatistics = new SphStatistics();
	
	private static Set<String> ips = new CopyOnWriteArraySet<String>();  
	
	private volatile long latestMillis = 0;
	
	/*** 缓存数据 ***/
	private Map<SphLogKey, Integer> cache = new ConcurrentHashMap<SphLogKey, Integer>();
	
	/*** 数据存储 ***/
	ReportContentInterface reportContent = ReportContent.getInstance();
	
	private SphStatistics() {
		StaticsThread staticsThread = new StaticsThread();
		staticsThread.setDaemon(true);
		staticsThread.start();
	}
	
	public static SphStatistics getInstance() {
		return sphStatistics;
	}
	
	public Map<SphLogKey, Integer> getCache() {
		return Collections.unmodifiableMap(cache);
	}

	
	public void removeCache() {
		cache.clear();
	}
	

	/***
	 * 汇总
	 * @param appName
	 * @param ip
	 * @param blockKey
	 * @param action
	 * @param blockCount
	 * @param collectTime
	 */
	public void summarize(String appName, String ip, String blockKey, String action, int blockCount,
			String collectTime) {
		
		SphLogKey key = new SphLogKey(appName, ip, blockKey, action, collectTime);
		
		if (cache.containsKey(key)) {
			int count = cache.get(key) + blockCount;
			cache.put(key, count);
		} else {
			cache.put(key, blockCount);
			ips.add(ip);
		}	
		
		this.latestMillis = Calendar.getInstance().getTimeInMillis();
	}
	
	/***
	 * 汇总与统计的线程
	 * 
	 * @author youji.zj
	 * @version 1.0
	 *
	 */
	private class StaticsThread extends Thread {
		
		@Override
		public void run() {
			while (true) {
				try {
					Utils.sleep(1000 * 60 * 3);
					
					logger.info("size is:" + cache.values().size());
					
					long nowMillis = Calendar.getInstance().getTimeInMillis();
					if ((nowMillis - latestMillis > 1000 * 60 * 3) && latestMillis > 0) {
						storeData();
					}
				} catch (Exception e) {
					logger.error("StaticsThread", e);
				}
			}
		} 
		
		private void storeData() throws Exception {
			logger.info("store data: " + cache.values().size());
			
			if (cache == null || cache.isEmpty()) {
				logger.warn("SphStoreJob no data!!!");
				return;
			}
			
			List<SphPo> tempL = new ArrayList<SphPo>();
			for (Map.Entry<SphLogKey, Integer> entry : cache.entrySet()) {
				SphLogKey key = entry.getKey();
				int value = entry.getValue();
				
				SphPo po = new SphPo();
				po.setAppName(key.getAppName());
				po.setIp(key.getIp());
				po.setBlockKey(key.getBlockKey());
				po.setBlockAction(key.getAction());
				po.setBloackCount(value);
				po.setCollectTime(key.getCollectTime());
				
				tempL.add(po);
				if (tempL.size() == 200) {
					reportContent.putReportDateOfSph(tempL);
					tempL.clear();
				}
			}
			
			if (tempL.size() > 0) {
				reportContent.putReportDateOfSph(tempL);
				tempL.clear();
			}
			
			cache.clear();
		}
	}
}
		
