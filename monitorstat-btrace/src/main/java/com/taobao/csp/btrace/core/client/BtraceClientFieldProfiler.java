package com.taobao.csp.btrace.core.client;

import com.taobao.csp.btrace.core.BtraceUtils;
import com.taobao.csp.btrace.core.FieldProfilerInfo;
import com.taobao.csp.btrace.core.FieldThreadProfilerData;

/***
 * 
 * @author zhongting.zy
 * @version 2011-10-24 ����13:39:35
 */
public class BtraceClientFieldProfiler {
	private final static int size = 65535;		//����һ���̵߳���������
	/*ThreadProfilerfieldData ������ݽṹͨ��ThreadId������ֻ��Ӳ��� */
	private static FieldThreadProfilerData[] mThreadProfiler = new FieldThreadProfilerData[size];

	public static FieldThreadProfilerData[] getThreadProfiler(){
		return mThreadProfiler;
	}

	/**
	 * ִ�м�ⷽ��ǰҪִ�еķ���
	 * @param value				ֱ�Ӵ�ջ�ж�ȡ��Field��ֵ
	 * @param fieldClassName	Field������Class������
	 * @param fieldType			Field������
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
			
			//�Ƴ��������뵽�������
			FieldProfilerInfo info = thrData.infoQueue.pop();
			info.setEndValue(BtraceUtils.toObjectString(value));
			TransformerProxy.acceptProfilerInfo(info);
		}catch (Exception e) {
		}
	}	
	
}
