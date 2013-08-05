package com.taobao.csp.cost.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.csp.cost.po.SimpleCostPo;
import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.CostConstants;
import com.taobao.csp.cost.util.LocalUtil;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class CspTairDependDao  extends MysqlRouteBase{

	private static Log logger = LogFactory.getLog(CspTairDependDao.class);
	public CspTairDependDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
	

	/**
	 * 获得tair group被各个应用调用的总次数
	 * 个数如下：
	 * tairGroup1$app1  100
	 * tairGroup1$app2  200
	 * tairGroup2$app1  100
	 * 
	 * @param groupName tair组名
	 * @return
	 */
	public Map<String, Long> getTairDepSummary(String groupName,Date date) {
		final Map<String, Long> map = new HashMap<String, Long>();
		
		String sql = "select concat(tair_group_name, '" + CostConstants.DEP_SEP + "', app_name) as dep, " +
				"sum(invoking_all_num) as num from csp_tair_provide_app_summary where " +
				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and tair_group_name  = ? " +
				"group by  dep";
		try {
			this.query(sql, new Object[]{ date, groupName }, new SqlCallBack() {
				
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
	 * 查询某个app条用tair的所有次数
	 * 
	 * @param appName
	 * @return
	 */
	public List<SimpleCostPo> getAppTairGSummary(String appName,Date date ) {
		final List<SimpleCostPo> poList = new ArrayList<SimpleCostPo>();
		
		String sql = "select tair_group_name," +
				"sum(invoking_all_num) as num from csp_tair_provide_app_summary where " +
				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and app_name  = ? " +
				" group by tair_group_name";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					SimpleCostPo spo=new SimpleCostPo(CostType.TAIR.toString());
					spo.setDependName(rs.getString("tair_group_name"));
					spo.setCallNum(rs.getLong("num"));
					
					poList.add(spo);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		new ThreadPoolExecutor(0, 3, 60, TimeUnit.SECONDS, 
				new java.util.concurrent.LinkedBlockingQueue(100),
				            Executors.defaultThreadFactory(), 
				            new ThreadPoolExecutor.DiscardPolicy());

		return poList;
	}

	public Map<String, Set<String>> getTairMachines(Date date) {
		final Map<String, Set<String>> map = new HashMap<String, Set<String>>();

		
//		String sql = "select tair_group_name, tair_host_ip from csp_tair_provider_app_detail where " +
//				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
//				" and tair_group_name is not null and tair_group_name != ''";
//		
		String sql="select tair_group_name,tair_host_ip from (select tair_group_name,tair_host_ip " +
				"from csp_tair_provider_app_detail where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = " +
				"DATE_FORMAT(?,\"%Y-%m-%d\") and" +
				" tair_group_name is not null and tair_group_name != ''" +
				" group by tair_group_name,tair_host_ip) tair_tmp;";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String groupName = rs.getString("tair_group_name");
					String hostIp = rs.getString("tair_host_ip");
					
					Set<String> ips;
					if (map.containsKey(groupName)) {
						ips = map.get(groupName);
					} else {
						ips = new HashSet<String>();
						map.put(groupName, ips);
					}
					ips.add(hostIp);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}


	public Map<String, Long> getTairProviderSummary(Date date) {
		final Map<String, Long> map = new HashMap<String, Long>();
		
		String sql = "select tair_group_name, sum(invoking_all_num) as num from csp_tair_provide_app_summary where " +
				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and tair_group_name is not null " +
				"and tair_group_name != '' group" +
				" by  tair_group_name";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String groupName = rs.getString("tair_group_name");
					long num = rs.getLong("num");
					map.put(groupName, num);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
}
