/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-6 - ����02:31:54
 * @version 1.0
 */
public class DateUtil {

	/** ��ȷ����������ڸ�ʽ yyyy-MM-dd HH:mm:ss.S */ 
	private static final String DATE_YMDHMSS_FORMAT = "yyyy-MM-dd HH:mm:ss.S";  
	
	/** ��ȷ��������ڸ�ʽ yyyy-MM-dd HH:mm:ss */ 
	private static final String DATE_YMDHMSFORMAT = "yyyy-MM-dd HH:mm:ss"; 
	
	/** ��ȷ��������ڸ�ʽ yyyy-MM-dd */
	private static final String DATE_YMDFORMAT = "yyyy-MM-dd";
	
	/** ��ȷ�����ӵ����ڸ�ʽ MM-dd HH:mm */ 
	public static final String DATE_MDHM_FORMAT = "MM-dd HH:mm";
	
	/**
	 * ת��Ϊ��ȷ����������ڸ�ʽ yyyy-MM-dd HH:mm:ss.S
	 */
	private static final ThreadLocal<SimpleDateFormat> dateYMDHMSSFormat = 
		new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_YMDHMSS_FORMAT);
        }
    };
    
	/**
	 * ת��Ϊ��ȷ��������ڸ�ʽ yyyy-MM-dd HH:mm:ss
	 */
	private static final ThreadLocal<SimpleDateFormat> dateYMDHMSFormat = 
		new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_YMDHMSFORMAT);
        }
    };
    
	/**
	 * ת��Ϊ��ȷ��������ڸ�ʽ yyyy-MM-dd
	 */
	private static final ThreadLocal<SimpleDateFormat> dateYMDFormat = 
		new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_YMDFORMAT);
        }
    };
    
	/**
	 * ת��Ϊ��ȷ��������ڸ�ʽ MM-dd HH:mm
	 */
	private static final ThreadLocal<SimpleDateFormat> dateMDHMFormat = 
		new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_MDHM_FORMAT);
        }
    };
    
    /**
     * 
    * <p>Description: ת��Ϊ��ȷ����������ڸ�ʽ yyyy-MM-dd HH:mm:ss.S</p>
    * @return
    * DateFormat
    * @author tom
    * @����02:13:23 - 2010-9-8
     */
    public static DateFormat getDateYMDHMSSFormat() {  
        return (DateFormat) dateYMDHMSSFormat.get();  
    }  
    
    /**
     * 
    * <p>Description: ת��Ϊ��ȷ��������ڸ�ʽ yyyy-MM-dd HH:mm:ss</p>
    * @return
    * DateFormat
    * @author tom
    * @����02:13:23 - 2010-9-8
     */
    public static DateFormat getDateYMDHMSFormat() {  
        return (DateFormat) dateYMDHMSFormat.get();  
    }  
    
    /**
     * 
    * <p>Description: ת��Ϊ��ȷ�����ӵ����ڸ�ʽ MM-dd HH:mm</p>
    * @return
    * DateFormat
    * @author tom
    * @����02:13:23 - 2010-9-8
     */
    public static DateFormat getDateMDHMFormat() {  
        return (DateFormat) dateMDHMFormat.get();  
    }  
    
    /**
     * 
    * <p>Description: ת��Ϊ��ȷ��������ڸ�ʽ yyyy-MM-dd</p>
    * @return
    * DateFormat
    * @author tom
    * @����02:13:23 - 2010-9-8
     */
    public static DateFormat getDateYMDFormat() {  
        return (DateFormat) dateYMDFormat.get();  
    }  
  
    /**
     * �����������ڼ���ķ�����
     * 
     * @param firstDate С��
     * @param lastDate ����
     * @return int Ĭ��-1
     */
    public static int getTimeIntervalMins(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }
        
        long intervalMilli = lastDate.getTime() - firstDate.getTime();
        return (int) (intervalMilli / (60 * 1000));
    }
    
    
    public static Date getTime(Date date, int CalendarType,int interval){
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	c.add(CalendarType, interval);
    	return c.getTime();
    	
    }

}
