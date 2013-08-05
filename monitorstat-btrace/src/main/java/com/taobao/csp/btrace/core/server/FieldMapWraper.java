package com.taobao.csp.btrace.core.server;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import com.taobao.csp.btrace.core.FieldProfilerInfo;

public class FieldMapWraper {

	private static final int OUTOFTIME_MILLSECONDS = 1000*60*60*5;		//5分钟
	private Timestamp stamp = new Timestamp(new Date().getTime());		//时间戳
	private HashMap<String, FieldProfilerInfo> fieldMap;				//<K,V> is <ThreadId,FieldProfilerInfo>
	private String key;		//key值
	
	public FieldMapWraper(String key) {
		this.key = key;
	}
	
	//延迟初始化
	public HashMap<String, FieldProfilerInfo> getFieldMap() {
		if(fieldMap == null) {
			this.fieldMap = new HashMap<String,FieldProfilerInfo>(); 
		}
		return fieldMap;
	}
	
	public long getStampTime() {
		return stamp.getTime();
	}
	
	//更新时间戳的时间到当前的时间
	public void setStampToNow() {
		stamp = new Timestamp(new Date().getTime());
	} 
	
	//这个对象是否超出OUTOFTIME_MILLSECONDS时间后没被更新了。
	public boolean isOutOfTime() {
		System.out.println(new Date().getTime() - stamp.getTime());
		boolean iFlag = (new Date().getTime() - stamp.getTime() > OUTOFTIME_MILLSECONDS);
		System.out.println(new Date().getTime() - stamp.getTime());
		return iFlag;
	}

	public String getKey() {
		return key;
	} 
	
//	public static void main(String[] args) {
//		FieldMapWraper obj = new FieldMapWraper(); 
//		System.out.println(obj.isOutOfTime());
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(obj.isOutOfTime());
//	}
}
