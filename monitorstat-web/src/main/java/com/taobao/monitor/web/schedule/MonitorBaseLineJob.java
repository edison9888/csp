package com.taobao.monitor.web.schedule;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.web.baseline.BaseLineManage;

public class MonitorBaseLineJob implements Job {
	
	private static final Logger logger = Logger.getLogger(MonitorBaseLineJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			logger.info("start BaseLineManage");
			BaseLineManage.get().createBaseLine();
			logger.info("end BaseLineManage");
		} catch (Exception e) {
			logger.error("MonitorBaseLineJob÷¥––“Ï≥£", e);
		}
	}

}
