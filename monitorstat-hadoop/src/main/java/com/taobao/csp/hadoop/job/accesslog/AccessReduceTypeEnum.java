package com.taobao.csp.hadoop.job.accesslog;

public enum AccessReduceTypeEnum {
	
	// MAIN###appName###ip###分钟 : pv次数###总rt
	MAIN,
	
	// CODE###appName###httpcode： pv次数
	CODE,
	
	// SOURCE###appName###sourceUrl：pv次数
	SOURCE,
	
	// REQUEST###appName###sourceUrl：pv次数
	REQUEST,
	
	// MINITE_PV###appName###yyyy-MM-dd HH-mm  : pv次数
	MINITE_PV,
	
	// URL_PV###appName###url   : pv次数
	URL_PV,
	
	// URL_REFER###appName###requestUrl###sourceUrl   : pv次数
	URL_REFER;  
	

}
