
package com.taobao.monitor.alarm.n.event;

import com.taobao.monitor.alarm.n.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 обнГ03:50:48
 */
public interface AlarmEvent {
	
	public void onAlarm(AlarmContext context);

}
