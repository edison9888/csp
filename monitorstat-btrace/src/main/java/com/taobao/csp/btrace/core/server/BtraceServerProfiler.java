
package com.taobao.csp.btrace.core.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.csp.btrace.core.MethodInfo;
import com.taobao.csp.btrace.core.ProfilerInfo;
import com.taobao.csp.btrace.core.ThreadProfilerData;



/**
 * 
 * @author xiaodu
 * @version 2011-8-19 上午09:52:35
 */
public class BtraceServerProfiler {
	
	private final static int size = 65535;
	
	private ThreadProfilerData[] mThreadProfiler = new ThreadProfilerData[size];
	
	private int recordMethodId;
	
	private Map<String,MethodExecuteInfo> methodExecuteMap = new HashMap<String,MethodExecuteInfo>();
	
	private List<MethodParamExecuteInfo> paramExecuteList = new ArrayList<MethodParamExecuteInfo>();
	
	public Map<String, MethodExecuteInfo> getMethodExecuteMap() {
		return methodExecuteMap;
	}
	public List<MethodParamExecuteInfo> getParamExecuteList() {
		return paramExecuteList;
	}

	public void recordMethod(int recordMethodId){
		this.recordMethodId = recordMethodId;
		
	}

	public ThreadProfilerData[] getThreadProfiler(){
		return mThreadProfiler;
	}
	
	
	//每个线程号只保存最近的200个信息
	public void putProfilerData(ProfilerInfo info){
		
		long threadId = info.getThreadId();
		
		try{
			ThreadProfilerData thrData = mThreadProfiler[(int)threadId];
			
			if(thrData ==null) {
				thrData = new ThreadProfilerData();
				mThreadProfiler[(int)threadId] = thrData;
			}
			if(recordMethodId == info.getMethodId()){
				if(thrData.infoQueue.size()>0){
					ProfilerInfo p = thrData.infoQueue.peek();
					MethodInfo pMethod = p.getMethodInfo();
					String p_key = pMethod.getClassName()+":"+pMethod.getMethodName()+":"+pMethod.getCodeLine();
					MethodExecuteInfo e = methodExecuteMap.get(p_key);
					
					if(e == null){
						e = new MethodExecuteInfo();
						methodExecuteMap.put(p_key, e);
					}
					e.setExecuteNum(e.getExecuteNum()+1);
					
					long useTime = info.getEndTime()-info.getStartTime();
					
					e.setAverageExecuteTime(e.getAverageExecuteTime()+useTime);
					
					if(paramExecuteList.size()<5){
						MethodParamExecuteInfo param = new MethodParamExecuteInfo();
						param.setAverageExecuteTime(useTime);
						param.setParamValues(info.getParamValues());
						
						paramExecuteList.add(param);
					}else{
						
						MethodParamExecuteInfo param = new MethodParamExecuteInfo();
						param.setAverageExecuteTime(useTime);
						param.setParamValues(info.getParamValues());
						
						MethodParamExecuteInfo mp = paramExecuteList.get(0);
						if(mp.getAverageExecuteTime() <useTime){
							paramExecuteList.remove(0);
							paramExecuteList.add(param);
							
							Collections.sort(paramExecuteList,new Comparator<MethodParamExecuteInfo>(){
								@Override
								public int compare(MethodParamExecuteInfo o1, MethodParamExecuteInfo o2) {
									return o1.getAverageExecuteTime() >o2.getAverageExecuteTime()?1:-1;
								}
								
							});
						}
						
						
					}
				}
			}
			
			
			
			if(thrData.infoQueue.size() > 500){
				try{
					thrData.infoQueue.pop();
				}catch (Exception e) {
				}
			}
			thrData.infoQueue.add(info);
			
			thrData.lastTime = System.currentTimeMillis();
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	
		
		
		
	}
	

	
	
	
}
