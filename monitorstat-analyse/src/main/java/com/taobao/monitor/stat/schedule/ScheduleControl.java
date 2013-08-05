package com.taobao.monitor.stat.schedule;


import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.taobao.monitor.stat.util.Config;

public class ScheduleControl {
	
	private static final Logger logger =  Logger.getLogger(ScheduleControl.class);
	
	public static void startSchedule(boolean test){		
		
		logger.info("start quartz test status:"+test+" - "+System.getenv("HOSTNAME"));
		System.out.println("start quartz test status:"+test+" - "+System.getenv("HOSTNAME"));
		
		if(test){
			AnalyseJob a = new AnalyseJob();
			try {
				a.execute(null);
			} catch (JobExecutionException e) {
				e.printStackTrace();
			}
		}else{		
			SchedulerFactory factory =  new StdSchedulerFactory();
			try {
				Scheduler scheduler = factory.getScheduler();
				
				
				{
				JobDetail jobDetail = new JobDetail("analyse-job", "analyse-group", AnalyseJob.class);		
				CronTrigger trigger = new CronTrigger("analyse-trigger", "trigger-group");
				trigger.setCronExpression(Config.getValue("QUARTZ_SCHEDULE_CRON"));
				scheduler.scheduleJob(jobDetail, trigger);
				}
				{
				JobDetail otherjob = new JobDetail("other-job", "other-group", OtherAnalyse.class);	
				CronTrigger othertrigger = new CronTrigger("other-trigger", "other_trigger-group");
				othertrigger.setCronExpression("0 50 8 * * ?");
				scheduler.scheduleJob(otherjob, othertrigger);
				}
				
				{
				JobDetail apachejob = new JobDetail("apache-job", "apache-group", ApacheJob.class);	
				CronTrigger apachetrigger = new CronTrigger("apache-trigger", "apache_trigger-group");
				apachetrigger.setCronExpression("0 50 0 * * ?");
				scheduler.scheduleJob(apachejob, apachetrigger);
				}
				
				{
				JobDetail selfjob = new JobDetail("self-job", "self-group", SelfJob.class);	
				CronTrigger selftrigger = new CronTrigger("self-trigger", "self_trigger-group");
				selftrigger.setCronExpression("0 0 8 * * ?");
				scheduler.scheduleJob(selfjob, selftrigger);
				}
				
				
//				{
//				JobDetail detailjob = new JobDetail("detail-job", "detail-group", DetailBusiJob.class);	
//				CronTrigger detailtrigger = new CronTrigger("detail-trigger", "detail_trigger-group");
//				detailtrigger.setCronExpression("0 0 1 * * ?");
//				scheduler.scheduleJob(detailjob, detailtrigger);
//				}
				
				scheduler.start();
			} catch (Exception e) {
				logger.error("调度程序出错 系统退出", e);
				System.out.println("调度程序出错 系统退出!");
				e.printStackTrace();
				System.exit(2);
			}
		
		}
	}

	

}
