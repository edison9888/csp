package com.taobao.monitor.alarm.source.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.source.artoo.ArtooPo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * 缓存从artoo平台获取的发布数据，因为artoo不支持按照时间段进行查询
 * @author baiyan
 *
 */
public class ArtooCacheDao extends MysqlRouteBase{
	private static final Logger logger = Logger.getLogger(ArtooCacheDao.class);
	
	/**
	 * @param po
	 * @return
	 */
	public boolean addArtooPo(ArtooPo po) {
		try {
			String sql = "insert into ms_monitor_artoo_cache (id, app_name,creator,call_system,deploy_time,plan_type," +
					"state,gmt_create) values(?,?,?,?,?,?,?,?)";
			this.execute(
					sql,
					new Object[] { po.getId(), po.getAppName(),
							po.getCreator(), po.getCallSystem(),
							po.getDeployTime(), po.getPlanType(),po.getState(),new Date()});
		} catch (Exception e) {
			logger.error("addArtooPo exception,po=" + po, e);
			return false;
		}

		return true;
	}
	
	public List<ArtooPo> findArtooPoListByAppNameAndTime(String appName,Date start,Date end){
		String sql = "select * from ms_monitor_artoo_cache where app_name=? and deploy_time>= ? and deploy_time <= ?";
		final List<ArtooPo> list = new ArrayList<ArtooPo>();
		try {
			this.query(sql, new Object[]{appName,start,end}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					ArtooPo po = getArtooPoFromResultSet(rs);
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("findArtooPoListByAppNameAndTime exception,appName=" + appName + ",start=" + start + ",end=" + end, e);
			return null;
		}
		return list.size()==0?null:list;
	}
	
	private ArtooPo getArtooPoFromResultSet(ResultSet rs) throws SQLException{
		ArtooPo po = new ArtooPo();
		po.setId(rs.getString("id"));
		po.setAppName(rs.getString("app_name"));
		po.setCallSystem(rs.getString("call_system"));
		po.setCreator(rs.getString("creator"));
		po.setDeployTime(rs.getString("deploy_time"));
		po.setPlanType(rs.getString("plan_type"));
		po.setState(rs.getString("state"));
		return po;
	}
}
