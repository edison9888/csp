
package com.taobao.csp.assign.job;

import com.taobao.csp.assign.job.entry.JobReportEntry;


/**
 * 
 * @author xiaodu
 * @version 2011-7-4 обнГ02:16:47
 */
public interface JobListen {
	
	public void listenReport(JobReportEntry entry);

}
