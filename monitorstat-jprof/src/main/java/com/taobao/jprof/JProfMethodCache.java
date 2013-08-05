/**
 * 
 */
package com.taobao.jprof;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对所有注入成功的method都保存一个对应的methodid,这样可以减少profiler的过程
 * 中产生大量的小对象.
 * @author luqi
 * 
 */
public class JProfMethodCache {

	public static final int INIT_CACHE_SIZE = 8192;
	public static Vector<JprofMethodInfo> mCacheMethods = new Vector<JprofMethodInfo>(INIT_CACHE_SIZE);
	public static Map<String,Integer> velocityVmMap = new ConcurrentHashMap<String, Integer>();
	public synchronized static void Init() {

	}

	public synchronized static int Request() {
		mCacheMethods.add(new JprofMethodInfo());
		return mCacheMethods.size() - 1;
	}
	
	public synchronized static int Request(String vmName,String className,String methodName){
		Integer i = velocityVmMap.get(vmName);
		if(i == null){
			mCacheMethods.add(new JprofMethodInfo());			
			int index = mCacheMethods.size() - 1;
			mCacheMethods.get(index).setMClassName(className) ;
			mCacheMethods.get(index).setMMethodName(methodName+"#"+vmName);
			velocityVmMap.put(vmName, index);
			return index;
		}else{
			return i;
		}
	}
	public synchronized static int Request(String vmName){
		Integer i = velocityVmMap.get(vmName);
		if(i == null){
			return 0;
		}else{
			return i;
		}
	}

	public synchronized static void UpdateFileName(int id, String fileName) {
		mCacheMethods.get(id).setMFileName(fileName);
	}

	public synchronized static void UpdateLineNum(int id, int linenum) {
		mCacheMethods.get(id).setMLineNum(linenum);
	}

	public synchronized static void UpdateMethodName(int id, String className, String methodName) {
		mCacheMethods.get(id).setMClassName(className) ;
		mCacheMethods.get(id).setMMethodName(methodName);
	}
	
	public static JprofMethodInfo getJprofMethodInfo(int id){
		return mCacheMethods.get(id);
	}
	
	public static String getClasssMethodInfo(int id){
		 JprofMethodInfo method = getJprofMethodInfo(id);
		 return method.toString();
	}

}
