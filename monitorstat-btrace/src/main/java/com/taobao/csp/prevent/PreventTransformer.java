
package com.taobao.csp.prevent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.regex.Pattern;

import com.taobao.csp.objectweb.asm.ClassReader;
import com.taobao.csp.objectweb.asm.ClassWriter;

/**
 * 
 * @author xiaodu
 * @version 2011-8-16 ÏÂÎç02:18:54
 */
public class PreventTransformer implements ClassFileTransformer{
	
	private List<ClassDetail> classDetailList = null;
	
	private String injectType = null;
	
	public PreventTransformer( List<ClassDetail> list,String injectType){
		this.classDetailList = list;
		this.injectType = injectType;
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
		
		try{
			for(ClassDetail clazz:classDetailList){
				String c = clazz.getClassName().replaceAll("\\.", "/");
				if(className.equals(c)){
					System.out.println(c);
					ClassReader reader = new ClassReader(classfileBuffer);
					ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
					PreventClassAdapter adapter = new PreventClassAdapter(writer,clazz,injectType);
					reader.accept(adapter, 0);
					classfileBuffer = writer.toByteArray();
				}
			}
		}catch (Throwable e) {
		}
		
		return classfileBuffer;
	}

}
