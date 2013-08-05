///**
// * monitorstat-alarm
// */
//package com.taobao.csp.dataserver.memcache.event;
//
//import org.apache.log4j.Logger;
//
//import com.taobao.csp.alarm.AlarmKeyContainer;
//import com.taobao.csp.alarm.check.mode.CheckMode;
//import com.taobao.csp.alarm.client.ZkAlarmClient;
//import com.taobao.csp.dataserver.item.ValueOperate;
//import com.taobao.csp.dataserver.memcache.entry.PropertyEntry;
//
///**
// * @author xiaodu
// * 
// *         ÉÏÎç11:34:36
// */
//public class HostAlarmOperateEvent implements DataEvent {
//
//	private static final Logger logger = Logger.getLogger(HostAlarmOperateEvent.class);
//
//	public void onEvent(PropertyEntry entry, ValueOperate operate) {
//
//		CheckMode mode = AlarmKeyContainer.get().getCheckMode(entry.getKeyEntry().getAppName(), entry.getKeyEntry().getKeyName(), entry.getPropertyName(),entry.getKeyEntry().getKeyScope().toString());
//		if (mode != null && mode.checkData(entry.getValues(), entry.getRecentlyIndex())) {
//			ZkAlarmClient.get().sendData(entry.getKeyEntry().getAppName(), entry.getKeyEntry().getKeyName(), entry.getPropertyName(), entry.getValues()[entry.getRecentlyIndex()],
//					entry.getKeyEntry().getKeyScope().toString());
//			
//			logger.info("APP ALARM"+entry.getKeyEntry().getAppName()+"&"+ entry.getKeyEntry().getKeyName()+"&"+entry.getPropertyName()+"&"+entry.getRecentlyTime());
//		}
//
//	}
//
//}
