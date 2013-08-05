package com.taobao.monitor.alarm.external.ao;

import java.util.List;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.web.ao.MonitorAlarmAo;
import com.taobao.monitor.web.util.aggregation.ApplicationNames;

public class ExternalAlarmDataFacade {
	private static final ExternalAlarmDataFacade instance = new ExternalAlarmDataFacade();
	private static MonitorAlarmAo alarmAO = MonitorAlarmAo.get();
	private static DiamondMonitorAO diamondAO = DiamondMonitorAO.instance();

	public ExternalAlarmDataFacade instance() {

		return instance;
	}

	public void proccessAllExternalAlarmData() {

		processDiamondData();

	}

	private void processDiamondData() {

		List<String> servers = diamondAO.getAlarmServers();
		List<String> httpServers = diamondAO.getAlarmHttpServers();
		if (servers.size() > 0) {
			for (String s : servers) {
				// TODO 搞明白这里的Context里内容的含义，以及怎么被使用的
				AlarmContext context = new AlarmContext();
				context.setAppId(ApplicationNames.DIAMOND.appId());
				context.setAppName(ApplicationNames.DIAMOND.appName());
				// context.set
				// TODO

			}
		}
		if (httpServers.size() > 0) {
			for (String s : httpServers) {
				// TODO
				AlarmContext context = new AlarmContext();
				context.setAppId(ApplicationNames.DIAMOND.appId());
				context.setAppName(ApplicationNames.DIAMOND.appName());
			}
		}
	}

	private ExternalAlarmDataFacade() {

	}
}
