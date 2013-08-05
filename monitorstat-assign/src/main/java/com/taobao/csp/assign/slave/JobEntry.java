
package com.taobao.csp.assign.slave;

import org.apache.log4j.Logger;

import com.taobao.csp.assign.job.IJob;
import com.taobao.csp.assign.job.JobReport;
import com.taobao.csp.assign.job.JobReportType;
import com.taobao.csp.assign.util.LocalUtil;

/**
 * 
 * @author xiaodu
 * @version 2011-7-5 上午11:13:53
 */
public class JobEntry implements Runnable{
	
	private static final Logger logger = Logger.getLogger(JobEntry.class);
	
	private  IJob job;
	
	private JobReport event;
	
	private ClassLoader jobClassLoader;
	
	public JobEntry(IJob job,JobReport event,ClassLoader jobClassLoader){
		this.job = job;
		this.jobClassLoader = jobClassLoader;
		if(event!=null){
			this.event = event;
		}else{
			event = new JobReport(){
				@Override
				public void doJobReport(IJob job, JobReportType state, Object obj) {
				}};
		}
		
	}

	public void run() {
		if(jobClassLoader!=null)
			Thread.currentThread().setContextClassLoader(jobClassLoader);
		
		logger.info("job start "+job.getJobId());
		
		
		event.doJobReport(this.job,JobReportType.Start,job.getDetail().toString()+" 开始执行!");
		try{
			job.execute(event);
		}catch (Throwable e) {
			String throwableMessage = LocalUtil.transerThrowableInfo(e);
			System.out.println(e);
			event.doJobReport(this.job,JobReportType.Exception,e);
			logger.info("error" + job.getJobId() + e);
			logger.info("errorTrace" + job.getJobId() + throwableMessage);
		}
		event.doJobReport(this.job,JobReportType.Complete,job.getDetail().toString()+" 完成执行!");
		
		logger.info("job end "+job.getJobId());
		
	}
	
	
	

}
