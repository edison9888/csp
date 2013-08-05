package com.taobao.csp.btrace.core.server;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import com.taobao.csp.btrace.core.FieldProfilerInfo;

public class FieldMapWraper {

	private static final int OUTOFTIME_MILLSECONDS = 1000*60*60*5;		//5����
	private Timestamp stamp = new Timestamp(new Date().getTime());		//ʱ���
	private HashMap<String, FieldProfilerInfo> fieldMap;				//<K,V> is <ThreadId,FieldProfilerInfo>
	private String key;		//keyֵ
	
	public FieldMapWraper(String key) {
		this.key = key;
	}
	
	//�ӳٳ�ʼ��
	public HashMap<String, FieldProfilerInfo> getFieldMap() {
		if(fieldMap == null) {
			this.fieldMap = new HashMap<String,FieldProfilerInfo>(); 
		}
		return fieldMap;
	}
	
	public long getStampTime() {
		return stamp.getTime();
	}
	
	//����ʱ�����ʱ�䵽��ǰ��ʱ��
	public void setStampToNow() {
		stamp = new Timestamp(new Date().getTime());
	} 
	
	//��������Ƿ񳬳�OUTOFTIME_MILLSECONDSʱ���û�������ˡ�
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
