package com.taobao.csp;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.taobao.csp.job.EagleeyeDataToCsp;

/**
 * ÿ�춨ʱִ�У���EagleEyeData������Ǩ�Ƶ�CSP��
 * @author zhongting.zy
 *
 */
public class AnalyseMain {

	private static final Logger logger = Logger.getLogger(AnalyseMain.class);
	
	public static void main(String[] args) {
		
		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail("Load EagleEyeData to CSP", "CSP Group", EagleeyeDataToCsp.class);
			CronTrigger cronTrigger = new CronTrigger("cronTrigger", "triggerGroup2");
			try {
				CronExpression cexp = new CronExpression("0/5 * * * * ?");
				cronTrigger.setCronExpression(cexp);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
		} catch (Exception e) {
			logger.error(e);
		}

	}

}
