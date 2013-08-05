package com.taobao.sentinel.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.po.RefIpPo;
import com.taobao.sentinel.util.LocalUtil;

public class RefIpDao extends MysqlRouteBase {
	
	private static final Logger logger =  Logger.getLogger(RefIpDao.class);
	
	public RefIpDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(Constants.SENTINEL_DATABASE));
	}
	
	public List<RefIpPo> findRefIps(final String refId) {
		String sql = "select * from sentinel_ref_ips where ref_id=? order by ip_addr";
		
		final List<RefIpPo> list = new ArrayList<RefIpPo>();
		
		try {
			this.query(sql, new Object[] { refId }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					RefIpPo po = new RefIpPo();
					po.setId(rs.getString("id"));
					po.setIp(rs.getString("ip_addr"));
					po.setRefId(refId);
					po.setNumber(list.size() + 1);
					list.add(po);
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	public Map<String, List<RefIpPo>> findAppRefIps(final String appName) {
		String sql = "select * from sentinel_ref_ips where ref_id like ?";
		
		final Map<String, List<RefIpPo>> map = new HashMap<String, List<RefIpPo>>();
		
		try {
			this.query(sql, new Object[] { LocalUtil.generateRefIdIndex(appName) }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					List<RefIpPo> list;
					String refId = rs.getString("ref_id");
					RefIpPo po = new RefIpPo();
					po.setId(rs.getString("id"));
					po.setIp(rs.getString("ip_addr"));
					po.setRefId(refId);
					
					if (map.containsKey(refId)) {
						list = map.get(refId);
						po.setNumber(list.size() + 1);
						list.add(po);
					} else {
						list = new ArrayList<RefIpPo>();
						map.put(refId, list);
						po.setNumber(list.size() + 1);
						list.add(po);
					}		
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		return map;
	}
	
	public void addRefIp(String id, String ipAddr, String refId) {
		String sql = "INSERT INTO sentinel_ref_ips(id, ip_addr, ref_id) VALUES(?,?,?)";
		try {
			this.execute(sql, new Object[]{ id, ipAddr, refId });
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
	
	public boolean checkExist(String appName, String refApp) {
		String refId = LocalUtil.generateRefId(appName, refApp);
		String sql = "select * from sentinel_ref_ips where ref_id=?";
		final List<String> recordNum = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { refId }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					recordNum.add("+1");
			}});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		if (recordNum.size() > 1) {
			logger.info("appName:" + appName + " refApp:" + refApp + " duplicated:" + recordNum.size());
		}
		if (recordNum.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean removeRefIpsByRefId(String refId) {
		String sql = "delete from sentinel_ref_ips where ref_id=?";

		try {
			this.execute(sql, new Object[] { refId });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}
	
	public boolean removeRefIpById(String id) {
		String sql = "delete from sentinel_ref_ips where id=?";

		try {
			this.execute(sql, new Object[] { id });
		} catch (Exception e) {
			logger.error("error", e);
			return false;
		}
		return true;
	}

}
