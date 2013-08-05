package com.taobao.csp.cost.service.day;

/***
 * cost type
 * @author youji.zj
 *
 */
public enum CostType {
	DB,
	TAIR,
	HSF,
	PV,
	HSF_PV,		//hsf
	HOST,		//机器
	ALL,		//APP级别所有
	DIRECT_ALL,//直接依赖
	GROUP_COUNT,//公司
	LINE_COUNT;	//产品线

}
