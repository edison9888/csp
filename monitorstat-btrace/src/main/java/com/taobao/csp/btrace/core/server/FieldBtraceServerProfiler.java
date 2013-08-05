package com.taobao.csp.btrace.core.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.taobao.csp.btrace.core.FieldProfilerInfo;
/**
 * 
 * @author zhongting.zy
 * @version 2011-10-25 ����19:47:09
 * ����Ƶ�FieldBtraceServerProfiler��ʹ��HashMap��������
 */
public class FieldBtraceServerProfiler {

	private final static int SIZE = 65535;

	//����Ƶ����ݽṹ��һ���̻߳ᶨʱ�������map�й��ڵ�Class
	//��ʽΪHashMap<"classname-methodname-fieldname",Hashmap<threadid,FieldProfilerInfo>>
	private  HashMap<String, FieldMapWraper> classMap = new HashMap<String, FieldMapWraper>();

//	static {
//		//����һ���̶߳���ȥɾ��һЩ���ڵ�Class
//		Thread thread = new Thread() {
//			public void run() {
//				while(true) {
//					
//					synchronized (this) {	//���ǵ�ϵͳ��ص�class����̫�࣬����������Ӧ�û��ã����Դ˴�ʹ��synchronized
//						ArrayList<FieldMapWraper> fieldMapValueList = (ArrayList<FieldMapWraper>) classMap.values();
//						for(FieldMapWraper fieldMapWraper: fieldMapValueList) {
//							if(fieldMapWraper.isOutOfTime()) {
//								classMap.remove(fieldMapWraper.getKey());
//							}
//						}
//					}			
//					
//					//1����ȥɾ��һ��
//					try {
//						TimeUnit.MINUTES.sleep(1);
//						Thread.sleep(111);
//					} catch (InterruptedException e) {
//						//e.printStackTrace();
//					}
//				}
//			}
//		};
//		thread.start();
//	}
	
	//������Ҫ�������Ƶ�
	public synchronized void putProfilerData(FieldProfilerInfo info){
		try {
			if(classMap.size() >= SIZE) {	//�Ƴ����ϵ��Ǹ�Class
				removeOldest();
			}
			String key = generateKey(info);
			FieldMapWraper fieldMapWraper = classMap.get(key);
			if(fieldMapWraper == null) {
				fieldMapWraper = new FieldMapWraper(key);
			}
			HashMap<String,FieldProfilerInfo> fieldMap = fieldMapWraper.getFieldMap();
			fieldMap.put(String.valueOf(info.getThreadId()), info);
			fieldMapWraper.setStampToNow();
			classMap.put(key, fieldMapWraper);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//����FieldMapWraper��Timestamp����ɾ�����ϵ��Ǹ�Class
	public  FieldMapWraper removeOldest() {
		//collection to list
		Collection<FieldMapWraper> collection = classMap.values();
		FieldMapWraper[] type = new FieldMapWraper[0];
		FieldMapWraper[] array = (FieldMapWraper[]) collection.toArray(type);
		List<FieldMapWraper> fieldMapValueList = Arrays.asList(array);
		
		Collections.sort(fieldMapValueList, new Comparator<FieldMapWraper>(){
			@Override
			public int compare(FieldMapWraper o1, FieldMapWraper o2) {
				return o1.getStampTime() > o2.getStampTime()? 1 :-1;
			}
		});	
		return classMap.remove(fieldMapValueList.get(0).getKey());
	}

	/**
	 * @param info ����info��classname,methodname,fieldname ƴ�ӳ�һ��keyֵ
	 * @return
	 */
	private String generateKey(FieldProfilerInfo info) {
		return generateKey(info.getFieldClassName(), info.getMethodName(), info.getFieldName());
	}
	
	/**
	 * ���أ���������������������map��keyֵ
	 * @param fieldClassName
	 * @param methodName
	 * @param fieldName
	 * @return
	 */
	public static String generateKey(String fieldClassName, String methodName, String fieldName) {
		if(fieldClassName != null) {
			fieldClassName = fieldClassName.replaceAll("\\.", "/");
		}
		return new StringBuilder(fieldClassName).append("-").append(methodName)
				.append("-").append(fieldName).toString();
	}
	
	public HashMap<String, FieldMapWraper> getClassMap() {
		return classMap;
	}	
}
