
package com.taobao.csp.capacity.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.capacity.po.CapacityRankingPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * 
 * @author xiaodu
 * @version 2011-4-12 ÏÂÎç07:16:17
 */
public class CapacityRankingDao extends MysqlRouteBase{
	
	private static final Logger logger =  Logger.getLogger(CapacityRankingDao.class);
	
	public CapacityRankingDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}
	
	
	public void deleteCapacityRanking(String name,Date date){
		
		String sql = "delete from scp_capacity_ranking where ranking_name=? and ranking_date=DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		
		try {
			this.execute(sql, new Object[]{name,date});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		
	}
	
	public void deleteCapacityRanking(String name){
		
		String sql = "delete from scp_capacity_ranking where ranking_name=? ";
		
		
		try {
			this.execute(sql, new Object[]{name});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		
	}
	
	
	public List<CapacityRankingPo> findCapacityLatestRankingPos(String name){
		String sql = "select * from scp_capacity_ranking where ranking_name=? and ranking_date=(" +
				" select max(ranking_date) from scp_capacity_ranking)";
		
		final List<CapacityRankingPo> list = new ArrayList<CapacityRankingPo>();
		
		try {
			this.query(sql, new Object[]{name}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityRankingPo po = new CapacityRankingPo();
					po.setAppId(rs.getInt("app_id"));
					po.setRankingName(rs.getString("ranking_name"));
					po.setCData(rs.getDouble("c_data"));
					po.setCRanking(rs.getInt("c_ranking"));
					po.setCLoadrunQps(rs.getDouble("c_loadrun_qps"));
					po.setCQps(rs.getDouble("c_qps"));
					po.setRankingDate(new Date(rs.getTimestamp("ranking_date").getTime()));
					po.setRankingFeature(rs.getString("ranking_feature"));
					po.setAppName(rs.getString("app_name"));
					po.setAppType(rs.getString("app_type"));
					list.add(po);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list;
	}
	
	public List<CapacityRankingPo> findCapacityHistoryPos(String rankingName, int appId){
		String sql = "select * from scp_capacity_ranking where ranking_name=? and app_id=? order by ranking_date desc limit 60";
		
		final List<CapacityRankingPo> list = new ArrayList<CapacityRankingPo>();
		
		try {
			this.query(sql, new Object[]{ rankingName, appId }, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityRankingPo po = new CapacityRankingPo();
					po.setAppId(rs.getInt("app_id"));
					po.setRankingName(rs.getString("ranking_name"));
					po.setCData(rs.getDouble("c_data"));
					po.setCRanking(rs.getInt("c_ranking"));
					po.setCLoadrunQps(rs.getDouble("c_loadrun_qps"));
					po.setCQps(rs.getDouble("c_qps"));
					po.setRankingDate(new Date(rs.getTimestamp("ranking_date").getTime()));
					po.setRankingFeature(rs.getString("ranking_feature"));
					po.setAppName(rs.getString("app_name"));
					po.setAppType(rs.getString("app_type"));
					list.add(po);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list;
	}
	
	public List<CapacityRankingPo> findCapacityRankingPo(String name,Date date ){
		String sql = "select * from scp_capacity_ranking where ranking_name=? and  ranking_date=DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		final List<CapacityRankingPo> list = new ArrayList<CapacityRankingPo>();
		
		try {
			this.query(sql, new Object[]{name,date}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityRankingPo po = new CapacityRankingPo();
					po.setAppId(rs.getInt("app_id"));
					po.setRankingName(rs.getString("ranking_name"));
					po.setCData(rs.getDouble("c_data"));
					po.setCRanking(rs.getInt("c_ranking"));
					po.setCLoadrunQps(rs.getDouble("c_loadrun_qps"));
					po.setCQps(rs.getDouble("c_qps"));
					po.setRankingDate(new Date(rs.getTimestamp("ranking_date").getTime()));
					po.setRankingFeature(rs.getString("ranking_feature"));
					po.setAppName(rs.getString("app_name"));
					po.setAppType(rs.getString("app_type"));
					list.add(po);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list;
	}
	
	public List<CapacityRankingPo> findCapacityRankingPoByAppIds(String name, String ids ){
		String sql = "select * from scp_capacity_ranking where ranking_name=?  and app_id in (" + ids + ")";
		
		final List<CapacityRankingPo> list = new ArrayList<CapacityRankingPo>();
		
		try {
			this.query(sql, new Object[]{ name }, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityRankingPo po = new CapacityRankingPo();
					po.setAppId(rs.getInt("app_id"));
					po.setRankingName(rs.getString("ranking_name"));
					po.setCData(rs.getDouble("c_data"));
					po.setCRanking(rs.getInt("c_ranking"));
					po.setCLoadrunQps(rs.getDouble("c_loadrun_qps"));
					po.setCQps(rs.getDouble("c_qps"));
					po.setRankingDate(new Date(rs.getTimestamp("ranking_date").getTime()));
					po.setRankingFeature(rs.getString("ranking_feature"));
					po.setAppName(rs.getString("app_name"));
					po.setAppType(rs.getString("app_type"));
					list.add(po);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list;
	}
	
	
	
	public CapacityRankingPo getCapacityRanking(int appId,String type,Date date){
		String sql = "select * from scp_capacity_ranking where app_id=? and ranking_name=? and ranking_date=DATE_FORMAT(?,\"%Y-%m-%d\") ";
		
		final CapacityRankingPo po = new CapacityRankingPo();
		
		try {
			this.query(sql, new Object[]{appId,type,date}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					po.setAppId(rs.getInt("app_id"));
					po.setRankingName(rs.getString("ranking_name"));
					po.setCData(rs.getDouble("c_data"));
					po.setCRanking(rs.getInt("c_ranking"));
					po.setCLoadrunQps(rs.getDouble("c_loadrun_qps"));
					po.setCQps(rs.getDouble("c_qps"));
					po.setRankingDate(new Date(rs.getTimestamp("ranking_date").getTime()));
					po.setRankingFeature(rs.getString("ranking_feature"));
					po.setAppName(rs.getString("app_name"));
					po.setAppType(rs.getString("app_type"));
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return po.getAppId()==0?null:po;
		
	}
	
	public CapacityRankingPo getLatestCapacityRanking(int appId){
		String sql = "select * from scp_capacity_ranking where app_id=? order by  ranking_date desc limit 1 ";
		
		final CapacityRankingPo po = new CapacityRankingPo();
		
		try {
			this.query(sql, new Object[]{ appId }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					po.setAppId(rs.getInt("app_id"));
					po.setRankingName(rs.getString("ranking_name"));
					po.setCData(rs.getDouble("c_data"));
					po.setCRanking(rs.getInt("c_ranking"));
					po.setCLoadrunQps(rs.getDouble("c_loadrun_qps"));
					po.setCQps(rs.getDouble("c_qps"));
					po.setRankingDate(new Date(rs.getTimestamp("ranking_date").getTime()));
					po.setRankingFeature(rs.getString("ranking_feature"));
					po.setAppName(rs.getString("app_name"));
					po.setAppType(rs.getString("app_type"));
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return po.getAppId()==0 ? null : po;
		
	}
	
	
	public boolean addCapacityRanking(CapacityRankingPo po){
		String sql = "insert into scp_capacity_ranking(app_id,app_name,app_type,ranking_name,c_data,c_ranking,c_qps,c_loadrun_qps,ranking_date,ranking_feature)" +
				"values(?,?,?,?,?,?,?,?,DATE_FORMAT(?,\"%Y-%m-%d\"),?)";
		
		try {
			this.execute(sql, new Object[]{po.getAppId(),po.getAppName(),po.getAppType(),po.getRankingName(),po.getCData(),
					po.getCRanking(),po.getCQps(),po.getCLoadrunQps(),po.getRankingDate(),po.getRankingFeature()});
		} catch (SQLException e) {
			logger.error("", e);
		}
		return true;
	}
	
	@Override
	public void execute(String sql) {
		try {
			super.execute(sql);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
//	public boolean updateCapacityRanking(CapacityRankingPo po){
//		String sql = "update scp_capacity_ranking set app_type=? c_data=?,c_ranking=?,c_qps=?, c_loadrun_qps=?,ranking_feature=?" +
//				"where app_id=? and ranking_name=? and ranking_date=DATE_FORMAT(?,\"%Y-%m-%d\")";
//		
//		try {
//			this.execute(sql, new Object[]{po.getAppType(),po.getCData(),
//					po.getCRanking(),po.getCQps(),po.getCLoadrunQps(),po.getRankingFeature(),po.getAppId(),po.getRankingName(),po.getRankingDate()});
//		} catch (SQLException e) {
//			logger.error("", e);
//		}
//		return true;
//	}
	

}
