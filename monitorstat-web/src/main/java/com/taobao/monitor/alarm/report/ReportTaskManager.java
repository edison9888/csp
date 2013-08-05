package com.taobao.monitor.alarm.report;

import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.time.DateUtils;

public class ReportTaskManager implements ServletContextListener {
	/**
	 * �����ձ����ʱ��
	 */
	public static final long PERIOD_DAY = DateUtils.MILLIS_PER_DAY;
	
	/**
	 * �ձ����͵�Сʱ
	 */
	public static final int REPORT_TIME_HOUR = 9;
	
	/**
	 * �ձ����͵ķ�
	 */
	public static final int REPORT_TIME_MINUTE = 10;
	
	public Timer timer;
	
	public void contextInitialized(ServletContextEvent event){
		timer = new Timer("CSP�澯����", true);
		timer.schedule(new ReportTimerTask(), getReportTimeDelay() , PERIOD_DAY); 
	}
	
	/**
	 * �ӷ��ձ���ʱ��ʼ�������ִ��ʱ��
	 * @return
	 */
	private long getReportTimeDelay(){
		Calendar cal = Calendar.getInstance();
		long nowMillis = cal.getTimeInMillis();
		cal.set(Calendar.HOUR_OF_DAY, REPORT_TIME_HOUR);
		cal.set(Calendar.MINUTE, REPORT_TIME_MINUTE);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return ((PERIOD_DAY + cal.getTimeInMillis()-nowMillis)%PERIOD_DAY);
	}
	
	/**  
	  * ��WebӦ�ý���ʱֹͣ����  
	  */  
	 public void contextDestroyed(ServletContextEvent event) {   
		 timer.cancel(); // ��ʱ������   
	 } 
}
