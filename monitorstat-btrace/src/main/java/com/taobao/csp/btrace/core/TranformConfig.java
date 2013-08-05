
package com.taobao.csp.btrace.core;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author xiaodu
 * @version 2011-9-13 下午04:34:07
 * modify by zhongting	2011-10-24 下午18:491:17
 * 新增fieldName相关逻辑
 */
public class TranformConfig {
	
	public static final int MONITOR_METHOD = 0;		//监控方法
	public static final int MONITOR_FIELD  = 1;		//监控属性
	private String id;
	
	private String classPatternName;	//需要注入的class
	
	private String methodPatternName;	//监视的Method
	
	private String transformerMethod;	//注入拦截的时候，调用的处理类
	
	
	private int type;					//监控类型。MONITOR_METHOD表示监控方法，MONITOR_FIELD表示监控属性
	private String fieldName;			//属性名称，只在type=MONITOR_FIELD时有用
	private boolean isStatic;			//属性是否为静态属性 
	
	private Set<String> tranformClassSet = new HashSet<String>();	//这个做什么用的，可能是批量注入的

	/**
	 * 根据FieldName的名称来设定传输类型和FieldName的值， fieldName不能为NULL
	 * @param fieldName
	 */
	public void setFieldNameAndTypeByFieldName(String fieldName) {
		this.fieldName = fieldName;
		if(fieldName.equals("")) {		//根据fieldName判断是显示方法还是显示属性
			this.setType(TranformConfig.MONITOR_METHOD);
		} else {
			this.setType(TranformConfig.MONITOR_FIELD);
		}		
	}


	public Set<String> getTranformClassSet() {
		return tranformClassSet;
	}


	public void setTranformClassSet(Set<String> tranformClassSet) {
		this.tranformClassSet = tranformClassSet;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getClassPatternName() {
		return classPatternName;
	}


	public void setClassPatternName(String classPatternName) {
		this.classPatternName = classPatternName;
	}


	public String getMethodPatternName() {
		return methodPatternName;
	}


	public void setMethodPatternName(String methodPatternName) {
		this.methodPatternName = methodPatternName;
	}


	public String getTransformerMethod() {
		return transformerMethod;
	}


	public void setTransformerMethod(String transformerMethod) {
		this.transformerMethod = transformerMethod;
	}


	public int getType() {
		return type;
	}

	//类型只能通过在初始化fieldName的时候一起设定
	private void setType(int type) {
		this.type = type;
	}


	public String getFieldName() {
		return fieldName;
	}


	public boolean isStatic() {
		return isStatic;
	}


	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	
//	public void setFieldName(String fieldName) {
//		this.fieldName = fieldName;
//	}

}
