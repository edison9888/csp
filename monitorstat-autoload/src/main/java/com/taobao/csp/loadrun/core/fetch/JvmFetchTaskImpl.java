
package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * jvm fetcher task
 * @author youji.zj
 * @version 2012-07-05
 */
public class JvmFetchTaskImpl extends BaseFetchTaskImpl{

	public JvmFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getJvmFetchCommand(target.getMode(), target.getTargetIp()));
	}

	public String getTaskName() {
		return "Mbean日志收集器";
	}
}
