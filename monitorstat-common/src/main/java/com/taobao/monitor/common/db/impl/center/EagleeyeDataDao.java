package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.CspCallsRelationship;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestPart;
public class EagleeyeDataDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(EagleeyeDataDao.class);

	public static final long LIMIT_SIZE = 1000; 
	public EagleeyeDataDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}


	public Map<String,List<CspCallsRelationship>> findCallsRelationship(){

		String sql = "select * from csp_calls_relationship ";

		final Map<String,List<CspCallsRelationship>> urlMap = new HashMap<String, List<CspCallsRelationship>>();

		try {
			this.query(sql,  new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {

					List<CspCallsRelationship> list = urlMap.get(rs.getString("source_url"));
					if(list == null){
						list = new ArrayList<CspCallsRelationship>();
						urlMap.put(rs.getString("source_url"), list);
					}

					CspCallsRelationship er = new CspCallsRelationship();
					er.setOrigin(rs.getString("origin"));
					er.setOriginApp(rs.getString("origin_app"));
					er.setSourceUrl(rs.getString("source_url"));
					er.setSourceApp(rs.getString("source_app"));
					er.setTarget(rs.getString("target"));
					er.setTargetApp(rs.getString("target_app"));
					er.setRate(rs.getFloat("rate"));
					list.add(er);
				}
			});
		} catch (Exception e) {
			logger.error("findCallsRelationship", e);
		}
		return urlMap;
	}




	public List<CspCallsRelationship> findCallsRelationshipBySourceUrl(String sourceUrl){

		String sql = "select * from csp_calls_relationship where source_url=?";

		final List<CspCallsRelationship> urllist = new ArrayList<CspCallsRelationship>();

		try {
			this.query(sql, new Object[]{sourceUrl}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {

					CspCallsRelationship er = new CspCallsRelationship();
					er.setOrigin(rs.getString("origin"));
					er.setOriginApp(rs.getString("origin_app"));
					er.setSourceUrl(rs.getString("source_url"));
					er.setSourceApp(rs.getString("source_app"));
					er.setTarget(rs.getString("target"));
					er.setTargetApp(rs.getString("target_app"));
					er.setRate(rs.getFloat("rate"));
					urllist.add(er);
				}
			});
		} catch (Exception e) {
			logger.error("findCallsRelationship", e);
		}
		return urllist;
	}



	public void deleteCallsRelationship(String source){
		String sql = "delete from csp_calls_relationship where source_url=?";
		try {
			this.execute(sql, new Object[]{source});
		} catch (SQLException e) {
			logger.error("deleteCallsRelationship", e);
		}
	}

	public void addCallsRelationship(CspCallsRelationship call){
		String sql = "insert into csp_calls_relationship(source_app,source_url,origin_app,origin,target_app,target,rate,collect_time)values(?,?,?,?,?,?,?,Now())";
		try {
			this.execute(sql, new Object[]{call.getSourceApp(),call.getSourceUrl(),call.getOriginApp(),call.getOrigin(),call.getTargetApp(),call.getTarget(),call.getRate()});
		} catch (SQLException e) {
			logger.error("addCallsRelationship", e);
		}
	}

	public List<CspCallsRelationship> getSubCallRelationShip(String sourceUrl, String origin) {
		String sql = "select * from csp_calls_relationship where source_url=? and origin=?";

		final List<CspCallsRelationship> urllist = new ArrayList<CspCallsRelationship>();

		try {
			this.query(sql, new Object[]{sourceUrl, origin}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {

					CspCallsRelationship er = new CspCallsRelationship();
					er.setOrigin(rs.getString("origin"));
					er.setOriginApp(rs.getString("origin_app"));
					er.setSourceUrl(rs.getString("source_url"));
					er.setSourceApp(rs.getString("source_app"));
					er.setTarget(rs.getString("target"));
					er.setTargetApp(rs.getString("target_app"));
					er.setRate(rs.getFloat("rate"));
					urllist.add(er);
				}
			});
		} catch (Exception e) {
			logger.error("findCallsRelationship", e);
		}
		return urllist;
	}

	/*******************Eagleeye数据********************/
	public void addCspEagleeyeApiRequestPart(CspEagleeyeApiRequestPart part) throws SQLException {
		String sql = "insert into csp_eagleeye_api_request_part(app_name,collect_time,response_content,api_type,version,sourcekey) value(?,?,?,?,?,?)";
		this.execute(sql, new Object[]{part.getAppName(), part.getCollectTime(), part.getResponseContent(),part.getApiType(), part.getVersion(), part.getSourcekey()});
	}

	public void addCspEagleeyeApiRequestDay(CspEagleeyeApiRequestDay day) throws SQLException {
		String sql = "insert into csp_eagleeye_api_request_day(app_name,collect_time,response_content,api_type,version,sourcekey) value(?,?,?,?,?,?)";
		this.execute(sql, new Object[]{day.getAppName(), day.getCollectTime(), day.getResponseContent(),day.getApiType(), day.getVersion(), day.getSourcekey()});
	}

	public List<CspEagleeyeApiRequestPart> searchEagleeyeApiRequestPart(String sourcekey, String api_type, Date collect_timeStart, Date collect_timeEnd) {
		String sql = "select * from csp_eagleeye_api_request_part where sourcekey=? and api_type = ? and collect_time between ? and ?";
		final List<CspEagleeyeApiRequestPart> list = new ArrayList<CspEagleeyeApiRequestPart>();
		try {
			this.query(sql, new Object[]{sourcekey, api_type, collect_timeStart, collect_timeEnd}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspEagleeyeApiRequestPart po = new CspEagleeyeApiRequestPart();
					setCspEagleeyeApiRequestPart(rs, po);
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}

	public List<CspEagleeyeApiRequestPart> searchEagleeyeApiRequestPart(String sourcekey, String api_type, String appName, Date collect_timeStart, Date collect_timeEnd) {
		String sql = "select * from csp_eagleeye_api_request_part where sourcekey=? and api_type = ? and collect_time between ? and ? and app_name = ?";
		final List<CspEagleeyeApiRequestPart> list = new ArrayList<CspEagleeyeApiRequestPart>();
		try {
			this.query(sql, new Object[]{sourcekey, api_type, collect_timeStart, collect_timeEnd,appName}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspEagleeyeApiRequestPart po = new CspEagleeyeApiRequestPart();
					setCspEagleeyeApiRequestPart(rs, po);
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}	

	private void setCspEagleeyeApiRequestPart(ResultSet rs, CspEagleeyeApiRequestPart po) throws SQLException {
		po.setAppName(rs.getString("app_name"));
		po.setCollectTime(rs.getTimestamp("collect_time"));
		po.setResponseContent(rs.getString("response_content"));
		po.setSourcekey(rs.getString("sourcekey"));
		po.setVersion(rs.getString("version"));
		po.setApiType(rs.getString("api_type"));
	}

	public CspEagleeyeApiRequestDay searchCspEagleeyeApiRequestDay(String sourcekey, String api_type, String collect_time) {
		String sql = "select * from csp_eagleeye_api_request_day where sourcekey=? and api_type = ? and collect_time = ?";
		final CspEagleeyeApiRequestDay po = new CspEagleeyeApiRequestDay();
		try {
			this.query(sql, new Object[]{sourcekey, api_type, collect_time}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					setCspEagleeyeApiRequestDay(rs, po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po;
	}

	public CspEagleeyeApiRequestDay searchRecentlyDayPo(String appName, String sourcekey, String api_type) {
		String sql = "select * from csp_eagleeye_api_request_day where app_name = ? and sourcekey=? and api_type = ? order by collect_time desc";
		final CspEagleeyeApiRequestDay po = new CspEagleeyeApiRequestDay();
		try {
			this.query(sql, new Object[]{appName, sourcekey, api_type}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					setCspEagleeyeApiRequestDay(rs, po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po;
	}

	public List<CspEagleeyeApiRequestDay> searchCspEagleeyeApiRequestDayList(String api_type, String appName, String collect_time) {
		String sql = "select * from csp_eagleeye_api_request_day where api_type = ? and collect_time = ? and app_name = ? and sourcekey != 'firsturl'";
		final List<CspEagleeyeApiRequestDay> list = new ArrayList<CspEagleeyeApiRequestDay>();
		try {
			this.query(sql, new Object[]{api_type, collect_time,appName}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					try {
						CspEagleeyeApiRequestDay po = new CspEagleeyeApiRequestDay();
						setCspEagleeyeApiRequestDay(rs, po);
						list.add(po);
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	public CspEagleeyeApiRequestDay searchCspEagleeyeApiRequestDay(String sourcekey, String api_type, String appName, String collect_time) {
		String sql = "select * from csp_eagleeye_api_request_day where sourcekey=? and api_type = ? and collect_time = ? and app_name = ?";
		final CspEagleeyeApiRequestDay po = new CspEagleeyeApiRequestDay();
		try {
			this.query(sql, new Object[]{sourcekey, api_type, collect_time,appName}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					setCspEagleeyeApiRequestDay(rs, po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po;
	}

	private void setCspEagleeyeApiRequestDay(ResultSet rs, CspEagleeyeApiRequestDay po) throws SQLException {
		po.setAppName(rs.getString("app_name"));
		po.setCollectTime(rs.getDate("collect_time"));
		po.setResponseContent(rs.getString("response_content"));
		po.setSourcekey(rs.getString("sourcekey"));
		po.setVersion(rs.getString("version"));
		po.setApiType(rs.getString("api_type"));
	}

	/**
	 * 根据api类型和时间来获取所有不同的sourcekey
	 * @param api_type
	 * @param collect_timeStart
	 * @param collect_timeEnd
	 * @return
	 */
	public Set<String> getDistinctSourceKey(String api_type, Date collect_timeStart, Date collect_timeEnd){
		final Set<String> set = new HashSet<String>();
		String sql = "select distinct sourcekey from csp_eagleeye_api_request_part where api_type = ? and collect_time between ? and ?";
		try {
			this.query(sql, new Object[]{api_type, collect_timeStart, collect_timeEnd}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					set.add(rs.getString("sourcekey"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return set;
	}

	public Set<String> getDistinctSourceKey(String api_type, String appName, Date collect_timeStart, Date collect_timeEnd){
		final Set<String> set = new HashSet<String>();
		String sql = "select distinct sourcekey from csp_eagleeye_api_request_part where app_name = ? and api_type = ? and collect_time between ? and ?";
		try {
			this.query(sql, new Object[]{appName, api_type, collect_timeStart, collect_timeEnd}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					set.add(rs.getString("sourcekey"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return set;
	}	

	public void deleteEagleeyePartBeforeDate(Date date){
		String sql = "delete from csp_eagleeye_api_request_part where collect_time < ?";
		try {
			this.execute(sql, new Object[]{date});
		} catch (SQLException e) {
			logger.error("BeforeDate", e);
		}
	}
}
