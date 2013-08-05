package com.taobao.monitor.common.db.impl.capacity;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityAppPo;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityRankingPo;

public class CspCapacityDao extends MysqlRouteBase {
	public CspCapacityDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}
	
	private static final Logger logger =  Logger.getLogger(CspCapacityDao.class);
	
	/***
	 * 查询应用的分组
	 * @param appId
	 * @return
	 */
	public String findCapacityGroups(int appId) {
		String sql = "select * from csp_capacity_app_datasource where app_id=?";
		
		final StringBuffer groupS = new StringBuffer();
		
		try {
			this.query(sql, new Object[]{appId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					groupS.append(rs.getString("group_names"));
				}});
		} catch (Exception e) {
			logger.error("CspCapacityDao error", e);
		}
		return groupS.toString();
	}
	
	/***
	 * 查询最新的容量排行数据
	 * @param name
	 * @return
	 */
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
	
	/***
	 * 按应用id查询最新的容量排行数据
	 * @param name
	 * @return
	 */
	public CapacityRankingPo findCapacityLatestRankingPo(int appId){
		String sql = "select * from scp_capacity_ranking where app_id=? and ranking_date=(" +
				" select max(ranking_date) from scp_capacity_ranking)";
		
		final List<CapacityRankingPo> list = new ArrayList<CapacityRankingPo>();
		
		try {
			this.query(sql, new Object[]{ appId }, new SqlCallBack(){

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
		
		return list.size() == 0 ? null : list.get(0);
	}
	
	/***
	 * 查询所有的容量应用
	 * @return
	 */
	public List<CapacityAppPo> findCapacityAppList(){
		String sql = "select * from csp_capacity_app_datasource";
		
		final List<CapacityAppPo> list = new ArrayList<CapacityAppPo>();
		
		try {
			this.query(sql,  new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CapacityAppPo po = new CapacityAppPo();
					po.setAppId(rs.getInt("app_id"));
					po.setDatasourceName(rs.getString("datasoure_name"));
					po.setAppType(rs.getString("app_type"));
					po.setDataFeature(rs.getString("data_feature"));
					po.setGroupNames(rs.getString("group_names"));
					po.setGrowthRate(rs.getInt("growth_rate"));
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public Map<String, String> findLatestCapacityCostByApp(String appName) {
		final Map<String, String> map = new HashMap<String, String>();
		
		String sql = "select * from csp_capacity_cost_info where app_name=? order by collect_time desc limit 1";
		try {
			this.query(sql, new Object[]{ appName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					DecimalFormat df = new DecimalFormat("0.00");
					String tPerCost = df.format(rs.getDouble("total_per_cost"));
					map.put("totalPerCost", tPerCost);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
}
