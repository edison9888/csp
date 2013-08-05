
package com.taobao.csp.btrace.core;

import java.io.Serializable;

/**
 * 
 * @author zhongting
 * @version 2011-10-21 下午18:37:35
 */
public class FieldProfilerInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6108740370890911705L;

	private long threadId;
	
	private String startValue;		//方法执行前，属性的值
	
	private String endValue;		//方法执行后，属性的值
	
	private String fieldClassName;	//所属class的名称
	
	private String fieldType;		//属性类型
	
	private String fieldName;		//属性名称
	
	private String methodName;		//检测方法属性
	
	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public String getStartValue() {
		return startValue;
	}

	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}

	public String getEndValue() {
		return endValue;
	}

	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	public String getFieldClassName() {
		return fieldClassName;
	}

	public void setFieldClassName(String fieldClassName) {
		this.fieldClassName = fieldClassName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/* 转成Json形式的字符串,fieldType 可能含有特殊字符串，暂时没有加入 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("threadId:").append(threadId).append(",fieldClassName:").append("fieldClassName,");
		sb.append("startValue:").append(startValue).append(",endValue:").append(endValue);
		sb.append(",fieldName:").append(fieldName).append(",methodName:").append(methodName).append("]");
		return sb.toString();
	}
	
	

}
