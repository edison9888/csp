/**
 * 
 */
package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.vo.Pv;

/**
 * 
 * @author xiaodu
 * @version 2010-4-16 上午11:21:55
 */
/**
 * @author xiaodu
 * 
 */
public class MonitorPvDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(MonitorPvDao.class);

	public void addRecordPv(int collectDay, long pv, long uv) {
		String sql = "insert into MS_MONITOR_PV(pv,uv,collect_day)values(?,?,?)";
		try {
			this.execute(sql, new Object[] { pv, uv, collectDay });
		} catch (SQLException e) {
			logger.error("addRecordPv 出错,", e);
		}
	}

	public void updateRecordPv(int collectDay, long pv, long uv) {
		String sql = "update MS_MONITOR_PV set pv=pv+" + pv + ",uv=uv+" + uv + " where collect_day=?";
		try {
			this.execute(sql, new Object[] { collectDay });
		} catch (SQLException e) {
			logger.error("addRecordPv 出错,", e);
		}
	}

	public List<Pv> findPv(int start, int end) {
		String sql = "select * from MS_MONITOR_PV where collect_day between ? and ?";

		final List<Pv> listPv = new ArrayList<Pv>();

		try {
			this.query(sql, new Object[] { start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					Pv pv = new Pv();
					pv.setPv(rs.getLong("pv"));
					pv.setUv(rs.getLong("uv"));
					pv.setCollectDay(rs.getInt("collect_day"));
					listPv.add(pv);
				}
			});
		} catch (Exception e) {
			logger.error("findPv 出错,", e);
		}
		return listPv;
	}

	public Pv findPv(int start) {
		String sql = "select * from MS_MONITOR_PV where collect_day = ?";

		final Pv pv = new Pv();

		try {
			this.query(sql, new Object[] { start }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					pv.setPv(rs.getLong("pv"));
					pv.setUv(rs.getLong("uv"));
					pv.setCollectDay(rs.getInt("collect_day"));
				}
			});
		} catch (Exception e) {
			logger.error("findPv 出错,", e);
		}
		return pv.getCollectDay() == 0 ? null : pv;
	}


}
