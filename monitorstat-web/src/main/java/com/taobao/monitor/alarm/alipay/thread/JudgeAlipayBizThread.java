package com.taobao.monitor.alarm.alipay.thread;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.taobao.monitor.alarm.alipay.AlipayBizAo;

/**
 * 固定线程，定时获取支付宝的业务，和淘宝发出的请求量进行对比
 * @author baiyan
 *
 */
public class JudgeAlipayBizThread {
	private final static int MINITE_BEFORE = -2;
	private final static int MINITE_INTERVAL = -3; //一次检查3分钟的数据
	
	private static long  taskDelayTime =  1;  // 每1分钟时获取一次支付宝的业务量，获取前两分钟的，保证支付宝的业务数据已汇总
	private static JudgeAlipayBizThread judgeAlipayBizThread = new JudgeAlipayBizThread();
	public static JudgeAlipayBizThread get(){
		return judgeAlipayBizThread;
	}
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	private Runnable runnable = new JudgeNotifyMessageRunnable();
	public void startup(){
		executor.scheduleWithFixedDelay(runnable, 0, taskDelayTime, TimeUnit.MINUTES);
	}

	
	class JudgeNotifyMessageRunnable implements Runnable{
		@Override
		public void run() {
			Calendar c  = Calendar.getInstance();
			c.add(Calendar.MINUTE,MINITE_BEFORE);
			Date end = c.getTime();
			c.add(Calendar.MINUTE,MINITE_INTERVAL);
			Date start = c.getTime();
			AlipayBizAo.get().checkAlipay(start, end);
		}
	}

}
