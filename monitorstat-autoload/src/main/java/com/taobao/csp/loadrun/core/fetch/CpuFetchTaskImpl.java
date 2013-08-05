
package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * cpu fetcher task
 * @author youji.zj
 * @version 2012-07-05
 */
public class CpuFetchTaskImpl extends BaseFetchTaskImpl{

	public CpuFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getCpuFetchCommand(target.getMode(), target.getTargetIp()));
	}

	public String getTaskName() {
		return "CPU信息收集器";
	}

}
