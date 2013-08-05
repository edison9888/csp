
/**
 * monitorstat-log
 */
package com.taobao.csp.log.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.taobao.csp.log.MonitorLog;

/**
 * @author xiaodu
 *
 * 
 */
public class TestLog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int threadNum=1;threadNum<2;threadNum++){
			System.out.println("thread num£º"+threadNum);
			StringBuilder sb = new StringBuilder();
			for(int size=0;size<1;size++){
			CountDownLatch  c = new CountDownLatch(threadNum);
			
			List<AddThread> lsit = new ArrayList<AddThread>();
			
			for(int i=0;i<5;i++){
				AddThread thread1 = new AddThread(c);
				thread1.start();
				lsit.add(thread1);
			}
			
			try {
				c.await();
			} catch (InterruptedException e) {
			}
			long time = 0;
			for(AddThread t:lsit){
				time+=t.time;
			}
			System.out.println(time);
			}
			System.out.println(sb);
		}
		

	}
	
	private static class AddThread extends Thread{
		
		public CountDownLatch count;
		
		public long time = 0l;
		
		public AddThread(CountDownLatch count){
			this.count = count;
		}
		
		public void run(){
			
			for(int i=0;i<10000000;i++)
				MonitorLog.addStat("key1", "key1", "key1", 1l, 1l);
			
			long s = System.currentTimeMillis();
			for(int i=0;i<10000000;i++)
				MonitorLog.addStat("key1", "key1", "key1", 1l, 1l);
			time = (System.currentTimeMillis() - s);
			count.countDown();
			
		}
		
	}

}
