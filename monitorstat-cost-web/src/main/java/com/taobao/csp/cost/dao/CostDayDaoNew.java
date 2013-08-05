package com.taobao.csp.cost.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.cost.po.CostAppTotal;
import com.taobao.csp.cost.po.CostAppType;
import com.taobao.csp.cost.po.CostBaseTotalPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * 总成本统计
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-19
 */
public class CostDayDaoNew  extends MysqlRouteBase{
	
	private static Logger logger = Logger.getLogger(CostDayDaoNew.class);
	
	public CostDayDaoNew(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}

	/**
	 * 获得基础依赖的成本
	 * 
	 * @param appname
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List<CostBaseTotalPo> getBaseCostTotal(String appname,String costType,
			Date sdate,Date edate) {

		String sql="select * from csp_cost_base_total where base_name=? and type=? and " +
				"collect_time>=? and collect_time<=?";
		final List<CostBaseTotalPo> pos = new ArrayList<CostBaseTotalPo>();
		try {
			//base_name,type,callNum,machine_num,per_cost,total_cost,collect_time
			this.query(sql, new Object[] {appname,costType,sdate,edate}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					CostBaseTotalPo cat=new CostBaseTotalPo();
					cat.setBaseName(rs.getString("base_name"));
					cat.setCallNum(rs.getLong("callNum"));
					cat.setCollectTime(rs.getDate("collect_time"));
					cat.setCostType(rs.getString("type"));
					cat.setMachineNum(rs.getInt("machine_num"));
					cat.setPerCost(rs.getDouble("base_name"));
					cat.setTotalCost(rs.getDouble("base_name"));
					
					pos.add(cat);
				}
			});
		} catch (Exception e) {
			logger.error("getBaseCostTotal", e);
		}
		if(pos.size()>0){
			return pos;
		}
		return null;
	}
	
	/**
	 * 插入基础依赖的总成本，比如[tair/db/应用的HOST]等
	 * 
	 * @param basePo
	 * @return
	 */
	public boolean addBaseCostAll(CostBaseTotalPo basePo,Date date) {
		boolean success = true;

		String sql = "insert into csp_cost_base_total(base_name,type,callNum,machine_num,per_cost," +
				"total_cost,collect_time)"
				+ "values(?,?,?,?,?,?,?)";
		
		try {
			this.execute(
					sql,
					new Object[] { basePo.getBaseName(), basePo.getCostType().toString(),
							basePo.getCallNum(), basePo.getMachineNum(),
							basePo.getPerCost(), basePo.getTotalCost(),date
					});
		} catch (SQLException e) {
			logger.error("insert new capacity cost error", e);
			success = false;
		}
		
		return success;
	}
	
	/**
	 * 获取app某种依赖的成本
	 * tair/db/hsf/host
	 * 
	 * @param appName
	 * @param type
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List<CostAppType> getTypeAppCostBy(String appName,String type, 
			Date sdate,Date edate) {
		String sql="select * from csp_cost_app_total where app_name=? and cost_type=? and " +
				"collect_time>=? and collect_time<=?";
		final List<CostAppType> pos = new ArrayList<CostAppType>();
		try {
			//app_name,cost_type,cost_name,callNum,
			//depend_cost,collect_time,group_name,line
			this.query(sql, new Object[] {appName,type,sdate,edate}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					CostAppType cat=new CostAppType();
					cat.setAppName(rs.getString("app_name"));
					
					cat.setCallNum(rs.getLong("callNum"));
					cat.setCollectTime(rs.getDate("collect_time"));
					cat.setCostName(rs.getString("cost_name"));
					cat.setCostType(rs.getString("cost_type"));
					cat.setDependCost(rs.getDouble("depend_cost"));
					cat.setGroupName(rs.getString("group_name"));
					cat.setLine(rs.getString("line"));
					
					pos.add(cat);
				}
			});
		} catch (Exception e) {
			logger.error("getBaseCostTotal", e);
		}
		return pos;
	}
	
	/**
	 * 获取依赖某app的成本
	 * tair/db/hsf/host
	 * 
	 * @param appName
	 * @param type
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List<CostAppType> getTypeAppCostBy(String costName, 
			Date sdate,Date edate) {
		String sql="select * from csp_cost_app_total where cost_name=? and cost_type='HSF_PV' and " +
				"collect_time>=? and collect_time<=?";
		final List<CostAppType> pos = new ArrayList<CostAppType>();
		try {
			//app_name,cost_type,cost_name,callNum,
			//depend_cost,collect_time,group_name,line
			this.query(sql, new Object[] {costName,sdate,edate}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					CostAppType cat=new CostAppType();
					cat.setAppName(rs.getString("app_name"));
					
					cat.setCallNum(rs.getLong("callNum"));
					cat.setCollectTime(rs.getDate("collect_time"));
					cat.setCostName(rs.getString("cost_name"));
					cat.setCostType(rs.getString("cost_type"));
					cat.setDependCost(rs.getDouble("depend_cost"));
					cat.setGroupName(rs.getString("group_name"));
					cat.setLine(rs.getString("line"));
					
					pos.add(cat);
				}
			});
		} catch (Exception e) {
			logger.error("getBaseCostTotal", e);
		}
		return pos;
	}
	
	
	/**
	 * 插入业务应用的总成本
	 * 
	 * 包括直接依赖成本和总成本
	 * @param basePo
	 * @return
	 */
	public boolean addTypeCostAll(CostAppType basePo) {
		boolean success = true;

		String sql = "insert into csp_cost_app_total(app_name,cost_type,cost_name,callNum," +
				"depend_cost,collect_time,group_name,line)"
				+ "values(?,?,?,?,?,?,?,?)";

		try {
			this.execute(
					sql,new Object[] { basePo.getAppName(), basePo.getCostType().toString(),
							basePo.getCostName(),	basePo.getCallNum(), 
							basePo.getDependCost(), basePo.getCollectTime(),
							basePo.getGroupName(),	basePo.getLine()
					});
		} catch (SQLException e) {
			logger.error("insert new capacity cost error", e);
			success = false;
		}
		return success;
	}
	
