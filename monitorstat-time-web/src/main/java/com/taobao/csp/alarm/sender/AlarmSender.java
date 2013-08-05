package com.taobao.csp.alarm.sender;


import org.apache.log4j.Logger;

import com.taobao.csp.alarm.AlarmKeyContainer;
import com.taobao.csp.alarm.check.AlarmReport;
import com.taobao.csp.alarm.po.AlarmContext;
import com.taobao.csp.alarm.service.UserService;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;


public class AlarmSender {
	private static Logger logger = Logger.getLogger(AlarmSender.class);
	private static AlarmSender alarmSender = new AlarmSender();
	private AlarmSender(){
	}
	public static AlarmSender get(){
		return alarmSender;
	}
	public boolean putAlarmInfo(AlarmContext alarmContext){
		alarmContextChain(alarmContext);
		return true;
	}
	public boolean putAlarmInfo(AlarmReport report){
		try {
			AlarmContext alarmContext = new AlarmContext();
			AppInfoPo appInfoPo = AppInfoCache.getAppInfoByAppName(report.getAppName());
			Integer appId = appInfoPo.getAppId();
			alarmContext.setAppId(appId);
			alarmContext.setAppName(report.getAppName());
			alarmContext.setKeyName(report.getKeyName());
			alarmContext.setProperty(report.getPropertyName());
			alarmContext.setTime(report.getTime());
			alarmContext.setValue(report.getValue());
			alarmContext.setKeyScope(report.getKeyScope());
			alarmContext.setModeName(report.getModeName());
			alarmContext.setKeyLevel(report.getKeyLevel());
			alarmContext.setRangeMessage(report.getCause());
			alarmContext.setKeyAlias(report.getKeyAlias());
			alarmContext.setContinuousAlarmTimes(report.getContinuous());	//FIX Bug，原来报警次数
//			List<AlarmContext> list = new ArrayList<AlarmContext>(); 
//			list.add(alarmContext);
			alarmContextChain(alarmContext);
		} catch (Exception e) {
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean putAlarmInfo(AlarmReport report,String ip){
		try {
			AlarmContext alarmContext = new AlarmContext();
			AppInfoPo appInfoPo = AppInfoAo.get().getAppInfoByAppName(report.getAppName());
			Integer appId = appInfoPo.getAppId();
			alarmContext.setAppId(appId);
			alarmContext.setAppName(report.getAppName());
			alarmContext.setKeyName(report.getKeyName());
			alarmContext.setProperty(report.getPropertyName());
			alarmContext.setTime(report.getTime());
			alarmContext.setValue(report.getValue());
			alarmContext.setKeyScope(report.getKeyScope());
			alarmContext.setModeName(report.getModeName());
			alarmContext.setKeyLevel(report.getKeyLevel());
			alarmContext.setKeyAlias(report.getKeyAlias());
			alarmContext.setIp(ip);
			alarmContext.setRangeMessage(report.getCause());
			alarmContext.setContinuousAlarmTimes(report.getContinuous());	//FIX Bug，原来报警次数
//			List<AlarmContext> list = new ArrayList<AlarmContext>(); 
//			list.add(alarmContext);
			alarmContextChain(alarmContext);
		} catch (Exception e) {
			logger.info(e);
			return false;
		}
		return true;
	}
	private void alarmContextChain(AlarmContext alarmContext){
		boolean flag = true;
		flag = UserService.get().lookup(alarmContext);
		if(flag == false)return;
	}
	public static void main(String args[]){
		AlarmKeyContainer.startup();
	}
}
