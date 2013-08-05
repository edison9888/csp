package com.taobao.csp.depend.alarm;

import java.util.Date;

import org.apache.log4j.Logger;

public class AlarmCenter {
	private static final Logger logger = Logger.getLogger(AlarmCenter.class);
	//程序触发
	public void startAlarmCheck() {
		logger.info("定时任务触发，时间->" + new Date());
		new HSFProviderAlarm().alarm(null);
	}
	
	//手动触发
	public void startAlarmCheckMannual(String appName) {
		logger.info("手动触发，时间->" + new Date() + ";appName=" + appName);
		new HSFProviderAlarm().alarm(appName);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AlarmCenter().startAlarmCheck();
	}

}
