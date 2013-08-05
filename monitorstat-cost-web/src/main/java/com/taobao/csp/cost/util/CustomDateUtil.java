package com.taobao.csp.cost.util;

import java.util.Calendar;
import java.util.Date;

public class CustomDateUtil {

	public static Date getStartMonth(){
		return getStartMonth(new Date());
	}
	
	public static Date getStartMonth(Date date){
		Calendar cd=Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.DAY_OF_MONTH, 1);
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE,0);
		cd.set(Calendar.SECOND, 0);
		
		return cd.getTime();
	}
	

	public static Date getEndMonth(){
		return getEndMonth(new Date());
	}
	
	public static Date getEndMonth(Date date){
		Calendar cd=Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.DAY_OF_MONTH, cd.getActualMaximum(Calendar.DAY_OF_MONTH));
		cd.set(Calendar.HOUR_OF_DAY, 23);
		cd.set(Calendar.MINUTE,59);
		cd.set(Calendar.SECOND, 59);
		
		return cd.getTime();
	}
	

	public static Date getStartDay(){
		return getStartMonth(new Date());
	}
	
	public static Date getStartDay(Date date){
		Calendar cd=Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE,0);
		cd.set(Calendar.SECOND, 0);
		
		return cd.getTime();
	}
	

	public static Date getEndDay(){
		return getEndMonth(new Date());
	}
	
	public static Date getEndDay(Date date){
		Calendar cd=Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.HOUR_OF_DAY, 23);
		cd.set(Calendar.MINUTE,59);
		cd.set(Calendar.SECOND, 59);
		
		return cd.getTime();
	}
	
	/**
	 * //			int dayOfWeek=cd.get(Calendar.DAY_OF_WEEK);//SUN 1 MON 2.. SATURDAY 7
//			dayOfWeek=dayOfWeek-1;//MON 1-SUN 7
//			//周1不变，周2减一，周3减2，依次类推，算到美洲的额
//			cd.add(Calendar.DATE, -(dayOfWeek-1));
			
	 * @param agrs
	 */
	public static void main(String[] agrs){
		System.out.println(getStartMonth());
		System.out.println(getEndMonth());
	}
}
