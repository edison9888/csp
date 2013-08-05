package com.taobao.monitor.trade.dailyreport;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class CronTaskStarter {
	private Logger logger = Logger.getLogger("trade.report");

	public void task() throws SchedulerException {

		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();

		long ctime = System.currentTimeMillis();

		JobDetail jobDetail = new JobDetail("tradeReportJob", "tradeReportJob01", SendTradeDailyReportJob.class);

		CronTrigger cronTrigger = new CronTrigger("tradeReportCron", "tradeReportCron01");
		try {
			CronExpression cexp = new CronExpression("* * 1 * * ?");//每天1点执行
			cronTrigger.setCronExpression(cexp);
			
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
		} catch (Exception e) {
			logger.warn("start trade email task ex", e);
			return ;
		}
	}
}
