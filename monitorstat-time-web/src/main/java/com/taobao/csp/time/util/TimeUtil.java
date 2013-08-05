
package com.taobao.csp.time.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.taobao.csp.dataserver.Constants;

public class TimeUtil {
	
	
	public static String formatTime(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String formatTime(long date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static int getMinuteIndexForCache(Date date, int minAhead) {
    return (date.getMinutes() + Constants.CACHE_TIME_INTERVAL - minAhead) % Constants.CACHE_TIME_INTERVAL;
	}
	
	public static long getLongValueOfObj(Object value) throws Exception {
		if(value == null)
			throw new Exception("obj is null");
		
		if (value instanceof Integer) {
			return (Integer)value;
		} else if (value instanceof Long) {
			return (Long)value;
		} else if (value instanceof Double) {
			return ((Double)value).longValue();
		} else if (value instanceof Float) {
			return ((Float)value).longValue();
		} else {
			return Long.parseLong(value.toString());
		}
	}
}
