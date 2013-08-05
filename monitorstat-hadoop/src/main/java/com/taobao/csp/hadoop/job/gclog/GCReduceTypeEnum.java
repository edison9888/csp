package com.taobao.csp.hadoop.job.gclog;
public enum GCReduceTypeEnum {
	/** 时间类别*/
	GC_COST_TIME,	//时间
	
	GC_MEMRORY,		//内存
	
	TIMES,			//次数
	
	/** 存储维度*/
	TIMELINE,		//按时间
	IP				//按主机
}