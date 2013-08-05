/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.stat.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 方便日期计算的工具类
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version      2010-6-2:下午08:16:13
 *
 */
public class CalendarUtil {
    
    
    /**
     * 获得date日期的最后时刻
     * Wed Jun 02 23:59:59 CST 2010
     * @param date
     * @return
     */
    public static Date getEndTime(Date date,int day){
	Date date1  = null; 
	if(date == null){
	    date1 = new Date();
	}
        
    
	Calendar cal = Calendar.getInstance();
	cal.setTime(date1);
	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) +1+day, 0, 0, 0);
	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 1);

	return  cal.getTime();
	
    }
    
    
    /**
     * 获得date日期的最开始时间 即 2009-08-12:00:00:00
     * @param date
     * @return
     */
    public static Date getStartTime(Date date,int day){
	
	Date date1  = null; 
	if(date == null){
	    date1 = new Date();
	}
        
    
	Calendar cal = Calendar.getInstance();
	cal.setTime(date1);
	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)+day , 0, 0, 0);
	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) );

	return  cal.getTime();
	
    }
    
    
 public static Date getPreviousTime(Date date){
	
	Date date1  = null; 
	if(date == null){
	    date1 = new Date();
	}
        
    
	Calendar cal = Calendar.getInstance();
	cal.setTime(date1);
	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)-1 , 0, 0, 0);
	cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) );

	return  cal.getTime();
	
    }
    
    
    
    
  public  static void  main(String[] args){
      System.out.println(CalendarUtil.getEndTime(null,-1));
      System.out.println(CalendarUtil.getStartTime(null,-1));
      System.out.println(CalendarUtil.getPreviousTime(null));
      
  }
	

}
