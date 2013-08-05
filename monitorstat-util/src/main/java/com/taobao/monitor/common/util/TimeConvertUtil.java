
package com.taobao.monitor.common.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2010-5-31 上午11:06:44
 */
public class TimeConvertUtil {
	
	
	public static String formatCurrentDate(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	public static String formatDate(Date date ,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 
	 * 格式化时间在一年中的第几天，补起4位 不够放0	 * 
	 * 如：20100531  返回值为 0151
	 * 
	 * @param date
	 * @return 
	 */
	public static String formatDayOfYear(Date date){		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);		
		int day = cal.get(Calendar.DAY_OF_YEAR);
		DecimalFormat df1 = new DecimalFormat("0000");
		return df1.format(day);		
	}
	
	/**
	 * 根据日期格式，解析返回时间对象
	 * @param date 字符串 时间
	 * @param format 时间格式表达式
	 * @return 时间对象
	 */
	public static Date parseStrToDayByFormat(String date,String format){
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	
	

}
