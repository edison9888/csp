package com.taobao.csp.btrace.core.client;

import com.taobao.csp.btrace.core.BtraceUtils;
import com.taobao.csp.btrace.core.FieldProfilerInfo;
import com.taobao.csp.btrace.core.FieldThreadProfilerData;

/***
 * 
 * @author zhongting.zy
 * @version 2011-10-24 上午13:39:35
 */
public class BtraceClientFieldProfiler {
	private final static int size = 65535;		//限制一下线程的最大访问数
	/*ThreadProfilerfieldData 这个数据结构通过ThreadId来管理，只添加不释 */
	private static FieldThreadProfilerData[] mThreadProfiler = new FieldThreadProfilerData[size];

	public static FieldThreadProfilerData[] getThreadProfiler(){
		return mThreadProfiler;
	}

	/**
	 * 执行检测方法前要执行的方法
	 * @param value				直接从栈中读取到Field的值
	 * @param fieldClassName	Field所属的Class的名称
	 * @param fieldType			Field的类型
	 */
	public static void start(Object value, String fieldClassName, String fieldType, String methodName, String fieldName) {
		//System.out.println("start:" + value);
		long id = Thread.currentThread().getId();		
		if(id > size) {
			return ;
		}

		try{
			FieldThreadProfilerData thrData = mThreadProfiler[(int)id];
			if(thrData == null){
				thrData = new FieldThreadProfilerData();
				mThreadProfiler[(int)id] = thrData;
			}
			
			FieldProfilerInfo info = new FieldProfilerInfo();
			info.setThreadId(id);
			info.setStartValue(BtraceUtils.toObjectString(value));	//object string -> json string
			info.setFieldType(fieldType);
			info.setFieldClassName(fieldClassName);
			info.setMethodName(methodName);
			info.setFieldName(fieldName);
			thrData.infoQueue.add(info);

		}catch (Exception e) {
		}
	}	
	
	public static void end(Object value){
		long id = Thread.currentThread().getId();		
		if(id > size) {
			return ;
		}
		
		try {
			FieldThreadProfilerData thrData = mThreadProfiler[(int)id];
			if(thrData == null){
				thrData = new FieldThreadProfilerData();
				mThreadProfiler[(int)id] = thrData;
			}
			
			//移出，并放入到传输队列
			FieldProfilerInfo info = thrData.infoQueue.pop();
			info.setEndValue(BtraceUtils.toObjectString(value));
			TransformerProxy.acceptProfilerInfo(info);
		}catch (Exception e) {
		}
	}	
	
}
