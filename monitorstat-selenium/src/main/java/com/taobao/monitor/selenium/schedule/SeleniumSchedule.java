
package com.taobao.monitor.selenium.schedule;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.taobao.monitor.selenium.util.SeleniumConstant;

/**
 * 
 * @author xiaodu
 * @version 2011-3-11 下午02:21:30
 */
public class SeleniumSchedule {
	
	private static final Logger logger = Logger.getLogger(SeleniumSchedule.class);
	
	private static SchedulerFactory factory = new StdSchedulerFactory();
	
	private static Scheduler scheduler = null;
	
	public static void addJob(String name,String trigger,Class c) {
		try{
			synchronized (factory) {
				
				if(scheduler ==null){
					startup();
				}
				
				if(scheduler != null){
					JobDetail job = new JobDetail(SeleniumConstant.JOBDETAIL_PREFIX
							+ name, SeleniumConstant.JOB_GROUP_NAME,c);
					CronTrigger t = new CronTrigger(SeleniumConstant.TRIGGER_PREFIX
							+ name, SeleniumConstant.TRIGGER_GROUP_NAME);
					t.setCronExpression(trigger);
					scheduler.scheduleJob(job, t);
				}
			}
		}catch (Exception e) {
			logger.error("SeleniumScheduleContainer addJob出错", e);
		}
		
	}
	
	public static void startup() throws SchedulerException{
		synchronized (factory) {
			if(scheduler == null){
				scheduler = factory.getScheduler();
			}
			scheduler.start();
		}
	}

	/**
	 * 获取Quartz Scheduler
	 * @author 斩飞
	 * @return
	 * @throws SchedulerException
	 * 2011-5-13 - 下午01:51:10
	 */
	public static Scheduler getScheduler() throws SchedulerException{
		synchronized (factory) {
			if(scheduler == null){
				startup();//一定要初始化
			}
		}
		return scheduler;
	}
}
