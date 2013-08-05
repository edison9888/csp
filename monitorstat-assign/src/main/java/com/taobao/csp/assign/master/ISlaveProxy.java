
package com.taobao.csp.assign.master;

import com.taobao.csp.assign.job.IJob;

/**
 * 
 * @author xiaodu
 * @version 2011-7-6 ионГ11:44:38
 */
public interface ISlaveProxy {
	
	public JobFeature submitJob(IJob job);
	
	public JobFeature endJob(IJob job);
	

}
