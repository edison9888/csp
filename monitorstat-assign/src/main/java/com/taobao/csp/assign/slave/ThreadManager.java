
package com.taobao.csp.assign.slave;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 * 
 * @author xiaodu
 * @version 2011-7-5 ÉÏÎç10:57:15
 */
public class ThreadManager {
	
	
	 private static final ThreadFactory daemonizedThreadFactory = new ThreadFactory() {
	        ThreadFactory delegate = Executors.defaultThreadFactory();
	        @Override
	        public Thread newThread(Runnable r) {
	            Thread result = delegate.newThread(r);
	            result.setDaemon(true);
	            return result;
	        }
	    };
	    
	
	 private static final ExecutorService serializedExecutor =  Executors.newFixedThreadPool(10);
	 
	 
	 public static void addExecuteTask(Runnable task) {
		 serializedExecutor.execute(task);
	}

}
