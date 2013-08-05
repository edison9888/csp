package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.rating.RatingApp;
import com.taobao.monitor.web.rating.RatingIndicator;
import com.taobao.monitor.web.rating.RatingIndicatorValue;
import com.taobao.monitor.web.rating.RatingOptimizeRecord;
import com.taobao.monitor.web.rating.RatingOptimizeSolution;

/**
 * 
 * @author xiaodu
 * @version 2010-8-18 下午02:32:44
 */
public class MonitorRatingDao extends MysqlRouteBase {

	private static final Logger logger = Logger
			.getLogger(MonitorRatingDao.class);


	/**
	 * 
	 * @param indicator
	 */
	public void addRatingIndicator(RatingIndicator indicator) {
		String delete = "delete from ms_rating_indicator where app_id=? and key_id=?";
		String sql = "insert into ms_rating_indicator"
				+ "(app_id,key_id,key_unit,indicator_key,indicator_weight,indicator_threshold_value,GMT_MODIFIED,GMT_CREATE,rushHour_start,rushHour_end) values(?,?,?,?,?,?,NOW(),NOW(),?,?)";
		try {
			this.execute(delete, new Object[] { indicator.getAppId(),
					indicator.getKeyId() });
			this.execute(sql, new Object[] { indicator.getAppId(),indicator.getKeyId(),indicator.getKeyUnit(),indicator.getIndicatorKeyName(),
					indicator.getIndicatorWeight(),
					indicator.getIndicatorThresholdValue(),
					indicator.getRushHour_start(),
					indicator.getRushHour_end()});
			
		} catch (SQLException e) {
			logger.error("addRatingIndicator: ", e);
		}
	}

	public void updateRatingIndicator(RatingIndicator indicator) {
		String sql = "update ms_rating_indicator set indicator_weight=?,indicator_threshold_value=?,GMT_MODIFIED=NOW(),rushHour_start=?,rushHour_end=? where app_id=? and key_id=?";
		try {
			this.execute(sql, new Object[] { 
					indicator.getIndicatorWeight(),
					indicator.getIndicatorThresholdValue(),
					indicator.getRushHour_start(),
					indicator.getRushHour_end(),
					indicator.getAppId(),
					indicator.getKeyId(),
					});
		} catch (SQLException e) {
			logger.error("updateRatingIndicator: ", e);
		}
	}

