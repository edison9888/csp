package com.taobao.csp.time.custom.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.time.web.po.UserCustomNaviKeyPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class KeyDao extends MysqlRouteBase{
	private Logger logger = Logger.getLogger(KeyDao.class);
	public KeyDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main"));
	}
	public boolean insertInfoByNaviId(List<UserCustomNaviKeyPo> list){
		try{
			String sql = "insert into csp_user_custom_navi_key(create_time,modify_time,navi_id,app_name,key_name,action_url,view_mod,key_property) values(?,?,?,?,?,?,?,?)";
			for(UserCustomNaviKeyPo po : list){
				this.execute(sql, new Object[]{new Timestamp(new Date().getTime()),new Timestamp(new Date().getTime()),po.getNavId(),po.getAppName(),po.getKeyName(),po.getActionUrl(),po.getViewMod(),po.getProperty()});
			}
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean findInfoByNaviId(final List<UserCustomNaviKeyPo> list,Integer navId){
		try{
			String sql = "select * from csp_user_custom_navi_key where navi_id = ?";
			this.query(sql, new Object[]{navId}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					UserCustomNaviKeyPo po = new UserCustomNaviKeyPo();
					setNavi(rs,po);
					list.add(po);
				}
				
			});
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean deleteInfoByNaviId(Integer naviId){
		try{
			String sql = "delete from csp_user_custom_navi_key where navi_id = ?";
			this.execute(sql, new Object[]{naviId});
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean deleteInfoByNaviId_AppName_KeyName(List<UserCustomNaviKeyPo> list){
		try{
			String sql = "delete from csp_user_custom_navi_key where navi_id =? and app_name =? and key_name = ?";
			for(UserCustomNaviKeyPo po : list){
				this.execute(sql, new Object[]{po.getNavId(),po.getAppName(),po.getKeyName()});
			}
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean deleteInfoByKeyId(List<Integer> list){
		try{
			String sql = "delete from csp_user_custom_navi_key where id=?";
			for(Integer keyId : list){
				this.execute(sql, new Object[]{keyId});
			}
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	private void setNavi(ResultSet rs,UserCustomNaviKeyPo po) throws SQLException{
		po.setActionUrl(rs.getString("action_url"));
		po.setAppName(rs.getString("app_name"));
		po.setKeyName(rs.getString("key_name"));
		po.setNavId(rs.getInt("navi_id"));
		po.setViewMod(rs.getString("view_mod"));
		po.setProperty(rs.getString("key_property"));
		po.setKeyId(rs.getInt("id"));
	}
}
