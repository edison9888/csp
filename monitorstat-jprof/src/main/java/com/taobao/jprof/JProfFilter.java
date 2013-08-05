package com.taobao.jprof;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * 需要对类进行注入修改的 package 名称，需要输入前缀就可以
 * 
 * @author luqi
 * 
 */
public class JProfFilter {

	private static Set<String> includePackage = new HashSet<String>();
	private static Set<String> excludePackage = new HashSet<String>();
	
	
	static{
		
		excludePackage.add("java");
		excludePackage.add("com.taobao.jprof");
	}

	/**
	 * 增加要注入的类，比如org.apache，只允许输入前缀，不支持通配符
	 *
	 * @param className
	 */
	public static void addIncludeClass(String className) {
		String icaseName = className.toLowerCase().replace('.', '/');
		includePackage.add(icaseName);
	}
	
	/**
	 * 增加要注入的类，比如org.apache，只允许输入前缀，不支持通配符
	 *
	 * @param className
	 */
	public static void addExcludeClass(String className) {
		String icaseName = className.toLowerCase().replace('.', '/');
		excludePackage.add(icaseName);
	}
	

	public static boolean IsNeedInject(String className) {
		String icaseName = className.toLowerCase().replace('.', '/');
		for (String v : includePackage) {
			if (icaseName.startsWith(v)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean IsNotNeedInject(String className) {
		String icaseName = className.toLowerCase().replace('.', '/');
		for (String v : excludePackage) {
			if (icaseName.startsWith(v)) {
				return false;
			}
		}
		return true;
	}
	
	public static void Dump(OutputStream strm){
		
	}
}
