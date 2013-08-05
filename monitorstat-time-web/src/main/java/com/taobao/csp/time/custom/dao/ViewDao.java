package com.taobao.csp.time.custom.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.time.web.po.UserCustomNaviViewPo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class ViewDao extends  MysqlRouteBase{
	private static Logger logger = Logger.getLogger(ViewDao.class);
	public boolean findViewUrl(final UserCustomNaviViewPo view){
		try{
			String sql = "select view_url from csp_user_custom_navi_view where view_Mod = ?";
			this.query(sql, new Object[]{view.getViewMod()}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					view.setViewUrl(rs.getString("view_url"));
				}
			});
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
	public boolean insertViewUrl(List<UserCustomNaviViewPo> list){
		try{
			String sql = "insert into csp_user_custom_navi_view(create_time,modify_time,view_mod,view_url) values(?,?,?,?)";
			for(UserCustomNaviViewPo userCustomNaviViewPo : list){
				this.execute(sql, new Object[]{new Timestamp(new Date().getTime()),new Timestamp(new Date().getTime()),userCustomNaviViewPo.getViewMod(),userCustomNaviViewPo.getViewUrl()});
			}
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	} 
	public boolean deleteViewUrl(List<String> viewMods){
		try{
			String sql = "delete from csp_user_custom_navi_view where view_mod = ?";
			for(String viewMod : viewMods){
				this.execute(sql,new Object[]{viewMod});
			}
		}catch(Exception e){
			logger.info(e);
			return false;
		}
		return true;
	}
}
