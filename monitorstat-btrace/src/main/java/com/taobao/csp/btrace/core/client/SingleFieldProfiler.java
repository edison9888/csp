package com.taobao.csp.btrace.core.client;

import java.lang.reflect.Field;
import com.taobao.csp.btrace.core.BtraceUtils;
import com.taobao.csp.btrace.core.FieldProfilerInfo;
import com.taobao.csp.objectweb.asm.Type;

public class SingleFieldProfiler {


	/**
	 * @param className		class必须是.分割的，如：com.taobao.csp.btrace.core.client.SingleFieldProfiler
	 * @param fieldName
	 */
	public static void getStaticFieldValue(String className, String fieldName) {
		long id = Thread.currentThread().getId();	
		try {
			Class<?> cls = Class.forName(className);
			Field field = cls.getDeclaredField(fieldName);
			field.setAccessible(true);

			FieldProfilerInfo info = new FieldProfilerInfo();
			info.setThreadId(id);
			info.setStartValue(BtraceUtils.toObjectString(field.get(null)));
			info.setFieldType(Type.getInternalName(field.getType()));
			info.setFieldClassName(className);
			info.setFieldName(fieldName);
			info.setMethodName("");
			TransformerProxy.acceptProfilerInfo(info);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		try {
//			//			//class必须是.分割的。属性必须是public的
//			//			//com.taobao.csp.btrace.core.client.SingleFieldProfiler
//			//			Class cls = Class.forName("com.taobao.csp.btrace.core.client.SingleFieldProfiler");
//			//			
//			//			Field field = cls.getDeclaredField("sss");
//			//			field.setAccessible(true);
//			//			
//			//			System.out.println(BtraceUtils.toObjectString(field.get(null)));
//
//			//test
//			//new SingleFieldProfiler().getStaticFieldValue("com.taobao.csp.btrace.core.client.SingleFieldProfiler","sss");
//
//		}
//		catch (Throwable e) {
//			System.err.println(e);
//		}		
//	}
}