	public List<RatingIndicator> getRatingIndicatorByAppId(int app_id) {
		final List<RatingIndicator> list = new ArrayList<RatingIndicator>();
		String sql = "select app_id,key_id,key_unit,indicator_key,indicator_weight,indicator_threshold_value,GMT_MODIFIED,GMT_CREATE,rushHour_start,rushHour_end from ms_rating_indicator where app_id=?";
		try {
			this.query(sql, new Object[] { app_id }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					RatingIndicator ri = new RatingIndicator();
					ri.setIndicatorKeyName(rs.getString("Indicator_key"));
					ri.setIndicatorWeight(rs.getInt("indicator_weight"));
					ri.setIndicatorThresholdValue(rs
							.getString("indicator_threshold_value"));
					ri.setGmtModified(rs.getDate("GMT_MODIFIED"));
					ri.setGmtCreate(rs.getDate("GMT_CREATE"));
					ri.setRushHour_start(rs.getInt("rushHour_start"));
					ri.setRushHour_end(rs.getInt("rushHour_end"));
					ri.setKeyUnit(rs.getString("key_unit"));
					ri.setKeyId(rs.getInt("key_id"));
					ri.setAppId(rs.getInt("app_id"));
					list.add(ri);
				}
			});
		} catch (Exception e) {
			logger.error("getRatingIndicatorByAppId: ", e);
		}

		return list;
	}

	public void deleteRatingApp(int app_id) {
		String sql = "update ms_rating_indicator set status=0 where app_id=?";
		try {
			this.execute(sql, new Object[] { app_id });
		} catch (SQLException e) {
			logger.error("deleteRatingApp: ", e);
		}
	}

	/**
	 * 
	 * @param indicator
	 */
	public void addRatingIndicatorValue(RatingIndicatorValue indicatorvalue) {

		String delete = "delete from MS_RATING_INDICATOR_VALUE where app_id=? and indicator_key=? and collect_day=?";

		try {
			this.execute(delete, new Object[] { indicatorvalue.getAppId(),
					indicatorvalue.getIndicatorKey(),
					indicatorvalue.getCollectDay() });
		} catch (SQLException e1) {
			logger.error("", e1);
		}
		String sql = "insert into MS_RATING_INDICATOR_VALUE"
				+ "(key_unit,key_id,app_id,indicator_key,indicator_weight,indicator_threshold_value,indicator_value,collect_day) values(?,?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[] {
					indicatorvalue.getKeyUnit(),
					indicatorvalue.getKeyId(),
					indicatorvalue.getAppId(),
					indicatorvalue.getIndicatorKey(),
					indicatorvalue.getIndicatorWeight(),
					indicatorvalue.getIndicatorThresholdValue(),
					indicatorvalue.getIndicatorValue(),
					indicatorvalue.getCollectDay() });
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	public List<RatingIndicatorValue> getRecentHealthIndexByAppId(
			final int app_id) {
		String sql = "select key_unit,key_id,indicator_key,indicator_value,indicator_weight,indicator_threshold_value,collect_day from ms_rating_indicator_value where app_id = ? and collect_day = (select max(collect_day) from ms_rating_indicator_value where app_id = ?)";
		final List<RatingIndicatorValue> list = new ArrayList<RatingIndicatorValue>();
		try {
			this.query(sql, new Object[] { app_id, app_id }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					RatingIndicatorValue riv = new RatingIndicatorValue();
					riv.setAppId(app_id);
					riv.setCollectDay(rs.getInt("collect_day"));
					riv.setIndicatorWeight(rs.getInt("indicator_weight"));
					riv.setIndicatorValue(rs.getDouble("indicator_value"));
					riv.setIndicatorThresholdValue(rs
							.getString("indicator_threshold_value"));
					riv.setIndicatorKey(rs.getString("indicator_key"));
					riv.setKeyId(rs.getInt("key_id"));
					riv.setKeyUnit(rs.getString("key_unit"));
					list.add(riv);
				}
			});
		} catch (Exception e) {
			logger.error("getRecentHealthIndexByAppId: ", e);
		}
		return list;
	}

	public List<RatingIndicatorValue> getHealthIndexByAppId(final int app_id) {
		String sql = "select key_unit,key_id,indicator_key,indicator_value,indicator_weight,indicator_threshold_value,collect_day from ms_rating_indicator_value where app_id=?";
		final List<RatingIndicatorValue> list = new ArrayList<RatingIndicatorValue>();
		try {
			this.query(sql, new Object[] { app_id }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					RatingIndicatorValue riv = new RatingIndicatorValue();
					riv.setAppId(app_id);
					riv.setCollectDay(rs.getInt("collect_day"));
					riv.setIndicatorWeight(rs.getInt("indicator_weight"));
					riv.setIndicatorValue(rs.getDouble("indicator_value"));
					riv.setIndicatorThresholdValue(rs
							.getString("indicator_threshold_value"));
					riv.setIndicatorKey(rs.getString("indicator_key"));
					riv.setKeyId(rs.getInt("key_id"));
					riv.setKeyUnit(rs.getString("key_unit"));
					list.add(riv);
				}
			});
		} catch (Exception e) {
			logger.error("getHealthIndexByAppId: ", e);
		}
		return list;
	}

	public void addRatingOptimizeRecord(RatingOptimizeRecord record) {
		String sql = "insert into MS_RATING_OPTIMIZE_RECORD"
				+ "(app_id, subject, submitter, owner, description, status, priority, comment, rating_value, collect_day, GMT_MODIFIED, GMT_CREATE) values(?,?,?,?,?,?,?,?,?,?,NOW(),NOW())";
		try {
			this.execute(sql, new Object[] { record.getAppId(),
					record.getSubject(), record.getSubmitter(),
					record.getOwner(), record.getDescription(),
					record.getStatus(), record.getPriority(),
					record.getComment(), record.getRating_value(),
					record.getCollect_day() });
		} catch (SQLException e) {
			logger.error("addRatingOptimizeRecord: ", e);
		}
	}

	public void updateRatingOptimizeRecord(RatingOptimizeRecord record) {
		String sql = "update MS_RATING_OPTIMIZE_RECORD set subject=?,submitter=?,owner=?,description=?,status=?,priority=?,comment=?,GMT_MODIFIED=NOW() where id=?";
		try {
			this
					.execute(sql, new Object[] { record.getSubject(),
							record.getSubmitter(), record.getOwner(),
							record.getDescription(), record.getStatus(),
							record.getPriority(), record.getComment(),
							record.getId() });
		} catch (SQLException e) {
			logger.error("updateRatingOptimizeRecord: ", e);
		}
	}

	public List<RatingOptimizeRecord> getRatingOptimizeRecord(final int app_id) {
		final List<RatingOptimizeRecord> list = new ArrayList<RatingOptimizeRecord>();
		String sql = "select id,subject,submitter,owner,description,status,priority,comment,rating_value,collect_day,GMT_MODIFIED,GMT_CREATE from MS_RATING_OPTIMIZE_RECORD where app_id=? order by id";
		try {
			this.query(sql, new Object[] { app_id }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					RatingOptimizeRecord ror = new RatingOptimizeRecord();
					ror.setId(rs.getInt("id"));
					ror.setAppId(app_id);
					ror.setSubject(rs.getString("subject"));
					ror.setSubmitter(rs.getString("submitter"));
					ror.setOwner(rs.getString("owner"));
					ror.setDescription(rs.getString("description"));
					ror.setStatus(rs.getString("status"));
					ror.setPriority(rs.getString("priority"));
					ror.setComment(rs.getString("comment"));
					ror.setRating_value(rs.getDouble("rating_value"));
					ror.setCollect_day(rs.getDate("collect_day"));
					String time = rs.getString("GMT_MODIFIED");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					ror.setGmtModified(sdf.parse(time));
					time = rs.getString("GMT_CREATE");
					ror.setGmtCreate(sdf.parse(time));
					list.add(ror);
				}
			});
		} catch (Exception e) {
			logger.error("getRatingOptimizeRecord: ", e);
		}

		return list;
	}

	public RatingOptimizeRecord getRatingOptimizeRecordById(final int id){
		final RatingOptimizeRecord ror = new RatingOptimizeRecord();
		String sql = "select subject,submitter,owner,description,status,priority,comment,rating_value,collect_day,GMT_MODIFIED,GMT_CREATE from MS_RATING_OPTIMIZE_RECORD where id=?";
		try {
			this.query(sql, new Object[] { id }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					ror.setId(id);
					ror.setSubject(rs.getString("subject"));
					ror.setSubmitter(rs.getString("submitter"));
					ror.setOwner(rs.getString("owner"));
					ror.setDescription(rs.getString("description"));
					ror.setStatus(rs.getString("status"));
					ror.setPriority(rs.getString("priority"));
					ror.setComment(rs.getString("comment"));
					ror.setRating_value(rs.getDouble("rating_value"));
					ror.setCollect_day(rs.getDate("collect_day"));
					String time = rs.getString("GMT_MODIFIED");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					ror.setGmtModified(sdf.parse(time));
					time = rs.getString("GMT_CREATE");
					ror.setGmtCreate(sdf.parse(time));
				}
			});
		} catch (Exception e) {
			logger.error("getRatingOptimizeRecordById: ", e);
		}

		return ror;
	}
	
	public void deleteRatingOptimizeRecord(RatingOptimizeRecord record) {
		String sql = "delete from MS_RATING_OPTIMIZE_RECORD where id=?";
		try {
			this.execute(sql, new Object[] { record.getId() });
		} catch (SQLException e) {
			logger.error("deleteRatingOptimizeRecord: ", e);
		}
	}

	public void addRatingOptimizeSolution(RatingOptimizeSolution solution) {
		String sql = "insert into MS_RATING_OPTIMIZE_SOLUTION"
				+ "(optimize_record_id, submitter, reason, solution, GMT_MODIFIED, GMT_CREATE) values(?,?,?,?,NOW(),NOW())";
		try {
			this.execute(sql, new Object[] { solution.getOptimize_record_id(),
					solution.getSubmitter(), solution.getReason(),
					solution.getSolution() });
		} catch (SQLException e) {
			logger.error("addRatingOptimizeSolution: ", e);
		}
	}

	public void updateRatingOptimizeSolution(RatingOptimizeSolution solution) {
		String sql = "update MS_RATING_OPTIMIZE_SOLUTION set submitter=?,reason=?,solution=?,GMT_MODIFIED=NOW() where id=?";
		try {
			this.execute(sql, new Object[] { solution.getSubmitter(),
					solution.getReason(), solution.getSolution(),
					solution.getId() });
		} catch (SQLException e) {
			logger.error("updateRatingOptimizeSolution: ", e);
		}
	}

	public List<RatingOptimizeSolution> getRatingOptimizeSolution(final int optimize_record_id) {
		final List<RatingOptimizeSolution> list = new ArrayList<RatingOptimizeSolution>();
		String sql = "select id,submitter,reason,solution,GMT_MODIFIED,GMT_CREATE from MS_RATING_OPTIMIZE_SOLUTION where optimize_record_id=? order by id";
		try {
			this.query(sql, new Object[] { optimize_record_id }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					RatingOptimizeSolution ros = new RatingOptimizeSolution();
					ros.setId(rs.getInt("id"));
					ros.setOptimize_record_id(optimize_record_id);
					ros.setSubmitter(rs.getString("submitter"));
					ros.setReason(rs.getString("reason"));
					ros.setSolution(rs.getString("solution"));
					ros.setGmtModified(rs.getDate("GMT_MODIFIED"));
					ros.setGmtCreate(rs.getDate("GMT_CREATE"));
					list.add(ros);
				}
			});
		} catch (Exception e) {
			logger.error("getRatingOptimizeSolution: ", e);
		}

		return list;
	}

	public RatingOptimizeSolution getRatingOptimizeSolutionById(final int id){
		final RatingOptimizeSolution ros = new RatingOptimizeSolution();
		String sql = "select optimize_record_id,submitter,reason,solution,GMT_MODIFIED,GMT_CREATE from MS_RATING_OPTIMIZE_SOLUTION where id=?";
		try {
			this.query(sql, new Object[] { id }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					ros.setId(id);
					ros.setOptimize_record_id(rs.getInt("optimize_record_id"));
					ros.setSubmitter(rs.getString("submitter"));
					ros.setReason(rs.getString("reason"));
					ros.setSolution(rs.getString("solution"));
					ros.setGmtModified(rs.getDate("GMT_MODIFIED"));
					ros.setGmtCreate(rs.getDate("GMT_CREATE"));
				}
			});
		} catch (Exception e) {
			logger.error("getRatingOptimizeSolutionById: ", e);
		}

		return ros;
	}
	
	public void deleteRatingOptimizeSolution(RatingOptimizeSolution solution) {
		String sql = "delete from MS_RATING_OPTIMIZE_SOLUTION where id=?";
		try {
			this.execute(sql, new Object[] { solution.getId() });
		} catch (SQLException e) {
			logger.error("deleteRatingOptimizeSolution: ", e);
		}
	}
	
