
package com.taobao.csp.btrace.core;

import java.io.Serializable;

/**
 * 
 * @author zhongting
 * @version 2011-10-24 ÏÂÎç14:45:47
 */
public class FieldInfo implements Serializable{
	
	private static final long serialVersionUID = 8754931502545497716L;

	private String fieldClassName;
	
	private String fieldName;
	
	private String fieldType;		//typeµÄÃèÊö·û
	
	private Object value;

	public String getFieldClassName() {
		return fieldClassName;
	}

	public void setFieldClassName(String fieldClassName) {
		this.fieldClassName = fieldClassName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

}
