package com.taobao.csp.cost.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.csp.cost.po.SimpleCostPo;
import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.CostConstants;
import com.taobao.csp.cost.util.LocalUtil;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * TDDL的依赖
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-3
 */
public class CspTddlDependDao  extends MysqlRouteBase{

	private static Log logger = LogFactory.getLog(CspTddlDependDao.class);
	
	public CspTddlDependDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_tddl"));
	}
	public Map<String, Long> getDbConsumerSummary(Date date) {
		final Map<String, Long> map = new HashMap<String, Long>();
		
		String sql = "select db_name, sum(execute_sum) as num from csp_app_consume_tddl_summary_new where " +
				" DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				"and db_name is not null " +
				"and db_name != '' group" +
				" by  db_name";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String dbName = rs.getString("db_name");
					long num = rs.getLong("num");
					map.put(dbName, num);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	

	/**
	 * 查询一个db的ip列表
	 * 
	 * @param tableId
	 * @param date
	 * @return
	 */
	public Map<String, Set<String>> getDbMachines(int tableId) {
		final Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		
//		String sql = "select db_name, db_ip from csp_app_consume_tddl_detail where " +
//				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
//				"and db_name is not null and db_name != ''";
		String sql="select db_name, db_ip from csp_app_consume_tddl_detail_"+tableId+" where" +
				" db_name is not null and db_name != '' group by db_name,db_ip";
		
		try {
			this.query(sql, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String dbName = rs.getString("db_name");
					String hostIp = rs.getString("db_ip");
					
					Set<String> ips;
					if (map.containsKey(dbName)) {
						ips = map.get(dbName);
					} else {
						ips = new HashSet<String>();
						map.put(dbName, ips);
					}
					ips.add(hostIp);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	

	/**
	 * 返回DB$APP的次数
	 * @param dbName
	 * @return
	 */
	public Map<String, Long> getDbDepSummary(String dbName,Date date) {
		final Map<String, Long> map = new HashMap<String, Long>();
		
		String sql = "select concat(db_name, '" + CostConstants.DEP_SEP + "', app_name) as dep, " +
				"sum(execute_sum) as num from csp_app_consume_tddl_summary_new where " +
				" DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and db_name = ? group" +
				" by  dep";
		try {
			this.query(sql, new Object[]{ date, dbName }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String dep = rs.getString("dep");
					long num = rs.getLong("num");
					map.put(dep, num);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	/**
	 * 返回APP各个DB的次数
	 * @param dbName
	 * @return
	 */
	public List<SimpleCostPo> getAppDbSummary(String appName,Date date) {
		final List<SimpleCostPo> poList = new ArrayList<SimpleCostPo>();
		
		String sql = "select db_name, " +
				"sum(execute_sum) as num from csp_app_consume_tddl_summary_new where " +
				" DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and app_name = ?" +
				" group by db_name";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					SimpleCostPo spo=new SimpleCostPo(CostType.DB.toString());
					spo.setDependName(rs.getString("db_name"));
					spo.setCallNum(rs.getLong("num"));
					
					poList.add(spo);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return poList;
	}
}
