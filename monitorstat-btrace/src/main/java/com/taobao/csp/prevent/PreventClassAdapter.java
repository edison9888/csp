package com.taobao.csp.prevent;

import com.taobao.csp.btrace.core.script.InjectDelayMethod;
import com.taobao.csp.btrace.core.script.InjectMethodException;
import com.taobao.csp.objectweb.asm.ClassAdapter;
import com.taobao.csp.objectweb.asm.ClassVisitor;
import com.taobao.csp.objectweb.asm.MethodVisitor;

public class PreventClassAdapter extends ClassAdapter {
		
	private ClassDetail classDetail = null;
	
	private String injectType = null;

	public PreventClassAdapter(ClassVisitor cv,ClassDetail classDetail,String injectType) {
		super(cv);
		this.classDetail = classDetail;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		
		if(classDetail !=null&&name.equals(classDetail.getMethod())){
			if(TaskDetail.INJECT_DELAY.equals(injectType)){
				if(classDetail.getDelay()>0)
					return new InjectDelayMethod(super.visitMethod(access, name, desc, signature, exceptions),classDetail.getDelay());
			}
			
			if(TaskDetail.INJECT_EXCEPTION.equals(injectType)){
					return new InjectMethodException(super.visitMethod(access, name, desc, signature, exceptions));
			}
		}
		
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	
	

}
