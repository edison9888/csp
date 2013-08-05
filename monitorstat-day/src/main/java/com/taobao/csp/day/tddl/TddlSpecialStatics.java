package com.taobao.csp.day.tddl;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import backtype.storm.utils.Utils;

import com.taobao.csp.day.ao.ReportContent;
import com.taobao.csp.day.ao.ReportContentInterface;

/***
 * tddlһЩ�������ݵļ���
 * 1�������Ӧʱ�� && �����Ӧʱ��Ĳ���ʱ��
 * 2����С��Ӧʱ�� && ��С��Ӧʱ��Ĳ���ʱ��
 * 
 * summary��Ļ���
 * �������Ӧʱ�����С��Ӧʱ�����summary��
 * 
 * TddlSpecialStaticsֻ����TddlStaticsBolt����ʼ����TddlStaticsBolt���߳�����Ҫ���ó�1
 * ��ֹ������
 * 
 * @author youji.zj
 * @version 1.0 2012-09-06
 *
 */
public class TddlSpecialStatics {
	
	public static Logger logger = Logger.getLogger(TddlSpecialStatics.class);
	
	private static TddlSpecialStatics tddlSpecialStatics = new TddlSpecialStatics();
	
	/*** ���ݴ洢 ***/
	ReportContentInterface reportContent = ReportContent.getInstance();
	
	private TddlSpecialStatics() {
		StaticsThread staticsThread = new StaticsThread();
		staticsThread.setDaemon(true);
		staticsThread.start();
	}
	
	// �����Ӧʱ��<"yyyy-MM-dd",<TddlResponseKey, TddlResponseValue>>
	private Map<String, Map<TddlResponseKey, TddlResponseValue>> cacheMaxResponse = new ConcurrentHashMap<String , Map<TddlResponseKey, TddlResponseValue>>();
	
	// ��С��Ӧʱ�� <"yyyy-MM-dd",<TddlResponseKey, TddlResponseValue>>
	private Map<String, Map<TddlResponseKey, TddlResponseValue>> cacheMinResponse = new ConcurrentHashMap<String , Map<TddlResponseKey, TddlResponseValue>>();
	
	public static TddlSpecialStatics getInstance() {
		return tddlSpecialStatics;
	}

	public Map<String, Map<TddlResponseKey, TddlResponseValue>> getCacheMaxResponse() {
		return Collections.unmodifiableMap(cacheMaxResponse);
	}

	public Map<String, Map<TddlResponseKey, TddlResponseValue>> getCacheMinResponse() {
		return Collections.unmodifiableMap(cacheMinResponse);
	}
	
	public void removeCacheMaxResponse(String key) {
		cacheMaxResponse.remove(key);
	}
	
	public void removeCacheMinResponse(String key) {
		cacheMinResponse.remove(key);
	}

	/***
	 * 
	 * @param appName
	 * @param dbFeature
	 * @param maxResp
	 * @param maxRespTime  "2012-09-07 12:20"
	 * @param minResp
	 * @param minRespTime  "2012-09-07 12:20"
	 */
	public void summarize(String appName, String dbFeature, int maxResp, String maxRespTime, int minResp, String minRespTime) {
		TddlResponseKey key = new TddlResponseKey(appName, dbFeature);
		String minDay = minRespTime.substring(0,10);
		String maxDay = maxRespTime.substring(0,10);
		
		TddlResponseValue maxValue = new TddlResponseValue(maxResp, maxRespTime);
		Map<TddlResponseKey, TddlResponseValue> maxResponseM = cacheMaxResponse.get(maxDay);
		if (maxResponseM == null) {
			maxResponseM = new ConcurrentHashMap<TddlResponseKey, TddlResponseValue>();
			cacheMaxResponse.put(maxDay, maxResponseM);
		}
		if (!maxResponseM.containsKey(key)) {
			maxResponseM.put(key, maxValue);
		} else {
			TddlResponseValue existMaxValue = maxResponseM.get(key);
			if (existMaxValue.getResponse() < maxValue.getResponse()) {
				maxResponseM.put(key, maxValue);
			}
		}
		
		TddlResponseValue minValue = new TddlResponseValue(minResp, minRespTime);
		Map<TddlResponseKey, TddlResponseValue> minResponseM = cacheMinResponse.get(minDay);
		if (minResponseM == null) {
			minResponseM = new ConcurrentHashMap<TddlResponseKey, TddlResponseValue>();
			cacheMinResponse.put(minDay, minResponseM);
		}
		if (!minResponseM.containsKey(key)) {
			minResponseM.put(key, minValue);
		} else {
			TddlResponseValue existMinValue = minResponseM.get(key);
			if (existMinValue.getResponse() > minValue.getResponse()) {
				minResponseM.put(key, minValue);
			}
		}
	}
	
	/***
	 * ������ͳ�Ƶ��߳�
	 * 
	 * @author youji.zj
	 * @version 1.0
	 *
	 */
	private class StaticsThread extends Thread {
		
		private StaticsThread() {
			SchedulerFactory factory =  new StdSchedulerFactory();
			Scheduler scheduler;
			try {
				scheduler = factory.getScheduler();
				
				// ���summary��
				JobDetail dayJob = new JobDetail("tddl-surramy-job", "job-group", DaySummaryStoreJob.class);		
				CronTrigger dayTrigger = new CronTrigger("tddl-summary-trigger", "trigger-group");
				dayTrigger.setCronExpression("0 35 0 * * ?");
				
				scheduler.scheduleJob(dayJob, dayTrigger);
				
				scheduler.start();
			} catch (SchedulerException e) {
				logger.error(e);
			} catch (ParseException e) {
				logger.error(e);
			}
		}
		
		@Override
		public void run() {
			while (true) {
				// һ���Ӵ�һ��
				Utils.sleep(1000  * 60);
				
				logger.info("max response key:" + cacheMaxResponse.keySet());
				for (Map<TddlResponseKey, TddlResponseValue> max : cacheMaxResponse.values()) {
					logger.info("max responce data size is " + max.keySet().size());
				}
				
				logger.info("min response key:" + cacheMaxResponse.keySet());
				for (Map<TddlResponseKey, TddlResponseValue> min : cacheMinResponse.values()) {
					logger.info("min responce data size is " + min.keySet().size());
				}
			}
		} 
	}
}
