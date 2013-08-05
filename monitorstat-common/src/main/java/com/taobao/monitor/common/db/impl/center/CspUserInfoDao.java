package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.CspUserInfoPo;

public class CspUserInfoDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(CspUserInfoDao.class);

	public CspUserInfoDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Main"));
	}

	public boolean insertCspUserInfo(CspUserInfoPo po) {
		String sql = "insert into csp_user_info(phone,wangwang,mail,phone_feature,wangwang_feature,permission_desc,accept_apps) values(?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[] { 
					po.getPhone(), po.getWangwang(), po.getMail(),
					po.getPhone_feature(), po.getWangwang_feature(),
					po.getPermission_desc(),po.getAccept_apps() });
		} catch (SQLException e) {
			logger.info(e);
			return false;
		}
		return true;
	}
	/**
	 * 通过邮件信息获取用户
	 *@author xiaodu
	 * @param email
	 * @return
	 *TODO
	 */
	public CspUserInfoPo findCspUserInfoByMail(String email){
		final CspUserInfoPo po = new CspUserInfoPo();
		String sql = "select * from csp_user_info where mail = ?";
		try {
			this.query(sql, new Object[] { email},
					new SqlCallBack() {

						@Override
						public void readerRows(ResultSet rs) throws Exception {
							fillCspUserInfo(rs, po);
						}

					});
		} catch (Exception e) {
			logger.info(e);
			return null;
		}
		return po.getMail()==null?null:po;
	}
	

	public boolean findCspUserInfo(String mail) {
		String sql = "select count(*) from csp_user_info where mail = ?";
		try {
			int c = this.getIntValue(sql, new Object[] { mail});
			if(c >0){
				return true;
			}
		} catch (Exception e) {
			logger.info("",e);
			return false;
		}
		return false;
	}
	/**
	 * 查询全部用户信息
	 *@author xiaodu
	 * @return
	 *TODO
	 */
	public List<CspUserInfoPo> findAllCspUserInfo() {
		String sql = "select * from csp_user_info";
		final List<CspUserInfoPo> poList = new ArrayList<CspUserInfoPo>();
		
		try {
			this.query(sql, new Object[] {}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspUserInfoPo po = new CspUserInfoPo();
					fillCspUserInfo(rs, po);
					poList.add(po);
				}
			});
		} catch (Exception e) {
			logger.info(e);
		}
		return poList;
	}


	private void fillCspUserInfo(ResultSet rs, CspUserInfoPo po)
			throws SQLException {
		po.setId(rs.getInt("id"));
		po.setAccept_apps(rs.getString("accept_apps"));
		po.setMail(rs.getString("mail"));
		po.setPermission_desc(rs.getString("permission_desc"));
		po.setPhone(rs.getString("phone"));
		po.setPhone_feature(rs.getString("phone_feature"));
		po.setWangwang(rs.getString("wangwang"));
		po.setWangwang_feature(rs.getString("wangwang_feature"));
	}

	public boolean updateCspUserInfo(final CspUserInfoPo po) {
		String sql = "UPDATE  csp_user_info  SET "
				+ " `phone` =  ?  , `wangwang` =  ?  , `phone_feature` =  ?  ,"
				+ " `wangwang_feature` =  ?  , `permission_desc` =  ?  , `accept_apps` =  ?  WHERE `mail` = ?  ";
		try {
			this.execute(sql, new Object[] { 
					po.getPhone(), po.getWangwang(),
					po.getPhone_feature(), po.getWangwang_feature(),
					po.getPermission_desc(), po.getAccept_apps(),
					po.getMail()});
		} catch (Exception e) {
			logger.info(e);
			return false;
		}
		return true;
	}
}
