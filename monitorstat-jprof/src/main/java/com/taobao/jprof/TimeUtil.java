
package com.taobao.jprof;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author xiaodu
 * @version 2010-8-10 ионГ08:59:18
 */
public class TimeUtil {
	
	static {
		TimeThread thread = new TimeThread();
		thread.setDaemon(true);
		thread.start();
	}
	
	private  static AtomicLong atomic = new  AtomicLong();
	
	public static long getNanoTime(){
		return atomic.get();
	}
	
	
	
	private static class TimeThread extends Thread{
		
		public void run(){
			while(true){				
				long time = System.nanoTime();
				atomic.set(time);
			}
		}
		
	}
	

}
