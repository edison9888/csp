
package com.taobao.monitor.web;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.taobao.monitor.web.core.dao.impl.MonitorTimeDao;

/**
 * 
 * @author xiaodu
 * @version 2010-5-6 下午01:59:04
 */
public class CreateTables {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//MonitorTimeDao dao = new MonitorTimeDao();
		String time = "20100720";
		
		
		System.out.println("开始创建 表："+time);
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyyMMdd");
		
		try {
			Date date = parseLogFormatDate.parse(time);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<200;i++){
				//System.out.println("创建 表："+parseLogFormatDate.format(cal.getTime()));
				sb.append("ms_monitor_data_"+parseLogFormatDate.format(cal.getTime())).append(" ");
			//	dao.createDateTable(parseLogFormatDate.format(cal.getTime()));
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			
			System.out.println(sb.toString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

}
