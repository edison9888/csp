package com.taobao.csp.day.tddl;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

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
* 负责数据的汇总和存储，为TddlBolt工作
* 同一进程中TddlStatistics为单例，StaticsThread在TddlStatistics构造器中启动
* 不过该类可能运行于不同的进程，因为TddlBolt允许有多个并发数（事实上会有多个）
 * 1、汇总存储demp表数据
 * 2、汇总存储detail表数据
 * 
 * @author youji.zj
 * @version 1.0 2012-08-25
 *
 */
public class TddlStatistics {
	
	public static Logger logger = Logger.getLogger(TddlStatistics.class);
	
	private static TddlStatistics tddlStatistics = new TddlStatistics();
	
	private static Set<String> ips = new CopyOnWriteArraySet<String>();  
	
	/*** 以小时为单位的数据 ***/
	private  Map<String, Map<TddlLogKey, TDDL>> cacheHour = new ConcurrentHashMap<String, Map<TddlLogKey, TDDL>>();
	
	/*** 以天为单位的数据 ***/
	private  Map<String, Map<TddlLogKey, TDDL>> cacheDay = new ConcurrentHashMap<String, Map<TddlLogKey, TDDL>>();
	
	/*** 数据存储 ***/
	ReportContentInterface reportContent = ReportContent.getInstance();
	
	private TddlStatistics() {
		StaticsThread staticsThread = new StaticsThread();
		staticsThread.setDaemon(true);
		staticsThread.start();
	}
	
	public static TddlStatistics getInstance() {
		return tddlStatistics;
	}
	
	public Map<String, Map<TddlLogKey, TDDL>> getCacheHour() {
		return Collections.unmodifiableMap(cacheHour);
	}

	public Map<String, Map<TddlLogKey, TDDL>> getCacheDay() {
		return Collections.unmodifiableMap(cacheDay);
	}
	
	public void removeCacheHour(String key) {
		cacheHour.remove(key);
	}
	
	public void removeCacheDay(String key) {
		cacheDay.remove(key);
	}

	/***
	 * 汇总
	 * @param appName
	 * @param dbFeature
	 * @param dbName
	 * @param dbIp
	 * @param dbPort
	 * @param sql
	 * @param executeSum
	 * @param executeTime
	 * @param maxResp
	 * @param maxRespTime
	 * @param minResp
	 * @param minRespTime
	 * @param collectTime "2012-09-06 16"
	 */
	public void summarize(String appName, String dbFeature, String dbName, String dbIp, String dbPort,
			String sql, long executeSum, long executeTime, int maxResp, String maxRespTime, int minResp,
			String minRespTime, String collectTime) {
		
		TddlLogKey hourKey = new TddlLogKey(appName, dbFeature, dbName, dbIp, dbPort, sql, collectTime);
		String collectDay = collectTime.substring(0, collectTime.length() - 3);
		TddlLogKey dayKey = new TddlLogKey(appName, dbFeature, dbName, dbIp, dbPort, sql, collectDay);
		
		// 汇总小时数据
		Map<TddlLogKey, TDDL> hourM;
		if (cacheHour.containsKey(collectTime)) {
			hourM = cacheHour.get(collectTime);
		} else {
			hourM = new ConcurrentHashMap<TddlLogKey, TDDL>();
			cacheHour.put(collectTime, hourM);
		}
		TDDL tddlHour;
		if (hourM.containsKey(hourKey)) {
			tddlHour = hourM.get(hourKey);
			tddlHour.setExeCount(tddlHour.getExeCount() + executeSum);
			tddlHour.setRespTime(tddlHour.getRespTime() + executeTime);
			if (tddlHour.getMaxResp() < maxResp) {
				tddlHour.setMaxResp(maxResp);
				tddlHour.setMaxRespDate(maxRespTime);
			}
			if (tddlHour.getMinResp() > minResp) {
				tddlHour.setMinResp(minResp);
				tddlHour.setMinRespDate(minRespTime);
			}
		} else {
			tddlHour = new TDDL();
			tddlHour.setExeCount(executeSum);
			tddlHour.setRespTime(executeTime);
			tddlHour.setMaxResp(maxResp);
			tddlHour.setMaxRespDate(maxRespTime);
			tddlHour.setMinResp(minResp);
			tddlHour.setMinRespDate(minRespTime);
			hourM.put(hourKey, tddlHour);
		}

		
		// 汇总天数据
		Map<TddlLogKey, TDDL> dayM;
		if (cacheDay.containsKey(collectDay)) {
			dayM = cacheDay.get(collectDay);
		} else {
			dayM = new ConcurrentHashMap<TddlLogKey, TDDL>();
			cacheDay.put(collectDay, dayM);
		}
		TDDL tddlDay;
		if (dayM.containsKey(dayKey)) {
			tddlDay = dayM.get(dayKey);
			tddlDay.setExeCount(tddlDay.getExeCount() + executeSum);
			tddlDay.setRespTime(tddlDay.getRespTime() + executeTime);
			if (tddlDay.getMaxResp() < maxResp) {
				tddlDay.setMaxResp(maxResp);
				tddlDay.setMaxRespDate(maxRespTime);
			}
			if (tddlDay.getMinResp() > minResp) {
				tddlDay.setMinResp(minResp);
				tddlDay.setMinRespDate(minRespTime);
			}
		} else {
			tddlDay = new TDDL();
			tddlDay.setExeCount(executeSum);
			tddlDay.setRespTime(executeTime);
			tddlDay.setMaxResp(maxResp);
			tddlDay.setMaxRespDate(maxRespTime);
			tddlDay.setMinResp(minResp);
			tddlDay.setMinRespDate(minRespTime);
			dayM.put(dayKey, tddlDay);
			ips.add(dayKey.getDbIp());
		}	
		
	}
	
