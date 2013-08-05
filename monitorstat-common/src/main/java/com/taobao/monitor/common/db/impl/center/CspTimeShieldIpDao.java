package com.taobao.monitor.common.db.impl.center;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class CspTimeShieldIpDao extends MysqlRouteBase{
	private static Logger logger = Logger.getLogger(CspTimeShieldIpDao.class);
	public CspTimeShieldIpDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main"));
	}
	public List<String> findAllIps(){
		final List<String> ips = new ArrayList<String>();
		String sql = "select ip from csp_time_shield_ip";
		try {
			this.query(sql, new Object[]{}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String ip = rs.getString("ip");
					ips.add(ip);
				}});
		} catch (Exception e) {
			logger.info(e);
		}
		return ips;
	}
	public boolean  deleteIps(List<String> ips){
		String sql = "delete from csp_time_shield_ip where ip = ?";
		for(String ip : ips){
		try {
			this.execute(sql, new Object[]{ip});
		} catch (Exception e) {
			logger.info(e);
		}
		}
		return true;
	}
	public boolean addIps(List<String> ips){
		String sql = "insert into csp_time_shield_ip(ip) values(?)";
		for(String ip: ips){
			try {
				this.execute(sql, new Object[]{ip});
			} catch (SQLException e) {
				logger.info(e);
			}
		}
		return true;
	}
}
