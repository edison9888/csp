package com.taobao.csp.cost.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.taobao.csp.cost.util.LocalUtil;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * web应用的依赖，包括uv，pv，域名等
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-3
 */
public class CspWebDependDao extends MysqlRouteBase {

	private static Log logger = LogFactory.getLog(CspWebDependDao.class);
	
	public CspWebDependDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
	
	//=====================PV=========================
	/**
	 * app url的请求次数<app,[[url,num],[url,num]]>
	 * @return
	 */
	public Map<String,  Map<String, Long>> getPvRequestSummary(Date date) {
		final Map<String,  Map<String, Long>> map = new HashMap<String, Map<String,Long>>();
		
		String sql = "select * from csp_app_request_url_summary where " +
				"DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
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

	/**
	 * 返回APP在一段时间内的PV
	 * 
	 * @param appName
	 * @param date
	 * @return
	 */
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
	
	//=================================UV==================================
	/**
	 * csp_domian_url_uv表中有两种类型的数据，domain和url，该表的数据由哈勃埋点统计，没埋点的就统计不到了
	 * domain表示一个域名下所有的uv和pv
	 * url表示domain+某个页面的uv和pv
	 */
	
	/**
	 * 返回某个域名的PV
	 * 
	 * @param domain
	 * @param date
	 * @return
	 */
	
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
	
	/**
	 * 返回某个域名的UV
	 * 
	 * @param domain
	 * @param date
	 * @return
	 */
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
	

	public int getLatestIPvByDomain(String domain) {
		String sql = "select ipv  from csp_domian_url_uv  where url = ? and url_type='domain' order by collect_time limit 1";
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

	public int getLatestUvByDomain(String domain) {
		String sql = "select uv  from csp_domian_url_uv  where url = ? and url_type='domain' order by collect_time limit 1";
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
	

}
