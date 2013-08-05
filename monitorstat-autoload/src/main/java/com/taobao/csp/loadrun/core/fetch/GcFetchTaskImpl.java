
package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * gc fetcher task
 * @author youji.zj
 * @version 2012-07-05
 */
public class GcFetchTaskImpl extends BaseFetchTaskImpl{

	public GcFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getGcFetchCommand(target.getMode(), target.getTargetIp()));
	}
	
	public String getTaskName() {
		return "GC日志收集器";
	}
}
