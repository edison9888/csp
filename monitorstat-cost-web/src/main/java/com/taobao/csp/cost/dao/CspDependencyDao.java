package com.taobao.csp.cost.dao;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.taobao.csp.cost.po.CapacityCostConfigPo;
import com.taobao.csp.cost.po.DepCapacityPo;
import com.taobao.csp.cost.po.HsfConsumerSummaryPo;
import com.taobao.csp.cost.po.HsfProviderSummaryPo;
import com.taobao.csp.cost.util.CostConstants;
import com.taobao.csp.cost.util.DependencyCapacityUtil;
import com.taobao.csp.cost.util.LocalUtil;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class CspDependencyDao extends MysqlRouteBase {
	
	private static Log logger = LogFactory.getLog(CspDependencyDao.class);
	
	public CspDependencyDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
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
	
	/**config server/diamondµÈ**/

}
