
package com.taobao.monitor.common.db.impl.other;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;



/**
 * @author xiaodu
 * @version 2010-4-29 下午07:39:52
 */
public class TcReportDao  extends MysqlRouteBase {
	public TcReportDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("SHAREREPORT_URL"));
	}
	
	private static final Logger logger = Logger.getLogger(TcReportDao.class);
	/**
	 * 取得全网交易总额
	 * @param startDate  yyyy-MM-dd
	 * @return
	 */
	public long sumAllamount1(String startDate){
		//select sum(order_actual_fee)  from share_report where gmt_create='2010-11-22'
		//select sum(order_actual_fee)  from share_report where gmt_create='2010-11-22' and report_type=2;

		String sql ="select sum(order_actual_fee) as amount from share_report where gmt_create = ? and report_type=2";
		
		try {
			long mount =  this.getLongValue(sql,new Object[]{startDate});
			return mount/100;
		} catch (Exception e) {
		}
		
		return 0;
	}
	
	public Map<String, Long> sumAllamount1(String startDate, String endDate){
		//select sum(order_actual_fee)  from share_report where gmt_create='2010-11-22'
		//select sum(order_actual_fee)  from share_report where gmt_create='2010-11-22' and report_type=2;
		final Map<String, Long> map = new HashMap<String, Long>();
		String sql ="select order_actual_fee as amount,gmt_create as time from share_report where gmt_create >= ? and gmt_create <= ? and report_type=2";
		try {
			this.query(sql, new Object[]{startDate, endDate}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					try {
						if(!map.containsKey(rs.getString("time"))) {
							map.put(rs.getString("time"), rs.getLong("amount"));
						} else {
							map.put(rs.getString("time"), map.get(rs.getString("time")) + rs.getLong("amount"));
						}
					} catch (Exception e) {
						logger.error("",e);
					}
				}
				
			});
		} catch (Exception e) {
			logger.error("",e);
		}
		
		return map;
	}	

}
