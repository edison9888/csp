package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.GraphData;
import com.taobao.csp.depend.po.url.RequestUrlSummary;
import com.taobao.csp.depend.po.url.UrlOriginSummary;
import com.taobao.csp.depend.po.url.UrlUv;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class UrlDao extends MysqlRouteBase {

	public UrlDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
	private static final Logger logger =  Logger.getLogger(UrlDao.class);

	public List<RequestUrlSummary> findRequestList(String appName, String collectDate) {
		String sql = "select * from csp_app_request_url_summary where app_name = ? and collect_time = ?";
		final List<RequestUrlSummary> list = new ArrayList<RequestUrlSummary>();
		try {
			this.query(sql, new Object[]{appName,collectDate}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {

					String request_url = rs.getString("request_url");
					if(request_url == null || request_url.equals("-"))
						return;
					RequestUrlSummary po = new RequestUrlSummary();
					po.setAppName(rs.getString("app_name"));
					po.setCollect_time(new Date(rs.getTimestamp("collect_time").getTime()));
					po.setPreRequestUrlNum(0);
					po.setRequestNum(rs.getLong("request_num"));
					po.setRequestTime(rs.getInt("request_time"));
					po.setRequestUrl(rs.getString("request_url"));
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("findRequestList：", e);
		}		
		return list;
	}

	public List<UrlOriginSummary> findOriginList(String appName, String collectDate) {
		String sql = "select * from csp_app_origin_url_summary where app_name = ? and collect_time = ?";
		final List<UrlOriginSummary> list = new ArrayList<UrlOriginSummary>();
		try {
			this.query(sql, new Object[]{appName,collectDate}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {

					String originUrl = rs.getString("origin_url");
					if(originUrl == null || originUrl.equals("-"))
						return;
					UrlOriginSummary po = new UrlOriginSummary();
					po.setAppName(rs.getString("app_name"));
					po.setCollect_time(new Date(rs.getTimestamp("collect_time").getTime()));
					po.setPreOriginUrlNum(0);
					po.setOriginUrl(rs.getString("origin_url"));
					po.setOriginUrlNum(rs.getLong("origin_url_num"));
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("findOriginList：", e);
		}   
		return list;
	}

	public List<GraphData> findOriginListHistory(String startDate, String endDate, String opsName, String url) {
		String sql = null;
		Object[] params = null;
		if(url != null) {
			sql = "select collect_time,origin_url_num,origin_url from csp_app_origin_url_summary where app_name = ? and collect_time between ? and ? and origin_url = ?";
			params = new Object[]{opsName, startDate, endDate, url};

		} else {
			sql = "select collect_time,origin_url_num,origin_url from csp_app_origin_url_summary where app_name = ? and collect_time between ? and ?";
			params = new Object[]{opsName, startDate, endDate};
		}
		final Map<String, GraphData> map = new HashMap<String, GraphData>();
		try {
			this.query(sql, params, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String originUrl = rs.getString("origin_url");
					String collect_time = rs.getString("collect_time");
					if(originUrl == null || originUrl.equals("-"))
						return;

					if(!map.containsKey(collect_time)) {
						GraphData data = new GraphData();
						data.setCallAllNum(rs.getLong("origin_url_num"));
						data.setCollectDate(rs.getDate("collect_time"));
						map.put(collect_time, data);
					} else {
						GraphData data = map.get(collect_time);
						data.setCallAllNum(data.getCallAllNum() + rs.getLong("origin_url_num"));
					}
				}});
		} catch (Exception e) {
			logger.error("findOriginList：", e);
		}   
		return new ArrayList<GraphData>(map.values());
	}  

	public List<GraphData> findRequestListHistory(String startDate, String endDate, String opsName, String url) {
		String sql = null;
		Object[] params = null;
		if(url != null) {
			sql = "select collect_time,request_num,request_url from csp_app_request_url_summary where app_name = ? and collect_time between ? and ? and request_url = ?";
			params = new Object[]{opsName, startDate, endDate, url};
		} else {
			sql = "select collect_time,request_num,request_url from csp_app_request_url_summary where app_name = ? and collect_time between ? and ?";
			params = new Object[]{opsName, startDate, endDate};
		}
		final Map<String, GraphData> map = new HashMap<String, GraphData>();
		try {
			this.query(sql, params, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					String request_url = rs.getString("request_url");
					String collect_time = rs.getString("collect_time");
					if(request_url == null || request_url.equals("-"))
						return;

					if(!map.containsKey(collect_time)) {
						GraphData data = new GraphData();
						data.setCallAllNum(rs.getLong("request_num"));
						data.setCollectDate(rs.getDate("collect_time"));
						map.put(collect_time, data);
					} else {
						GraphData data = map.get(collect_time);
						data.setCallAllNum(data.getCallAllNum() + rs.getLong("request_num"));
					}
				}});
		} catch (Exception e) {
			logger.error("findOriginList：", e);
		}   
		return new ArrayList<GraphData>(map.values());
	}

	/**
	 * 添加url或domain  uv信息
	 *@author xiaodu
	 * @param uv
	 *TODO
	 */
	public void addUrlUv(UrlUv uv){

		String sql = "insert into csp_domian_url_uv(url,uv,ipv,url_type,collect_time)values(?,?,?,?,?)";

		try {
			this.execute(sql, new Object[]{uv.getUrl(),uv.getUv(),uv.getIpv(),uv.getUrlType(),uv.getCollectTime()});
		} catch (SQLException e) {
			logger.error("addUrlUv：", e);
		}
	}

	/**
	 * like search uv
	 * @param collect_time
	 * @param url_type
	 * @param url
	 * @return
	 */
	public List<UrlUv> queryUrlUv(String collect_time, String url_type, String url, int pageNo, int pageSize) {
		Object[] params = null;
		List<Object> paramsList = new ArrayList<Object>();
		String sql = getUVPVQuerySql(collect_time, url_type, url, paramsList);

		sql += " limit ?, ?";
		paramsList.add((pageNo - 1)*pageSize);
		paramsList.add(pageSize);

		params = paramsList.toArray();

		final List<UrlUv> list = new ArrayList<UrlUv>();
		try {
			this.query(sql, params, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					UrlUv uv = new UrlUv();
					uv.setUrl(rs.getString("url"));
					uv.setUv(rs.getInt("uv"));
					uv.setIpv(rs.getInt("ipv"));
					uv.setUrlType(rs.getString("url_type"));
					uv.setCollectTime(rs.getDate("collect_time"));
					list.add(uv);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}

//	//查询总数
//	public long getTotalCountBySql(String collect_time, String url_type, String url) {
//		Object[] params = null;
//		List<Object> paramsList = new ArrayList<Object>();
//		String querySql = getUVPVQuerySql(collect_time, url_type, url, paramsList);
//		params = paramsList.toArray();
//		String countSql = "select count(*) total from (" + querySql + ") t ";
//
//		final List<Long> resultList = new ArrayList<Long>();
//		try {
//			this.query(countSql, params, new SqlCallBack() {
//				public void readerRows(ResultSet rs) throws Exception {
//					long total = rs.getLong("total");
//					resultList.add(total);
//				}
//			});
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//
//		if(resultList.size() != 0)
//			return resultList.get(0);
//		return 0;
//	}


	public String getUVPVQuerySql(String collect_time, String url_type, String url, final List<Object> list) {
		String sql = "select url,uv,ipv,url_type as url_type ,collect_time as collect_time from csp_domian_url_uv where collect_time=? and url_type = ?";
		list.add(collect_time);
		list.add(url_type);
		if(url != null && !url.trim().equals("")) {
			sql += " and url  LIKE  CONCAT('%', ? ,'%')";
			list.add(url);
		}
		sql += "order by uv desc";
		return sql;
	}

	public Map<String, Long> queryUrl(String start, String end, String queryType, String urlType) {
		String sqlType = "ipv";
		if(queryType == "uv") {
			sqlType = "uv";
		}
		String sql = "select sum(" + sqlType + ") as amount,collect_time from csp_domian_url_uv where url_type = ? and collect_time >= ? and collect_time <= ? group by collect_time";
		final Map<String, Long> map = new HashMap<String, Long>();
		try {
			this.query(sql, new Object[]{urlType, start, end}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					if(!map.containsKey(rs.getString("collect_time"))) {
						map.put(rs.getString("collect_time"), rs.getLong("amount"));
					} else {
						map.put(rs.getString("collect_time"), map.get(rs.getString("collect_time")) + rs.getLong("amount"));
					}	        	
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}
	
	public Map<String, Long> queryByUrl(String start, String end, String queryType, String urlType, String url) {
		String sqlType = "ipv";
		if(queryType == "uv") {
			sqlType = "uv";
		}
		String sql = "select sum(" + sqlType + ") as amount,collect_time from csp_domian_url_uv where url_type = ? and collect_time >= ? and collect_time <= ? and url = ? group by collect_time";
		final Map<String, Long> map = new HashMap<String, Long>();
		try {
			this.query(sql, new Object[]{urlType, start, end, url}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					if(!map.containsKey(rs.getString("collect_time"))) {
						map.put(rs.getString("collect_time"), rs.getLong("amount"));
					} else {
						map.put(rs.getString("collect_time"), map.get(rs.getString("collect_time")) + rs.getLong("amount"));
					}	        	
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}	
}
