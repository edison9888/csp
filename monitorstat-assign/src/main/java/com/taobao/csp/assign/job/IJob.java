
package com.taobao.csp.assign.job;

import java.io.Serializable;

/**
 * 
 * @author xiaodu
 * @version 2011-7-5 上午10:36:04
 */
public interface IJob extends Serializable{
	
	public String getJobId();
	
	public Object getDetail();
	
	public void execute(JobReport report)throws Exception;
	
	public void stopJob()throws Exception;
	
	/**
	 * 用于设置超时时间
	 * @author denghaichuan.pt
	 * @version 2012-3-2
	 */
	public long getDeadline();
	public void setDeadline(long deadline);

}
