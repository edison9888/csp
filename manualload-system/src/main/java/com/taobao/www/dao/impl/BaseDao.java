package com.taobao.www.dao.impl;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class BaseDao extends MysqlRouteBase{
	
	private static final Logger logger = Logger.getLogger(BaseDao.class);

	private static BaseDao dao = new BaseDao();
	
	public BaseDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_autoload_old"));
	}
	
	public BaseDao getTangDao() {
		return dao;
	}
	
	public void addMonitorDataOfDayTDDLSummeryNew(String basic) {
		String sql;
		try {
		    sql = "insert into csp_app_consume_tddl_summary_new (app_name, db_feature, db_name, db_ip, db_port, execute_sum, time_average, min_time, max_time,room_feature, collect_time, gmt_create) " +
					" values " + "dsds";
			this.execute(sql);
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	
}
