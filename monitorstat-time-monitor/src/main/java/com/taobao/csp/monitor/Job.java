
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor;



/**
 * @author xiaodu
 *
 * обнГ2:39:58
 */
public interface Job extends Runnable{
	
	public String getJobID();
	
	public JobInfo getJobInfo();
	
	public DataAnalyse getDataAnalyse();
	
	public DataCollector getDataCollector();
	
	public void execute();
	
	public boolean isFire();
	
	public void close();

}
