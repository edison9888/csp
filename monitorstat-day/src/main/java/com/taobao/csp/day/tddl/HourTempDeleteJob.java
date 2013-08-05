package com.taobao.csp.day.tddl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.csp.day.ao.ReportContent;
import com.taobao.csp.day.ao.ReportContentInterface;

public class HourTempDeleteJob implements Job {
	
	public static Logger logger = Logger.getLogger(HourTempDeleteJob.class);
	
	/*** Êý¾Ý´æ´¢ ***/
	ReportContentInterface reportContent = ReportContent.getInstance();
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -2);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		String collectTime = sf.format(calendar.getTime());
		
		reportContent.deleteTempTddl("csp_app_consume_tddl_hour_temp0", collectTime);
		reportContent.deleteTempTddl("csp_app_consume_tddl_hour_temp1", collectTime);
		reportContent.deleteTempTddl("csp_app_consume_tddl_hour_temp2", collectTime);
	}

}
