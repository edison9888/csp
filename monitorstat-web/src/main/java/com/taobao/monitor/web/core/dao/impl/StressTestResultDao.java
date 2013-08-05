package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class StressTestResultDao extends MysqlRouteBase {
	public StressTestResultDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_autoload"));
	}

	private static final Logger logger = Logger.getLogger(StressTestResultDao.class);

	public List<LoadrunResult> getSystemResourceRelation(int appId, String date, String date1) {
		final List<LoadrunResult> list = new ArrayList<LoadrunResult>();
		String sql = "SELECT * FROM ms_monitor_loadrun_result WHERE app_id = ? "
				+ "AND collect_time BETWEEN STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d') AND loadrun_type <> 'hsf' "
				+ "AND (loadrun_key = 'CPU' OR loadrun_key = 'Rest' OR loadrun_key = 'Load' OR loadrun_key = 'Qps') ORDER BY collect_time,loadrun_key";
		try {
			this.query(sql, new Object[] { appId, date, date1 }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					LoadrunResult ri = new LoadrunResult();
					ri.setAppId(rs.getInt("app_id"));
					ri.setTargetIp(rs.getString("app_id"));
					ri.setControlFeature(rs.getString("control_feature"));
					String type = rs.getString("loadrun_key");
					if (type.equals("CPU")) {
						ri.setKey(ResultKey.CPU);
					} else if (type.equals("Load")) {
						ri.setKey(ResultKey.Load);
					} else if (type.equals("Qps")) {
						ri.setKey(ResultKey.Qps);
					} else {
						ri.setKey(ResultKey.Rest);
					}
					String loadrunType = rs.getString("loadrun_type");
					if (loadrunType.equals("apache")) {
						ri.setLoadrunType(AutoLoadType.apache);
					} else if (loadrunType.equals("httpLoad")) {
						ri.setLoadrunType(AutoLoadType.httpLoad);
					}
					ri.setValue(Double.valueOf(rs.getString("loadrun_value")));
					ri.setLoadrunOrder(rs.getInt("loadrun_order"));
					ri.setCollectTime(rs.getDate("collect_time"));
					list.add(ri);
				}
			});
		} catch (Exception e) {
			logger.error("getSystemResourceRelation: ", e);
		}

		return list;
	}

	public List<LoadrunResult> getMinorGcInfo(int appId, String date, String date1) {
		final List<LoadrunResult> list = new ArrayList<LoadrunResult>();
		String sql = "SELECT * FROM ms_monitor_loadrun_result WHERE app_id = ? " +
				"AND collect_time BETWEEN STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d') AND loadrun_type <> 'hsf' " +
				"AND (loadrun_key = 'GC_Memory' OR loadrun_key = 'GC_Min_Time') ORDER BY collect_time,loadrun_key";
		try {
			this.query(sql, new Object[] { appId, date, date1 }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					LoadrunResult ri = new LoadrunResult();
					ri.setAppId(rs.getInt("app_id"));
					ri.setTargetIp(rs.getString("app_id"));
					ri.setControlFeature(rs.getString("control_feature"));
					String type = rs.getString("loadrun_key");
					if (type.equals("GC_Memory")) {
						ri.setKey(ResultKey.GC_Memory);
					} else if (type.equals("GC_Min_Time")) {
						ri.setKey(ResultKey.GC_Min_Time);
					}
					String loadrunType = rs.getString("loadrun_type");
					if (loadrunType.equals("apache")) {
						ri.setLoadrunType(AutoLoadType.apache);
					} else if (loadrunType.equals("httpLoad")) {
						ri.setLoadrunType(AutoLoadType.httpLoad);
					}
					ri.setValue(Double.valueOf(rs.getString("loadrun_value")));
					ri.setLoadrunOrder(rs.getInt("loadrun_order"));
					ri.setCollectTime(rs.getDate("collect_time"));
					list.add(ri);
				}
			});
		} catch (Exception e) {
			logger.error("getMinorGcInfo: ", e);
		}

		return list;
	}
	public List<LoadrunResult> getPageMemoryInfo(int appId, String date, String date1) {
		final List<LoadrunResult> list = new ArrayList<LoadrunResult>();
		String sql = "SELECT * FROM ms_monitor_loadrun_result WHERE app_id = ? " +
				"AND collect_time BETWEEN STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d') AND loadrun_type <> 'hsf' " +
				"AND (loadrun_key = 'Qps' OR loadrun_key = 'GC_Memory' OR loadrun_key = 'PageSize') ORDER BY collect_time,loadrun_key";
		try {
			this.query(sql, new Object[] { appId, date, date1 }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					LoadrunResult ri = new LoadrunResult();
					ri.setAppId(rs.getInt("app_id"));
					ri.setTargetIp(rs.getString("app_id"));
					ri.setControlFeature(rs.getString("control_feature"));
					String type = rs.getString("loadrun_key");
					if (type.equals("GC_Memory")) {
						ri.setKey(ResultKey.GC_Memory);
					} else if (type.equals("Qps")) {
						ri.setKey(ResultKey.Qps);
					} else if (type.equals("PageSize")) {
						ri.setKey(ResultKey.PageSize);
					}
					String loadrunType = rs.getString("loadrun_type");
					if (loadrunType.equals("apache")) {
						ri.setLoadrunType(AutoLoadType.apache);
					} else if (loadrunType.equals("httpLoad")) {
						ri.setLoadrunType(AutoLoadType.httpLoad);
					}
					ri.setValue(Double.valueOf(rs.getString("loadrun_value")));
					ri.setLoadrunOrder(rs.getInt("loadrun_order"));
					ri.setCollectTime(rs.getDate("collect_time"));
					list.add(ri);
				}
			});
		} catch (Exception e) {
			logger.error("getPageMemoryInfo: ", e);
		}

		return list;
	}
}
