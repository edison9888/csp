
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse;

import java.util.Calendar;
import java.util.Date;

/**
 * @author xiaodu
 *
 * 下午4:29:38
 */
public class TimeUtil {
	
	/**
	 * 将时间的秒 和毫秒去除 ，并转换成long
	 * @param date
	 * @return
	 */
	public static long converMinuteTime(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long cTime = cal.getTimeInMillis();
		
		return cTime;
		
	}

}
