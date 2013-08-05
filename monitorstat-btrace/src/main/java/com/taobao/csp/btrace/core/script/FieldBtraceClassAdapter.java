package com.taobao.csp.btrace.core.script;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.regex.Pattern;
import com.taobao.csp.btrace.core.client.TransformerProxy;
import com.taobao.csp.objectweb.asm.ClassVisitor;
import com.taobao.csp.objectweb.asm.MethodAdapter;
import com.taobao.csp.objectweb.asm.MethodVisitor;

public class FieldBtraceClassAdapter extends BaseBtraceClassAdapter {

	private String fieldName;
	private Boolean isStatic;
	
	public FieldBtraceClassAdapter(ClassVisitor cv, String className,
			String methodPattern, String visitedClass, String tranformId,
			String fieldName, boolean isStatic, TransformerProxy proxy) {
		super(cv, className, methodPattern, visitedClass, tranformId, proxy);
		this.fieldName = fieldName;
		this.isStatic = isStatic;
	}

	/** visitField ��BtraceBaseClassAdapter��ʵ��*/
	
	
	//����Ҫdebug����һ�£�fieldMap�ͷ������ʳ�ʼ����ʼ��˳������
	@Override
	public MethodVisitor visitMethod(int access, String methodName, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
		//MethodVisitor mv,String fieldName,Map<String,FieldInfo> fieldMap
		if(methodName.equals(CONSTRUCTOR) || methodName.equals(STATIC_CONSTRUCTOR)) {	//���캯������
			return mv;
		} else {
			Pattern p = Pattern.compile(methodPattern);
			if(p.matcher(methodName).matches()){
				Class<?> c;
				try {
					c = Class.forName(visitedClass);
					Constructor<?> constructor = c.getConstructor(MethodVisitor.class, String.class, String.class, Boolean.class, Map.class);
					Object obj = constructor.newInstance(mv, methodPattern, fieldName, isStatic, fieldMap);	//�ݲ�֧��ͨ�����ֱ�Ӵ���methodPattern
					return (MethodAdapter)obj;
				} catch (Exception e) {
					e.printStackTrace();
					return mv;
				} 
			}
		}
		return mv;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
