
package com.taobao.csp.btrace.core;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author xiaodu
 * @version 2011-9-13 ����04:34:07
 * modify by zhongting	2011-10-24 ����18:491:17
 * ����fieldName����߼�
 */
public class TranformConfig {
	
	public static final int MONITOR_METHOD = 0;		//��ط���
	public static final int MONITOR_FIELD  = 1;		//�������
	private String id;
	
	private String classPatternName;	//��Ҫע���class
	
	private String methodPatternName;	//���ӵ�Method
	
	private String transformerMethod;	//ע�����ص�ʱ�򣬵��õĴ�����
	
	
	private int type;					//������͡�MONITOR_METHOD��ʾ��ط�����MONITOR_FIELD��ʾ�������
	private String fieldName;			//�������ƣ�ֻ��type=MONITOR_FIELDʱ����
	private boolean isStatic;			//�����Ƿ�Ϊ��̬���� 
	
	private Set<String> tranformClassSet = new HashSet<String>();	//�����ʲô�õģ�����������ע���

	/**
	 * ����FieldName���������趨�������ͺ�FieldName��ֵ�� fieldName����ΪNULL
	 * @param fieldName
	 */
	public void setFieldNameAndTypeByFieldName(String fieldName) {
		this.fieldName = fieldName;
		if(fieldName.equals("")) {		//����fieldName�ж�����ʾ����������ʾ����
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

	//����ֻ��ͨ���ڳ�ʼ��fieldName��ʱ��һ���趨
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
