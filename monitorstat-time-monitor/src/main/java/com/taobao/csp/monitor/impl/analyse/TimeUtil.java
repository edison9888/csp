
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse;

import java.util.Calendar;
import java.util.Date;

/**
 * @author xiaodu
 *
 * ����4:29:38
 */
public class TimeUtil {
	
	/**
	 * ��ʱ����� �ͺ���ȥ�� ����ת����long
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
