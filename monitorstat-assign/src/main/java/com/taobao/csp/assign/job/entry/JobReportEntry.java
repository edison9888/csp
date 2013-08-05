
package com.taobao.csp.assign.job.entry;

import java.io.Serializable;

import javax.print.attribute.standard.JobState;

import com.taobao.csp.assign.job.IJob;
import com.taobao.csp.assign.job.JobReportType;

/**
 * 
 * @author xiaodu
 * @version 2011-7-12 下午03:27:16
 */
public class JobReportEntry implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7511249473215698339L;

	private IJob job;
	
	private JobReportType jobState;
	
	private Object message; //这个对象必须是 Serializable
	
	public JobReportEntry(IJob job,JobReportType jobState){
		this(job,jobState,null);
	}
	
	public JobReportEntry(IJob job,JobReportType jobState,Object message){
		this.job = job;
		this.jobState = jobState;
		this.message = message;
	}
	
	public JobReportType getJobState(){
		return jobState;
	}
	
	public Object getMessage(){
		return message;
	}

	public IJob getJob() {
		return job;
	}

	@Override
	public String toString() {
		return "jobId:"+job.getJobId()+" JobState:"+jobState.name()+" message:"+message;
	}
	
	

}
