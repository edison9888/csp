package com.taobao.csp.alarm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.time.web.po.BaselineCheckPo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class BaselineCheckDao extends MysqlRouteBase{
	private static final Logger logger = Logger.getLogger(BaselineCheckDao.class);
	public boolean insert(BaselineCheckPo po){
		if(po.getProcessTime()==null)po.setProcessTime(new Date());
		boolean flag = true;
		String sql = "insert into csp_time_baseline_record(app_name,key_name,property_name,week_day,scope,state,process_time) values(?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{po.getAppName(),po.getKeyName(),po.getPropertyName(),po.getWeekDay(),po.getScope(),po.getState(),po.getProcessTime()});
		} catch (SQLException e) {
			logger.info(e);
			return false;
		}
		return flag;
	}
	public boolean updateState(BaselineCheckPo po){
		boolean flag = true;
		boolean judge =isExsit(po);
		if(judge==true){
		if(po.getProcessTime()==null){
			String sql2 = "update csp_time_baseline_record set state = ? where app_name = ? and key_name = ? and property_name = ? and scope = ? and week_day = ? order by state desc,scope asc";
			try {
				this.execute(sql2, new Object[]{po.getState(),po.getAppName(),po.getKeyName(),po.getPropertyName(),po.getScope(),po.getWeekDay()});
			} catch (SQLException e) {
				logger.info(e);
				return true;
			}	
		}else{
			String sql = "update csp_time_baseline_record set process_time= ?,state = ? where app_name = ? and key_name = ? and property_name = ? and scope = ? and week_day = ?";
			try {
				this.execute(sql, new Object[]{po.getProcessTime(),po.getState(),po.getAppName(),po.getKeyName(),po.getPropertyName(),po.getScope(),po.getWeekDay()});
			} catch (SQLException e) {
				logger.info(e);
				return true;
			}
		}
		}else{
			return insert(po);
		}
		return flag;
	}
	public boolean isExsit(BaselineCheckPo po){
		boolean flag = true;
		final List<BaselineCheckPo> list = new ArrayList<BaselineCheckPo>();
		String sql = "select * from csp_time_baseline_record  where app_name = ? and key_name = ? and property_name = ? and scope = ? and week_day = ?";
			try {
				this.query(sql, new Object[]{po.getAppName(),po.getKeyName(),po.getPropertyName(),po.getScope(),po.getWeekDay()}, new SqlCallBack(){

					@Override
					public void readerRows(ResultSet rs) throws Exception {
						BaselineCheckPo po = new BaselineCheckPo();
						list.add(po);
					}});
			} catch (Exception e) {
				logger.info(e);
				return true;
			}
		if(list.size()==0)return false;
		return flag;
	}
	public List<BaselineCheckPo> findByAppName(String appName){
		final List<BaselineCheckPo> list = new ArrayList<BaselineCheckPo>();
		String sql = "select * from csp_time_baseline_record where app_name = ?";
		try {
			this.query(sql, new Object[]{appName}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					BaselineCheckPo po = new BaselineCheckPo();
					po.setAppName(rs.getString("app_name"));
					po.setKeyName(rs.getString("key_name"));
					po.setPropertyName(rs.getString("property_name"));
					po.setWeekDay(rs.getInt("week_day"));
					po.setScope(rs.getString("scope"));
					po.setState(rs.getString("state"));
					po.setProcessTime(rs.getTimestamp("process_time"));
					list.add(po);
				}});
		} catch (Exception e) {
			logger.info(e);
			return null;
		}
		return list;
	}
}
