
package com.taobao.monitor.alarm.n.event;

import java.util.ArrayList;
import java.util.List;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.Service;

/**
 * 
 * @author xiaodu
 * @version 2011-3-1 ÉÏÎç09:41:33
 */
public class EventService implements Service<AlarmEvent>{
	
	private EventService(){
		
		init();
	}
	
	private static EventService service = new EventService();
	
	public static EventService get(){
		return service;
	}
	
	
	private List<AlarmEvent> eventList = new ArrayList<AlarmEvent>();

	@Override
	public void init() {
		
		eventList.add(new RecordAlarmEvent());
		eventList.add(new BaseLineAlarmEvent());
	}

	@Override
	public boolean lookup(AlarmContext context) {
		
		for(AlarmEvent event:eventList){
			event.onAlarm(context);
		}
		return true;
	}

	@Override
	public void register(AlarmEvent t) {
		if(t!=null)
		eventList.add(t);
	}

	@Override
	public void unregister(AlarmEvent t) {
		if(t!=null)
			eventList.remove(t);
	}

}
