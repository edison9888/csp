package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * thread count fetcher task
 * @author youji.zj
 * @version 2012-12-17
 */
public class ThreadCountFetchTaskImpl extends BaseFetchTaskImpl {
	
	public ThreadCountFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getThreadCountFetchCommand(target.getMode(), target.getTargetIp()));
	}
	
	public String getTaskName() {
		return "线程数收集器";
	}
}
