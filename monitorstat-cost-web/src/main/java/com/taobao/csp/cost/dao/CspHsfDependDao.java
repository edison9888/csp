package com.taobao.csp.cost.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.csp.cost.po.DepCapacityPo;
import com.taobao.csp.cost.po.HsfConsumerSummaryPo;
import com.taobao.csp.cost.po.HsfProviderSummaryPo;
import com.taobao.csp.cost.po.SimpleCostPo;
import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.DependencyCapacityUtil;
import com.taobao.csp.cost.util.LocalUtil;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * HSF的统计
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-3
 */
public class CspHsfDependDao  extends MysqlRouteBase{

	public static final double DEP_QPS_THRESHOD = 1.0d;
	private static Log logger = LogFactory.getLog(CspHsfDependDao.class);
	
	public CspHsfDependDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}

	/**
	 * 计算provider提供的调用方次数
	 * @return
	 */
	public List<HsfProviderSummaryPo> getHsfProviderSummaryPos(Date date) {
		final List<HsfProviderSummaryPo> list = new ArrayList<HsfProviderSummaryPo>();
	
		String sql = "select * from csp_app_dep_hsf_provide_summary where " +
				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") " +
				"= DATE_FORMAT(?,\"%Y-%m-%d\")";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					HsfProviderSummaryPo po = new HsfProviderSummaryPo();
					po.setProviderName(rs.getString("provider_name"));
					po.setProviderGroup(rs.getString("provider_group"));
					po.setCallSum(rs.getLong("call_sum"));
					po.setRushTimeQps(rs.getFloat("rush_time_qps"));
					po.setRushTimeRt(rs.getFloat("rush_time_rt"));
					po.setRoomFeature(rs.getString("room_feature"));
					po.setCollectTime(rs.getDate("collect_time"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list;
	}
	
	/***
	 * 返回应用提供的HSF信息
	 * 
	 * @param providerName
	 * @param collectTime
	 * @return
	 */
	public List<DepCapacityPo> findDepCapacityByProviderDate(String providerName, Date collectTime ) {

		String sql = "select * from csp_app_dep_hsf_consume_summary where provider_name=? and " +
				"collect_time=DATE_FORMAT(?,\"%Y-%m-%d\") and rush_time_qps>? ";
		
		final List<DepCapacityPo> poList = new ArrayList<DepCapacityPo>();
		
		try {
			this.query(sql, new Object[]{ providerName, collectTime,
					DEP_QPS_THRESHOD }, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("consume_name");
					String providerName = rs.getString("provider_name");
					String providerGroup = rs.getString("provider_group");
					double qps = rs.getDouble("rush_time_qps");
					String roomFeature = rs.getString("room_feature");
					Map<String, Double> roomQps  = DependencyCapacityUtil.generateRoomQps(roomFeature);
					Date collectTime = rs.getDate("collect_time");
					
					DepCapacityPo po = new DepCapacityPo();
					po.setProviderApp(providerName);
					po.setProviderGroup(providerGroup);
					po.setConsumerApp(appName);
					po.setDepQps(qps);
					po.setCollectTime(collectTime);
					po.setRoomQps(roomFeature);
					po.setRoomQpsMap(roomQps);
					
					if(!poList.contains(po)){
						poList.add(po);
					}
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return poList;
	}
	
	/**
	 * 获得应用的基本调用信息
	 * 
	 * @param consumeName
	 * @param collectTime
	 * @return
	 */
	public List<SimpleCostPo> getAppHsfSummary(String consumeName, Date collectTime ) {
		String sql = "select provider_name,sum(call_sum) as num from csp_app_dep_hsf_consume_summary " +
				"where CONSUME_NAME=? and " +
				"collect_time=DATE_FORMAT(?,\"%Y-%m-%d\") group by provider_name";
		
		final List<SimpleCostPo> poList = new ArrayList<SimpleCostPo>();
		
		try {
			this.query(sql, new Object[]{ consumeName, collectTime}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					SimpleCostPo spo=new SimpleCostPo(CostType.HSF.toString());
					spo.setDependName(rs.getString("provider_name"));
					spo.setCallNum(rs.getLong("num"));
					
					poList.add(spo);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return poList;
	}

	/**
	 * 返回应用的hsf依赖
	 * 
	 * @param consumeName
	 * @param collectTime
	 * @return
	 */
	public List<DepCapacityPo> findDepCapacityByAppDate(String consumeName, Date collectTime ) {
		String sql = "select * from csp_app_dep_hsf_consume_summary where CONSUME_NAME=? and " +
				"collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		final List<DepCapacityPo> poList = new ArrayList<DepCapacityPo>();
		
		try {
			this.query(sql, new Object[]{ consumeName, collectTime}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("consume_name");
					String providerName = rs.getString("provider_name");
					String providerGroup = rs.getString("provider_group");
					
					double qps = rs.getDouble("rush_time_qps");
					String roomFeature = rs.getString("room_feature");
					Map<String, Double> roomQps  = DependencyCapacityUtil.generateRoomQps(roomFeature);
					Date collectTime = rs.getDate("collect_time");
					
					DepCapacityPo po = new DepCapacityPo();
					po.setProviderApp(providerName);
					po.setProviderGroup(providerGroup);
					po.setConsumerApp(appName);
					po.setDepQps(qps);
					po.setCollectTime(collectTime);
					po.setRoomQps(roomFeature);
					po.setRoomQpsMap(roomQps);
					po.setCallSum(rs.getLong("call_sum"));
					
					
					if(!poList.contains(po)){
						poList.add(po);
					}
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return poList;
	}
	

	/**
	 * 计算consumer调用各个provider的次数
	 * @param date
	 * @return
	 */
	public List<HsfConsumerSummaryPo> getHsfConsumerSummaryPos(Date date) {
		final List<HsfConsumerSummaryPo> list = new ArrayList<HsfConsumerSummaryPo>();
		
		String sql = "select * from csp_app_dep_hsf_consume_summary where " +
				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					HsfConsumerSummaryPo po = new HsfConsumerSummaryPo();
					po.setProviderName(rs.getString("provider_name"));
					po.setProviderGroup(rs.getString("provider_group"));
					po.setCallSum(rs.getLong("call_sum"));
					po.setRushTimeQps(rs.getFloat("rush_time_qps"));
					po.setRushTimeRt(rs.getFloat("rush_time_rt"));
					po.setRoomFeature(rs.getString("room_feature"));
					po.setCollectTime(rs.getDate("collect_time"));
					po.setConsumeName(rs.getString("consume_name"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list;
	}
	

	/***
	 * 返回providerd的所有consume
	 * @param appName
	 * @param collectTime
	 * @return
	 */
	public Set<String> getDepMeHsfApp(String appName, Date collectTime ) {
		String sql = "select * from csp_app_dep_hsf_consume_summary where provider_name=? and " +
				"collect_time=DATE_FORMAT(?,\"%Y-%m-%d\") and provider_group = 'All'";
		
		final Set<String> depSet = new LinkedHashSet<String>();
		
		try {
			this.query(sql, new Object[]{ appName, collectTime }, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("consume_name");
					if (!StringUtils.isBlank(appName)) {
						depSet.add(appName);
					}
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return depSet;
	}
	

	/***
	 * 返回consume依赖的所有provider
	 * 
	 * @param appName
	 * @param collectTime
	 * @return
	 */
	public Set<String> getMeDepHsfApp(String appName, Date collectTime ) {
		String sql = "select * from csp_app_dep_hsf_consume_summary where consume_name=? and " +
				"collect_time=DATE_FORMAT(?,\"%Y-%m-%d\") and provider_group = 'All'";
		
		final Set<String> depSet = new LinkedHashSet<String>();
		
		try {
			this.query(sql, new Object[]{ appName, collectTime }, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("provider_name");
					if (!StringUtils.isBlank(appName)) {
						depSet.add(appName);
					}
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return depSet;
	}

	/**
	 * 返回所有provider以及ip
	 * @return
	 */
	public Map<String, Set<String>> getHsfProviderMachineNums() {
		final Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select provider_app, provider_machine_ip from csp_hsf_provider_app_detail where " +
				"DATE_FORMAT(collect_date,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("provider_app");
					String machine = rs.getString("provider_machine_ip");
					
					Set<String> set;
					if (map.containsKey(appName)) {
						set = map.get(appName);
					} else {
						set = new HashSet<String>();
						map.put(appName, set);
					}
					
					
					set.add(machine);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	

	/***
	 * get detail hsf provider info
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public Map<String, Integer> getHsfProviderInfo(String provideAppName, String collectDay){
		
		final Map<String, Integer> map = new HashMap<String, Integer>();
		String sql = "select key_name,sum(call_num) as count from csp_hsf_provider_app_detail where provider_app = ? and collect_date=? group by key_name";
		final String prefix = "IN_HSF-ProviderDetail_";
		try {
			this.query(sql, new Object[]{provideAppName,collectDay}, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String key = rs.getString("key_name").substring(prefix.length());
					int count = rs.getInt("count");
					// 异常信息也在里面，需要过滤掉，仅以IN_HSF-ProviderDetail_开头
					if (key.startsWith("com")) {
						map.put(key, count);
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	
}
