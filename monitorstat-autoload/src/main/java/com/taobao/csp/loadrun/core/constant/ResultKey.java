
package com.taobao.csp.loadrun.core.constant;

import java.io.Serializable;

/**
 * ʱ���׼
 * �����ۼ�
 * ����ƽ��
 * @author xiaodu
 * @version 2011-6-23 ����10:24:37
 */
public enum ResultKey implements Serializable{
	
	Qps("QPS",1),
	Rest("��Ӧʱ��(ms)",2),
	PageSize("ҳ���С(K)",3),
	Http_Fetches("Http��������",3),
	Http_State_200("http����200",3),
	Http_State_302("http����302",3),
	
	Apache_Pv("apacheÿ��pv",4),
	Apache_State_200("apache����200",5),
	Apache_Rest("apache��Ӧʱ��(ms)",5),
	Apache_PageSize("apacheҳ���С(K)",6),
	
	Tomcat_Pv("tomcatÿ��pv",7),
	Tomcat_State_200("tomcat����200",7),
	Tomcat_Rest("tomcat��Ӧʱ��(ms)",8),
	Tomcat_PageSize("tomcatҳ���С(K)",9),
	
	Jvm_Memeory("jvm�ڴ�ʹ����(%)",10),
	
	Hsf_pv("hsf�ӿ�QPS",11),
	Hsf_Rest("hsf�ӿ���Ӧʱ��(ms)",12),
	
	CPU("CPU(%)",13),
	Load("LOAD",14),
	Io("IO",15),
	
	GC_CMS("CMS ����",16),
	GC_Full("FUll GC����",17),
	GC_Full_Time("FUll GC����ʱ��(ms)",18),
	GC_Min("GC����",19),
	GC_Min_Time("GC����ʱ��(ms)",20),
	GC_Memory("���������ڴ�����(K)",21),
	
	AJP_BLOCKED("ajp����", 22),
	AJP_RUNNABLE("ajp����", 23),
	AJP_WAITING("ajp�ȴ�", 24);
	
	
	private String name;
	private int sort;
	
	ResultKey(String name,int sort){
		this.name = name;
		this.sort = sort;
	}
	

	public int getSort() {
		return sort;
	}


	public String getName() {
		return name;
	}
}
