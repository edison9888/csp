package com.taobao.csp.alarm.ao;

import java.util.List;


import com.taobao.csp.alarm.dao.MonitorAlarmDao;
import com.taobao.csp.alarm.po.AlarmSendPo;
import com.taobao.csp.alarm.po.UserAcceptInfo;


public class MonitorAlarmAo {
	private static MonitorAlarmAo ao = new MonitorAlarmAo();
	private static MonitorAlarmDao dao = new MonitorAlarmDao();
	
	private MonitorAlarmAo(){
	}
	public static MonitorAlarmAo get(){
		return ao;
	}
	public void addAlarmSend(AlarmSendPo info){
		dao.addAlarmSend(info);
	}
	public List<AlarmSendPo> findAllAlarmSend(String alarmDateStart,String alarmDateEnd){
		return dao.findAllAlarmSend(alarmDateStart, alarmDateEnd);
	}
	public List<UserAcceptInfo> findAllUserAcceptMsg(String alarmDateStart,String alarmDateEnd){
		return dao.findAllUserAcceptMsg(alarmDateStart, alarmDateEnd);
	}
	public void addUserAcceptMsg(UserAcceptInfo info){
		dao.addUserAcceptMsg(info);
	}
}
