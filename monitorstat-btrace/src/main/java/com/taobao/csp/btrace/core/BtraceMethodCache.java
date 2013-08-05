package com.taobao.csp.btrace.core;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.objectweb.asm.Type;

/**
 * 对所有注入成功的method都保存一个对应的methodid,这样可以减少profiler的过程
 * 中产生大量的小对象.
 * @author luqi
 * 
 */
public class BtraceMethodCache {

	public static final int INIT_CACHE_SIZE = 512;
	public static Vector<MethodInfo> mCacheMethods = new Vector<MethodInfo>(INIT_CACHE_SIZE);
	public static Map<String,Integer> velocityVmMap = new ConcurrentHashMap<String, Integer>();
	
	public synchronized static int request() {
		mCacheMethods.add(new MethodInfo());
		return mCacheMethods.size() - 1;
	}
	
	public synchronized static void setClassName(int id,String className){
		MethodInfo info = mCacheMethods.get(id);
		if(info != null){
			info.setClassName(className);
		}
	}
	
	public synchronized static void setMethodName(int id,String methodName){
		MethodInfo info = mCacheMethods.get(id);
		if(info != null){
			info.setMethodName(methodName);
		}
	}
	
	public synchronized static void setClassMethodName(int id,String className,String methodName,String desc){
		MethodInfo info = mCacheMethods.get(id);
		if(info != null){
			info.setClassName(className);
			info.setMethodName(methodName);
			Type[] paramTypes = Type.getArgumentTypes(desc);
			
			if(paramTypes != null){
				String[] t = new String[paramTypes.length];
				for(int i=0;i<paramTypes.length;i++){
					t[i] = paramTypes[i].getDescriptor();
				}
				info.setParamDesc(t);
			}
			Type returnType = Type.getReturnType(desc);
			info.setReturnDesc(returnType.getDescriptor());
		}
	}
	
	public synchronized static void setClassMethodParameters(int id,String paramName){
		MethodInfo info = mCacheMethods.get(id);
		if(info != null){
			info.getParametersNames().add(paramName);
		}
	}
	
	
	
	
	public synchronized static void setCodeLine(int id,int codeline){
		MethodInfo info = mCacheMethods.get(id);
		if(info != null){
			info.setCodeLine(codeline);
		}
	} 
	
	




}
