package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.web.tddl.TddlPo;
import com.taobao.monitor.web.tdod.TdodPo;

/**
 * 对csp_tdod_deny_summary表进行操作的dao类
 * @author denghaichuan.pt
 * @version 2011-10-31
 */
public class MonitorTdodDao extends MysqlRouteBase  {
	
	private static final Logger logger = Logger.getLogger(MonitorTdodDao.class);
	
	public MonitorTdodDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
	

	/**
	 * 查询当前日期所有的tdod list
	 * @author denghaichuan.pt
	 * @version 2012-3-20
	 * @param date
	 * @return
	 */
	public List<TdodPo> findAllTdodList(String date) {
		final List<TdodPo> tdodList = new ArrayList<TdodPo>();
		String sql = "select * from csp_tdod_deny_summary where collect_time=?";
		
		try {
			
			this.query(sql, new Object[]{date}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					TdodPo po = new TdodPo();
					po.setAppName(rs.getString("app_name"));
					po.setCount(rs.getInt("count"));
					po.setIp(rs.getString("deny_ip"));
					tdodList.add(po);
				}});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return tdodList;
	}
	
	/**
	 * 查询某些应用Tdod deny情况
	 * @author denghaichuan.pt
	 * @version 2012-3-20
	 * @param date
	 * @return
	 */
	public List<TdodPo> findAllTdodList(final Set<String> appNameSet, String date) {
		final List<TdodPo> tdodList = new ArrayList<TdodPo>();
		String sql = "select * from csp_tdod_deny_summary where and collect_time=?";
		
		try {
			
			this.query(sql, new Object[]{date}, new SqlCallBack(){

				public void readerRows(ResultSet rs) throws Exception {
					TdodPo po = new TdodPo();
					String appName = rs.getString("app_name");
					if (appNameSet.contains(appName)) {
						po.setAppName(appName);
						po.setCount(rs.getInt("count"));
						po.setIp(rs.getString("deny_ip"));
						tdodList.add(po);
					}
				}});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return tdodList;
	}

}
