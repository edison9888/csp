
package com.taobao.csp.assign.master;

import com.taobao.csp.assign.job.IJob;
import com.taobao.csp.assign.job.JobListen;
import com.taobao.csp.assign.job.entry.JobReportEntry;

/**
 * 
 * @author xiaodu
 * @version 2011-7-4 ����01:21:29
 */
public class JobFeature {
	
	private IJob job;
	
	private JobListen listen = new JobListen(){
		@Override
		public void listenReport(JobReportEntry entry) {
		}
	};
	
	/**
	 * ��Ǵ������Ƿ�ʼ���У����ڳ�ʱ�ж�
	 */
	private boolean isRun;
	
	
	public boolean isRun() {
		return isRun;
	}
	
	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	public IJob getJob() {
		return job;
	}

	public JobFeature(IJob job){
		this.job = job;
	}
	
	public String getJobId(){
		return job.getJobId();
	}
	
	public void addJobListen(JobListen listen){
		this.listen = listen;
	}

	public JobListen getListen() {
		return listen;
	}

	public void setListen(JobListen listen) {
		this.listen = listen;
	}

	public void setJob(IJob job) {
		this.job = job;
	}
	
	
	

}
