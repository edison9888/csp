
package com.taobao.csp.btrace.core;

import java.io.Serializable;

/**
 * 
 * @author zhongting
 * @version 2011-10-21 ����18:37:35
 */
public class FieldProfilerInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6108740370890911705L;

	private long threadId;
	
	private String startValue;		//����ִ��ǰ�����Ե�ֵ
	
	private String endValue;		//����ִ�к����Ե�ֵ
	
	private String fieldClassName;	//����class������
	
	private String fieldType;		//��������
	
	private String fieldName;		//��������
	
	private String methodName;		//��ⷽ������
	
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

	/* ת��Json��ʽ���ַ���,fieldType ���ܺ��������ַ�������ʱû�м��� */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("threadId:").append(threadId).append(",fieldClassName:").append("fieldClassName,");
		sb.append("startValue:").append(startValue).append(",endValue:").append(endValue);
		sb.append(",fieldName:").append(fieldName).append(",methodName:").append(methodName).append("]");
		return sb.toString();
	}
	
	

}
