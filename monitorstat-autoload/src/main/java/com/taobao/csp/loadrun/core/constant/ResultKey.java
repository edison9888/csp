
package com.taobao.csp.loadrun.core.constant;

import java.io.Serializable;

/**
 * 时间基准
 * 次数累加
 * 次数平均
 * @author xiaodu
 * @version 2011-6-23 上午10:24:37
 */
public enum ResultKey implements Serializable{
	
	Qps("QPS",1),
	Rest("响应时间(ms)",2),
	PageSize("页面大小(K)",3),
	Http_Fetches("Http请求总量",3),
	Http_State_200("http返回200",3),
	Http_State_302("http返回302",3),
	
	Apache_Pv("apache每秒pv",4),
	Apache_State_200("apache返回200",5),
	Apache_Rest("apache响应时间(ms)",5),
	Apache_PageSize("apache页面大小(K)",6),
	
	Tomcat_Pv("tomcat每秒pv",7),
	Tomcat_State_200("tomcat返回200",7),
	Tomcat_Rest("tomcat响应时间(ms)",8),
	Tomcat_PageSize("tomcat页面大小(K)",9),
	
	Jvm_Memeory("jvm内存使用率(%)",10),
	
	Hsf_pv("hsf接口QPS",11),
	Hsf_Rest("hsf接口响应时间(ms)",12),
	
	CPU("CPU(%)",13),
	Load("LOAD",14),
	Io("IO",15),
	
	GC_CMS("CMS 次数",16),
	GC_Full("FUll GC次数",17),
	GC_Full_Time("FUll GC消耗时间(ms)",18),
	GC_Min("GC次数",19),
	GC_Min_Time("GC消耗时间(ms)",20),
	GC_Memory("单次请求内存消耗(K)",21),
	
	AJP_BLOCKED("ajp阻赛", 22),
	AJP_RUNNABLE("ajp运行", 23),
	AJP_WAITING("ajp等待", 24);
	
	
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
