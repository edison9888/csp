/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.web.report;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.web.ao.MonitorReportConfigAo;
import com.taobao.monitor.web.schedule.QuartzManager;
import com.taobao.monitor.web.schedule.ReportJob;
import com.taobao.monitor.web.vo.ReportTemplate;

/**
 * 新增报表、修改报表切面.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-12 - 下午09:20:14
 * @version 1.0
 */
@Aspect
@Component
public class ReportInterceptor {

	private static final Logger logger = Logger.getLogger(ReportInterceptor.class);
	
	@Resource(name="monitorReportConfigAo")
	private MonitorReportConfigAo reportConfigAo;

	@Pointcut("execution(public * com.taobao.monitor.web.ao..*.addReport(..))")
	public void addReport(){};
	
	@Pointcut("execution(public * com.taobao.monitor.web.ao..*.updateReport(..))")
	public void updateReport(){};
	
	@Pointcut("execution(public * com.taobao.monitor.web.ao..*.deleteReport(..))")
	public void deleteReport(){};
	
	/**
	 * 新增job通知
	 * @author 斩飞
	 * @param reportTemplate
	 * 2011-5-13 - 下午05:19:23
	 */
	@AfterReturning("addReport() && args(reportTemplate)")
	public void afterAddReport(ReportTemplate reportTemplate) {
		//获取任务Id
		QuartzManager.addJob(reportTemplate.getReportId()+"", 
				reportTemplate.getQuartzCron(),
				ReportJob.class);
		logger.info("新增报表结束,开始新增报表发送调度程序！");
	}
	
	/**
	 * 修改job通知
	 * @author 斩飞
	 * @param reportTemplate
	 * 2011-5-13 - 下午05:20:42
	 */
	@AfterReturning("updateReport() && args(reportTemplate)")
	public void afterUpdateReport(ReportTemplate reportTemplate) {
		int reportId = reportTemplate.getReportId();
		String jobName = Constants.JOBDETAIL_PREFIX + reportId;
		String triggerName = Constants.TRIGGER_PREFIX + reportId;
		QuartzManager.modifyJobTime(reportId+"", jobName, triggerName, 
				reportTemplate.getQuartzCron(), ReportJob.class);
		logger.info("修改报表结束,开始修改报表发送调度程序！");
	}
	
	/**
	 * 删除job通知
	 * @author 斩飞
	 * @param reportId
	 * 2011-5-13 - 下午05:20:58
	 */
	@AfterReturning("deleteReport() && args(reportId)")
	public void afterDeleteReport(int reportId) {
		String jobName = Constants.JOBDETAIL_PREFIX + reportId;
		String triggerName = Constants.TRIGGER_PREFIX + reportId;
		QuartzManager.removeJob(jobName, triggerName);
		logger.info("修改报表结束,开始修改报表发送调度程序！");
	}
}
