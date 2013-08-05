package com.taobao.monitor.alarm.report;

import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.time.DateUtils;

public class ReportTaskManager implements ServletContextListener {
	/**
	 * 报警日报间隔时间
	 */
	public static final long PERIOD_DAY = DateUtils.MILLIS_PER_DAY;
	
	/**
	 * 日报发送的小时
	 */
	public static final int REPORT_TIME_HOUR = 9;
	
	/**
	 * 日报发送的分
	 */
	public static final int REPORT_TIME_MINUTE = 10;
	
	public Timer timer;
	
	public void contextInitialized(ServletContextEvent event){
		timer = new Timer("CSP告警报表", true);
		timer.schedule(new ReportTimerTask(), getReportTimeDelay() , PERIOD_DAY); 
	}
	
	/**
	 * 从发日报的时开始计算程序执行时间
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
	  * 在Web应用结束时停止任务  
	  */  
	 public void contextDestroyed(ServletContextEvent event) {   
		 timer.cancel(); // 定时器销毁   
	 } 
}
