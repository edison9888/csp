package com.taobao.monitor.alarm.alipay.thread;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.taobao.monitor.alarm.alipay.AlipayBizAo;

/**
 * �̶��̣߳���ʱ��ȡ֧������ҵ�񣬺��Ա����������������жԱ�
 * @author baiyan
 *
 */
public class JudgeAlipayBizThread {
	private final static int MINITE_BEFORE = -2;
	private final static int MINITE_INTERVAL = -3; //һ�μ��3���ӵ�����
	
	private static long  taskDelayTime =  1;  // ÿ1����ʱ��ȡһ��֧������ҵ��������ȡǰ�����ӵģ���֤֧������ҵ�������ѻ���
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
