package com.taobao.monitor.trade.dailyreport;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SendTradeDailyReportJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		EmailSender es = new EmailSender();
		es.sendEmail();
	}
}
