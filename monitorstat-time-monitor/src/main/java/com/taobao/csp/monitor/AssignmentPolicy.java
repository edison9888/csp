
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor;

import java.util.List;


/**
 * @author xiaodu
 *
 * обнГ2:30:33
 */
public interface AssignmentPolicy {
	
	public String assign(JobInfo job,List<String> executors);

}
