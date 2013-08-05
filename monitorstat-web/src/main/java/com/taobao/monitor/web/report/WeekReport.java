
package com.taobao.monitor.web.report;

import com.taobao.monitor.web.util.RequestByUrl;

/**
 * 
 * @author xiaodu
 * @version 2010-9-26 обнГ03:06:01
 */
public class WeekReport {
	
	
	public static  String getWeekReportByJsp(){		
		String url = "http://127.0.0.1:9999/monitorstat/report/report_week.jsp";		
		return RequestByUrl.getMessageByJsp(url);
	}

}
