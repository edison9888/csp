package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.DataUrlPo;

public class DataUrlDao  extends MysqlRouteBase {
	
	private static final Logger logger =  Logger.getLogger(DataUrlDao.class);
	
	public DataUrlDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public List<DataUrlPo> findAll() {
		String sql = "select * from sentinel_data_url order by app_name";
		
		final List<DataUrlPo> list = new LinkedList<DataUrlPo>();
		
		try {
			this.query(sql,  new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					DataUrlPo po = new DataUrlPo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setNumber(list.size() + 1);
					po.setDataUrl(rs.getString("data_url"));
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public DataUrlPo findDataUrl(String appName) {
		String sql = "select * from sentinel_data_url where app_name=?";
		
		final List<DataUrlPo> list = new LinkedList<DataUrlPo>();
		
		try {
			this.query(sql, new Object [] { appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					DataUrlPo po = new DataUrlPo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setNumber(list.size() + 1);
					po.setDataUrl(rs.getString("data_url"));
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}
	
	public void addWhenNotExist(String id, String appName, String dataUrl) {
		String sql = "INSERT INTO sentinel_data_url(id, app_name, data_url) VALUES(?,?,?)";
		
		boolean exist = checkExist(appName);
		
		if (exist) {
			return;
		}
		
		try {

			this.execute(sql, new Object[]{ id, appName, dataUrl});
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean checkExist(String appName) {
		String sql = "select * from sentinel_data_url where app_name=?";
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
	
	public boolean updateDataUrl(String appName, String dataUrl) {
		String sql = "update sentinel_data_url set data_url=? where app_name=?";

		try {
			this.execute(sql, new Object[] { dataUrl, appName });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}

}
