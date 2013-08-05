package com.taobao.csp.loadrun.core.fetch.fetcher;


import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;

/***
 * fetcher工厂
 * @author youji.zj 
 * @version 2012-06-20
 */
public class FetcherFactory {
	
	public static Logger logger = Logger.getLogger(FetcherFactory.class);
	
	/***
	 * 根据压测配置的压测模式生产对应的fetcher
	 * 由于只能建10个连接，gc的fetcher改用hubagent
	 * 
	 * @param task
	 * @param target
	 * @param command
	 * @return
	 */
	public static IFetcher createFetcher(IFetchTask task, LoadrunTarget target, String command) {
		logger.info(target.getTargetIp() + " create fetcher, mode is " + target.getMode() + ".");
		logger.info(target.getTargetIp() + " command is " + command + ".");
		
		if (task.getTaskName().equals("GC日志收集器")) {
			return new ScriptFetcher(task, command);
		}
		
		if (target.getMode() == AutoLoadMode.SSH) {
			return new SshFetcher(task, command, target.getTargetIp(), target.getTargetUserName(), target.getTargetPassword());
		} else if (target.getMode() == AutoLoadMode.SCRIPT) {
			return new ScriptFetcher(task, command);
		} else {
			return new SshFetcher(task, command, target.getTargetIp(), target.getTargetUserName(), target.getTargetPassword());
		}
		
	}

}
