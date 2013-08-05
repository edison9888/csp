package com.taobao.csp.alarm.ao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.taobao.csp.alarm.dao.AlarmMessageVisitDao;
import com.taobao.csp.time.web.po.AlarmMessageVisitPo;

public class AlarmMessageVisitAo {
	private static AlarmMessageVisitAo ao = new AlarmMessageVisitAo();
	private AlarmMessageVisitDao dao = new AlarmMessageVisitDao();
	private AlarmMessageVisitAo(){
	}
	public static AlarmMessageVisitAo get(){
		return ao;
	}
	
	public List<AlarmMessageVisitPo> findRecords(String appName,String keyName,String propertyName,Date alarmTime){
		return dao.findRecords(appName, keyName, propertyName, alarmTime);
	}
	
	public boolean update(String appName,String keyName,String propertyName,Date time,String visitor){
		return dao.update(appName, keyName, propertyName, time, visitor);
	}
	
	public boolean insert(AlarmMessageVisitPo po){
		return dao.insert(po);
	}
	public static void main(String args[]){
		AlarmMessageVisitPo po = new AlarmMessageVisitPo();
		po.setAppName("detail");
		po.setKeyName("xxxx");
		po.setPropertyName("adsfasdf");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(100000);
		Date date = cal.getTime();
		po.setAlarmTime(date);
		AlarmMessageVisitAo.get().insert(po);
		AlarmMessageVisitAo.get().update("detail", "xxxx","adsfasdf", date, "test");
		System.exit(0);
	}
}
