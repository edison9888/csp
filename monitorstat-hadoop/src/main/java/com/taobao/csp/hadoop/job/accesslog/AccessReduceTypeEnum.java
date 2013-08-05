package com.taobao.csp.hadoop.job.accesslog;

public enum AccessReduceTypeEnum {
	
	// MAIN###appName###ip###���� : pv����###��rt
	MAIN,
	
	// CODE###appName###httpcode�� pv����
	CODE,
	
	// SOURCE###appName###sourceUrl��pv����
	SOURCE,
	
	// REQUEST###appName###sourceUrl��pv����
	REQUEST,
	
	// MINITE_PV###appName###yyyy-MM-dd HH-mm  : pv����
	MINITE_PV,
	
	// URL_PV###appName###url   : pv����
	URL_PV,
	
	// URL_REFER###appName###requestUrl###sourceUrl   : pv����
	URL_REFER;  
	

}
