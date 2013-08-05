package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * hsf fetcher task
 * @author youji.zj
 * @version 2012-07-05
 */
public class HsfFetchTaskImpl extends BaseFetchTaskImpl {
	
	public HsfFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getHsfFetchCommand(target.getMode(), target.getTargetIp()));
	}
	
	public String getTaskName() {
		return "HSF日志收集器";
	}
}
