package com.taobao.csp.day.apache;

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
import com.taobao.csp.day.po.ApacheSpecialPo;

public class ApacheSpecialStatistics {
	
	public static Logger logger = Logger.getLogger(ApacheSpecialStatistics.class);
	
	private static ApacheSpecialStatistics statistics = new ApacheSpecialStatistics();
	
	private volatile long latestMillis = 0;
	
	/*** 缓存数据 ***/
	private Map<ApacheSpecialLogKey, ApacheSepcial> cache = new ConcurrentHashMap<ApacheSpecialLogKey, ApacheSepcial>();
	
	/*** 数据存储 ***/
	ReportContentInterface reportContent = ReportContent.getInstance();
	
	private ApacheSpecialStatistics() {
		StaticsThread staticsThread = new StaticsThread();
		staticsThread.setDaemon(true);
		staticsThread.start();
	}
	
	public static ApacheSpecialStatistics getInstance() {
		return statistics;
	}
	
	public Map<ApacheSpecialLogKey, ApacheSepcial> getCache() {
		return Collections.unmodifiableMap(cache);
	}

	
	public void removeCache() {
		cache.clear();
	}
	
	/***
	 * 汇总数据
	 * @param appName
	 * @param requestUrl
	 * @param httpCode
	 * @param requestNum
	 * @param rt
	 * @param collectTime
	 */
	public void summarize(String appName, String requestUrl, String httpCode, long requestNum, long rt,
			String collectTime) {
		ApacheSpecialLogKey key = new ApacheSpecialLogKey(appName, requestUrl, httpCode, collectTime);
		
		ApacheSepcial value;
		if (cache.containsKey(key)) {
			value = cache.get(key);
		} else {
			value = new ApacheSepcial();
			cache.put(key, value);
		}	
		value.setRequstNum(value.getRequstNum() + requestNum);
		value.setRt(value.getRt() + rt);
		
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
		
		/*** 数据存储 ***/
		ReportContentInterface reportContent = ReportContent.getInstance();
		
		@Override
		public void run() {
			while (true) {
				try {
					Utils.sleep(1000 * 60 * 2);
					logger.info("size is:" + cache.values().size());
					
					long nowMillis = Calendar.getInstance().getTimeInMillis();
					if (nowMillis - latestMillis > 1000 * 60 * 3) {
						storeData();
					}
				} catch (Exception e) {
					logger.error("StaticsThread", e);
				}
			}
		}
		
		private void storeData() throws Exception {
			logger.info("store data size :" + cache.size());
			List<ApacheSpecialPo> tempL = new ArrayList<ApacheSpecialPo>();
			for (Map.Entry<ApacheSpecialLogKey, ApacheSepcial> entry : cache.entrySet()) {
				ApacheSpecialLogKey key = entry.getKey();
				ApacheSepcial value = entry.getValue();
				
//				if (value.getRequstNum() < 1000) {
//					continue;
//				}
				
				ApacheSpecialPo po = new ApacheSpecialPo();
				po.setAppName(key.getAppName());
				po.setRequstUrl(key.getRequstUrl());
				po.setHttpCode(key.getHttpCode());
				po.setCollectTime(key.getCollectTime());
				po.setRequstNum(value.getRequstNum());
				po.setRt(value.getRt());
				long rt = po.getRt() / po.getRequstNum();
				po.setRt(rt);
				
				
				tempL.add(po);
				if (tempL.size() == 200) {
					reportContent.putReportDateOfApacheSpecial(tempL);
					tempL.clear();
				}
			}
			
			if (tempL.size() > 0) {
				reportContent.putReportDateOfApacheSpecial(tempL);
				tempL.clear();
			}
			
			cache.clear();
			logger.info("finish store...");
		}
	}
}
		
