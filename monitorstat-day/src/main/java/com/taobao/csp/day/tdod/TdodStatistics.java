package com.taobao.csp.day.tdod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import backtype.storm.utils.Utils;

import com.taobao.csp.day.ao.ReportContent;
import com.taobao.csp.day.ao.ReportContentInterface;
import com.taobao.csp.day.po.TdodPo;

public class TdodStatistics {
	
	public static Logger logger = Logger.getLogger(TdodStatistics.class);
	
	private static TdodStatistics sphStatistics = new TdodStatistics();
	
	private volatile long latestMillis = 0;
	
	/*** 缓存数据 ***/
	private Map<TdodLogKey, Integer> cache = new ConcurrentHashMap<TdodLogKey, Integer>();
	
	/*** 数据存储 ***/
	ReportContentInterface reportContent = ReportContent.getInstance();
	
	private TdodStatistics() {
		StaticsThread staticsThread = new StaticsThread();
		staticsThread.setDaemon(true);
		staticsThread.start();
	}
	
	public static TdodStatistics getInstance() {
		return sphStatistics;
	}
	
	public Map<TdodLogKey, Integer> getCache() {
		return Collections.unmodifiableMap(cache);
	}

	
	public void removeCache() {
		cache.clear();
	}
	

	/***
	 * 汇总
	 * @param appName
	 * @param blockCount
	 * @param collectTime
	 */
	public void summarize(String appName, int blockCount, String collectTime) {
		
		TdodLogKey key = new TdodLogKey(appName, collectTime);
		
		if (cache.containsKey(key)) {
			int count = cache.get(key) + blockCount;
			cache.put(key, count);
		} else {
			cache.put(key, blockCount);
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
					Utils.sleep(1000 * 60 * 2);
					
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
			
			List<TdodPo> tempL = new ArrayList<TdodPo>();
			for (Map.Entry<TdodLogKey, Integer> entry : cache.entrySet()) {
				TdodLogKey key = entry.getKey();
				int value = entry.getValue();
				
				TdodPo po = new TdodPo();
				po.setAppName(key.getAppName());
				po.setBloackCount(value);
				po.setCollectTime(key.getCollectTime());
				
				tempL.add(po);
				if (tempL.size() == 200) {
					reportContent.putReportDateOfTdod(tempL);
					tempL.clear();
				}
			}
			
			if (tempL.size() > 0) {
				reportContent.putReportDateOfTdod(tempL);
				tempL.clear();
			}
			
			cache.clear();
		}
	}
}
		
