package com.taobao.sentinel.sync;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.taobao.sentinel.bo.CommonBo;
import com.taobao.sentinel.bo.SyncBo;
import com.taobao.sentinel.util.SpringUtil;

public class SynchroOpsDiamond {
	
	public static Logger logger = Logger.getLogger(SynchroOpsDiamond.class);
	
	private static boolean started = false;
	
	private static SynchroOpsDiamond synchroOpsDiamond = new SynchroOpsDiamond();
	
	private LinkedBlockingQueue<String> changedApps = new LinkedBlockingQueue<String>(1000);
	
	private SynchroOpsDiamond() {}
	
	/***
	 * check ops free, if the ips of apps are changed
	 * repush corresponding config to diamond 
	 */
	public synchronized static void start() {
		if (!started) {
			logger.info("start SynchroOpsDiamond work.....");
			synchroOpsDiamond.synchOpsFree();
			synchroOpsDiamond.synchDiamond();
			
			started = true;
		}
	}
	
	public void synchOpsFree() {
		Executor executor = Executors.newFixedThreadPool(1);
		OpsFreeTask opsFreeTask = new OpsFreeTask();
		executor.execute(opsFreeTask);
	}
	
	public void synchDiamond() {
		Executor executor = Executors.newFixedThreadPool(1);
		DiamondTask diamondTask = new DiamondTask();
		executor.execute(diamondTask);
	}
	
	// update from ops free
	class OpsFreeTask implements Runnable {

		@Override
		public void run() {
			while(true) {
				logger.info("begin sync ops free data...");
				
				CommonBo commonBo = (CommonBo)SpringUtil.getBean("commonBo");
				SyncBo syncBo = (SyncBo)SpringUtil.getBean("syncBo");
				
				long startTime = System.currentTimeMillis();
				
				Set<String> appSet = commonBo.findAllApps();
				for (String appName : appSet) {
					boolean needSync = syncBo.synchApp(appName).size() > 0;
					if (needSync) {
						try {
							changedApps.put(appName);
						} catch (InterruptedException e) {
							logger.info("put queue error", e);
						}
					}
				}
				
				long endTime = System.currentTimeMillis();
				
				long useTime = endTime - startTime;
				logger.info("end sync. Cost time: " + useTime);
				
				try {
					TimeUnit.SECONDS.sleep(60 * 60);
				} catch (InterruptedException e) {
					logger.error("OpsFreeTask sleep error", e);
				}
			}
			
		}

	}
	
	// push changed app to diamond
	class DiamondTask implements Runnable {

		@Override
		public void run() {
			while(true) {
				String appName = null;
				try {
					appName = changedApps.take();
				} catch (InterruptedException e) {
					logger.info("take queue error", e);
				}
				
				CommonBo commonBo = (CommonBo)SpringUtil.getBean("commonBo");
				if (appName != null) {
					logger.info("auto push data to diamond: " + appName);
					commonBo.autoPushConfig(appName);
				}
			}
		}
	}

}
