
package com.taobao.csp.assign.slave;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author xiaodu
 * @version 2011-7-5 上午10:57:15
 */
public class ThreadPoolManager {
	

	 // 线程池维护线程的最少数量
	 private final static int CORE_POOL_SIZE = 3;

	 // 线程池维护线程的最大数量
	 private final static int MAX_POOL_SIZE = 10;

	 // 线程池维护线程所允许的空闲时间
	 private final static int KEEP_ALIVE_TIME = 0;
	
	 // 线程池所使用的缓冲队列大小
	 private final static int WORK_QUEUE_SIZE = 10;

	 // 任务调度周期
	 private final static int TASK_QOS_PERIOD = 10;

	 // 任务缓冲队列
	 private Queue<Runnable> taskQueue = new LinkedList<Runnable>();

	 /*
	  * 线程池超出界线时将任务加入缓冲队列
	  */
	 private RejectedExecutionHandler handler = new RejectedExecutionHandler() {
	  public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
	   taskQueue.offer(task);
	  }
	 };

	 /*
	  * 将缓冲队列中的任务重新加载到线程池
	  */
	 private Runnable accessBufferThread = new Runnable() {
	  public void run() {
	   if (hasMoreAcquire()) {
	    threadPool.execute(taskQueue.poll());
	   }
	  }
	 };

	 /*
	  * 创建一个调度线程池
	  */
	 private  ScheduledExecutorService scheduler = Executors
	   .newScheduledThreadPool(1);

	 /*
	  * 通过调度线程周期性的执行缓冲队列中任务
	  */
	 private  ScheduledFuture<?> taskHandler = scheduler.scheduleAtFixedRate(
	   accessBufferThread, 0, TASK_QOS_PERIOD, TimeUnit.MILLISECONDS);

	 /*
	  * 线程池
	  */
	 private ThreadPoolExecutor threadPool = null;

	 /*
	  * 将构造方法访问修饰符设为私有，禁止任意实例化。
	  */
	 public ThreadPoolManager(final ClassLoader cl) {
		 
		 threadPool = new ThreadPoolExecutor(
				   CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				   new ArrayBlockingQueue<Runnable>(WORK_QUEUE_SIZE), this.handler);
		 threadPool.setThreadFactory(new ThreadFactory(){
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setContextClassLoader(cl);
				return thread;
			}});

	 }


	 /*
	  * 消息队列检查方法
	  */
	 private boolean hasMoreAcquire() {
	  return !taskQueue.isEmpty();
	 }

	 /*
	  * 向线程池中添加任务方法
	  */
	 public void addExecuteTask(Runnable task) {
	  if (task != null) {
	   threadPool.execute(task);
	  }
	 }
	 
	 
	 public void addExecuteTask(Runnable task,ClassLoader cl) {
		  if (task != null) {
		   threadPool.execute(task);
		  }
	}

}