	/***
	 * 汇总与统计的线程
	 * 
	 * @author youji.zj
	 * @version 1.0
	 *
	 */
	private class StaticsThread extends Thread {
		
		private StaticsThread() {
			SchedulerFactory factory =  new StdSchedulerFactory();
			try {
				Scheduler scheduler = factory.getScheduler();
				
				// 入库detail表
				JobDetail dayJob = new JobDetail("tddl-day-job", "job-group", DayDetailStoreJob.class);		
				CronTrigger dayTrigger = new CronTrigger("tddl-day-trigger", "trigger-group");
				dayTrigger.setCronExpression("0 20 0 * * ?");
				
				// 入库temp表
				JobDetail hourJob = new JobDetail("tddl-hour-job", "job-group", HourTempStoreJob.class);		
				CronTrigger hourTrigger = new CronTrigger("tddl-hour-trigger", "trigger-group");
				hourTrigger.setCronExpression("0 15 * * * ?");
				
				// 删除temp表
				JobDetail deleteJob = new JobDetail("tddl-delete-job", "job-group", HourTempDeleteJob.class);		
				CronTrigger deleteTrigger = new CronTrigger("tddl-delete-trigger", "trigger-group");
				deleteTrigger.setCronExpression("0 30 2 * * ?");
				
				scheduler.scheduleJob(dayJob, dayTrigger);
				scheduler.scheduleJob(hourJob, hourTrigger);
				scheduler.scheduleJob(deleteJob, deleteTrigger);
				
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
				Utils.sleep(1000 * 60 * 5);
				
				logger.info("hour key:" + cacheHour.keySet());
				for (Map.Entry<String, Map<TddlLogKey, TDDL>> entry : cacheHour.entrySet()) {
					String hour = entry.getKey();
					Map<TddlLogKey, TDDL> valueM = entry.getValue();
					logger.info(hour + " data size is " + valueM.size());
					
					
				}
				
				logger.info("day key:" + cacheDay.keySet());
				for (Map.Entry<String, Map<TddlLogKey, TDDL>> entry : cacheDay.entrySet()) {
					String day = entry.getKey();
					Map<TddlLogKey, TDDL> valueM = entry.getValue();
					logger.info(day + " data size is " + valueM.size());
				}
				
				logger.info("ips size" + ips.size());
				logger.info("ips:" + ips);
			}
		} 
	}
}
		
