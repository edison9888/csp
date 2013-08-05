
package com.taobao.monitor.dependent.control;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.dependent.ao.CspDependentCheckAo;
import com.taobao.monitor.dependent.dao.CspDependentCheckDao;
import com.taobao.monitor.dependent.po.AutoCheckDependentPo;

/**
 * 
 * @author xiaodu
 * @version 2011-5-10 下午01:33:29
 */
public class DependentCheckThread implements Runnable{
	
	private static final Logger logger = Logger.getLogger(DependentCheckThread.class);
	
	
	private static DependentCheckThread obj =null;
	
	public static synchronized void startup(){
		if(obj == null){
			obj = new DependentCheckThread();
		}
	}
	
	
	final private static String RUN_STATUS_WAIT = "wait_run";
	final private static String RUN_STATUS_RUNNING = "running";
	final private static String RUN_STATIS_FINISH = "run_finish";
	
	private Thread thread = null;
	
	
	private DependentCheckThread(){
		thread = new Thread(this);
		thread.setName("Thread - DependentCheckThread ");
		thread.start();
	}

	public void run() {
		List<AutoCheckDependentPo> list = CspDependentCheckAo.get().findAllunRunCheck(RUN_STATUS_WAIT);
		for(AutoCheckDependentPo entry: list){
			entry.setRunStartTime(new Date());
			entry.setRunStatus(RUN_STATUS_RUNNING);
			boolean b = CspDependentCheckAo.get().updateDependentCheck(entry);
			if(b){
				try {
					ControlBuild_tmp control = new ControlBuild_tmp(entry.getTargetId(),entry.getAppName(),entry.getUserName(),
							entry.getUserPwd(),entry.getAutoRunScriptId(),entry.getForbidIps());
					String result = control.testDependent();
					entry.setRunResult(result);
				} catch (IOException e) {
					entry.setRunResult("构建执行类失败!");
				}
				entry.setRunEndTime(new Date());
				entry.setRunStatus(RUN_STATIS_FINISH);
				CspDependentCheckAo.get().updateDependentCheck(entry);	
			}
		}
		
		
		try {
			Thread.sleep(60*1000*5);
		} catch (InterruptedException e) {
		}
		
		logger.info(this.thread.getName()+" running");
	}
	
	
	public static void main(String[] s){
		DependentCheckThread.startup();
		
		
	}

}
