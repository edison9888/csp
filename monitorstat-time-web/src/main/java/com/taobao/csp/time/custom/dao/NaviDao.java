package com.taobao.csp.time.custom.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.time.web.po.UserCustomNaviMainPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class NaviDao extends MysqlRouteBase{
	private static Logger logger = Logger.getLogger(NaviDao.class);
	public NaviDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main"));
	}
	public boolean updateNaviName(Integer id,String naviName){
		boolean flag = true;
		String sql = "update csp_user_custom_navi_main set navi_name = ? where navi_id = ?";
		try {
			this.execute(sql, new Object[]{naviName,id});
		} catch (SQLException e) {
			return false;
		}
		return flag;
	}
	public boolean insertNavisByUserName(String userName,List<String> naviNames){
		try{
			String sql = "insert into csp_user_custom_navi_main(create_time,modify_time,domain_name,navi_name,type) values(?,?,?,?,?)";
			for(String naviName : naviNames){
				this.execute(sql, new Object[]{new Timestamp(new Date().getTime()),new Timestamp(new Date().getTime()),userName,naviName,"user"});
			}
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean insertNavisByAppName(String appName,List<String> naviNames){
		try{
			String sql = "insert into csp_user_custom_navi_main(create_time,modify_time,domain_name,navi_name,type) values(?,?,?,?,?)";
			for(String naviName : naviNames){
				this.execute(sql, new Object[]{new Timestamp(new Date().getTime()),new Timestamp(new Date().getTime()),appName,naviName,"app"});
			}
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean findNaviInfoByUserNameAndNaviName(String userName,String naviName,final UserCustomNaviMainPo po){
		try{
			String sql = "select * from csp_user_custom_navi_main where domain_name = ? and navi_name = ? and type = user";
			this.query(sql, new Object[]{userName,naviName}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					setNavi(rs,po);
				}
				
			});
			return true;
		}catch(Exception e){
			logger.info(e);
			return false;
		}
	}
	public boolean findNavisByUserName(String userName,final List<UserCustomNaviMainPo> navis){
		try{
			String sql = "select * from csp_user_custom_navi_main where domain_name = ? and type = 'user'";
			this.query(sql, new Object[]{userName},new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					UserCustomNaviMainPo po = new UserCustomNaviMainPo();
					setNavi(rs,po);
					navis.add(po);
				}
				
			});
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean findNavisByAppName(String appName,final List<UserCustomNaviMainPo> navis){
		try{
			String sql = "select * from csp_user_custom_navi_main where domain_name = ? and type = 'app'";
			this.query(sql, new Object[]{appName},new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					UserCustomNaviMainPo po = new UserCustomNaviMainPo();
					setNavi(rs,po);
					navis.add(po);
				}
				
			});
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	
	public boolean deleteNavisByUserName(String userName){
		try{
			String sql = "delete from csp_user_custom_navi_main where domain_name = ? and type = user";
			this.execute(sql, new Object[]{userName});
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	
	public boolean deleteNavisByUserName(String userName,List<Integer> naviIds){
		try{
			String sql = "delete from csp_user_custom_navi_main where domain_name = ? and navi_id = ? and type = 'user'";
			for(Integer naviId : naviIds){
				this.execute(sql, new Object[]{userName,naviId});
			}
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean deleteNavisByAppName(String appName,List<Integer> naviIds){
		try{
			String sql = "delete from csp_user_custom_navi_main where domain_name = ? and navi_id = ? and type = 'app'";
			for(Integer naviId : naviIds){
				this.execute(sql, new Object[]{appName,naviId});
			}
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	
	private void setNavi(ResultSet rs,UserCustomNaviMainPo po) throws SQLException{
		po.setNaviId(rs.getInt("navi_id"));
		po.setNaviName(rs.getString("navi_name"));
		po.setUserName(rs.getString("domain_name"));
		po.setType(rs.getString("type"));
	}
	
	public static void main(String args[]){
	}
}
