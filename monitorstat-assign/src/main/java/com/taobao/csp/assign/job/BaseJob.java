
package com.taobao.csp.assign.job;

import java.util.UUID;

/**
 * 
 * @author xiaodu
 * @version 2011-7-6 上午11:31:44
 */
public abstract class BaseJob implements IJob{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1831103363188784813L;
	
	/**
	 * 超时期限
	 */
	public long deadLine = -1l;
	
	public String jobId = null;
	
	public String getJobId(){
		return jobId;
	}
	
	public BaseJob(){
		this(UUID.randomUUID().toString());
	}
	
	public BaseJob(String jobId){
		this.jobId = jobId;
	}
	
	public BaseJob(long deadLine){
		this(UUID.randomUUID().toString());
		this.deadLine = deadLine;
	}
	
	public long getDeadline() {
		return deadLine;
	}
	
	public void setDeadline(long deadline) {
		this.deadLine = deadline;
	}
	
}