	/**
	 * 添加总成本（公司级别，业务线级别，APP级别）
	 * 
	 * @param mapEntry
	 * @return
	 */
	public boolean addTotalCostAll(CostAppTotal mapEntry) {
		boolean success = true;
		String sql = "insert into csp_cost_app_total_count(" +
				"sname,pname,cost_type,time_type,cost_all,sdate,callNum,per_cost) values(?,?,?,?,?,?,?,?)";
		try {
			this.execute(
					sql,new Object[] {mapEntry.getsName(),mapEntry.getpName(), mapEntry.getCostType(),
							mapEntry.getTimeType(),	mapEntry.getCostAll(), mapEntry.getsTime(), 
							mapEntry.getCallNum(), mapEntry.getPreCost()
					});
		} catch (SQLException e) {
			logger.error("addTotalCostWithTime exception", e);
			success = false;
		}
		
		return success;
	}
	
	/**
	 * 根据一级，二级产品线获得应用成本
	 * 
	 * @param mapEntry
	 * @return
	 */
	public List<CostAppTotal> getAppMonthCost(String level,String name,Date sdate,Date edate) {
		StringBuilder sb=new StringBuilder(
				"select sname,pname,cost_type,time_type,cost_all,sdate,callNum,per_cost " +
				"from csp_cost_app_total_count ");
		sb.append("where cost_type=? and");
		if(!StringUtils.isBlank(name)){
			sb.append(" pname=? and");
		}
		sb.append(" sdate>=? and sdate<=?");
		
		Object[] qObj=null;
		if(!StringUtils.isBlank(name)){
			qObj= new Object[]{ level,name,sdate,edate };
		}else{

			qObj= new Object[]{ level,sdate,edate };
		}
		
		final List<CostAppTotal> list = new ArrayList<CostAppTotal>();
		try {
			this.query(sb.toString(), qObj, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					
					CostAppTotal cat=new CostAppTotal();
					cat.setpName(rs.getString("pname"));
					cat.setsName(rs.getString("sname"));
					cat.setCostAll(rs.getDouble("cost_all"));
					cat.setCallNum(rs.getLong("callNum"));
					cat.setPreCost(rs.getDouble("per_cost"));
					cat.setCostType(rs.getString("cost_type"));
					cat.setsTime(rs.getDate("sdate"));
					cat.setTimeType(rs.getInt("time_type"));
					
					list.add(cat);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	/**
	 * 获得APP一段时间内的成本
	 * 
	 * @param name
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List<CostAppTotal> getAppCost(String name,Date sdate,Date edate) {
		StringBuilder sb=new StringBuilder(
				"select sname,cost_type,time_type,pname,callNum," +
				"cost_all,per_cost,sdate from csp_cost_app_total_count ");
		sb.append("where sname=? and sdate>=? and sdate<=?");
		
		final List<CostAppTotal> list = new ArrayList<CostAppTotal>();
		try {
			this.query(sb.toString(), new Object[]{ name,sdate,edate }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					
					CostAppTotal cat=new CostAppTotal();
					cat.setsName(rs.getString("sname"));
					cat.setCostType(rs.getString("cost_type"));
					cat.setTimeType(rs.getInt("time_type"));
					cat.setpName(rs.getString("pname"));
					cat.setCallNum(rs.getLong("callNum"));
					cat.setCostAll(rs.getDouble("cost_all"));
					cat.setPreCost(rs.getDouble("per_cost"));
					cat.setsTime(rs.getDate("sdate"));
					
					list.add(cat);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
				
	
	/**
	 * 取得平均值
	 * 
	 * @param level
	 * @param name
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List<CostAppTotal> getAvgMonthCost(String level,String name,Date sdate,Date edate) {
		StringBuilder sb=new StringBuilder(
				"select sname,cost_type,time_type,pname,avg(callNum) avgCallNum," +
				"avg(cost_all) avgCostAll,avg(per_cost) avgPerCall " +
				"from csp_cost_app_total_count ");
		sb.append("where cost_type=? and callNum!=1111 and");
		if(!StringUtils.isBlank(name)){
			sb.append(" pname=? and");
		}
		sb.append(" sdate>=? and sdate<=?");
		
		sb.append(" group by sname");
		Object[] qObj=null;
		if(!StringUtils.isBlank(name)){
			qObj= new Object[]{ level,name,sdate,edate };
		}else{

			qObj= new Object[]{ level,sdate,edate };
		}
		
		final List<CostAppTotal> list = new ArrayList<CostAppTotal>();
		try {
			this.query(sb.toString(), qObj, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					
					CostAppTotal cat=new CostAppTotal();
					cat.setsName(rs.getString("sname"));
					cat.setCostType(rs.getString("cost_type"));
					cat.setTimeType(rs.getInt("time_type"));
					cat.setpName(rs.getString("pname"));
					cat.setPreCallNum(rs.getDouble("avgCallNum"));
					cat.setPreCostAll(rs.getDouble("avgCostAll"));
					cat.setPrePreCost(rs.getDouble("avgPerCall"));
					
					list.add(cat);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
}
