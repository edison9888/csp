
package com.taobao.monitor.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2010-5-31 ����11:17:55
 */
public class TableNameConverUtil {
	
	private static final String DAY_TABLE_NAME="ms_monitor_data";
	private static final String TIME_TABLE_NAME="ms_monitor_data";
	private static final String BACKUP_TABLE_NAME="ms_monitor_backup";
	private static final String TDDL_HOUR_TABLE_NAME="csp_app_consume_tddl_hour_temp";
	
	/**
	 * ȡ���ձ��ķֱ�������ձ������ֱ�ʽΪ ����+������һ���е�����
	 * ms_monitor_data_0001 ms_monitor_data_0002 ....ms_monitor_data_0366
	 * @param dateStr yyyy-MM-dd HH:mm
	 * @return
	 */
	public static String formatDayTableName(String dateStr){
		
		Date date = TimeConvertUtil.parseStrToDayByFormat(dateStr, "yyyy-MM-dd HH:mm");
		String sp = TimeConvertUtil.formatDayOfYear(date);		
		return DAY_TABLE_NAME+"_"+sp;	
		
	}
	/**
	 * ȡ���ձ��ķֱ�������ձ������ֱ�ʽΪ ����+������һ���е�����
	 * ms_monitor_data_0001 ms_monitor_data_0002 ....ms_monitor_data_0366
	 * @param date 
	 * @return
	 */
	public static String formatDayTableName(Date date){
		String sp = TimeConvertUtil.formatDayOfYear(date);		
		return DAY_TABLE_NAME+"_"+sp;	
	}
	
	
	/**
	 * ȡ��ʵʱ�ֱ���� ʵʱ����Ϊ ����+yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTimeTableName(Date date){
		
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyyMMdd");		
		return TIME_TABLE_NAME+"_"+parseLogFormatDate.format(date);		
	}
	/**
	 * ȡ�ñ��ݷֱ���� ���ݱ���Ϊ ����+MMdd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatBackupTableName(Date date){
		
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("MMdd");		
		return BACKUP_TABLE_NAME+"_"+parseLogFormatDate.format(date);		
	}
	
	/**
	 * ȡ��tddlСʱ��ı���
	 * @author denghaichuan.pt
	 * @version 2012-4-19
	 * @param dateTime yyyy-MM-dd HH:00:00
	 * @return
	 */
	public static String formatTddlTmpTableName(String dateTime) {
		String hour = dateTime.substring(11, 13);
		return TDDL_HOUR_TABLE_NAME + Integer.parseInt(hour) % 3;
	}

}
