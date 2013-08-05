package com.taobao.monitor.stat.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.stat.db.po.ShareReportPo;

/**
 * xiaoxie
 * 2010-4-28
 */
public class ShareReportDao extends MysqlRouteBase {
	private static Logger log = Logger.getLogger(ShareReportDao.class);
	public ShareReportDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("SHAREREPORT_URL"));
	}
	/**
	 * 这个方法必须在每天的临晨1:30以后调用
	 * @param collectTime
	 * @return
	 */
	public ShareReportPo findAppSqlInfo(String collectTime) {
		 
		final ShareReportPo po = new ShareReportPo();
		
		try {
			this.query("select sum(ORDER_TOTAL_COUNT) as ORDER_TOTAL_COUNT  FROM  share_report WHERE  gmt_create = ? and report_type=1", 
					new Object[]{collectTime},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws SQLException {
					long createOrderCount = rs.getLong("ORDER_TOTAL_COUNT");
			 
					po.setCreateOrderCount(createOrderCount);
					 
					 
				}
			});
			this.query("select sum(ORDER_TOTAL_COUNT) as ORDER_TOTAL_COUNT  FROM  share_report WHERE  gmt_create = ? and report_type=2", 
					new Object[]{collectTime},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws SQLException {
					long payOrderCount = rs.getLong("ORDER_TOTAL_COUNT");
			 
					po.setPayOrderCount(payOrderCount);
					 
					 
				}
			});
			
			
		}catch (Exception e) {
			log.error("读取share_report db列表出错", e);
		}
		return po;
	}
	
	/**
	 * 取得全网交易总额
	 * @param startDate  yyyy-MM-dd
	 * @return
	 */
	public long sumAllamount(String startDate){
		//select sum(order_actual_fee)  from share_report where gmt_create='2010-11-22'
		//select sum(order_actual_fee)  from share_report where gmt_create='2010-11-22' and report_type=2;

		String sql ="select sum(order_actual_fee) as amount from share_report where gmt_create = ? and report_type=2";
		
		try {
			long mount =  this.getLongValue(sql,new Object[]{startDate});
			return mount/100;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	
}
