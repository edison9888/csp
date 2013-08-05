
package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * apache fetcher task
 * @author youji.zj
 * @version 2012-07-05
 */
public class ApacheFetchTaskImpl extends BaseFetchTaskImpl{
	
	public ApacheFetchTaskImpl(LoadrunTarget target, String command) {
		super(target, command);
	}

	public ApacheFetchTaskImpl(LoadrunTarget target) {
		this(target,  CommandFactory.getApacheFetchCommand(target.getMode(), target.getTargetIp()));
	}

	@Override
	public String getTaskName() {
		return "apahce日志收集器";
	}
}
