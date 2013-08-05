
package com.taobao.monitor.time.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.taobao.util.CollectionUtil;

public class TimeUtil {
	
	
	public static String formatTime(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String formatTime(long date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String formatMillisTime(long mills,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format( new Date(mills) );
	}
	
	   /**
     * 返回在dateList里面与standardTime相隔最近的时间点
     * @param standardTime
     * @param dateList
     * @return
     */
	public static Date getTheMostNearTime(Date standardTime,Set<Date> dateList){
		if(CollectionUtil.isEmpty(dateList)){
			return null;
		}
		Calendar standardCalendar = Calendar.getInstance();
		standardCalendar.setTime(standardTime);
		
		long minMillisDif = Long.MAX_VALUE;
		Date theMostNearTime = new Date();
		Calendar compardCalendar = Calendar.getInstance();
		
		for(Date date:dateList){
			compardCalendar.setTime(date);
			long cunrrentDif = Math.abs(compardCalendar.getTimeInMillis() - standardCalendar.getTimeInMillis());
			if(cunrrentDif < minMillisDif){
				theMostNearTime = date;
				minMillisDif = cunrrentDif;
			}
		}
		return theMostNearTime;
	}
	
}
