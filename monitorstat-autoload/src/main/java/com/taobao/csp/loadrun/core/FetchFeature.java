
package com.taobao.csp.loadrun.core;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.taobao.csp.loadrun.core.fetch.CpuFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.fetch.LoadFetchTaskImpl;
import com.taobao.csp.loadrun.core.listen.FetchListen;
import com.taobao.csp.loadrun.core.result.FetchResult;
import com.taobao.csp.loadrun.core.result.ResultDetailCell;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;
import com.taobao.csp.loadrun.core.valid.Valid;


/**
 * 
 * @author xiaodu
 * @version 2011-6-22 下午02:50:11
 */
public class FetchFeature {
	
	private IFetchTask fetchTask;
	
	
	private Valid validFetch = null;
	
	public FetchFeature(IFetchTask fetchTask){
		this.fetchTask = fetchTask;
	}
	
	public String getTaskName(){
		return fetchTask.getTaskName();
	}
	
	/***
	 * cpu和load采集被停为采集异常
	 * @return
	 */
	public boolean fetchException(){
		boolean exception = false;
		if ((fetchTask instanceof CpuFetchTaskImpl) || (fetchTask instanceof LoadFetchTaskImpl)) {
			if (!fetchTask.isRun()) {
				exception = true;
			}
		}
		return exception;
	}
	
	
	public void addValidFetch(Valid valid){
		this.validFetch = valid;
	}

	public void addListenValid(FetchListen valid){
		fetchTask.addFetchListen(valid);
	}
	
	public boolean doValid(){
		if(this.validFetch != null){
			if(getResult() != null){
				return this.validFetch.valid(getResult());
			}
		}
		return true;
	}
	
	public void close(){
		fetchTask.stopFetch();
	}
	
	
	public FetchResult getResult(){
		return fetchTask.getResult();
	}
	
	public Map<ResultDetailKey, ResultDetailCell> getResultDetail(){
		return fetchTask.getResultDetail();
	}
	
	
	public void runFetch(CountDownLatch count){
		fetchTask.doTask(count);
	}

}
