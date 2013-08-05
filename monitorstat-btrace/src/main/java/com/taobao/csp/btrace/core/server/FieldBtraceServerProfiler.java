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
 * @version 2011-10-25 下午19:47:09
 * 新设计的FieldBtraceServerProfiler，使用HashMap代替数组
 */
public class FieldBtraceServerProfiler {

	private final static int SIZE = 65535;

	//新设计的数据结构，一个线程会定时清理这个map中过期的Class
	//格式为HashMap<"classname-methodname-fieldname",Hashmap<threadid,FieldProfilerInfo>>
	private  HashMap<String, FieldMapWraper> classMap = new HashMap<String, FieldMapWraper>();

//	static {
//		//开启一个线程定期去删除一些过期的Class
//		Thread thread = new Thread() {
//			public void run() {
//				while(true) {
//					
//					synchronized (this) {	//考虑到系统监控的class不会太多，遍历的性能应该还好，所以此处使用synchronized
//						ArrayList<FieldMapWraper> fieldMapValueList = (ArrayList<FieldMapWraper>) classMap.values();
//						for(FieldMapWraper fieldMapWraper: fieldMapValueList) {
//							if(fieldMapWraper.isOutOfTime()) {
//								classMap.remove(fieldMapWraper.getKey());
//							}
//						}
//					}			
//					
//					//1分钟去删除一次
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
	
	//这里需要并发控制的
	public synchronized void putProfilerData(FieldProfilerInfo info){
		try {
			if(classMap.size() >= SIZE) {	//移出最老的那个Class
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

	//按照FieldMapWraper的Timestamp排序，删除最老的那个Class
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
	 * @param info 根据info的classname,methodname,fieldname 拼接成一个key值
	 * @return
	 */
	private String generateKey(FieldProfilerInfo info) {
		return generateKey(info.getFieldClassName(), info.getMethodName(), info.getFieldName());
	}
	
	/**
	 * 重载，根据下面三个参数生成map的key值
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
