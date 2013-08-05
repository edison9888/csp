package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.InterfaceInvokePo;

public class InterfaceInvokeDao extends MysqlRouteBase {

private static final Logger logger =  Logger.getLogger(ConfigVersionDao.class);
	
	public InterfaceInvokeDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public InterfaceInvokePo findById(String id) {
		String sql = "select * from sentinel_interface_invoke where id=?";
		
		final List<InterfaceInvokePo> list = new LinkedList<InterfaceInvokePo>();
		
		try {
			this.query(sql, new Object[] { id }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					InterfaceInvokePo po = new InterfaceInvokePo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setRefApp(rs.getString("ref_app"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setUser(rs.getString("user"));
					po.setStrong(rs.getString("strong"));
					po.setEstimateQps(rs.getInt("estimate_qps"));
					po.setRemark(rs.getString("remark"));
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list.get(0);
	}
	
	public List<InterfaceInvokePo> findAll() {
		String sql = "select * from sentinel_interface_invoke order by app_name, interface_info";
		
		final List<InterfaceInvokePo> list = new LinkedList<InterfaceInvokePo>();
		
		try {
			this.query(sql,  new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					InterfaceInvokePo po = new InterfaceInvokePo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setRefApp(rs.getString("ref_app"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setUser(rs.getString("user"));
					po.setStrong(rs.getString("strong"));
					po.setEstimateQps(rs.getInt("estimate_qps"));
					po.setRemark(rs.getString("remark"));
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public List<InterfaceInvokePo> findByApp(String appName) {
		String sql = "select * from sentinel_interface_invoke where app_name=?  order by ref_app,interface_info ";
		
		final List<InterfaceInvokePo> list = new LinkedList<InterfaceInvokePo>();
		
		try {
			this.query(sql,  new Object[] { appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					InterfaceInvokePo po = new InterfaceInvokePo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setRefApp(rs.getString("ref_app"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setUser(rs.getString("user"));
					po.setStrong(rs.getString("strong"));
					po.setEstimateQps(rs.getInt("estimate_qps"));
					po.setRemark(rs.getString("remark"));
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public List<InterfaceInvokePo> findByRefApp(String refApp) {
		String sql = "select * from sentinel_interface_invoke where ref_app=? order by app_name,interface_info";
		
		final List<InterfaceInvokePo> list = new LinkedList<InterfaceInvokePo>();
		
		try {
			this.query(sql,  new Object[] { refApp }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					InterfaceInvokePo po = new InterfaceInvokePo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setRefApp(rs.getString("ref_app"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setUser(rs.getString("user"));
					po.setStrong(rs.getString("strong"));
					po.setEstimateQps(rs.getInt("estimate_qps"));
					po.setRemark(rs.getString("remark"));
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public List<InterfaceInvokePo> findByAppAndRefApp(String appName, String refApp) {
		String sql = "select * from sentinel_interface_invoke where app_name=? and ref_app=? " +
				"order by interface_info";
		
		final List<InterfaceInvokePo> list = new LinkedList<InterfaceInvokePo>();
		
		try {
			this.query(sql,  new Object[] { appName, refApp }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					InterfaceInvokePo po = new InterfaceInvokePo();
					po.setId(rs.getString("id"));
					po.setAppName(rs.getString("app_name"));
					po.setRefApp(rs.getString("ref_app"));
					po.setInterfaceInfo(rs.getString("interface_info"));
					po.setUser(rs.getString("user"));
					po.setStrong(rs.getString("strong"));
					po.setEstimateQps(rs.getInt("estimate_qps"));
					po.setRemark(rs.getString("remark"));
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public List<String> findAppNames() {
		String sql = "select distinct app_name from sentinel_interface_invoke";
		
		final List<String> list = new LinkedList<String>();
		
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("app_name");
					list.add(appName);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public List<String> findRefAppNames() {
		String sql = "select distinct ref_app from sentinel_interface_invoke";
		
		final List<String> list = new LinkedList<String>();
		
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String refApp = rs.getString("ref_app");
					list.add(refApp);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public void add(InterfaceInvokePo po){
		String sql = "INSERT INTO sentinel_interface_invoke(id, app_name, ref_app," +
				"interface_info, estimate_qps, strong, remark, user) " +
				"VALUES(?,?,?,?,?,?,?,?)";
		
		try {

			this.execute(sql, new Object[]{ po.getId(), po.getAppName(), po.getRefApp(),
					po.getInterfaceInfo(), po.getEstimateQps(), po.getStrong(), po.getRemark(), po.getUser()});
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean update(InterfaceInvokePo po){
		String sql = "UPDATE sentinel_interface_invoke 	set app_name=?, ref_app=?," +
				"interface_info=?, estimate_qps=?, strong=?, remark=?, user=? " +
				"where id=?";
		
		try {

			this.execute(sql, new Object[]{ po.getAppName(), po.getRefApp(),
					po.getInterfaceInfo(), po.getEstimateQps(), po.getStrong(), 
					po.getRemark(), po.getUser(), po.getId()});
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		
		return true;
	}
	
	public boolean delete(String id){
		String sql = "DELETE FROM sentinel_interface_invoke where id=?";
		
		try {

			this.execute(sql, new Object[]{ id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		
		return true;
	}
	
	public boolean checkExist(String appName, String refApp, String interfaceInfo) {
		String sql = "select * from sentinel_interface_invoke where app_name=? and ref_app=? and interface_info=?";
		final List<String> recordNum = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { appName, refApp, interfaceInfo }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					recordNum.add("+1");
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (recordNum.size() > 1) {
			logger.error("interfaceName:" + appName + " refApp:" + refApp + " inteface:" + interfaceInfo + " duplicated:" + recordNum.size());
		}
		if (recordNum.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean checkExistExceptSelf(String appName, String refApp, String interfaceInfo, String id) {
		String sql = "select * from sentinel_interface_invoke where app_name=? and ref_app=? and interface_info=? and id !=?";
		final List<String> recordNum = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { appName, refApp, interfaceInfo, id }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					recordNum.add("+1");
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (recordNum.size() > 1) {
			logger.error("interfaceName:" + appName + " refApp:" + refApp + " inteface:" + interfaceInfo + " duplicated:" + recordNum.size());
		}
		if (recordNum.size() > 0) {
			return true;
		}
		return false;
	}
}
