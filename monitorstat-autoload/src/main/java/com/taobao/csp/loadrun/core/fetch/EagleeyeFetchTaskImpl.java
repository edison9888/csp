package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * eagleeye fetcher task
 * @author youji.zj
 * @version 2012-11-19
 */
public class EagleeyeFetchTaskImpl extends BaseFetchTaskImpl {
	
	public EagleeyeFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getEagleeyeFetchCommand(target.getMode(), target.getTargetIp()));
	}
	
	public String getTaskName() {
		return "eagleeye日志收集器";
	}
}
