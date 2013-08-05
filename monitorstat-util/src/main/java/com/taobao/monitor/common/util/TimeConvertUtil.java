
package com.taobao.monitor.common.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2010-5-31 ����11:06:44
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
	 * ��ʽ��ʱ����һ���еĵڼ��죬����4λ ������0	 * 
	 * �磺20100531  ����ֵΪ 0151
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
	 * �������ڸ�ʽ����������ʱ�����
	 * @param date �ַ��� ʱ��
	 * @param format ʱ���ʽ���ʽ
	 * @return ʱ�����
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
