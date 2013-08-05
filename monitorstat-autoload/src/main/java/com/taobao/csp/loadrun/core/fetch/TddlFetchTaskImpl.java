package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * tddl fetcher task
 * @author youji.zj
 * @version 2012-07-05
 */
public class TddlFetchTaskImpl extends BaseFetchTaskImpl {
	
	public TddlFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getTddlFetchCommand(target.getMode(), target.getTargetIp()));
	}

	public String getTaskName() {
		return "Tddl日志收集器";
	}
}
