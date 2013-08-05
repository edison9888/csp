package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.CspMapDependkeyBlacklistPo;
import com.taobao.monitor.common.po.CspMapKeyInfoPo;

public class CspMapKeyInfoDao extends MysqlRouteBase {
	private static final Logger logger =  Logger.getLogger(CspMapKeyInfoDao.class);

	public CspMapKeyInfoDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}

	public void addCspMapKeyInfoPo(CspMapKeyInfoPo po) throws SQLException {
		String sql = "insert into csp_map_key_info(keyname,appname,key_level,control_type,updateby,update_time,is_black,key_status,rate) value(?,?,?,?,?,?,?,?,?)";
		this.execute(sql, new Object[]{po.getKeyname(),po.getAppname(),po.getKeyLevel(),po.getControlType(),po.getUpdateBy(),
				po.getUpdateTime(),po.getIsBlack(),po.getKeyStatus(),po.getRate()});
	}

	public void updateCspMapKeyInfoPo(CspMapKeyInfoPo po) throws SQLException {
		String sql = "update csp_map_key_info set keyname = ?,appname = ?,key_level = ?,control_type = ?,updateby = ?,update_time = ?,is_black = ?,key_status = ?,rate=? where id = ?";
		this.execute(sql, new Object[]{po.getKeyname(),po.getAppname(),po.getKeyLevel(),po.getControlType(),po.getUpdateBy(),
				po.getUpdateTime(),po.getIsBlack(),po.getKeyStatus(), po.getRate(),po.getId()});
	}

	public List<CspMapKeyInfoPo> getKeyList(int isBlack, String appname) {
		String sql = "select * from csp_map_key_info where appname=? and is_black=?";
		final List<CspMapKeyInfoPo> list = new ArrayList<CspMapKeyInfoPo>();
		try {
			this.query(sql, new Object[]{appname, isBlack},  new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspMapKeyInfoPo po = new CspMapKeyInfoPo();
					try {
						fillPo(rs, po);
						list.add(po);
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}

	public CspMapKeyInfoPo getMapKeyPoById(String id) throws Exception {
		String sql = "select * from csp_map_key_info where id=?";
		final CspMapKeyInfoPo po = new CspMapKeyInfoPo();
		this.query(sql, new Object[]{id},  new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				fillPo(rs, po);
			}
		});
		return po;
	}
	
	public CspMapKeyInfoPo getMapKeyPoByKey(String appName, String keyName) throws Exception {
		String sql = "select * from csp_map_key_info where keyname = ? and appname = ?";
		final CspMapKeyInfoPo po = new CspMapKeyInfoPo();
		this.query(sql, new Object[]{appName, keyName},  new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				fillPo(rs, po);
			}
		});
		return po;
	}
	
	private void fillPo(ResultSet rs, CspMapKeyInfoPo po) throws SQLException {
		po.setId(rs.getLong("id"));
		po.setKeyname(rs.getString("keyname"));
		po.setAppname(rs.getString("appname"));
		po.setKeyLevel(rs.getInt("key_level"));
		po.setControlType(rs.getInt("control_type"));
		po.setUpdateBy(rs.getString("updateby"));
		po.setUpdateTime(rs.getDate("update_time"));
		po.setIsBlack(rs.getInt("is_black"));
		po.setKeyStatus(rs.getInt("key_status"));
		po.setRate(rs.getDouble("rate"));
	}

	/**************************csp_map_dependkey_blacklistœ‡πÿ***************************************************/
	private void fillPo(ResultSet rs, CspMapDependkeyBlacklistPo po) throws SQLException {
		po.setAppName(rs.getString("appname"));
		po.setKeyName(rs.getString("keyname"));
		po.setOwnApp(rs.getString("ownapp"));
		po.setUpdateBy(rs.getString("updateby"));
//		po.setUpdateTime(rs.getDate("update_time"));
		po.setUpdateTime(rs.getTimestamp("update_time"));
	}
	
	public List<CspMapDependkeyBlacklistPo> searchByOwnApp(String ownAppName) throws Exception {
		String sql = "select * from csp_map_dependkey_blacklist where ownapp = ?";
		final List<CspMapDependkeyBlacklistPo> list = new ArrayList<CspMapDependkeyBlacklistPo>();
		this.query(sql, new Object[]{ownAppName},  new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) {
				try {
					CspMapDependkeyBlacklistPo po = new CspMapDependkeyBlacklistPo();
					fillPo(rs, po);
					list.add(po);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		});
		return list;
	}

	public void addBlackPo(CspMapDependkeyBlacklistPo po) throws Exception {
		String sql = "insert into csp_map_dependkey_blacklist(ownapp,appname,keyname,updateby,update_time) value(?,?,?,?,?)";
		this.execute(sql, new Object[]{po.getOwnApp(),po.getAppName(),po.getKeyName(),po.getUpdateBy(),po.getUpdateTime()});
	}
	
}