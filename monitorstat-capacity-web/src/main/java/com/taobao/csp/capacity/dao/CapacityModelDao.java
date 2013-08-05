package com.taobao.csp.capacity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.capacity.po.CapacityModelPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class CapacityModelDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(CapacityModelDao.class);

	public CapacityModelDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}
	
	public long findCapacityModelPv(String appName, String date) {
		long num = 0;
		String sql = "select sum(request_num) from csp_app_request_url_summary where app_name = ? and collect_time =?";

		try {
			num = this.getLongValue(sql, new String[] { appName, date }, DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
		} catch (Exception e) {
			logger.error(e);
		}
		return num;
	}
	
	public long findCapacityModelHsf(String appName, String date) {
		long num = 0;
		String sql = "select sum(CALL_SUM) from csp_app_dep_hsf_consume_summary where provider_name=? and collect_time=? and provider_group=?";

		try {
			num = this.getLongValue(sql, new String[] { appName, date, "All" }, DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
		} catch (Exception e) {
			logger.error(e);
		}
		return num;
	}
	
	public Map<String, Long> findCapacityModelHsfInvoke(String appName, String date, long limit) {

		String sql = "select * from csp_app_dep_hsf_consume_summary where provider_name=? and collect_time=? and provider_group=? and call_sum > ?";

		final Map<String, Long> map = new HashMap<String, Long>();
		try {
			this.query(sql, new Object [] { appName, date, "All", limit }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String consumeApp = rs.getString("consume_name");
					long invokeNum = rs.getLong("call_sum");
					map.put(consumeApp, invokeNum);
				}
			}, DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}
	
	public Map<String, Long> findCapacityModelTairInvoke(String appName, String date, final Set<String> limitTair) {

		String sql = "select tair_group_name as group_name,sum(invoking_all_num) as call_num " +
				" from csp_tair_provide_app_summary where collect_time = ?  " +
				"and app_name = ? and  tair_group_name != ?  group by tair_group_name;";

		final Map<String, Long> map = new HashMap<String, Long>();
		try {
			this.query(sql, new Object [] { date, appName, "" }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String groupName = rs.getString("group_name");
					long callName = rs.getLong("call_num");
//					if (limitTair.contains(groupName)) {
//						map.put(groupName, callName);
//					}
					map.put(groupName, callName);
				}
			}, DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
		} catch (Exception e) {
			logger.error(e);
		}
		
		return map;
	}

	/**
	 * 功能：获取所有的预测模型信息。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-22
	 */
	public List<CapacityModelPo> findCapacityModelPoList() {

		String sql = "select id,res_from,res_to,rel_ratio from csp_capacity_model  order by id desc ";

		final List<CapacityModelPo> list = new ArrayList<CapacityModelPo>();
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityModelPo po = new CapacityModelPo();
					po.setId(rs.getString("id"));
					po.setResFrom(rs.getString("res_from"));
					po.setResTo(rs.getString("res_to"));
					po.setRelRatio(rs.getDouble("rel_ratio"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findCapacityModelPoList is fail !", e);
		}
		return list;
	}

	/**
	 * 功能：新增一个model对象。
	 * 
	 * @param po
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-22
	 */
	public void insertToCapacityModelData(CapacityModelPo po) {
		String sql = "insert into `csp_capacity_model` (`id`,`res_from`, `res_to`, `rel_ratio`)" + " values('"
				+ po.getId() + "','" + po.getResFrom() + "','" + po.getResTo() + "','" + po.getRelRatio() + "');";
		logger.info("save model info start !");
		try {
			this.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" insertToCapacityModelData is fail !", e);
		}
	}

	/**
	 * 功能：获取要修改model对象。
	 * 
	 * @param po
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-22
	 */
	public CapacityModelPo editCapacityModelData(String modelId) {
		String sql = "select * from csp_capacity_model where id=? ";
		final CapacityModelPo po = new CapacityModelPo();

		try {
			this.query(sql, new Object[] { modelId }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					setApp(po, rs);
				}
			});
		} catch (Exception e) {
			logger.error("editCapacityModelData is fail ! ", e);
		}
		return po;
	}

	/**
	 * 功能：将查询结果集转化为PO对象。
	 * 
	 * @param po
	 * 
	 * @param rs
	 * 
	 * @throws SQLException
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-22
	 */
	private void setApp(CapacityModelPo po, ResultSet rs) throws SQLException {
		po.setId(rs.getString("id"));
		po.setResFrom(rs.getString("res_from"));
		po.setResTo(rs.getString("res_to"));
		po.setRelRatio(rs.getDouble("rel_ratio"));
	}

	/**
	 * 功能：更新一个model对象。
	 * 
	 * @param po
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-22
	 */
	public boolean updateToCapacityModelData(CapacityModelPo po) {
		String sql = "update `csp_capacity_model`  set  rel_ratio = " + po.getRelRatio() + " where id ='" + po.getId()
				+ "'";
		logger.info("updateToCapacityModelData info start !");
		try {
			this.execute(sql);
		} catch (Exception e) {
			logger.error(" updateToCapacityModelData is fail !", e);
			return false;
		}
		return true;
	}

	/**
	 * 功能：删除一个model对象。
	 * 
	 * @param po
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-22
	 */
	public boolean delToCapacityModelData(String id) {
		String sql = "delete from `csp_capacity_model` where id ='" + id + "'";
		logger.info("delToCapacityModelData info start !");
		try {
			this.execute(sql);
		} catch (Exception e) {
			logger.error(" delToCapacityModelData is fail !", e);
			return false;
		}
		return true;
	}

	@Override
	public void execute(String sql) {
		try {
			super.execute(sql);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
