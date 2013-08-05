package com.taobao.monitor.alarm.opsfree;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ���ն��ڵ�ʱ������ȥopsfree��ѯָ��Ӧ�õĻ������������޸Ļ��������ķ�ֵ��
 * @author baiyan
 *
 */
public class OpsFreeAppHostAmountThread  {
	private long  initalDelayTime =  1;   //һ��Сʱ��ȡһ��
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
