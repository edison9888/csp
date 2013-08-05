package com.taobao.csp.cost.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.cost.po.CapacityCostConfigPo;
import com.taobao.csp.cost.util.LocalUtil;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * ∂‘Config_Server,diamondµ»“¿¿µ
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-3
 */
public class CspAppDependDao extends MysqlRouteBase  {

	private static Logger logger = Logger.getLogger(CspAppDependDao.class);
	
	public CspAppDependDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}

	/***
	 * get applications which depend me, from netstat, not logs
	 * @param appName
	 * @return
	 */
	public Map<String, Set<String>> getDepMeApp(String appName) {
		String sql = "select * from csp_app_depend_app where dep_ops_name=? and collect_time="
				+ " (select max(collect_time) from csp_app_depend_app)";

		final Map<String, Set<String>> dep = new LinkedHashMap<String, Set<String>>();

		try {
			this.query(sql, new Object[] { appName }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("ops_name");
					String depType = rs.getString("dep_app_type");
					if (depType == null) depType = "unknownType";
					if (dep.containsKey(depType)) {
						dep.get(depType).add(appName);
					} else {
						Set<String> appSet = new LinkedHashSet<String>();
						appSet.add(appName);
						dep.put(depType, appSet);
					}

				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return dep;
	}
	

	/***
	 * get applications which  I depend, from netstat, not logs
	 * @param appName
	 * @return
	 */
	public Map<String, Set<String>> getMeDepApp(String appName) {
		String sql = "select * from csp_app_depend_app where ops_name=? and collect_time="
				+ " (select max(collect_time) from csp_app_depend_app)";

		
		final Map<String, Set<String>> dep = new LinkedHashMap<String, Set<String>>();
		
		try {
			this.query(sql, new Object[]{ appName }, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("dep_ops_name");
					String depType = rs.getString("dep_app_type");
					if (depType == null) depType = "unknownType";
					if (dep.containsKey(depType)) {
						dep.get(depType).add(appName);
					} else {
						Set<String> appSet = new LinkedHashSet<String>();
						appSet.add(appName);
						dep.put(depType, appSet);
					}
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return dep;
	}
	

	public Map<String, List<CapacityCostConfigPo>> getCapacityCostConfigPos() {
		final Map<String, List<CapacityCostConfigPo>> map = new HashMap<String, List<CapacityCostConfigPo>>();
		Date date = findLatestConfigDate();
		
		String sql = "select distinct dep_ops_name,ops_name,dep_app_type" +
				" from csp_app_depend_app where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")" +
				" and dep_app_type = 'configserver'";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					List<CapacityCostConfigPo> list;
					String goupName = rs.getString("dep_ops_name");
					String appName = rs.getString("ops_name");
					CapacityCostConfigPo po = new CapacityCostConfigPo();
					po.setAppName(appName);
					po.setConfigGroup(goupName);
					po.setDepType(rs.getString("dep_app_type"));
					
					if (map.containsKey(goupName)) {
						list = map.get(goupName);
					} else {
						list = new ArrayList<CapacityCostConfigPo>();
						map.put(goupName, list);
					}
					
					list.add(po);
					po.setNum(list.size());
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	

	public Map<String, List<CapacityCostConfigPo>> getCapacityCostDiamondPos() {
		final Map<String, List<CapacityCostConfigPo>> map = new HashMap<String, List<CapacityCostConfigPo>>();
//		Date date = findLatestConfigDate();
		
		String sql = "select distinct dep_ops_name,ops_name,dep_app_type" +
				" from csp_app_depend_app where " +
				"  dep_app_type='diamond'";
		try {
			this.query(sql, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					List<CapacityCostConfigPo> list;
					String goupName = rs.getString("dep_ops_name");
					String appName = rs.getString("ops_name");
					CapacityCostConfigPo po = new CapacityCostConfigPo();
					po.setAppName(appName);
					po.setConfigGroup(goupName);
					po.setDepType(rs.getString("dep_app_type"));
					
					if (map.containsKey(goupName)) {
						list = map.get(goupName);
					} else {
						list = new ArrayList<CapacityCostConfigPo>();
						map.put(goupName, list);
					}
					
					list.add(po);
					po.setNum(list.size());
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}

	public Date findLatestConfigDate() {
		final List<Date> list = new ArrayList<Date>();
		
		String sql = "select max(collect_time) as collect_time from csp_app_depend_app";
		try {
			this.query( sql , new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Date date = rs.getDate("collect_time");
					list.add(date);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list.isEmpty() ? LocalUtil.getCapacityCostDate() : list.get(0);
	}
	
	
}
