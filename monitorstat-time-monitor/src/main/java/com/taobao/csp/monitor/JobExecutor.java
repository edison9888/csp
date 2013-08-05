
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor;


/**
 * @author xiaodu
 *
 * обнГ2:31:21
 */
public interface JobExecutor {
	
	public String getName();
	
	public boolean assignJob(JobInfo job);
	
	public boolean updateJob(JobInfo job);
	
	public void releaseAllJob();
	
	public void releaseJob(String jobId);
	
	public void releaseAppJob(String appName);
	
	public void stop();
	
	
	

}
