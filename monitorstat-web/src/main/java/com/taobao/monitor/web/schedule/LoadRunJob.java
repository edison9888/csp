//
//package com.taobao.monitor.web.schedule;
//
//import org.apache.log4j.Logger;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//import com.taobao.monitor.web.cache.WeekReportCache;
//import com.taobao.monitor.web.loadrun.LoadRunManage;
//
///**
// * 
// * @author xiaodu
// * @version 2010-5-26 ÏÂÎç02:39:05
// */
//public class LoadRunJob implements Job {
//	private static  Logger log = Logger.getLogger(MailReportJob.class);
//	
//	public void execute(JobExecutionContext context)
//			throws JobExecutionException {
//		log.info("start load run");
//		LoadRunManage.get().doLoadRun();
//		try{
//			WeekReportCache.get().reset();
//		} catch (Exception e) {
//		}
//	}
//	
//	
//	
//
//}
