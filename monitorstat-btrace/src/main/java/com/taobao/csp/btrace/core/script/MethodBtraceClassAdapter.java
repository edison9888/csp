
package com.taobao.csp.btrace.core.script;

import java.lang.reflect.Constructor;
import java.util.regex.Pattern;

import com.taobao.csp.btrace.core.client.TransformerProxy;
import com.taobao.csp.objectweb.asm.ClassVisitor;
import com.taobao.csp.objectweb.asm.FieldVisitor;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;

/**
 * 
 * @author xiaodu
 * @version 2011-8-26 上午10:55:27
 * 
 */
public class MethodBtraceClassAdapter extends BaseBtraceClassAdapter {
	

	
	public MethodBtraceClassAdapter(ClassVisitor cv, String className,
			String methodPattern, String visitedClass, String tranformId, TransformerProxy proxy) {
		super(cv, className, methodPattern, visitedClass, tranformId, proxy);
	}

	/** visitField 在BtraceBaseClassAdapter中实现*/
	
	@Override
	public MethodVisitor visitMethod(int access, String methodName, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
		
		
		if(methodName.equals(CONSTRUCTOR) || methodName.equals(STATIC_CONSTRUCTOR)) {
			return mv;
		} else {
			//为了忽略非get和set方法。
			if(fieldNameList.contains(methodName)){
				return mv;
			}
			//对于非Get和Set的方法，且符合我们要求的方法，使用新增的代理类进行处理
			Pattern p = Pattern.compile(methodPattern);
			if(p.matcher(methodName).matches()){
				Class<?> c;
				try {
					c = Class.forName(visitedClass);
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
