package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.NotifyPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 *
 */
public class NotifyDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(NotifyDao.class);

	public NotifyDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}

	public List<NotifyPo> getNotifyListByAppName(String appName, String selectDate) {
		final List<NotifyPo> list = new ArrayList<NotifyPo>();
		String sql = "SELECT * FROM csp_notify_consumer_summary WHERE app_name = ? and collect_time = ?";
		try {
			this.query(sql, new Object[] { appName,selectDate }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					NotifyPo po = new NotifyPo();
					po.setSiteName(rs.getString("site_name"));
					po.setGroupName(rs.getString("notify_group_name"));
					po.setOperType(rs.getString("notify_oper_type"));

					po.setRa_s_count(rs.getLong("ra_s_count"));
					po.setRa_f_count(rs.getLong("ra_f_count"));
					po.setS_count(rs.getLong("s_count"));

					po.setWs_count(rs.getLong("ws_count"));
					po.setF_count(rs.getLong("f_count"));
					po.setRa_f_count(rs.getLong("ra_s_count"));
					po.setRe_count(rs.getLong("re_count"));
					po.setNc_count(rs.getLong("nc_count"));
					
					po.setTimeout_count(rs.getLong("timeout_count"));	
				
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
}
