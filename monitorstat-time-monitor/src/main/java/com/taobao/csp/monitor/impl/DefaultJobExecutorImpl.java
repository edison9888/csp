
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.taobao.csp.monitor.GroupMaker;
import com.taobao.csp.monitor.Job;
import com.taobao.csp.monitor.JobExecutor;
import com.taobao.csp.monitor.JobInfo;

/**
 * @author xiaodu
 *
 * ÏÂÎç3:07:42
 */
public class DefaultJobExecutorImpl implements JobExecutor,Runnable{
	
	private static final Logger logger =  Logger.getLogger("JobExecutor");
	
	private Map<String,Job> jobMap = new ConcurrentHashMap<String, Job>();
	
	private String name;
	
	private Thread thread = null;
	
	private GroupMaker group;
	
	//private int THREAD_POOL_NUM = Runtime.getRuntime().availableProcessors()*6;
	private int THREAD_POOL_NUM = 20;
	
	private ThreadPoolExecutor executor = new ThreadPoolExecutor(THREAD_POOL_NUM, THREAD_POOL_NUM,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
	
	
	private AtomicBoolean stop = new AtomicBoolean(false);
	
	public DefaultJobExecutorImpl(String name,GroupMaker group){
		this.name = name;
		thread = new Thread(this);
		thread.setName("DefaultJobExecutorImpl - "+name);
		thread.start();
		this.group = group;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public  boolean assignJob(JobInfo job) {
		synchronized(this){
			Job b = jobMap.remove(job.getJobID());
			try{
				 b = new DefaultJob(job);
				
				jobMap.put(job.getJobID(), b);
				
				logger.info("JobExecutor["+name+" ] group ["+group.getGroupName()+"] get job "+job.getAppName()+""+job.getIp()+"$"+job.getSite()+"$"+job.getJobName()+"$"+job.getFilepath()+"");
				return true;
			}catch (Exception e) {
				logger.error("JobExecutor["+name+" ] get job"+job.getIp()+"$"+job.getJobName()+"$"+job.getFilepath()+"", e);
			}
		}
		return false;
	}
	

	@Override
	public  boolean updateJob(JobInfo job){
		synchronized (this) {
			return assignJob(job);
		}
	}

	@Override
	public   void releaseAllJob() {
		synchronized (this) {
			jobMap.clear();
			logger.info("JobExecutor["+name+" ] release all job "+jobMap.size());
		}
	}

	@Override
	public  void releaseJob(String jobId) {
		synchronized (this) {
			Job job = jobMap.remove(jobId);
			if(job != null){
				job.close();
				
				logger.info("JobExecutor["+name+" ] release job"+job.getJobInfo().getIp()+"$"+job.getJobInfo().getJobName()+"$"+job.getJobInfo().getFilepath()+"");
				
			}
		}
	}

	@Override
	public void releaseAppJob(String appName) {
		synchronized (this) {
			Iterator<Map.Entry<String,Job>> it = jobMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,Job> entry = it.next();
				
				if(entry.getValue().getJobInfo().getAppName().equals(appName)){
					Job job = entry.getValue();
					it.remove();
					logger.info("JobExecutor["+name+" ] release job"+job.getJobInfo().getIp()+"$"+job.getJobInfo().getJobName()+"$"+job.getJobInfo().getFilepath()+"");
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			synchronized (stop) {
				stop.wait(20000);
			}
		} catch (InterruptedException e) {
		}
		while(!stop.get()){
			try {
				synchronized (stop) {
					stop.wait(5000);
				}
			} catch (InterruptedException e) {
			}
			logger.info("JobExecutor["+name+" ] have jobs "+jobMap.size()+"");
			Iterator<Map.Entry<String,Job>> it = jobMap.entrySet().iterator();
			while(it.hasNext()&&!stop.get()){
				
				Map.Entry<String,Job> entry = it.next();
				
				if(entry.getValue().isFire()){
						executor.submit(entry.getValue());
				}
			}
			reportInfo();
		}
	}

	@Override
	public void stop() {
		stop.set(true);
		synchronized (stop) {
			stop.notifyAll();
		}
		executor.shutdownNow();
		
	
		
	}
	
	
	public void reportInfo(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("jobCount", ""+jobMap.size());
		map.put("executor", "[ActiveCount:"+executor.getActiveCount()+",PoolSize:"
		+executor.getPoolSize()+",CorePoolSize:"+executor.getCorePoolSize()+",TaskCount:"+executor.getTaskCount()+",CompletedTaskCount:"+executor.getCompletedTaskCount()+"]");
		map.put("jvm", "[maxMemory:"+Runtime.getRuntime().maxMemory()+",totalMemory:"+Runtime.getRuntime().totalMemory()+",freeMemory:"+Runtime.getRuntime().freeMemory()+"]");
		
		group.activateHeart(map);
	}
	
	
	

	

}
