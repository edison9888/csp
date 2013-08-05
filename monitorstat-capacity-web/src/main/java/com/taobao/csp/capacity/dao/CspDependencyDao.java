package com.taobao.csp.capacity.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.capacity.CapacityConstants;
import com.taobao.csp.capacity.constant.CostConstants;
import com.taobao.csp.capacity.po.CapacityCostConfigPo;
import com.taobao.csp.capacity.po.DepCapacityPo;
import com.taobao.csp.capacity.po.HsfConsumerSummaryPo;
import com.taobao.csp.capacity.po.HsfProviderSummaryPo;
import com.taobao.csp.capacity.util.DependencyCapacityUtil;
import com.taobao.csp.capacity.util.LocalUtil;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class CspDependencyDao extends MysqlRouteBase {
	
	private static Logger logger = Logger.getLogger(com.taobao.csp.capacity.dao.CspDependencyDao.class);
	public CspDependencyDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
	
	/***
	 * get hsf depend info from depend database, then transfer into depend capacity
	 * @param providerName
	 * @param collectTime
	 * @return
	 */
	public List<DepCapacityPo> findDepCapacityByAppDate(String providerName, Date collectTime ) {
		String sql = "select * from csp_app_dep_hsf_consume_summary where provider_name=? and collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")" +
				" and rush_time_qps>? ";
		
		final List<DepCapacityPo> poList = new ArrayList<DepCapacityPo>();
		
		try {
			this.query(sql, new Object[]{ providerName, collectTime, CapacityConstants.DEP_QPS_THRESHOD }, new SqlCallBack(){
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
	
	/***
	 * get hsf applications which depends me
	 * @param appName
	 * @param collectTime
	 * @return
	 */
	public Set<String> getDepMeHsfApp(String appName, Date collectTime ) {
		String sql = "select * from csp_app_dep_hsf_consume_summary where provider_name=? and collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")"
				+ " and provider_group = 'All'";
		
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
	 * get hsf applications which I depend
	 * @param appName
	 * @param collectTime
	 * @return
	 */
	public Set<String> getMeDepHsfApp(String appName, Date collectTime ) {
		String sql = "select * from csp_app_dep_hsf_consume_summary where consume_name=? and collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")"
				+ " and provider_group = 'All'";
		
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
	
	/***
	 * get applications which depend me, from netstat, not logs
	 * @param appName
	 * @return
	 */
	public Map<String, Set<String>> getDepMeApp(String appName) {
		String sql = "select * from csp_app_depend_app where dep_ops_name=? and collect_time="
				+ " (select max(collect_time) from csp_app_depend_app)";

		final Map<String, Set<String>> dep = new LinkedHashMap<String, Set<String>>();

		try {
			this.query(sql, new Object[] { appName }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("ops_name");
					String depType = rs.getString("dep_app_type");
					if (depType == null) depType = "unknownType";
					if (dep.containsKey(depType)) {
						dep.get(depType).add(appName);
					} else {
						Set<String> appSet = new LinkedHashSet<String>();
						appSet.add(appName);
						dep.put(depType, appSet);
					}

				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return dep;
	}
	
	/***
	 * get applications which  I depend, from netstat, not logs
	 * @param appName
	 * @return
	 */
	public Map<String, Set<String>> getMeDepApp(String appName) {
		String sql = "select * from csp_app_depend_app where ops_name=? and collect_time="
				+ " (select max(collect_time) from csp_app_depend_app)";

		
		final Map<String, Set<String>> dep = new LinkedHashMap<String, Set<String>>();
		
		try {
			this.query(sql, new Object[]{ appName }, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("dep_ops_name");
					String depType = rs.getString("dep_app_type");
					if (depType == null) depType = "unknownType";
					if (dep.containsKey(depType)) {
						dep.get(depType).add(appName);
					} else {
						Set<String> appSet = new LinkedHashSet<String>();
						appSet.add(appName);
						dep.put(depType, appSet);
					}
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return dep;
	}
	
	/***
	 * get recent source url info
	 * @param appName
	 * @return key is url, value is access count
	 */
	public Map<String, Integer> getAppSource(String appName) {
		String sql = "select distinct origin_url,origin_url_num from csp_app_origin_url_summary where app_name=? and collect_time="
			+ " (select max(collect_time) from csp_app_origin_url_summary where app_name=?)";
		
		final Map<String, Integer> originMap = new LinkedHashMap<String, Integer>();
		try {
			this.query(sql, new Object[]{ appName }, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String originUrl = rs.getString("origin_url");
					int originNum = rs.getInt("origin_url_num");
					originMap.put(originUrl, originNum);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return originMap;
	}
	
	/***
	 * get source url info by date
	 * @param appName
	 * @param date
	 * @return key is url, value is access count
	 */
	public Map<String, Integer> getOrigin(String appName, String date) {
		String sql = "select distinct origin_url,origin_url_num from csp_app_origin_url_summary where app_name=? and collect_time=?";
		
		final Map<String, Integer> originMap = new LinkedHashMap<String, Integer>();
		try {
			this.query(sql, new Object[]{ appName, date }, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String originUrl = rs.getString("origin_url");
					int originNum = rs.getInt("origin_url_num");
					originMap.put(originUrl, originNum);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return originMap;
	}
	
	/***
	 * get request url by date
	 * @param appName
	 * @param date
	 * @return
	 */
	public Map<String, Integer> getRequest(String appName, String date) {
		String sql = "select distinct request_url,request_url_num from csp_app_origin_url_summary where app_name=? and collect_time=?";
		
		final Map<String, Integer> originMap = new LinkedHashMap<String, Integer>();
		try {
			this.query(sql, new Object[]{ appName, date }, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String originUrl = rs.getString("request_url");
					int originNum = rs.getInt("request_url_num");
					if (originMap.containsKey(originUrl)) {
						int value = originMap.get(originUrl);
						originMap.put(originUrl, value + originNum);
					} else {
						originMap.put(originUrl, originNum);
					}
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return originMap;
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
	
	public List<HsfProviderSummaryPo> getHsfProviderSummaryPos() {
		final List<HsfProviderSummaryPo> list = new ArrayList<HsfProviderSummaryPo>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select * from csp_app_dep_hsf_provide_summary where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and provider_group = 'All'";
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
	
	public List<HsfConsumerSummaryPo> getHsfConsumerSummaryPos(String providerName) {
		final List<HsfConsumerSummaryPo> list = new ArrayList<HsfConsumerSummaryPo>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select * from csp_app_dep_hsf_consume_summary where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and provider_group = 'All' and provider_name = ?";
		try {
			this.query(sql, new Object[]{ date, providerName }, new SqlCallBack() {
				
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
	
	public Map<String, Set<String>> getHsfProviderMachineNums() {
		final Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select provider_app, provider_machine_ip from csp_hsf_provider_app_detail where DATE_FORMAT(collect_date,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
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
	
	public Map<String,  Map<String, Long>> getPvRequestSummary() {
		final Map<String,  Map<String, Long>> map = new HashMap<String, Map<String,Long>>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select * from csp_app_request_url_summary where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("app_name");
					String reqUrl = rs.getString("request_url");
					Long reqNum = rs.getLong("request_num");
					
					Map<String, Long> urlNum;
					if (map.containsKey(appName)) {
						urlNum = map.get(appName);
					} else {
						urlNum = new HashMap<String, Long>();
						map.put(appName, urlNum);
					}
					
					urlNum.put(reqUrl, reqNum);
					
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Map<String, Long> getPvOriginSummary(String appName) {
		final Map<String, Long> map = new HashMap<String, Long>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select * from csp_app_origin_url_summary where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and app_name = ?";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String originUrl = rs.getString("origin_url");
					Long num = rs.getLong("origin_url_num");
					map.put(originUrl, num);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Map<String, Long> getTairProviderSummary() {
		final Map<String, Long> map = new HashMap<String, Long>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select tair_group_name, sum(invoking_all_num) as num from csp_tair_provide_app_summary where " +
				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and tair_group_name is not null and tair_group_name != '' group" +
				" by  tair_group_name";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String groupName = rs.getString("tair_group_name");
					long num = rs.getLong("num");
					map.put(groupName, num);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Map<String, Long> getTairDepSummary(String appName) {
		final Map<String, Long> map = new HashMap<String, Long>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select concat(tair_group_name, '" + CostConstants.DEP_SEP + "', app_name) as dep, " +
				"sum(invoking_all_num) as num from csp_tair_provide_app_summary where " +
				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and tair_group_name  = ? group" +
				" by  dep";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String dep = rs.getString("dep");
					long num = rs.getLong("num");
					map.put(dep, num);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Map<String, Set<String>> getTairMachines() {
		final Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select tair_group_name, tair_host_ip from csp_tair_provider_app_detail where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and tair_group_name is not null and tair_group_name != ''";
		
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String groupName = rs.getString("tair_group_name");
					String hostIp = rs.getString("tair_host_ip");
					
					Set<String> ips;
					if (map.containsKey(groupName)) {
						ips = map.get(groupName);
					} else {
						ips = new HashSet<String>();
						map.put(groupName, ips);
					}
					ips.add(hostIp);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Map<String, Long> getDbConsumerSummary() {
		final Map<String, Long> map = new HashMap<String, Long>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select db_name, sum(execute_sum) as num from csp_app_consume_tddl_summary_new where " +
				" DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and db_name is not null and db_name != '' group" +
				" by  db_name";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String dbName = rs.getString("db_name");
					long num = rs.getLong("num");
					map.put(dbName, num);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Map<String, Long> getDbDepSummary(String appName) {
		final Map<String, Long> map = new HashMap<String, Long>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select concat(db_name, '" + CostConstants.DEP_SEP + "', app_name) as dep, " +
				"sum(execute_sum) as num from csp_app_consume_tddl_summary_new where " +
				" DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") and db_name = ? group" +
				" by  dep";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String dep = rs.getString("dep");
					long num = rs.getLong("num");
					map.put(dep, num);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Map<String, Set<String>> getDbMachines() {
		final Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		Date date = LocalUtil.getCapacityCostDate();
		
		String sql = "select db_name, db_ip from csp_app_consume_tddl_detail where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				"and db_name is not null and db_name != ''";
		
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String dbName = rs.getString("db_name");
					String hostIp = rs.getString("db_ip");
					
					Set<String> ips;
					if (map.containsKey(dbName)) {
						ips = map.get(dbName);
					} else {
						ips = new HashSet<String>();
						map.put(dbName, ips);
					}
					ips.add(hostIp);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Map<String, List<CapacityCostConfigPo>> getCapacityCostConfigPos() {
		final Map<String, List<CapacityCostConfigPo>> map = new HashMap<String, List<CapacityCostConfigPo>>();
		Date date = findLatestConfigDate();
		
		String sql = "select distinct dep_ops_name,ops_name,dep_app_type" +
				" from csp_app_depend_app where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")" +
				" and dep_app_type = 'configserver'";
		try {
			this.query(sql, new Object[]{ date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					List<CapacityCostConfigPo> list;
					String goupName = rs.getString("dep_ops_name");
					String appName = rs.getString("ops_name");
					CapacityCostConfigPo po = new CapacityCostConfigPo();
					po.setAppName(appName);
					po.setConfigGroup(goupName);
					po.setDepType(rs.getString("dep_app_type"));
					
					if (map.containsKey(goupName)) {
						list = map.get(goupName);
					} else {
						list = new ArrayList<CapacityCostConfigPo>();
						map.put(goupName, list);
					}
					
					list.add(po);
					po.setNum(list.size());
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Map<String, List<CapacityCostConfigPo>> getCapacityCostDiamondPos() {
		final Map<String, List<CapacityCostConfigPo>> map = new HashMap<String, List<CapacityCostConfigPo>>();
//		Date date = findLatestConfigDate();
		
		String sql = "select distinct dep_ops_name,ops_name,dep_app_type" +
				" from csp_app_depend_app where " +
				"  dep_app_type='diamond'";
		try {
			this.query(sql, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					List<CapacityCostConfigPo> list;
					String goupName = rs.getString("dep_ops_name");
					String appName = rs.getString("ops_name");
					CapacityCostConfigPo po = new CapacityCostConfigPo();
					po.setAppName(appName);
					po.setConfigGroup(goupName);
					po.setDepType(rs.getString("dep_app_type"));
					
					if (map.containsKey(goupName)) {
						list = map.get(goupName);
					} else {
						list = new ArrayList<CapacityCostConfigPo>();
						map.put(goupName, list);
					}
					
					list.add(po);
					po.setNum(list.size());
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Date findLatestConfigDate() {
		final List<Date> list = new ArrayList<Date>();
		
		String sql = "select max(collect_time) as collect_time from csp_app_depend_app";
		try {
			this.query( sql , new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Date date = rs.getDate("collect_time");
					list.add(date);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list.isEmpty() ? LocalUtil.getCapacityCostDate() : list.get(0);
	}
	
	public Map<String, Long> getPvRequestByAppNameDate(String appName, Date date) {
		final Map<String, Long> map = new LinkedHashMap<String,Long>();
		
		String sql = "select * from csp_app_request_url_summary where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")" +
				" and app_name=? order by request_num desc";
		try {
			this.query(sql, new Object[]{ date, appName }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String reqUrl = rs.getString("request_url");
					Long reqNum = rs.getLong("request_num");
					
					map.put(reqUrl, reqNum);
					
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	
	public Long getHsfProviderPv(String appName, Date date) {
		String sql = "select call_sum from csp_app_dep_hsf_provide_summary where provider_name = ? and DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
		final List<Long> list = new ArrayList<Long>();
		
		try {
			this.query(sql, new Object[]{ appName, date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Long callNumLong = rs.getLong("call_sum");
					list.add(callNumLong);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list.size() == 0 ? 0 : list.get(0);
		
	}
	
	public int getLatestUvByDomain(String domain) {
		String sql = "select uv  from csp_domian_url_uv  where url = ? and url_type='domain' order by collect_time desc limit 1";
		final List<Integer> list = new ArrayList<Integer>();
		
		try {
			this.query(sql, new Object[]{ domain }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Integer uv = rs.getInt("uv");
					list.add(uv);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list.size() == 0 ? 0 : list.get(0);
	}
	
	public int getLatestIPvByDomain(String domain) {
		String sql = "select ipv  from csp_domian_url_uv  where url = ? and url_type='domain' order by collect_time desc limit 1";
		final List<Integer> list = new ArrayList<Integer>();
		
		try {
			this.query(sql, new Object[]{ domain }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Integer ipv = rs.getInt("ipv");
					list.add(ipv);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list.size() == 0 ? 0 : list.get(0);
	}
	
	public int getUvByDomainAndDate(String domain, Date date) {
		String sql = "select uv  from csp_domian_url_uv  where url = ? and " +
				" DATE_FORMAT(collect_time, \"%Y-%m-%d\") = DATE_FORMAT(?, \"%Y-%m-%d\") and url_type='domain'";
		final List<Integer> list = new ArrayList<Integer>();
		
		try {
			this.query(sql, new Object[]{ domain, date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Integer uv = rs.getInt("uv");
					list.add(uv);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list.size() == 0 ? 0 : list.get(0);
	}
	
	public int getIPvByDomainAndDate(String domain, Date date) {
		String sql = "select ipv  from csp_domian_url_uv  where url = ? and " +
				" DATE_FORMAT(collect_time, \"%Y-%m-%d\") = DATE_FORMAT(?, \"%Y-%m-%d\") and url_type='domain'";
		final List<Integer> list = new ArrayList<Integer>();
		
		try {
			this.query(sql, new Object[]{ domain, date }, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Integer uv = rs.getInt("ipv");
					list.add(uv);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list.size() == 0 ? 0 : list.get(0);
	}
	
}
