package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * io fetcher task
 * @author youji.zj
 * @version 2012-12-17
 */
public class IoFetchTaskImpl extends BaseFetchTaskImpl {
	
	public IoFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getIoFetchCommand(target.getMode(), target.getTargetIp()));
	}
	
	public String getTaskName() {
		return "IoÊÕ¼¯Æ÷";
	}
}
