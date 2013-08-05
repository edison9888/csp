package com.taobao.jprof;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * ��Ҫ�������ע���޸ĵ� package ���ƣ���Ҫ����ǰ׺�Ϳ���
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
	 * ����Ҫע����࣬����org.apache��ֻ��������ǰ׺����֧��ͨ���
	 *
	 * @param className
	 */
	public static void addIncludeClass(String className) {
		String icaseName = className.toLowerCase().replace('.', '/');
		includePackage.add(icaseName);
	}
	
	/**
	 * ����Ҫע����࣬����org.apache��ֻ��������ǰ׺����֧��ͨ���
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
