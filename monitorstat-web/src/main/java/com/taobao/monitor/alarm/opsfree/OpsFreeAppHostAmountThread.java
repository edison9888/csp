package com.taobao.monitor.alarm.opsfree;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 按照定期的时间间隔，去opsfree查询指定应用的机器数量，来修改机器数量的阀值表
 * @author baiyan
 *
 */
public class OpsFreeAppHostAmountThread  {
	private long  initalDelayTime =  1;   //一个小时获取一次
	private long  taskDelayTime =  1; 
	private static OpsFreeAppHostAmountThread ops = new OpsFreeAppHostAmountThread();
	public static OpsFreeAppHostAmountThread get(){
		return ops;
	}
	private ScheduledThreadPoolExecutor executorForConfigServer = new ScheduledThreadPoolExecutor(1);
	
	private ScheduledThreadPoolExecutor executorForLoadBalance = new ScheduledThreadPoolExecutor(1);
	
	private Runnable runnableForConfigServer = new GetAppHostAmountForConfigServerRunnable();
	private Runnable runnableForLoadBalance = new GetAppHostAmountRunnable();
	
	public void startup(){
		executorForConfigServer.scheduleWithFixedDelay(runnableForConfigServer, initalDelayTime, 
				taskDelayTime, TimeUnit.HOURS);
		
		executorForLoadBalance.scheduleWithFixedDelay(runnableForLoadBalance, initalDelayTime, 
				taskDelayTime, TimeUnit.HOURS);
	}

	public static void main(String arsg[]){
		 Runnable task1 = new GetAppHostAmountForConfigServerRunnable();
		 task1.run();
		 
		 Runnable task2 = new GetAppHostAmountRunnable();
		 task2.run();
		 
	}
}
