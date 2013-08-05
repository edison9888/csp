package com.taobao.monitor.alarm.trade.notify.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.taobao.monitor.alarm.trade.notify.NotifyMessageCountAo;

public class JudgeNotifyMessageThread {
	public static long  taskDelayTime =  2;  // ÿ2����ʱ��ȡһ��notify��Ϣ��������յ�����
	private static JudgeNotifyMessageThread notifyMessageCountThread = new JudgeNotifyMessageThread();
	public static JudgeNotifyMessageThread get(){
		return notifyMessageCountThread;
	}
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	private Runnable runnable = new JudgeNotifyMessageRunnable();
	public void startup(){
		executor.scheduleWithFixedDelay(runnable, 0, taskDelayTime, TimeUnit.MINUTES);
	}

	
	class JudgeNotifyMessageRunnable implements Runnable{
		@Override
		public void run() {
			NotifyMessageCountAo.get().checkNotifyMesageCountRecPro();
		}
	}

}
