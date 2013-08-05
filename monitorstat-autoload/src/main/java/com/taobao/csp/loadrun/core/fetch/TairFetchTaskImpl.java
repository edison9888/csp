package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * tair fetcher task
 * @author youji.zj
 * @version 2012-07-05
 */
public class TairFetchTaskImpl extends BaseFetchTaskImpl {
	
	public TairFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getTairFetchCommand(target.getMode(), target.getTargetIp()));
	}
	
	public String getTaskName() {
		return "Tair日志收集器";
	}
	
}
