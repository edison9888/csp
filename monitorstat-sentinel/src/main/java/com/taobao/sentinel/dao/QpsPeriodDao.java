package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.QpsPeriodPo;

public class QpsPeriodDao extends MysqlRouteBase {
	
	private static final Logger logger =  Logger.getLogger(QpsPeriodDao.class);
	
	public QpsPeriodDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public List<QpsPeriodPo> findQpsPeriods() {
		String sql = "select * from sentinel_flow_period";
		
		final List<QpsPeriodPo> list = new ArrayList<QpsPeriodPo>();
		
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					QpsPeriodPo po = new QpsPeriodPo();
					po.setAppName(rs.getString("app_name"));
					po.setPeriod(rs.getInt("period_second"));
					
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public int findPeriod(String appName) {
		String sql = "select period_second from sentinel_flow_period where app_name=?";
		
		int period = 10;
		try {
			period = this.getIntValue(sql, new Object [] {appName } );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (period < 1) {
			period = 10;
		}
		
		return period;
	}
	
	public void addWhenNotExist(String appName) {
		String sql = "INSERT INTO sentinel_flow_period(app_name, period_second) VALUES(?,?)";
		
		boolean exist = checkExist(appName);
		
		if (exist) {
			return;
		}
		
		try {
			this.execute(sql, new Object[]{ appName, Constants.DEFAULT_PERIOD});
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean checkExist(String appName) {
		String sql = "select * from sentinel_flow_period where app_name=?";
		final List<String> recordNum = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					recordNum.add("+1");
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (recordNum.size() > 1) {
			logger.error("appName:" + appName + " duplicated:" + recordNum.size());
		}
		if (recordNum.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean updatePeriod(String appName, int period) {
		String sql = "update sentinel_flow_period set period_second=? where app_name=?";
		try {
			this.execute(sql, new Object[]{ period, appName });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		
		return true;
	}
}
