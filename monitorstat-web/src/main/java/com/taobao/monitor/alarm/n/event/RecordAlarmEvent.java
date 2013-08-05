
package com.taobao.monitor.alarm.n.event;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.web.ao.MonitorAlarmAo;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 обнГ03:52:05
 */
public class RecordAlarmEvent implements AlarmEvent{

	@Override
	public void onAlarm(AlarmContext context) {
		MonitorAlarmAo.get().recordAlarmEvent(
				context.getKeyId(),context.getAppId(),
				context.getSiteId(),context.getRecentlyDate(),
				context.getRecentlyValue());
	}

}
