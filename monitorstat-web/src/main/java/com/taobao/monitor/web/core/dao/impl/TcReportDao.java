
package com.taobao.monitor.web.core.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;



/**
 * @author xiaodu
 * @version 2010-4-29 下午07:39:52
 */
public class TcReportDao  extends MysqlRouteBase {
	
	private static Logger logger = Logger.getLogger(TcReportDao.class);
	
	public TcReportDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("SHAREREPORT_NEW"));
	}
	
	/**
	 * 取得全网交易总额
	 * @param startDate  yyyy-MM-dd
	 * @return
	 */
	public long sumAllamount(String startDate){
		//select sum(order_actual_fee) from notify_message_0_7 where gmt_create >= '2012-11-25' and gmt_create <'2012-11-26' and  pay_status=2
		//select sum(order_actual_fee) from notify_message_1_7 where gmt_create >= '2012-11-25' and gmt_create <'2012-11-26' and  pay_status=2

		String sql_0 = getSql(startDate, 2, 0, "sum(order_actual_fee)");
		String sql_1 = getSql(startDate, 2, 1, "sum(order_actual_fee)");
		
		try {
			long mount_0 =  this.getLongValue(sql_0);
			long mount_1 =  this.getLongValue(sql_1);
			return (mount_0 + mount_1) / 100;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	private String getSql(String collectTime, int payStatus, int tableIndex, String function) {
		String tableName = "notify_message_" + tableIndex + "_";
		String sql = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sf.parse(collectTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			if (weekDay == 0) {
				weekDay = 7;
			}
			tableName += weekDay;
			
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			String nextDay = sf.format(calendar.getTime());
			
			sql = "select " + function + " from " + tableName + " where gmt_create >= '" + collectTime + "' and gmt_create <'" + nextDay + "' and " +
					" pay_status=" + payStatus + " and is_detail =1";
			
		} catch (ParseException e) {
			logger.error(e);
		}
		
		return sql;
	}
	
	
	
	

}
