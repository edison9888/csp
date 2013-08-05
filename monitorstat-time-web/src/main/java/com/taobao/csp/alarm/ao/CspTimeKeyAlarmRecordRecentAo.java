//package com.taobao.csp.alarm.ao;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import com.taobao.csp.alarm.dao.CspTimeKeyAlarmRecordRecentDao;
//import com.taobao.csp.alarm.po.AlarmContext;
//import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
//
//public class CspTimeKeyAlarmRecordRecentAo implements Runnable{
//	private static Logger logger = Logger.getLogger(CspTimeKeyAlarmRecordRecentAo.class);
//	private static CspTimeKeyAlarmRecordRecentAo ao = new CspTimeKeyAlarmRecordRecentAo();
//	private CspTimeKeyAlarmRecordRecentDao dao = new CspTimeKeyAlarmRecordRecentDao();
//	private Thread thread;
//	private CspTimeKeyAlarmRecordRecentAo(){
//		thread = new Thread(this);
//		thread.setDaemon(true);
//		thread.start();
//	}
//	
//	public static CspTimeKeyAlarmRecordRecentAo get(){
//		return ao;
//	}
//	public boolean insert(List<AlarmContext> list){
//		return dao.insert(list);
//	}
//	
//	public List<CspTimeKeyAlarmRecordPo> findAlarmInfo(String appName,String keyName,String property,String keyScope){
//		return dao.findAlarmInfo(appName, keyName, property, keyScope);
//	}
//	public boolean deleteAlarmInfoBefore(Date time){
//		return dao.deleteAlarmInfoBefore(time);
//	}
//	@Override
//	public void run() {
//		while(true){
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(new Date());
//			cal.add(Calendar.MINUTE, -5);
//			cal.set(Calendar.SECOND, 0);
//			dao.deleteAlarmInfoBefore(cal.getTime());
//			try {
//				Thread.sleep(60000*5);
//			} catch (InterruptedException e) {
//				logger.info(e);
//			}
//		}
//	}
//	public static void main(String args[]){
//		CspTimeKeyAlarmRecordRecentAo.get().findAlarmInfo("detail", "Exception", "E-times","HOST");
//	}
//}
