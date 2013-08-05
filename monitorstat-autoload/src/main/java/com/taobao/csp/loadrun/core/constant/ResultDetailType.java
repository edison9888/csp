package com.taobao.csp.loadrun.core.constant;

/***
 * ѹ��������Ϣkey������
 * @author youji.zj
 * @version 2012-06-22
 */
public enum ResultDetailType {
	
	URL("urlͳ����Ϣ", 1),
	
	TOMCAT_URL("tomcat urlͳ��", 2),
	
	HSF_PROVIDER("HSF�ṩ��Ϣ", 3),
	
	HSF_CONSUMER("HSF������Ϣ", 4),
	
	TAIR("Tair������Ϣ", 5),
	
	TDDL("Tddl������Ϣ", 6),
	
	PERFORMANCE_INDEX("����ָ��", 7),
	
	HSF_PV_RT("HSF������Ϣ", 8),
	
	IO_DATA("IO��Ϣ", 9);
	
	
	private String key;
	
	private int sort;
	
	private ResultDetailType(String key, int sort) {
		this.key = key;
		this.sort = sort;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
