
package com.taobao.monitor.web.report;

import com.taobao.monitor.web.util.RequestByUrl;

/**
 * 
 * @author xiaodu
 * @version 2010-9-6 обнГ02:39:22
 */
public class DistribeReport {

	
	public static  String getDistribeReportByJsp(String appNames,String collectDay){		
		String url = "http://127.0.0.1:9999/monitorstat/distrib/distrib_provider_distinct.jsp?appNames="+appNames+"&collectDay="+collectDay;		
		return RequestByUrl.getMessageByJsp(url);
	}

}
