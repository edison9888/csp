package com.taobao.monitor.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.taobao.monitor.alarm.source.constants.CommonConstants;

public class DateFormatUtil {
	public static Date stringToDate(String string) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.parse(string);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Date getTheDayBegin(Date endTime){
		Calendar cal = Calendar.getInstance();
		if(endTime == null){
			endTime = new Date();
		}
		cal.setTime(endTime);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		return cal.getTime();
	}
	
	public static String formatMailParameterTime( Date paramTime ){
		SimpleDateFormat standardSdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return standardSdf.format(paramTime);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String formatBeidouParameterTime( Date paramTime){
		SimpleDateFormat standardSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return standardSdf.format(paramTime);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Date fomatPickTime(String strTime){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			return sdf.parse(strTime);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Date getTime(Date time, int type, int amount){
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		c.add(type, amount);
		return c.getTime();
	}
	
	/**
	 * 根据传入的时间段，判断这个时间段是否属于当前时间
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean isConcurrent(Date start,Date end){
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		//增大时间的判断范围，为了更准确
		c.add(CommonConstants.TIME_TYPE, CommonConstants.STARTTIME_BEFORE-1);
		if(start.after(c.getTime()) && end.before(now)){
			return true;
		}else{
			return false;
		}
	}
	public static Date fomatTextTime(String strTime){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return sdf.parse(strTime);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
