
package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.vo.WebWWData;

/**
 * 
 * @author xiaodu
 * @version 2010-9-16 下午03:42:47
 */
public class MonitorWebWwDao extends MysqlRouteBase{
	
	private static final Logger logger = Logger.getLogger(MonitorWebWwDao.class);
	
	/**
	 * 获取某天某时刻在线人数
	 * 
	 * @param time
	 * @return
	 */
	public WebWWData getWebWWOnlineNumberByTime(final String time) {
		final WebWWData result = new WebWWData();
		result.setDate(time);
		String tableTime = time.replaceAll("-", "").substring(0, 8);
		String sql = "select m_data from MS_MONITOR_DATA_" + tableTime
				+ " where app_id=? and key_id=? and site_id=? and collect_time=?";

		try {
			this.query(sql, new Object[] { "14", "5551", "42", time }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					result.setNumber(rs.getLong("m_data"));
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(14));
			return result;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	/**
	 * 获取历史同一时刻最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public WebWWData getWebWWOnlineHistoryNumberByTime(final String time, final String type) {
		final WebWWData result = new WebWWData();
		result.setNumber(-1);
		String sql = "select webww_number,webww_date from webww_online_number where webww_type=? and webww_time=?";

		try {
			this.query(sql, new Object[] { type, time }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					result.setNumber(rs.getLong("webww_number"));
					result.setDate(rs.getString("webww_date"));
				}
			});
			return result;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	/**
	 * 更新历史同一时刻最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public boolean updateWebWWOnlineHistoryNumberByTime(final String time, final String type, final long number,
			final String date) {

		String sql = "update webww_online_number set webww_number= ?,webww_date=? where webww_type=? and webww_time=?";
		try {
			this.execute(sql, new Object[] { number, date, type, time });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 插入历史同一时刻最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public boolean insertWebWWOnlineHistoryNumberByTime(final String time, final String type, final long number,
			final String date) {
		String sql = "insert into webww_online_number (webww_number,webww_type,webww_time,webww_date)values(?,?,?,?)";
		try {
			this.execute(sql, new Object[] { number, type, time, date });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 获取某天最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public WebWWData getWebWWOnlineMaxNumberByDay(final String type, final String day) {
		final WebWWData result = new WebWWData();
		result.setNumber(-1);
		result.setDate(day);
		String sql = "select webww_number,webww_date from webww_online_number where webww_type=? and webww_day=?";

		try {
			this.query(sql, new Object[] { type, day }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					result.setNumber(rs.getLong("webww_number"));
					result.setDate(rs.getString("webww_date"));
				}
			});
			return result;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	/**
	 * 更新某天最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public boolean updateWebWWOnlineMaxNumberByDay(final String type, final String time, final String day,
			final long number, final String date) {
		String sql = "update webww_online_number set webww_number= ?,webww_time=?,webww_date=? where webww_type=? and webww_day=?";
		try {
			this.execute(sql, new Object[] { number, time, date, type, day });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 插入某天最高在线人数
	 * 
	 * @param type
	 * @param day
	 * @param number
	 * @param date
	 * @return
	 */
	public boolean insertWebWWOnlineMaxNumberByDay(final String type, final String time, final String day,
			final long number, final String date, final String des, final String commet) {
		String sql = "insert into webww_online_number (webww_number,webww_type,webww_time,webww_date,webww_day,webww_des,webww_commet)values(?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[] { number, type, time, date, day, des, commet });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 获取历史最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public WebWWData getWebWWOnlineHistoryMaxNumber(final String type) {
		final WebWWData result = new WebWWData();
		result.setNumber(-1);
		String sql = "select webww_number,webww_time,webww_day,webww_des from webww_online_number where webww_type=?";

		try {
			this.query(sql, new Object[] { type }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					result.setNumber(rs.getLong("webww_number"));
					result.setDate(rs.getString("webww_day") + rs.getString("webww_des") + rs.getString("webww_time"));
				}
			});
			return result;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	/**
	 * 更新历史最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public boolean updateWebWWOnlineHistoryMaxNumber(final String type, final String time, final String date,
			final String day, final long number, final String des) {

		String sql = "update webww_online_number set webww_number= ?,webww_time=?,webww_date=?,webww_day=?, webww_des=? where webww_type=?";
		try {
			this.execute(sql, new Object[] { number, time, date, day, des, type });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 插入历史最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public boolean insertWebWWOnlineHistoryMaxNumber(final String type, final String time, final String date,
			final String day, final long number, final String des, final String commet) {
		String sql = "insert into webww_online_number (webww_number,webww_type,webww_time,webww_date,webww_day,webww_des,webww_commet)values(?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[] { number, type, time, date, day, des, commet });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 获取历史同一天最高在线
	 * 
	 * @return
	 */
	public WebWWData getWebWWOnlineHistoryWeekMaxNumber(final String type, final String des) {
		final WebWWData result = new WebWWData();
		result.setNumber(-1);
		String sql = "select webww_number,webww_date from webww_online_number where webww_type=? and webww_des=?";

		try {
			this.query(sql, new Object[] { type, des }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					result.setNumber(rs.getLong("webww_number"));
					result.setDate(rs.getString("webww_date"));
				}
			});
			return result;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	/**
	 * 更新历史同一天最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public boolean updateWebWWOnlineHistoryWeekMaxNumber(final String type, final String time, final String date,
			final String day, final long number, final String des) {

		String sql = "update webww_online_number set webww_number= ?,webww_time=?,webww_date=?,webww_day=? where webww_type=? and webww_des=?";
		try {
			this.execute(sql, new Object[] { number, time, date, day, type, des });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 插入历史同一天最高在线人数
	 * 
	 * @param time
	 * @return
	 */
	public boolean insertWebWWOnlineHistoryWeekMaxNumber(final String type, final String time, final String date,
			final String day, final long number, final String des, final String commet) {
		String sql = "insert into webww_online_number (webww_number,webww_type,webww_time,webww_date,webww_day,webww_des,webww_commet)values(?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[] { number, type, time, date, day, des, commet });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 获取最近两周最高在线人数获取最近两周最高在线人数
	 * 
	 * @param type
	 * @return
	 */
	public LinkedList<WebWWData> findWebWangWangMaxOnlineNumberOfTwoWeeks(String type) throws Exception {
		final LinkedList<WebWWData> values = new LinkedList<WebWWData>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this
				.query(
						"select webww_number,webww_day,webww_time,webww_des from webww_online_number where webww_type = ? and webww_day < ? order by webww_id desc limit 14",
						new Object[] { type, format.format(new Date()) }, new SqlCallBack() {
							
							public void readerRows(ResultSet rs) throws Exception {
								WebWWData result = new WebWWData();
								result.setNumber(rs.getLong("webww_number"));
								result.setDate(rs.getString("webww_day") + rs.getString("webww_des")
										+ rs.getString("webww_time"));
								values.add(result);
							}
						});
		return values;
	}

	public WebWWData getWebWWOnlineNumber(int type, Date date) throws Exception {
		final WebWWData result = new WebWWData();
		String sql = "select webww_number as webww_number,webww_time from webww_online_number where webww_id =(select max(webww_id) as webww_id from webww_online_number where webww_type=?)";

		this.query(sql, new Object[] { type }, new SqlCallBack() {
			
			public void readerRows(ResultSet rs) throws Exception {
				result.setNumber(rs.getLong("webww_number"));
				result.setDate(rs.getTimestamp("webww_time").toString());
			}
		});
		return result;
	}

	public void setWebWWOnlineNumber(int type, long number, Date date) throws SQLException {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		long id = 0;
		if (type == 1 || type == 2) {
			id = this
					.getLongValue("select webww_id from webww_online_number where webww_type=?", new Object[] { type });
		} else {
			id = this.getLongValue("select webww_id from webww_online_number where webww_time>? and webww_type=?",
					new Object[] { now.getTime(), type });
		}
		if (id > 0) {
			this.execute("update webww_online_number set webww_type=?,webww_number=?,webww_time=? where webww_id = ?",
					new Object[] { type, number, date, id });
		} else {
			this.execute("insert into webww_online_number (webww_type,webww_number,webww_time)values(?,?,?)",
					new Object[] { type, number, date });
		}
	}

	// monitor_time_时间表

	/**
	 * 获取最近一分钟在线人数
	 */
	public LinkedList<WebWWData> findWebWangWangOnlineNumber(long appId, long keyId, Date now) throws Exception {
		final LinkedList<WebWWData> values = new LinkedList<WebWWData>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		this.query("select m_data,collect_time from ms_monitor_data_" + format.format(now)
				+ " where app_id = ? and key_id=? and collect_time < ? order by collect_time desc limit 12", new Object[] { appId,
				keyId, now }, new SqlCallBack() {
			
			public void readerRows(ResultSet rs) throws Exception {
				WebWWData result = new WebWWData();
				result.setNumber(rs.getLong("m_data"));
				String date = rs.getTimestamp("collect_time").toString();
				result.setDate(date.substring(0, date.length() - 2));
				values.add(result);
			}
		}, DbRouteManage.get().getDbRouteByTimeAppid((int)appId));
		return values;
	}

	/**
	 * 获取某一时刻某应用的在线人数
	 * @param appId
	 * @param keyId
	 * @param siteId
	 * @param time
	 * @return
	 */
	public WebWWData getNumberByTimeAndIds(long appId, long keyId, long siteId,
			String time) {
		final WebWWData result = new WebWWData();
		result.setDate(time);
		String tableTime = time.replaceAll("-", "").substring(0, 8);
		String sql = "select m_data from MS_MONITOR_DATA_" + tableTime
				+ " where app_id=? and key_id=? and site_id=? and collect_time=?";

		try {
			this.query(sql, new Object[] { appId, keyId, siteId, time }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					result.setNumber(rs.getLong("m_data"));
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(14));
			return result;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

}
