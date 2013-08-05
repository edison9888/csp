package com.taobao.csp.dataserver.memcache.time;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.taobao.csp.dataserver.memcache.time.HashedWheelTimer.CSPThreadFactory;


public class CspTimeSchedu {
	public final static HashedWheelTimer timer = new HashedWheelTimer(
			new CSPThreadFactory(),30,TimeUnit.SECONDS,7);
	
	public static ThreadPoolExecutor time_execute = new ThreadPoolExecutor(
			2, 2,0L,TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	
//	public static void cspTask(String key,String property,int index,long createTime){
//		CspTimeTask ctt=new CspTimeTask(key,property,index,createTime,timer);
//		timer.newTimeout(ctt, 3, TimeUnit.MINUTES);
//	}
	public static void cspTask(HashedWheelTimeout timewheelout){
		//ctt.initStat();
		timer.newTimeout(timewheelout, 3, TimeUnit.MINUTES);
	}
	
	//测试代码
	public static void main(String[] args) throws InterruptedException {
		final HashedWheelTimer htimer = new HashedWheelTimer(100,
				TimeUnit.MILLISECONDS);
		final AbstraceTimeTask at=new AbstraceTimeTask() {
			public void run() {
//				System.out.println("deadline:"+((HashedWheelTimeout)this.getTimeout()).getDeadline());
//				System.out.println("current:"+System.currentTimeMillis());
				
				//System.out.println(ToStringBuilder.reflectionToString(timeout));
				//System.out.println(System.currentTimeMillis());
				//System.out.println("timeout 5");
				
				//System.exit(1);
				//htimer.newTimeout(this,6, TimeUnit.SECONDS);
			}
		};
		
		//htimer.newTimeout(at, 199, TimeUnit.MILLISECONDS);
//		// 10s打印一次
//		timer.newTimeout(new TimerTask() {
//			public void run(Timeout timeout) throws Exception {
//				System.out.println("timeout 10");
//			}
//		}, 10, TimeUnit.SECONDS);
		
//		long ss=System.currentTimeMillis();
//		
//		//测试在不同时间段下的任务重新计算
//		final CountDownLatch cdl=new CountDownLatch(10);
//		for(int i=0;i<10;i++){
//			// 5s打印一次
//			new Thread(){
//				
//				public void run(){
//
//					for(int j=0;j<100000;j++){
//						htimer.newTimeout(at, 501, TimeUnit.MILLISECONDS);
//					}
//					cdl.countDown();
//				}
//				
//			}.start();
//			
//		}
//		cdl.await();
//		System.out.println("over"+(System.currentTimeMillis()-ss));
//		System.out.println("over"+(System.currentTimeMillis()-ss));

	}
}