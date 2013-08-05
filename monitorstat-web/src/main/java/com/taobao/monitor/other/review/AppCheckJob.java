package com.taobao.monitor.other.review;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AppCheckJob implements Job {

	private static final Logger logger = Logger.getLogger(AppCheckJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			DataReview ar = DataReview.getInstance();
			ar.checkAuth();
			// ar.getResult();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
