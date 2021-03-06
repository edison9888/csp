
package com.taobao.monitor.common.db.impl.day;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;


/**
 * mysql> desc business_data_detail;
+--------------+------------+------+-----+---------+-------+
| Field        | Type       | Null | Key | Default | Extra |
+--------------+------------+------+-----+---------+-------+
| update_time  | datetime   | NO   | PRI | NULL    |       | 
| data_num    | bigint(20) | YES  |     | NULL    |       | 
| data_addup   | bigint(20) | YES  |     | NULL    |       | 
| data_type    | int(2)     | NO   | PRI | NULL    |       | 
| service_type | int(2)     | NO   | PRI | NULL    |       | 
+--------------+------------+------+-----+---------+-------+
service_type  pv  3
data_type  list.taobao.com  6 
          search1.taobao.com  4 
          item.taobao.com   2
          buy.taobao.com  17
          trade.taobao.com  5
          shop.taobao.com  3
update_time   时间点
data_num      与前一时间点内累计值
data_addup     当天到当前时间累计值

 * @author xiaodu
 * @version 2010-4-7 上午09:46:41
 */
public class HaBoDataDao extends MysqlRouteBase {
	
		

	private HaBoDataDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Main_HaBo"));
	}
	
	private static HaBoDataDao dao = new HaBoDataDao();
	
	public static HaBoDataDao getInstance() {
		return dao;
	}
	
	public long sumAllamount(Date startDate){
		
		String sql ="select amount from tc_alipay_sum where check_day = ?";
		
		try {
			return this.getLongValue(sql,new Object[]{startDate});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	
	/**
	 * 
	 * @param type 
	 * @param dateTime 2010-04-06
	 * @return
	 */
	public long findAppDatePv(String type,String dateTime){
		String sql = "select sum(data) as pvcount from business_data_daycount where service_type=6 and data_type in ("+type+") and check_day = ?";
		
		final Pv pv = new Pv();
		
		try {
			this.query(sql, new Object[]{ dateTime }, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws SQLException {					
					long _pv = rs.getLong("pvcount");				
					pv.num = _pv;
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return pv.num;
	}
	
	
	/**
	 * 
	 * @param type 
	 * @param beginDate 2010-04-06
	 * @return
	 */
	public long findAppPvBetweenDate(String type,String beginDate, String endDate){
		String sql = "select sum(data) as pvcount from business_data_daycount where service_type=6 and data_type =? and check_day between ? and ?";
		
		final Pv pv = new Pv();
		
		try {
			this.query(sql, new Object[]{type, beginDate+" 00:00:00",endDate+" 23:59:59"}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws SQLException {					
					long _pv = rs.getLong("pvcount");				
					pv.num = _pv;
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return pv.num;
	}
	
	
	
	/**
	 * 查询一天内的 每个时间点的pv量
	 * @param type
	 * @param dateTime
	 * @return
	 */
	public Map<String, Long> findPvByTime(String type,String dateTime){
		
		
		final Map<String, Pv> keyValueMap = new HashMap<String, Pv>();
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		

		String sql = "select data_addup,update_time,data_num from business_data_detail where service_type=3 and data_type in ("+type+") and update_time between ? and ?";
		
		
		try {
			this.query(sql, new Object[]{dateTime+" 00:00:00",dateTime+" 23:59:59"}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws SQLException {						
					long _pv = rs.getLong("data_num");						
					String dateTime = rs.getString("update_time");
					String time = dateTime.substring(0, 16);
					
					Pv po = keyValueMap.get(time);
					if(po==null){
						po = new Pv();
						po.num = _pv;
						keyValueMap.put(time, po);
					}else{
						po.num += _pv;
						keyValueMap.put(time, po);
					}
					
											
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Long> map = new HashMap<String, Long>();
		
		for(Map.Entry<String, Pv> entry:keyValueMap.entrySet()){
			map.put(entry.getKey(), entry.getValue().num);
		}
		
		return map;
	}
	
	
	private class Pv{
		private long num;
	}

}
