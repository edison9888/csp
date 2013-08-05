
package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * load fetcher task
 * @author youji.zj
 * @version 2012-07-05
 */
public class LoadFetchTaskImpl extends BaseFetchTaskImpl{

	public LoadFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getLoadFetchCommand(target.getMode(), target.getTargetIp()));
	}

	public String getTaskName() {
		return "LOAD信息收集器";
	}

}
