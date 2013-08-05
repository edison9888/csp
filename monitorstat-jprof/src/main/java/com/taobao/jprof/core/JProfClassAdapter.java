/**
 * 
 */
package com.taobao.jprof.core;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

import com.taobao.jprof.JProfManager;

/**
 * @author luqi
 *
 */
public class JProfClassAdapter extends ClassAdapter {
	private String mClassName;
	private String mFileName = null;
	private List<String> fieldNameList = new ArrayList<String>();
	private boolean velocityEngineClass = false;
	
	
	@Override
	public void visit(int version, int access, String name,  
            String signature, String superName, String[] interfaces) {
		
		if("org/apache/velocity/app/VelocityEngine".equals(name)){
			velocityEngineClass = true;
		}
//		if("com/taobao/monitor/web/ao/MonitorTimeAo".equals(name)){
//			System.out.println(name);
//			velocityEngineClass = true;
//		}
		
		super.visit(version, access, name, signature, superName, interfaces);
	}
	public JProfClassAdapter(ClassVisitor visitor, String theClass) {
		super(visitor);
		this.mClassName = theClass;
	}
    public void visitSource(final String source, final String debug) {
        super.visitSource(source, debug);
        mFileName = source;
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
	public MethodVisitor visitMethod(int arg,
			String name,
			String descriptor,
			String signature,
			String[] exceptions) {
		
		if(JProfManager.get().isIgnoreGetSetMethod()){
			if(fieldNameList.contains(name)){
				//System.out.println("isIgnoreGetSetMethod£º"+name);
				return super.visitMethod(arg, name, descriptor, signature, exceptions);
			}
		}
		if ("<init>".equals(name)) {
			return super.visitMethod(arg, name, descriptor, signature, exceptions);
		}
		
//		if(velocityEngineClass&&name.equals("findLikeKeyAverageByLimit5")){
//			System.out.println("findLikeKeyAverageByLimit5£º"+name);
		if(velocityEngineClass&&name.equals("mergeTemplate")&&descriptor.equals("(Ljava/lang/String;Ljava/lang/String;Lorg/apache/velocity/context/Context;Ljava/io/Writer;)Z")){		
			MethodVisitor mv = super.visitMethod(arg, 
					name, 
					descriptor, 
					signature, 
					exceptions);
			return new VelocityEngineMethodAdapter(mv, mFileName, mClassName, name);
		}else{
			MethodVisitor mv = super.visitMethod(arg, 
					name, 
					descriptor, 
					signature, 
					exceptions);
			MethodAdapter ma = new JProfMethodAdapter(mv, mFileName, mClassName, name);
			return ma;
		}		
	}
	
}
