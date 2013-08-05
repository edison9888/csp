package com.taobao.csp.cost.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.cost.po.CapacityCostDepPo;
import com.taobao.csp.cost.po.CapacityCostInfoPo;
import com.taobao.csp.cost.po.CapacityCostRatioPo;
import com.taobao.csp.cost.po.DependencyCapacityPo;
import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.DepMode;
import com.taobao.csp.cost.util.LocalUtil;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * 总成本信息的DAO
 */
public class CostDayDao extends MysqlRouteBase {
	
	private static Logger logger = Logger.getLogger(CostDayDao.class);
	
	public CostDayDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}

	public boolean addCapacityCostInfo(CapacityCostInfoPo po) {
		boolean success = true;

		String sql = "insert into csp_capacity_cost_info(app_name,cost_type,pv,machine_num,per_cost,self_cost," +
				"depend_cost,depend_per_cost,total_cost,total_per_cost,collect_time)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?)";

		try {
			this.execute(
					sql,
					new Object[] { po.getAppName(), po.getCostType().toString(), po.getPv(), po.getMachineNum(),
							po.getPerCost(), po.getSelfCost(),  po.getDependCost(), po.getDependPerCost(), po.getTotalCost(), 
							po.getTotalPerCost(), Calendar.getInstance().getTime()
					});
		} catch (SQLException e) {
			logger.error("insert capacity cost error", e);
			success = false;
		}
		
		return success;
	}
	
	public List<CapacityCostInfoPo> findCapacityCostListAll() {
		final List<CapacityCostInfoPo> list = new ArrayList<CapacityCostInfoPo>();
		Date date = findLatestDate();
		
		String sql = "select * from csp_capacity_cost_info where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") order by total_per_cost desc";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCostInfoPo po = new CapacityCostInfoPo();
					po.setAppName(rs.getString("app_name"));
					po.setCostType(CostType.valueOf(rs.getString("cost_type")));
					po.setPv(rs.getLong("pv"));
					po.setMachineNum(rs.getInt("machine_num"));
					po.setPerCost(rs.getDouble("per_cost"));
					po.setSelfCost(rs.getLong("self_cost"));
					po.setDependCost(rs.getLong("depend_cost"));
					po.setDependPerCost(rs.getDouble("depend_per_cost"));
					po.setTotalCost(rs.getLong("total_cost"));
					po.setTotalPerCost(rs.getDouble("total_per_cost"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					po.setNum(list.size() + 1);
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public List<CapacityCostInfoPo> findCapacityCostListNormal() {
		final List<CapacityCostInfoPo> list = new ArrayList<CapacityCostInfoPo>();
		Date date = findLatestDate();
		
		String sql = "select * from csp_capacity_cost_info where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and (cost_type = '" + CostType.PV.toString() + "' or cost_type = '" + CostType.HSF.toString() + "' " +
						" or cost_type = '" + CostType.HSF_PV.toString() + "') order by pv desc";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCostInfoPo po = new CapacityCostInfoPo();
					po.setAppName(rs.getString("app_name"));
					po.setCostType(CostType.valueOf(rs.getString("cost_type")));
					po.setPv(rs.getLong("pv"));
					po.setMachineNum(rs.getInt("machine_num"));
					po.setPerCost(rs.getDouble("per_cost"));
					po.setSelfCost(rs.getLong("self_cost"));
					po.setDependCost(rs.getLong("depend_cost"));
					po.setDependPerCost(rs.getDouble("depend_per_cost"));
					po.setTotalCost(rs.getLong("total_cost"));
					po.setTotalPerCost(rs.getDouble("total_per_cost"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					po.setNum(list.size() + 1);
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public CapacityCostInfoPo findLatestCapacityCostByApp(String appName) {
		final List<CapacityCostInfoPo> list = new ArrayList<CapacityCostInfoPo>();
		
		String sql = "select * from csp_capacity_cost_info where app_name=? order by collect_time desc limit 1";
		try {
			this.query(sql, new Object[]{ appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCostInfoPo po = new CapacityCostInfoPo();
					po.setAppName(rs.getString("app_name"));
					po.setCostType(CostType.valueOf(rs.getString("cost_type")));
					po.setPv(rs.getLong("pv"));
					po.setMachineNum(rs.getInt("machine_num"));
					po.setPerCost(rs.getDouble("per_cost"));
					po.setSelfCost(rs.getLong("self_cost"));
					po.setDependCost(rs.getLong("depend_cost"));
					po.setDependPerCost(rs.getDouble("depend_per_cost"));
					po.setTotalCost(rs.getLong("total_cost"));
					po.setTotalPerCost(rs.getDouble("total_per_cost"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					po.setNum(list.size() + 1);
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public List<CapacityCostInfoPo> findCapacityCostListTair() {
		final List<CapacityCostInfoPo> list = new ArrayList<CapacityCostInfoPo>();
		Date date = findLatestDate();
		
		String sql = "select * from csp_capacity_cost_info where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and cost_type = '" + CostType.TAIR.toString() + "' order by pv desc";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCostInfoPo po = new CapacityCostInfoPo();
					po.setAppName(rs.getString("app_name"));
					po.setCostType(CostType.valueOf(rs.getString("cost_type")));
					po.setPv(rs.getLong("pv"));
					po.setMachineNum(rs.getInt("machine_num"));
					po.setPerCost(rs.getDouble("per_cost"));
					po.setSelfCost(rs.getLong("self_cost"));
					po.setDependCost(rs.getLong("depend_cost"));
					po.setDependPerCost(rs.getDouble("depend_per_cost"));
					po.setTotalCost(rs.getLong("total_cost"));
					po.setTotalPerCost(rs.getDouble("total_per_cost"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					po.setNum(list.size() + 1);
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public List<CapacityCostInfoPo> findCapacityCostListDB() {
		final List<CapacityCostInfoPo> list = new ArrayList<CapacityCostInfoPo>();
		Date date = findLatestDate();
		
		String sql = "select * from csp_capacity_cost_info where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and cost_type = '" + CostType.DB.toString() + "' order by pv desc";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCostInfoPo po = new CapacityCostInfoPo();
					po.setAppName(rs.getString("app_name"));
					po.setCostType(CostType.valueOf(rs.getString("cost_type")));
					po.setPv(rs.getLong("pv"));
					po.setMachineNum(rs.getInt("machine_num"));
					po.setPerCost(rs.getDouble("per_cost"));
					po.setSelfCost(rs.getLong("self_cost"));
					po.setDependCost(rs.getLong("depend_cost"));
					po.setDependPerCost(rs.getDouble("depend_per_cost"));
					po.setTotalCost(rs.getLong("total_cost"));
					po.setTotalPerCost(rs.getDouble("total_per_cost"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					po.setNum(list.size() + 1);
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public List<CapacityCostInfoPo> findCapacityCost(String appName) {
		final List<CapacityCostInfoPo> list = new ArrayList<CapacityCostInfoPo>();
		Date date = findLatestDate();
		
		String sql = "select * from csp_capacity_cost_info where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and (cost_type = '" + CostType.PV.toString() + "' or cost_type = '" + CostType.HSF.toString() + "' " +
						" or cost_type = '" + CostType.HSF_PV.toString() + "') and app_name=? order by collect_time desc";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCostInfoPo po = new CapacityCostInfoPo();
					po.setAppName(rs.getString("app_name"));
					po.setCostType(CostType.valueOf(rs.getString("cost_type")));
					po.setPv(rs.getLong("pv"));
					po.setMachineNum(rs.getInt("machine_num"));
					po.setPerCost(rs.getDouble("per_cost"));
					po.setSelfCost(rs.getLong("self_cost"));
					po.setDependCost(rs.getLong("depend_cost"));
					po.setDependPerCost(rs.getDouble("depend_per_cost"));
					po.setTotalCost(rs.getLong("total_cost"));
					po.setTotalPerCost(rs.getDouble("total_per_cost"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					po.setNum(list.size() + 1);
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public Date findLatestDate() {
		final List<Date> list = new ArrayList<Date>();
		
		String sql = "select max(collect_time) as collect_time from csp_capacity_cost_info";
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
	
	public boolean addCapacityCostDep(String appName, String depAppName, String depType, DepMode depMode, long depPv, long depCost) {
		boolean success = true;

		String sql = "insert into csp_capacity_cost_depend(app_name,dep_app_name,dep_type,dep_mode,dep_pv,dep_cost,collect_time)"
				+ "values(?,?,?,?,?,?,?)";

		try {
			this.execute(
					sql,
					new Object[] { appName, depAppName, depType, depMode.toString(), depPv, depCost, Calendar.getInstance().getTime()
					});
		} catch (SQLException e) {
			logger.error("insert capacity cost error", e);
			success = false;
		}
		
		return success;
	}
	
	public List<CapacityCostDepPo> findCapacityCostDepList(String appName) {
		final List<CapacityCostDepPo> list = new ArrayList<CapacityCostDepPo>();
		Date date = findLatestDate();
		
		String sql = "select cost.app_name as app_name, dep.dep_app_name as dep_app_name, dep.dep_type as dep_type, dep.dep_pv as dep_pv, " +
				" dep.dep_cost as dep_cost, dep.collect_time as collect_time, cost.machine_num as dep_machine_num, " +
				" cost.pv as dep_total_pv, cost.self_cost as dep_self_cost, cost.per_cost as dep_per_cost, dep.dep_mode as dep_mode" +
				" from csp_capacity_cost_depend dep inner join csp_capacity_cost_info cost on " +
				"dep.dep_app_name = cost.app_name and DATE_FORMAT(dep.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(cost.collect_time,\"%Y-%m-%d\") " +
				" where DATE_FORMAT(dep.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and dep.app_name = ?  order by dep_app_name asc";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCostDepPo po = new CapacityCostDepPo();
					po.setAppName(rs.getString("app_name"));
					po.setDepAppName(rs.getString("dep_app_name"));
					po.setDepType(CostType.valueOf(rs.getString("dep_type")));
					po.setDepPv(rs.getLong("dep_pv"));
					po.setDepCost(rs.getLong("dep_cost"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					po.setDepAppMachineNum(rs.getInt("dep_machine_num"));
					po.setDepAppTotalPv(rs.getLong("dep_total_pv"));
					po.setDepAppPerCost(rs.getDouble("dep_per_cost"));
					po.setDepAppSelfCost(rs.getLong("dep_self_cost"));
					po.setDepMode(DepMode.valueOf(rs.getString("dep_mode")));
					po.setNum(list.size() + 1);

					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public List<CapacityCostDepPo> findCapacityCostDepSelfList(String appName) {
		final List<CapacityCostDepPo> list = new ArrayList<CapacityCostDepPo>();
		Date date = findLatestDate();
		
		String sql = "select dep.app_name as app_name, dep.dep_app_name as dep_app_name, dep.dep_type as dep_type, dep.dep_pv as dep_pv, " +
				" dep.dep_cost as dep_cost, dep.collect_time as collect_time, cost.machine_num as dep_machine_num, " +
				" cost.pv as dep_total_pv, cost.self_cost as dep_self_cost, cost.per_cost as dep_per_cost " +
				" from csp_capacity_cost_depend dep inner join csp_capacity_cost_info cost on " +
				"dep.dep_app_name = cost.app_name and DATE_FORMAT(dep.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(cost.collect_time,\"%Y-%m-%d\") " +
				" where DATE_FORMAT(dep.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and dep.dep_app_name = ? and dep.dep_mode = ? order by dep_cost desc";
		try {
			this.query(sql, new Object[]{ date, appName, "直接"  }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCostDepPo po = new CapacityCostDepPo();
					po.setAppName(rs.getString("app_name"));
					po.setDepAppName(rs.getString("dep_app_name"));
					po.setDepType(CostType.valueOf(rs.getString("dep_type")));
					po.setDepPv(rs.getLong("dep_pv"));
					po.setDepCost(rs.getLong("dep_cost"));
					po.setCollectTime(rs.getTimestamp("collect_time"));
					po.setDepAppMachineNum(rs.getInt("dep_machine_num"));
					po.setDepAppTotalPv(rs.getLong("dep_total_pv"));
					po.setDepAppPerCost(rs.getDouble("dep_per_cost"));
					po.setDepAppSelfCost(rs.getLong("dep_self_cost"));
					po.setNum(list.size() + 1);
					
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public List<DependencyCapacityPo> findDependCapacityPos(String appName) {
		final List<DependencyCapacityPo> list = new ArrayList<DependencyCapacityPo>();
		Date date = findLatestDate();
		
		String sql = "select dep.dep_app_name as dep_app_name, dep.dep_pv as dep_pv, " +
				" cost.pv as dep_total_pv " +
				" from csp_capacity_cost_depend dep inner join csp_capacity_cost_info cost on " +
				"dep.dep_app_name = cost.app_name and DATE_FORMAT(dep.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(cost.collect_time,\"%Y-%m-%d\") " +
				" where DATE_FORMAT(dep.collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and dep.app_name = ?  and (cost.cost_type = '" + CostType.PV.toString() + "' or cost_type = '" + CostType.HSF.toString() + "' " +
				" or cost_type = '" + CostType.HSF_PV.toString() + "') order by dep_app_name asc";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					DependencyCapacityPo po = new DependencyCapacityPo();
					long depPv = rs.getLong("dep_pv");
					long totalPv = rs.getLong("dep_total_pv");
					
					po.setDepApp(rs.getString("dep_app_name"));
					po.setDepPv(depPv);
					po.setTotalPv(totalPv);
					po.setPercent(((double) depPv) / totalPv * 100);
					po.setNum(list.size() + 1); 

					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public Double getCostRatio(String appName) {
		final List<Double> list = new ArrayList<Double>();
		
		String sql = "select ratio from csp_capacity_cost_ratio where app_name = ? ";
		try {
			this.query(sql, new Object[]{  appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Double value = rs.getDouble("ratio");
					list.add(value);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		double dRatio;
		if (list.size() > 0) {
			dRatio = list.get(0);
		} else  {
			dRatio = 1.0d;
			addCostRatio(appName, dRatio);
		}
		
		return dRatio;
	}
	
	public boolean updateCostRatio(String appName, double ratio) {
		String sql = "update csp_capacity_cost_ratio set ratio=? where app_name=? ";
		try {
			this.execute(sql, new Object[]{ ratio, appName });
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		
		return true;
	}
	
	public List<CapacityCostRatioPo> findCapacityCostRatios() {
		final List<CapacityCostRatioPo> pos = new ArrayList<CapacityCostRatioPo>();
		String sql = "select * from csp_capacity_cost_ratio order by app_name";
		
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityCostRatioPo po = new CapacityCostRatioPo();
					po.setAppName(rs.getString("app_name"));
					po.setRatio(rs.getDouble("ratio"));
					pos.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return pos;
	}
	
	private boolean addCostRatio(String appName, double ratio) {
		String sql = "insert into csp_capacity_cost_ratio (app_name, ratio) values (?,?) ";
		try {
			this.execute(sql, new Object[]{  appName, ratio });
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		
		return true;
	}

}
