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
 * @version 2011-10-24 下午19:49:17
 * 代理类的基类
 */
public class BaseBtraceClassAdapter extends ClassAdapter {

	public static final String CONSTRUCTOR = "<init>";
	public static final String STATIC_CONSTRUCTOR = "<clinit>";
	
	protected String className;		//被监控的类全名
	protected String methodPattern;	//监控方法的正则表达
	protected String visitedClass;	//实现代理类的那个Class
	protected String tranformId;	//主键，暂时不用
	protected TransformerProxy transformerProxy;	//暂时还没用
	
	protected List<String> fieldNameList = new ArrayList<String>();	
	protected Map<String,FieldInfo> fieldMap = new HashMap<String,FieldInfo>();	
	
	private String fieldName;		//要监控的属性的名称
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
		info.setValue(value);	//初始化的值，可能为null
		
		fieldMap.put(name, info);
		
		//test:debug的时候，做一下map的某个对象的引用型判断
		//如info.set等，然后查看map中对象的值是否发生变化。
		
		return super.visitField(access, name, desc, signature, value);
	}	

	
}
