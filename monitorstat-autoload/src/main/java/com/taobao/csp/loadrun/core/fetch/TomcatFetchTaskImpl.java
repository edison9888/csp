
package com.taobao.csp.loadrun.core.fetch;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/***
 * tomcat fetcher task
 * @author youji.zj
 * @version 2012-07-05
 */
public class TomcatFetchTaskImpl extends BaseFetchTaskImpl{

	public TomcatFetchTaskImpl(LoadrunTarget target) {
		super(target, CommandFactory.getTomcatFetchCommand(target.getMode(), target.getTargetIp()));
	}
	
	public String getTaskName() {
		return "tomcat信息收集器";
	}
	

}
