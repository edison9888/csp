
package com.taobao.csp.monitor.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author xiaodu
 * @version 2010-11-2 ÉÏÎç11:16:15
 */
public class MonitorTimeUtil {
	
	public static String getLogRecordDate(String logRecord){
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d)");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			return m.group(1);
		}
		
		return null;
	}
	
	
	public static String getLogRecordTime(String logRecord){
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d)");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			return m.group(1);
		}
		
		return null;
	}

}
