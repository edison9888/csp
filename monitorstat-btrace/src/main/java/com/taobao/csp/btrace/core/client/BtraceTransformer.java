
package com.taobao.csp.btrace.core.client;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * 
 * @author xiaodu
 * @version 2011-8-16 ÏÂÎç02:18:54
 */
public class BtraceTransformer implements ClassFileTransformer{
	
	private TransformerProxy proxy = null;
	
	public BtraceTransformer(TransformerProxy proxy){
		this.proxy = proxy;
	}
	

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if (className.startsWith("com/taobao/csp/btrace")) {
			return classfileBuffer;
		}
		if (className.startsWith("java/")) {
			return classfileBuffer;
		}
		if (className.startsWith("javax/")) {
			return classfileBuffer;
		}
		
//		if (className.startsWith("cmo/taobao/test/TestFieldThread")){
//			System.out.println(className);
//		}
		
		byte[] b = proxy.getClassTransform(className, classfileBuffer);
		if(b != null){
			return b;
		}		
		return classfileBuffer;
	}

}
