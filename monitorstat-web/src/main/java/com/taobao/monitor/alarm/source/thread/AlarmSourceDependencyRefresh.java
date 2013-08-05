package com.taobao.monitor.alarm.source.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.taobao.monitor.alarm.source.AlarmSourceConclude;

public class AlarmSourceDependencyRefresh {
	private long  initalDelayTime =  1;   //一个小时获取一次
	private long  taskDelayTime =  1; 
	private static AlarmSourceDependencyRefresh ops = new AlarmSourceDependencyRefresh();
	public static AlarmSourceDependencyRefresh get(){
		return ops;
	}
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	private Runnable runnable = new RefreshSourceDenpendThread();
	public void startup(){
		executor.scheduleWithFixedDelay(runnable, initalDelayTime, 
				taskDelayTime, TimeUnit.HOURS);
	}

	class RefreshSourceDenpendThread implements Runnable{

		@Override
		public void run() {
			AlarmSourceConclude.getKeySourceDefine();
			
		}
		
	}
}
