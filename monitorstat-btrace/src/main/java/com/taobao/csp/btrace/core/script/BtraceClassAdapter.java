
package com.taobao.csp.btrace.core.script;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.taobao.csp.btrace.core.client.TransformerProxy;
import com.taobao.csp.btrace.core.packet.TransformerClassCommand;
import com.taobao.csp.objectweb.asm.ClassAdapter;
import com.taobao.csp.objectweb.asm.ClassVisitor;
import com.taobao.csp.objectweb.asm.FieldVisitor;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;

/**
 * 
 * @author xiaodu
 * @version 2011-8-26 ÉÏÎç10:55:27
 */
public class BtraceClassAdapter extends ClassAdapter{
	
	public static final String CONSTRUCTOR = "<init>";
	
	private String className;
	private String methodPattern;
	private String methodVisitClass;
	private String tranformId;
	private TransformerProxy transformerProxy;
	
	private List<String> fieldNameList = new ArrayList<String>();
	
	public BtraceClassAdapter(ClassVisitor cv,String className,String methodPattern,String methodVisitClass,String tranformId,TransformerProxy proxy) {
		super(cv);
		this.className = className;
		this.methodPattern = methodPattern;
		this.methodVisitClass = methodVisitClass;
		this.transformerProxy = proxy;
		this.tranformId = tranformId;
	}
	
	
	@Override
	public FieldVisitor visitField(int access, String name,  
            String desc, String signature, Object value) {
		
		String up = name.substring(0,1).toUpperCase()+name.substring(1,name.length());
		String getFieldName = "get"+up;
		String setFieldName = "set"+up;
		fieldNameList.add(getFieldName);
		fieldNameList.add(setFieldName);
		
		return super.visitField(access, name, desc, signature, value);
	}
	
	
	@Override
	public MethodVisitor visitMethod(int access, String methodName, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
		
		
		if(methodName.equals(CONSTRUCTOR) ){
			return mv;
		}else{
			
			if(fieldNameList.contains(methodName)){
				return mv;
			}
			Pattern p = Pattern.compile(methodPattern);
			if(p.matcher(methodName).matches()){
				Class<?> c;
				try {
					c = Class.forName(methodVisitClass);
					Constructor<?> constructor = c.getConstructor(int.class,MethodVisitor.class,String.class,String.class,String.class);
					Object obj = constructor.newInstance(access,mv,className,methodName,desc);
					return (MethodAdapter)obj;
					
				} catch (Exception e) {
					e.printStackTrace();
					return mv;
				} 
			}
		}
		return mv;
	}

}
