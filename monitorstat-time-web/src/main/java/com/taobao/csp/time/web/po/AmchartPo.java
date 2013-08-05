package com.taobao.csp.time.web.po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmchartPo {
	private List<Map<String,Object>> valueList = new ArrayList<Map<String,Object>>();
	/**
	 * po���field,��ʾ������
	 */
	private Map<String,String> fieldMap = new HashMap<String,String>();
	/**
	 * Ŀǰ֧��mm,hh,DD,MM ע�⣬���ִ�Сд
	 */
	private String timeUnit = "dd";
	private String yTitle;
	/**
	 * x���ʱ���������ơ�Ĭ��Ϊdate������Ϊlong ���� long date = 1230l;
	 */
	private String xField = "date";
	
	public List<Map<String, Object>> getValueList() {
		return valueList;
	}
	public void setValueList(List<Map<String, Object>> valueList) {
		this.valueList = valueList;
	}
	public Map<String, String> getFieldMap() {
		return fieldMap;
	}
	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public String getyTitle() {
		return yTitle;
	}
	public void setyTitle(String yTitle) {
		this.yTitle = yTitle;
	}
	public String getxField() {
		return xField;
	}
	public void setxField(String xField) {
		this.xField = xField;
	}
}
