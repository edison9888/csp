
package com.taobao.csp.assign.example;

import com.taobao.csp.assign.job.BaseJob;
import com.taobao.csp.assign.job.JobReport;

/**
 * 
 * @author xiaodu
 * @version 2011-7-6 ионГ10:28:24
 */
public class LoadRunJob extends BaseJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5679570422238256335L;
	
	


	public String getDetail() {
		return "123";
	}

	public void execute(JobReport report) throws Exception {
		System.out.println("execute and sleep 10000");
		
		Thread.sleep(2000);
		
		System.out.println("sleep end");
		
	}

	@Override
	public void stopJob() throws Exception {
		
	}

	

}
