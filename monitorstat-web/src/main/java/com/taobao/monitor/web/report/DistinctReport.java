
package com.taobao.monitor.web.report;

import com.taobao.monitor.web.util.RequestByUrl;

/**
 * 
 * @author xiaodu
 * @version 2010-9-16 обнГ05:36:06
 */
public class DistinctReport {
	
	public static String getAppDistinct(String appId){
		
		String url = "http://127.0.0.1:9999//monitorstat/report/report_distinct.jsp?appId="+appId;		
		return RequestByUrl.getMessageByJsp(url);
		
	}

}
