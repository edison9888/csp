package com.taobao.csp.btrace.core.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.csp.btrace.core.FieldInfo;
import com.taobao.csp.btrace.core.client.TransformerProxy;
import com.taobao.csp.objectweb.asm.ClassAdapter;
import com.taobao.csp.objectweb.asm.ClassVisitor;
import com.taobao.csp.objectweb.asm.FieldVisitor;

/**
 * 
 * @author zhongting.zy
 * @version 2011-10-24 ����19:49:17
 * ������Ļ���
 */
public class BaseBtraceClassAdapter extends ClassAdapter {

	public static final String CONSTRUCTOR = "<init>";
	public static final String STATIC_CONSTRUCTOR = "<clinit>";
	
	protected String className;		//����ص���ȫ��
	protected String methodPattern;	//��ط�����������
	protected String visitedClass;	//ʵ�ִ�������Ǹ�Class
	protected String tranformId;	//��������ʱ����
	protected TransformerProxy transformerProxy;	//��ʱ��û��
	
	protected List<String> fieldNameList = new ArrayList<String>();	
	protected Map<String,FieldInfo> fieldMap = new HashMap<String,FieldInfo>();	
	
	private String fieldName;		//Ҫ��ص����Ե�����
	/**
	 * @param cv
	 * @param className
	 * @param methodPattern
	 * @param visitedClass
	 * @param tranformId
	 * @param fieldName
	 * @param proxy
	 */
	public BaseBtraceClassAdapter (ClassVisitor cv, String className, String methodPattern, 
			String visitedClass, String tranformId, TransformerProxy proxy) {
		
		super(cv);
		this.className = className;
		this.methodPattern = methodPattern;
		this.visitedClass = visitedClass;
		this.transformerProxy = proxy;
		this.tranformId = tranformId;
	}
	
	@Override
	public FieldVisitor visitField(int access, String name,  
            String desc, String signature, Object value) {
		String up = name.substring(0,1).toUpperCase() + name.substring(1, name.length());
		String getFieldName = "get" + up;
		String setFieldName = "set" + up;
		fieldNameList.add(getFieldName);
		fieldNameList.add(setFieldName);
		
		FieldInfo info = fieldMap.get(name); 
		if(info == null) {
			info = new FieldInfo();
		}
		info.setFieldName(name);
		info.setFieldClassName(className);
		info.setFieldType(desc);
		info.setValue(value);	//��ʼ����ֵ������Ϊnull
		
		fieldMap.put(name, info);
		
		//test:debug��ʱ����һ��map��ĳ��������������ж�
		//��info.set�ȣ�Ȼ��鿴map�ж����ֵ�Ƿ����仯��
		
		return super.visitField(access, name, desc, signature, value);
	}	

	
}
