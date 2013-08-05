
package com.taobao.csp.btrace.core.client;

import com.taobao.csp.btrace.core.ProfilerInfo;
import com.taobao.csp.btrace.core.ThreadProfilerData;



/**
 * 
 * @author xiaodu
 * @version 2011-8-19 ÉÏÎç09:52:35
 */
public class BtraceClientProfiler {
	
	private final static int size = 65535;
	
	private static ThreadProfilerData[] mThreadProfiler = new ThreadProfilerData[size];
	
	
	
	
	
	public static ThreadProfilerData[] getThreadProfiler(){
		return mThreadProfiler;
	}
	

	
	public static void start(int methodId){
		long id = Thread.currentThread().getId();		
		if(id>size){
			return ;
		}
		
		try{
			ThreadProfilerData thrData = mThreadProfiler[(int)id];
			if(thrData==null){
				thrData = new ThreadProfilerData();
				mThreadProfiler[(int)id] = thrData;
			}
			ProfilerInfo info = new ProfilerInfo();
			info.setThreadId(id);
			info.setMethodId(methodId);
			info.setMethodStackNum(thrData.statckNum++);
			info.setStartTime(System.nanoTime());
			thrData.infoQueue.add(info);

		}catch (Exception e) {
		}
	}
	
	public static void setParamValues(String[] paramValues){
		long id = Thread.currentThread().getId();		
		if(id>size){
			return ;
		}
		
		try{
			ThreadProfilerData thrData = mThreadProfiler[(int)id];
			if(thrData==null){
				thrData = new ThreadProfilerData();
				mThreadProfiler[(int)id] = thrData;
			}
			ProfilerInfo info = thrData.infoQueue.peek();
			info.setParamValues(paramValues);
		}catch (Exception e) {
		}
	}
	
	
	public static void setReturnValue(String obj){
		long id = Thread.currentThread().getId();		
		if(id>size){
			return ;
		}

		try{
			ThreadProfilerData thrData = mThreadProfiler[(int)id];
			if(thrData==null){
				thrData = new ThreadProfilerData();
				mThreadProfiler[(int)id] = thrData;
			}
			ProfilerInfo info = thrData.infoQueue.peek();
			info.setReturnValue(obj);
		}catch (Exception e) {
		}
		
	}
	
	
	public static void end(int methodId){
		long id = Thread.currentThread().getId();		
		if(id>size){
			return ;
		}
		
		try{
			ThreadProfilerData thrData = mThreadProfiler[(int)id];
			if(thrData==null){
				thrData = new ThreadProfilerData();
				mThreadProfiler[(int)id] = thrData;
			}
			
			ProfilerInfo info = thrData.infoQueue.pop();
			info.setEndTime(System.nanoTime());
			thrData.statckNum--;
			
			TransformerProxy.acceptProfilerInfo(info);

		}catch (Exception e) {
		}
		
		
	}
	
	
	
}