//	/**
//	 * 取得这个应用的所有评分信息
//	 * 
//	 * @param appId
//	 * @return
//	 */
//	public List<AppRating> findAppRating(int appId, Date collectTime) {
//
//		String sql = "select * from ms_monitor_rating where app_id=? and collect_time=?";
//
//		final List<AppRating> list = new ArrayList<AppRating>();
//
//		try {
//			this.query(sql, new Object[] { appId, collectTime }, new SqlCallBack() {
//				public void readerRows(ResultSet rs) throws Exception {
//					AppRating appRating = new AppRating();
//					int appid = rs.getInt("app_id");
//					String ratingAim = rs.getString("rating_aim");
//					double rating = rs.getDouble("rating");
//					String ratingDesc = rs.getString("rating_desc");
//					int ratingWeigth = rs.getInt("RATING_WEIGHT");
//
//					appRating.setAppId(appid);
//					appRating.setRating(rating);
//					appRating.setRatingDesc(ratingDesc);
//					appRating.setRatingAim(ratingAim);
//					appRating.setRatingWeigth(ratingWeigth);
//					list.add(appRating);
//				}
//			});
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//
//		return list;
//	}

	public List<RatingApp> findAllRatingApp() {
		String sql = "select i.*,a.app_name from ms_rating_indicator i,ms_monitor_app a where i.app_id=a.app_id and i.status=1";
		final Map<Integer, RatingApp> map = new HashMap<Integer, RatingApp>();

		try {
			this.query(sql, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					String appName = rs.getString("app_name");
					RatingApp app = map.get(appId);
					if (app == null) {
						app = new RatingApp();
						app.setAppId(appId);
						app.setAppName(appName);
						map.put(appId, app);
					}

					RatingIndicator indicator = new RatingIndicator();
					indicator.setAppId(appId);
					indicator.setId(rs.getInt("id"));
					indicator.setIndicatorKeyName(rs.getString("Indicator_key"));
					indicator.setIndicatorThresholdValue(rs
							.getString("Indicator_threshold_value"));
					indicator.setIndicatorWeight(rs.getInt("indicator_weight"));
					indicator.setKeyUnit(rs.getString("key_unit"));
					indicator.setKeyId(rs.getInt("key_id"));
					app.getIndicatorList().add(indicator);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		List<RatingApp> list = (new ArrayList<RatingApp>());
		list.addAll(map.values());
		return list;
	}

}
