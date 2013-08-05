
package com.taobao.csp.loadrun.web;

import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.listen.LoadrunListen;
import com.taobao.csp.loadrun.core.run.BaseLoadrunTask;
import com.taobao.csp.loadrun.core.run.LoadrunFactory;

/**
 * 
 * @author xiaodu
 * @version 2011-7-11 ÉÏÎç11:33:32
 */
public class LoadrunWebContainer {
	
//	private LoadrunTarget target = null;
//	
//	private boolean run = false;
//	
//	private BaseLoadrunTask task = null;
	
	
	private LoadrunListen loadrunListen;
	
	
	private Map<Integer,BaseLoadrunTask> loadrunTask = new HashMap<Integer, BaseLoadrunTask>();
	
	public LoadrunListen getLoadrunListen() {
		return loadrunListen;
	}

	public void setLoadrunListen(LoadrunListen loadrunListen) {
		this.loadrunListen = loadrunListen;
	}

	public BaseLoadrunTask getTask(Integer appId) {
		synchronized (this) {
			BaseLoadrunTask task = loadrunTask.get(appId);
			return task;
		}
	}
	
	public boolean isRun(Integer appId){
		BaseLoadrunTask task = getTask(appId);
		if(task == null){
			return false;
		}else{
			return true;
		}
	}
	
	public Map<ResultKey, Double> getResult(Integer appId){
		
		synchronized (this) {
			BaseLoadrunTask task = loadrunTask.get(appId);
			if(task  != null){
				return task.getLoadResult();
			}
		}
		return null;
	}
	
	private LoadrunWebContainer(){
		
	}
	
	public void startup(Integer appId,LoadrunTarget target) throws Exception{
		synchronized (this) {
			BaseLoadrunTask task = loadrunTask.get(appId);
			if(task  == null){
				task = LoadrunFactory.createLoadrun(target);
				if(loadrunListen != null){
					task.addLoadrunListen(loadrunListen);
				}
				
				task.recordThreashold();
				task.runFetchs();
				loadrunTask.put(appId, task);
			}
		}
	}
	
	public void stop(Integer appId){
		synchronized (this) {
			BaseLoadrunTask task = loadrunTask.get(appId);
			if(task  != null){
				task.stopTask();
				task.waitForStop();
				loadrunTask.remove(appId);
			}
		}
	}
		
	public void doControl(Integer appId,String ... arg) throws Exception{
		synchronized (this) {
			BaseLoadrunTask task = loadrunTask.get(appId);
			if(task  != null){
				if(!task.isDoload())
					task.doLoadrun(arg);
			}
		}
		
		
	}
	
	public Map<Integer, BaseLoadrunTask> getAllManualTaskInRun() {
		return loadrunTask;
	}
}
