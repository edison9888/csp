package com.taobao.csp.depend.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.tddl.ConsumeDbDetail;
import com.taobao.csp.depend.po.tddl.ConsumeTDDLDetail;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.csp.depend.util.SQLPreParser;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.common.util.page.Pagination;

public class CspTddlConsumeDao extends MysqlRouteBase {
	private static final Logger logger = Logger
			.getLogger(CspTddlConsumeDao.class);

	public CspTddlConsumeDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_tddl"));
	}

	/**
	 * Tddl��ѯ3��ҳ�� ��ѯ����DB������Ӧ�ö�Ӧ��sql���
	 * 
	 * @return
	 */
	public Pagination<ConsumeTDDLDetail> itemDetail(String appName,
			String dbName, String query1Type, String query1Value, int pageNo,
			int pageSize, String sortType, String searchsql) {
		Pagination<ConsumeTDDLDetail> page = null;
		List<Object> list = new ArrayList<Object>();
		String destSql = getItemDetailQuerySql(appName, dbName, query1Type,
				query1Value, list, sortType, searchsql);
		Object[] destParam = list.toArray();
		page = fillPage1(destSql, destParam, new SqlCallBackForItemDetailPo(),
				pageNo, pageSize);
		return page;
	}

	/**
	 * ����total executeSum and totalTimeAverage // Array is Long Float
	 */
	public void queryItemDetailTotal(final Long[] array, String appName,
			String dbName, String query1Type, String query1Value,
			String searchsql) throws Exception {
		List<Object> list = new ArrayList<Object>();
		String destSql = getItemDetailQuerySql(appName, dbName, query1Type,
				query1Value, list, null, searchsql);
		Object[] destParam = list.toArray();
		this.query(destSql, destParam, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				array[0] = array[0] + rs.getLong("execute_sum");
				// ����ȱʧ���ȣ�ÿһ�λ���SQL�����1����6.66->6,��������Ӧʱ�䰴С�㣬�����ӱ����Ը�һЩ��)
				array[1] = (long) (array[1] + rs.getFloat("time_average")
						* rs.getLong("execute_sum"));
			}
		});
	}

	private String getItemDetailQuerySql(String appName, String dbName,
			String query1Type, String query1Value, List<Object> list,
			String sortType, String searchsql) {
		Date hourStart = null, hourEnd = null;
		boolean solutionA = query1Type.equals("1");

		String tableName = null, dateCompPart = "= ?";
		Object[] destParam = null;

		// ���query1Type��ֵΪ2������query1Value
		if (!solutionA) {
			// ʹ��between and��������format date����Ч��
			Integer hours = Integer.parseInt(query1Value);
			Date now = new Date();
			hourStart = initAndClear(now.getTime(), hours);
			hourEnd = initAndClear(now.getTime(), hours + 1);
			tableName = "csp_app_consume_tddl_hour_temp" + (hours % 3);
		} else {
			tableName = getTableByTime(MethodUtil.getDate(query1Value));
		}

		if (!solutionA)
			dateCompPart = "between ? and ?";

		String sql1 = "SELECT sql_text, SUM(execute_sum) as execute_sum, SUM(time_average*execute_sum)/SUM(execute_sum) as time_average, SUM(time_average*execute_sum) as total_time_average FROM "
				+ tableName
				+ " WHERE  app_name=? AND db_name = ? and"
				+ " collect_time "
				+ dateCompPart
				+ " and sql_text LIKE  CONCAT('%', ? ,'%')";
		sql1 += " GROUP BY sql_text ";
		if (sortType != null) {
			if (sortType.equals("execNum")) {
				sql1 += " ORDER BY execute_sum DESC";
			} else if (sortType.equals("elaTime")) {
				sql1 += " ORDER BY time_average DESC";
			} else {// totalelaTime
				sql1 += " ORDER BY total_time_average DESC"; // ����ִ��ʱ������
			}
		}

		if (solutionA)
			destParam = appendParams(destParam, appName, dbName, query1Value,
					searchsql);
		else
			destParam = appendParams(destParam, appName, dbName, hourStart,
					hourEnd, searchsql);
		list.clear();
		for (Object obj : destParam) {
			list.add(obj);
		}
		return sql1;
	}

	/**
	 * ��ҳ��ѯ�б�֧�ַ�ҳ
	 * 
	 * @param type
	 *            Ӧ��ά�ȣ�����DBά��
	 * @param name
	 *            Ӧ������DB��
	 * @param day
	 *            ָ�����ڣ���ѯĳһ��ģ�day��Ϊnull
	 * @return
	 * 
	 */
	public Pagination<ConsumeTDDLDetail> list(int type, String name, Date day,
			int pageNo, int pageSize, String sortType) {
		logger.debug("---------list()");
		name = name.trim();
		// name = name.equals("") ? "%" : "%" + name + "%";
		boolean appType = type == 1;
		if (appType) {
			return listForAPP(name, day, pageNo, pageSize, sortType);
		} else {
			return listForDB(name, day, pageNo, pageSize, sortType);
		}
	}

	/*
	 * ��ҳ��ѯapp�б�����Ҷ����ͨ���ˣ���ѯ����Ĺ��ܿ���ȥ����ֻ��ѯ��ʷ���ݣ�
	 */
	public Pagination<ConsumeTDDLDetail> listForAPP(String name, Date day,
			int pageNo, int pageSize, String sortType) {
		String dayStr = MethodUtil.getStringOfDate(day);
		final List<ConsumeTDDLDetail> resultList = new ArrayList<ConsumeTDDLDetail>();
		String sqlSortType = "";
		if (sortType != null) {
			if (sortType.equals("execNum")) {
				sqlSortType = "execute_sum";
			} else if (sortType.equals("elaTime")) {
				sqlSortType = "time_average";
			} else {
				sqlSortType = "app_name";
			}
		} else {
			sqlSortType = "execute_sum";
		}

		String sql = "select app_name,sum(execute_sum) as execute_sum,sum(execute_sum*time_average)/sum(execute_sum) as time_average from csp_app_consume_tddl_summary_new where collect_time = ?";
		if (StringUtils.isNotBlank(name)) {
			sql += " and app_name = ?";
		}
		sql += " group by app_name order by " + sqlSortType + " desc limit ?,?";
		logger.info("listForAPP");
		logger.info("��listForAPP��ʷ->" + name + ";day=" + day + ";sortType="
				+ sortType + ";pageNo=" + pageNo + ";pageSize=" + pageSize);
		logger.info("sql->" + sql);

		Object param[] = null;
		if (StringUtils.isNotBlank(name)) {
			param = new Object[] { dayStr, name, (pageNo - 1) * pageSize,
					pageSize };
		} else {
			param = new Object[] { dayStr, (pageNo - 1) * pageSize, pageSize };
		}
		try {
			this.query(sql, param, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					String name = rs.getString("app_name");
					if (name != null) {
						ConsumeTDDLDetail po = new ConsumeTDDLDetail();
						po.setExecuteSum(rs.getLong("execute_sum"));
						po.setTimeAverage(rs.getFloat("time_average"));
						po.setName(rs.getString("app_name"));
						resultList.add(po);
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		final Pagination<ConsumeTDDLDetail> page = new Pagination<ConsumeTDDLDetail>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setList(resultList);
		page.setTotalCount(0);
		return page;
	}

	/*
	 * ��ҳ��ѯdb�б�����Ҷ����ͨ���ˣ���ѯ����Ĺ��ܿ���ȥ����ֻ��ѯ��ʷ���ݣ�
	 */
	public Pagination<ConsumeTDDLDetail> listForDB(String name, Date day,
			int pageNo, int pageSize, String sortType) {
		String dayStr = MethodUtil.getStringOfDate(day);
		final List<ConsumeTDDLDetail> resultList = new ArrayList<ConsumeTDDLDetail>();
		String sqlSortType = "";
		if (sortType != null) {
			if (sortType.equals("execNum")) {
				sqlSortType = "execute_sum";
			} else if (sortType.equals("elaTime")) {
				sqlSortType = "time_average";
			} else {
				sqlSortType = "db_name";
			}
		} else {
			sqlSortType = "execute_sum";
		}

		String sql = "SELECT db_name, db_ip, db_port, sum(execute_sum) as execute_sum,sum(execute_sum*time_average)/sum(execute_sum) as time_average from csp_app_consume_tddl_summary_new where collect_time = ?";
		if (StringUtils.isNotBlank(name)) {
			sql += " and db_name = ?";
		}
		sql += " group by db_name,db_ip,db_port order by " + sqlSortType
				+ " desc limit ?,?";

		logger.info("��listForDB��ʷ->" + name + ";day=" + day + ";sortType="
				+ sortType + ";pageNo=" + pageNo + ";pageSize=" + pageSize);
		logger.info("sql->" + sql);

		Object param[] = null;
		if (StringUtils.isNotBlank(name)) {
			param = new Object[] { dayStr, name, (pageNo - 1) * pageSize,
					pageSize };
		} else {
			param = new Object[] { dayStr, (pageNo - 1) * pageSize, pageSize };
		}
		try {
			this.query(sql, param, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					String name = rs.getString("db_name");
					if (name != null) {
						ConsumeTDDLDetail po = new ConsumeTDDLDetail();
						po.setExecuteSum(rs.getLong("execute_sum"));
						po.setTimeAverage(rs.getFloat("time_average"));
						po.setName(rs.getString("db_name"));
						po.setDbIp(rs.getString("db_ip"));
						po.setDbPort(rs.getString("db_port"));
						resultList.add(po);
					}
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		final Pagination<ConsumeTDDLDetail> page = new Pagination<ConsumeTDDLDetail>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setList(resultList);
		page.setTotalCount(0);
		return page;
	}

	// /** ��ȡ����00:00:00�ĺ���ֵ */
	// private long getTodayBegin() {
	// Calendar todayC = Calendar.getInstance();
	// todayC.set(Calendar.MILLISECOND, 0);
	// todayC.set(Calendar.SECOND, 0);
	// todayC.set(Calendar.MINUTE, 0);
	// todayC.set(Calendar.HOUR_OF_DAY, 0);
	// return todayC.getTimeInMillis();
	// }

	/**
	 * 1������db��ѯʱ��name��ip��port�������벻��ѯ Tddl ����ҳ���ѯ
	 */
	public Pagination<ConsumeTDDLDetail> query(String query1Type,
			String query1Value, String query2Type, String query3Type,
			String name, String ip, String port, int pageNo, int pageSize,
			String sortType) {

		Date hourStart = null, hourEnd = null;
		boolean solutionA = query1Type.equals("1");
		boolean solutionC = query2Type.equals("1");
		boolean solutionD = query3Type.equals("1");
		// getTableByTime()
		String tableName = null, dateCompPart = "= ?", col_1 = "db_name", col_2 = "app_name", queryPart1 = "", destSql = null;
		Object[] destParam = null;

		String orderByStr = "";
		// ���query1Type��ֵΪ2������query1Value
		if (!solutionA) {
			// ʹ��between and��������format date����Ч��
			Integer hours = Integer.parseInt(query1Value);
			Date now = new Date();
			hourStart = initAndClear(now.getTime(), hours);
			hourEnd = initAndClear(now.getTime(), hours + 1);
			tableName = "csp_app_consume_tddl_hour_temp" + (hours % 3);
		} else {
			tableName = getTableByTime(MethodUtil.getDate(query1Value));
		}
		if (sortType != null) {
			if (sortType.equals("execNum")) {
				orderByStr += " ORDER BY execute_sum DESC";
			} else {
				orderByStr += " ORDER BY time_average DESC";
			}
		}

		if (!solutionA)
			dateCompPart = "between ? and ?";

		if (!solutionC) {
			col_1 = "app_name";
			col_2 = "db_name";
		}
		// �Ƿ����and
		boolean needAnd = false;
		if (!name.equals("")) {
			destParam = appendParams(destParam, name);
			queryPart1 = col_2 + " = ? ";
			needAnd = true;
		}

		if (!solutionC) {
			if (!ip.equals("")) {
				destParam = appendParams(destParam, ip);
				queryPart1 = queryPart1 + appendAnd(needAnd, " db_ip = ? ");
				needAnd = true;
			}
			if (!port.equals("")) {
				destParam = appendParams(destParam, port);
				queryPart1 = queryPart1 + appendAnd(needAnd, " db_port = ? ");
				needAnd = true;
			}
		}
		if (needAnd)
			queryPart1 = queryPart1 + " and ";

		StringBuilder sql1Buf = new StringBuilder();
		sql1Buf.append("SELECT ");
		sql1Buf.append(" " + col_1);
		sql1Buf.append(" name,  SUM(execute_sum) execute_sum , MAX(CONVERT(SUBSTRING_INDEX(max_time,'#',1), UNSIGNED)) max_time");
		sql1Buf.append(" , MIN(CONVERT(SUBSTRING_INDEX(min_time,'#',1), UNSIGNED)) min_time,");
		sql1Buf.append(" SUM(time_average * execute_sum)/SUM(execute_sum) time_average");
		sql1Buf.append(" FROM " + tableName + " WHERE " + queryPart1);
		sql1Buf.append(" collect_time " + dateCompPart);
		// -- ��ΪҪ�� time_average * execute_sum���г˷�������Ҫȷ��time_average��Ϊ0
		sql1Buf.append(" AND time_average != 0 AND execute_sum != 0"
				+ " GROUP BY ");
		sql1Buf.append(col_1 + orderByStr);
		String sql1 = sql1Buf.toString();

		// ��SQL��ѯ
		StringBuilder sql2Buf = new StringBuilder();
		sql2Buf.append("SELECT sum(execute_sum)as execute_sum,sum(execute_sum*time_average)/sum(execute_sum) as time_average, sql_text,max(max_time) as max_time ,min(min_time) as min_time FROM "
				+ tableName);
		sql2Buf.append(" WHERE   " + queryPart1 + " collect_time "
				+ dateCompPart);
		sql2Buf.append(" group by sql_text");
		sql2Buf.append(orderByStr);

		String sql2 = sql2Buf.toString();
		if (solutionA)
			destParam = appendParams(destParam, query1Value);
		else
			destParam = appendParams(destParam, hourStart, hourEnd);

		Pagination<ConsumeTDDLDetail> page = null;
		if (solutionD) {
			destSql = sql1;
			page = fillPage1(destSql, destParam, new SqlCallBackForGroupPo(),
					pageNo, pageSize);
		} else {
			destSql = sql2;
			page = fillPage1(destSql, destParam, new SqlCallBackForTDDLPo(),
					pageNo, pageSize);
		}
		return page;
	}

	/**
	 * @author wb-lixing 2012-4-19 ����04:18:28
	 */
	private String appendAnd(boolean needAnd, String sql) {
		String dest = "";
		if (needAnd)
			dest = dest + " and ";
		dest += sql;
		return dest;
	}

	public Date initAndClear(long time, int hours) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		c.set(Calendar.HOUR_OF_DAY, hours);

		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		return c.getTime();
	}

	// <T> just for setList
	abstract class PageSqlCallBack<T> implements SqlCallBack {
		public void setList(List<T> poList) {
			this.poList = poList;
		}

		protected List<T> poList;
	}

	// Ӧ��(��DB)�б�ҳ��ʹ��
	class SqlCallBackForItemDetailPo extends PageSqlCallBack<ConsumeTDDLDetail> {
		public void readerRows(ResultSet rs) throws Exception {
			ConsumeTDDLDetail po = new ConsumeTDDLDetail();
			po.setExecuteSum(rs.getLong("execute_sum"));
			po.setTimeAverage(rs.getFloat("time_average"));
			po.setSqlText(rs.getString("sql_text"));
			poList.add(po);
		}
	}

	// Ӧ��(��DB)�б�ҳ��ʹ��
	// ͳ�ƽ��ʹ��һ��SqlCallBack
	class SqlCallBackForListPo extends PageSqlCallBack<ConsumeTDDLDetail> {
		private boolean appType;

		public void setAppType(boolean appType) {
			this.appType = appType;
		}

		public void readerRows(ResultSet rs) throws Exception {
			ConsumeTDDLDetail po = new ConsumeTDDLDetail();
			po.setExecuteSum(rs.getLong("execute_sum"));
			po.setTimeAverage(rs.getFloat("time_average"));
			po.setName(rs.getString("name"));
			if (!appType) {
				po.setDbIp(rs.getString("db_ip"));
				po.setDbPort(rs.getString("db_port"));
			}
			poList.add(po);
		}
	}

	// ͳ�ƽ��ʹ��һ��SqlCallBack
	class SqlCallBackForGroupPo extends PageSqlCallBack<ConsumeTDDLDetail> {
		public void readerRows(ResultSet rs) throws Exception {
			ConsumeTDDLDetail po = new ConsumeTDDLDetail();

			fillTDDLGroupPo(rs, po);
			poList.add(po);
		}
	}

	// ֱ����ʾsql_text��ʹ��һ��SqlCallBack
	class SqlCallBackForTDDLPo extends PageSqlCallBack<ConsumeTDDLDetail> {
		public void readerRows(ResultSet rs) throws Exception {
			ConsumeTDDLDetail po = new ConsumeTDDLDetail();
			po.setSqlText(rs.getString("sql_text"));
			po.setExecuteSum(rs.getLong("execute_sum"));
			po.setTimeAverage(rs.getFloat("time_average"));
			po.setMaxTime(rs.getString("max_time"));
			po.setMinTime(rs.getString("min_time"));
			po.setName(rs.getString("sql_text"));
			poList.add(po);
		}
	}

	/** ���Ϊsql_text */
	public <T> Pagination<T> fillPage1(String oriSql, Object[] params,
			PageSqlCallBack<T> pageSqlCallBack, int pageNo, int pageSize) {
		final Pagination<T> page = new Pagination<T>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		final List<T> poList = new ArrayList<T>();
		page.setList(poList);
		page.setTotalCount(0);
		String sql = oriSql + " limit ?, ?";
		try {
			params = appendParams(params, (pageNo - 1) * pageSize, pageSize);
			pageSqlCallBack.setList(poList);
			logger.info("-----sql: " + sql);
			logger.info("-----params: " + Arrays.toString(params));
			this.query(sql, params, pageSqlCallBack);
			// System.out.println("----------------sql: " + sql);
		} catch (Exception e) {
			logger.error("", e);
		}
		page.setList(poList);
		return page;
	}

	/** ��Object[]��׷�Ӳ��� */
	public Object[] appendParams(Object[] params, Object... appendParams) {
		if (params == null)
			params = new Object[0];
		Object[] destParams = new Object[params.length + appendParams.length];
		for (int i = 0; i < params.length; i++) {
			destParams[i] = params[i];
		}
		for (int i = 0; i < appendParams.length; i++) {
			destParams[params.length + i] = appendParams[i];
		}
		return destParams;
	}

	/**
	 * @author wb-lixing 2012-4-18 ����03:49:07
	 * @param rs
	 * @param po
	 * @throws SQLException
	 */
	private void fillTDDLGroupPo(ResultSet rs, ConsumeTDDLDetail po)
			throws SQLException {
		po.setExecuteSum(rs.getLong("execute_sum"));
		po.setTimeAverage(rs.getFloat("time_average"));
		po.setMaxTime(rs.getString("max_time"));
		po.setMinTime(rs.getString("min_time"));
		po.setName(rs.getString("name"));

	}

	// public void fillConsumeTDDLDetail(ResultSet rs, ConsumeTDDLDetail po)
	// throws SQLException {
	// po.setAppName(rs.getString("app_name"));
	// po.setDbName(rs.getString("db_name"));
	// po.setSqlText(rs.getString("sql_text"));
	// po.setExecuteType(rs.getString("execute_type"));
	// po.setExecuteSum(rs.getLong("execute_sum"));
	// po.setTimeAverage(rs.getFloat("time_average"));
	// po.setMaxTime(rs.getString("max_time"));
	// po.setMinTime(rs.getString("min_time"));
	// po.setAppHostIp(rs.getString("app_host_ip"));
	// po.setAppSiteName(rs.getString("app_site_name"));
	// po.setCollect_time(new Date(rs.getTimestamp("collect_time").getTime()));
	// po.setName(rs.getString("name"));
	// }

	public List<ConsumeDbDetail> findTddlConsumeDetail(String opsName,
			String selectDate) {

		String sql = "select * from csp_app_consume_db_detail where app_name=? and collect_time=?";

		final List<ConsumeDbDetail> poList = new ArrayList<ConsumeDbDetail>();

		try {
			this.query(sql, new Object[] { opsName, selectDate },
					new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					ConsumeDbDetail po = new ConsumeDbDetail();
					po.setAppName(rs.getString("app_name"));
					po.setDbName(rs.getString("db_name"));
					po.setSqlText(rs.getString("sql_text"));
					po.setExecuteType(rs.getString("execute_type"));
					po.setExecuteSum(rs.getLong("execute_sum"));
					po.setTimeAverage(rs.getFloat("time_average"));
					po.setAppHostIp(rs.getString("app_host_ip"));
					po.setAppSiteName(rs.getString("app_site_name"));
					po.setCollect_time(new Date(rs.getTimestamp(
							"collect_time").getTime()));

					if (!poList.contains(po)) {
						poList.add(po);
					}
				}
			});
		} catch (Exception e) {
			logger.error("findTddlConsumeDetail:", e);
		}
		return poList;
	}

	public List<ConsumeDbDetail> findTddlConsumeDetailMachine(String opsName,
			String selectDate, String executeType, String sqlText) {

		String sql = "select * from csp_app_consume_db_detail where app_name=? and collect_time=? and execute_type=? and sql_text=?";

		final List<ConsumeDbDetail> poList = new ArrayList<ConsumeDbDetail>();

		try {
			this.query(sql, new Object[] { opsName, selectDate, executeType,
					sqlText }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					ConsumeDbDetail po = new ConsumeDbDetail();
					po.setAppName(rs.getString("app_name"));
					po.setDbName(rs.getString("db_name"));
					po.setSqlText(rs.getString("sql_text"));
					po.setExecuteType(rs.getString("execute_type"));
					po.setExecuteSum(rs.getLong("execute_sum"));
					po.setTimeAverage(rs.getFloat("time_average"));
					po.setAppHostIp(rs.getString("app_host_ip"));
					po.setAppSiteName(rs.getString("app_site_name"));
					po.setCollect_time(new Date(rs.getTimestamp("collect_time")
							.getTime()));

					if (!poList.contains(po)) {
						poList.add(po);
					}
				}
			});
		} catch (Exception e) {
			logger.error("findTddlConsumeDetail:", e);
		}
		return poList;
	}

	/**
	 * time, info
	 */
	public List<ConsumeTDDLDetail> getRecentlySqlInfo(final String sql_text,
			String appName, String dbName) {
		Date todayBeginDate = Utlitites.getBeginOfTheDay(new Date());
		// <time, ConsumeTDDLDetail>
		final Map<String, ConsumeTDDLDetail> map = new HashMap<String, ConsumeTDDLDetail>();
		for (int i = 0; i < 3; i++) {
			StringBuilder sb = new StringBuilder(
					"SELECT collect_time, SUM(execute_sum) as execute_sum, SUM(time_average*execute_sum) as total_time_average ");
			sb.append(" FROM csp_app_consume_tddl_hour_temp").append(i);
			sb.append(" where  collect_time > ? and app_name=? AND db_name = ? and sql_text = ? GROUP BY collect_time");
			Object[] params = new Object[] { todayBeginDate, appName, dbName,
					sql_text };
			try {
				this.query(sb.toString(), params, new SqlCallBack() {
					public void readerRows(ResultSet rs) throws Exception {
						ConsumeTDDLDetail po = map.get(rs
								.getString("collect_time"));
						if (po == null) {
							po = new ConsumeTDDLDetail();
							map.put(rs.getString("collect_time"), po);
							po.setExecuteSum(rs.getLong("execute_sum"));
							po.setTimeAverage(rs.getFloat("total_time_average"));
							po.setSqlText(sql_text);
							po.setCollect_time(rs.getDate("collect_time"));
							po.setCollect_time_str(rs.getString("collect_time"));

						} else {
							po.setExecuteSum(rs.getLong("execute_sum")
									+ po.getExecuteSum());
							po.setTimeAverage(rs.getFloat("total_time_average")
									+ po.getTimeAverage());
							po.setSqlText(sql_text);
							po.setCollect_time(rs.getDate("collect_time"));
							po.setCollect_time_str(rs.getString("collect_time"));
						}
					}
				});
			} catch (Exception e) {
				logger.error("getRecentlySqlInfo:", e);
			}
		}
		List<ConsumeTDDLDetail> list = new ArrayList<ConsumeTDDLDetail>(
				map.values());
		for (ConsumeTDDLDetail po : list) { // ����һ��ƽ��ʱ��
			po.setTimeAverage(po.getTimeAverage() / po.getExecuteSum());
		}

		Collections.sort(list, new Comparator<ConsumeTDDLDetail>() {
			@Override
			public int compare(ConsumeTDDLDetail o1, ConsumeTDDLDetail o2) {
				return o1.getCollect_time_str().compareTo(
						o2.getCollect_time_str());
			}
		});
		return list;
	}

	private String getTableByTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String table = "csp_app_consume_tddl_detail_"
				+ String.format("%03d", cal.get(Calendar.DAY_OF_YEAR));
		logger.info(table);
		return table;
	}

	public List<ConsumeTDDLDetail> getHistoryGraphData(String startDate,
			String endDate, String name, String type) {
		String sql = "";
		if (type == null || "1".equals(type)) {
			sql = "select app_name as name,sum(execute_sum) as execute_sum,sum(execute_sum*time_average)/sum(execute_sum) as time_average,collect_time from csp_app_consume_tddl_summary_new where collect_time between ? and ? and app_name = ? group by collect_time";
		} else {
			sql = "select db_name as name,sum(execute_sum) as execute_sum,sum(execute_sum*time_average)/sum(execute_sum) as time_average,collect_time from csp_app_consume_tddl_summary_new where collect_time between ? and ? and db_name = ? group by collect_time";
		}
		logger.info("getHistoryGraphData sql ->" + sql);
		logger.info(String.format(
				"param -> startDate=%s,endDate=%s,name=%s,type=%s", startDate,
				endDate, name, type));

		final List<ConsumeTDDLDetail> list = new ArrayList<ConsumeTDDLDetail>();
		try {
			this.query(sql, new Object[] { startDate, endDate, name },
					new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					ConsumeTDDLDetail data = new ConsumeTDDLDetail();
					data.setName(rs.getString("name"));
					data.setCollect_time_str(rs
							.getString("collect_time"));
					data.setCollect_time(new Date(rs.getTimestamp(
							"collect_time").getTime()));
					data.setExecuteSum(rs.getLong("execute_sum"));
					data.setTimeAverage(rs.getFloat("time_average"));
					list.add(data);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}

	/**
	 * @author ���� 2012-9-6 ����3:29:46
	 * 
	 *         ����:tddl sqltext �ֱ�ϲ���������
	 */

	public List<ConsumeTDDLDetail> tableMergeList(String appName,
			String dbName, String selectDate) {
		List<ConsumeTDDLDetail> list = new ArrayList<ConsumeTDDLDetail>();;
		// ����appName,dbName,dayȥ��ѯsqltext��¼�б�

		String tableName = getTableByTime(MethodUtil.getDate(selectDate));

		String sql = "select * from " + tableName
				+ " where app_name=? and db_name=? and collect_time=?";
		logger.info("sql->" + sql);
		logger.info(String.format("appName=%s,dbName=%s,selectDate=%s",appName,dbName, selectDate));

		final List<ConsumeTDDLDetail> poList = new ArrayList<ConsumeTDDLDetail>();

		try {
			this.query(sql, new Object[] { appName, dbName, selectDate },
					new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					ConsumeTDDLDetail po = new ConsumeTDDLDetail();
					po.setSqlText(rs.getString("sql_text"));
					po.setExecuteSum(rs.getLong("execute_sum"));
					po.setTimeAverage(rs.getFloat("time_average"));
					poList.add(po);
				}
			});

			list = analysisAndMerge(poList);

		} catch (Exception e) {
			logger.error("tableMergeList:", e);
		}
		return list;
	}

	/**
	 * @author ���� 2012-9-7 ����10:02:53
	 * @param poList
	 *            �����������ϲ��ֱ���ͬ��sql
	 */

	private List<ConsumeTDDLDetail> analysisAndMerge(
			List<ConsumeTDDLDetail> poList) {
		// Map keyΪtableName�� value
		// ConsumeTDDLDetail���ͣ�����"ͬ�����ĵ�һ��sql_text��,�ۼ�ִ�������ۼ���ʱ��
		Map<String, ConsumeTDDLDetail> result = new HashMap<String, ConsumeTDDLDetail>();
		for (ConsumeTDDLDetail po : poList) {
			try {
				String sqlText = po.getSqlText();
				String tableName = SQLPreParser.findTableName(sqlText);
				String thinTableName = tableName.replaceFirst("_\\d+\\b", "");
				//ȥ�������е�"_\d+"��sql
				String thinSql = sqlText.replaceAll(tableName, thinTableName);
				ConsumeTDDLDetail sameTableSql = result.get(thinSql);
				if (sameTableSql == null) {
					sameTableSql = new ConsumeTDDLDetail();
					result.put(thinSql, sameTableSql);
					sameTableSql.setSqlText(thinSql);
				}
				// ���㵥��sql����ʱ��,Ȼ���ۼ�
				sameTableSql.setTotalTime(po.getTimeAverage()
						* po.getExecuteSum() + sameTableSql.getTotalTime());
				// �ۼӴ���
				sameTableSql.setExecuteSum(po.getExecuteSum()
						+ sameTableSql.getExecuteSum());
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		// ����ƽ��ʱ��
		Collection<ConsumeTDDLDetail> mergedCol = result.values();
		for (Iterator<ConsumeTDDLDetail> iter = mergedCol.iterator(); iter
				.hasNext();) {
			ConsumeTDDLDetail sameTableSql = iter.next();
			float averTime = 0;
			long exeSum = sameTableSql.getExecuteSum();
			if (exeSum != 0) {
				averTime = sameTableSql.getTotalTime() / exeSum;
				// ����3λ����
				BigDecimal bd = new BigDecimal(averTime);
				bd = bd.setScale(3, RoundingMode.HALF_EVEN);
				averTime = bd.floatValue();
			}
			sameTableSql.setTimeAverage(averTime);

			// ��totalTime�ľ�������Ϊ3
			BigDecimal bd = new BigDecimal(sameTableSql.getTotalTime());
			bd = bd.setScale(3, RoundingMode.HALF_EVEN);
			sameTableSql.setTotalTime(bd.floatValue());
		}
		List<ConsumeTDDLDetail> list = new ArrayList<ConsumeTDDLDetail>();
		list.addAll(mergedCol);
		// ��������������
		Collections.sort(list, new Comparator<ConsumeTDDLDetail>() {
			@Override
			public int compare(ConsumeTDDLDetail o1, ConsumeTDDLDetail o2) {
				return (int) ((int) o2.getExecuteSum() - o1.getExecuteSum());
			}
		});
		return list;
	}

	/**
	 * @author ���� 2012-9-7 ����3:58:01
	 * @param appName
	 * @param dbName
	 * @param selectDate
	 * @return
	 */

	public List<ConsumeTDDLDetail> readWriteRate(String appName, String dbName,
			String selectDate) {
		List<ConsumeTDDLDetail> list = new ArrayList<ConsumeTDDLDetail>();
		String tableName = getTableByTime(MethodUtil.getDate(selectDate));

		String sql = "select * from " + tableName
				+ " where app_name=? and db_name=? and collect_time=?";
		logger.info("sql->" + sql);
		logger.info(String.format("appName=%s,dbName=%s,selectDate=%s",appName,dbName, selectDate));

		final List<ConsumeTDDLDetail> poList = new ArrayList<ConsumeTDDLDetail>();

		try {
			this.query(sql, new Object[] { appName, dbName, selectDate },
					new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					ConsumeTDDLDetail po = new ConsumeTDDLDetail();
					po.setSqlText(rs.getString("sql_text"));
					po.setExecuteSum(rs.getLong("execute_sum"));
					po.setTimeAverage(rs.getFloat("time_average"));
					poList.add(po);
				}
			});

			list = readWriteRateAnalysis(poList);

		} catch (Exception e) {
			logger.error("tableMergeList:", e);
		}
		return list;
	}

	/**
	 * @author ���� 2012-9-7 ����3:59:41
	 * 
	 *         ��������Ķ�������д��������д����
	 * @param poList
	 * @return
	 */

	private List<ConsumeTDDLDetail> readWriteRateAnalysis(
			List<ConsumeTDDLDetail> poList) {
		// Map keyΪtableName�� value
		// ConsumeTDDLDetail���ͣ��ۼӶ��������ۼ�д����,�ۼ�ִ�������ۼ���ʱ��
		Map<String, ConsumeTDDLDetail> result = new HashMap<String, ConsumeTDDLDetail>();
		for (ConsumeTDDLDetail po : poList) {
			String sqlText = po.getSqlText();
			if (!isValid(sqlText)) {
				continue;
			}
			boolean isRead = isRead(sqlText);
			String tableName = SQLPreParser.findTableName(sqlText);
			if(tableName == null)
				continue;
			ConsumeTDDLDetail sameTableSql = result.get(tableName);
			if (sameTableSql == null) {
				sameTableSql = new ConsumeTDDLDetail();
				sameTableSql.setTableName(tableName);
				result.put(tableName, sameTableSql);
			}
			//�ֱ��ۼӶ���д����
			if(isRead)
				sameTableSql.setReadCount(po.getExecuteSum() + sameTableSql.getReadCount());
			else
				sameTableSql.setWriteCount(po.getExecuteSum()+ sameTableSql.getWriteCount());

			//�ۼ�ִ����
			sameTableSql.setExecuteSum(po.getExecuteSum() + sameTableSql.getExecuteSum());

			// ���㵥��sql����ʱ��,Ȼ���ۼ�
			sameTableSql.setTotalTime(po.getTimeAverage() * po.getExecuteSum()
					+ sameTableSql.getTotalTime());
		}

		// ����ƽ��ʱ��,�����д����
		Collection<ConsumeTDDLDetail> mergedCol = result.values();
		for (Iterator<ConsumeTDDLDetail> iter = mergedCol.iterator(); iter
				.hasNext();) {
			ConsumeTDDLDetail sameTableSql = iter.next();
			//����ƽ��ʱ��
			float averTime = 0;
			long exeSum = sameTableSql.getExecuteSum();
			if (exeSum != 0) {
				averTime = sameTableSql.getTotalTime() / exeSum;
				// ����3λ����
				BigDecimal bd = new BigDecimal(averTime);
				bd = bd.setScale(3, RoundingMode.HALF_EVEN);
				averTime = bd.floatValue();
			}
			sameTableSql.setTimeAverage(averTime);
			//�����д����
			long r = sameTableSql.getReadCount();
			long w = sameTableSql.getWriteCount();
			String rateStr = "";
			if(w==0){
				rateStr = r +" : 0";
			} else if(r==0){
				rateStr = "0 " +": "+ w;
			}else {
				float rate = r*1f /w; 
				BigDecimal bd = new BigDecimal(rate);
				bd = bd.setScale(1, RoundingMode.HALF_EVEN);
				rate = bd.floatValue();
				rateStr = String.valueOf(rate);
				//�滻ĩβ��".0"��ǿ�ɶ���
				rateStr = rateStr.replaceAll("\\.0\\b", "");
				rateStr = rateStr + " : 1";
			}
			sameTableSql.setReadWriteRate(rateStr);


			// ��totalTime�ľ�������Ϊ3
			BigDecimal bd = new BigDecimal(sameTableSql.getTotalTime());
			bd = bd.setScale(3, RoundingMode.HALF_EVEN);
			sameTableSql.setTotalTime(bd.floatValue());
		}

		List<ConsumeTDDLDetail> list = new ArrayList<ConsumeTDDLDetail>();
		list.addAll(mergedCol);
		// ��������������
		Collections.sort(list, new Comparator<ConsumeTDDLDetail>() {
			@Override
			public int compare(ConsumeTDDLDetail o1, ConsumeTDDLDetail o2) {
				return (int) ((int) o2.getExecuteSum() - o1.getExecuteSum());
			}
		});
		return list;
	}

	/**
	 * @author ���� 2012-9-7 ����4:20:27
	 * 
	 *         �ж�SQL�Ƿ���Ч��
	 * @param sqlText
	 * @return
	 */

	private boolean isValid(String sql) {

		if (sql == null)
			return false;
		sql = sql.trim(); // trim����ȥ��\\s,�������з����Ʊ����
		if (sql.length() < 7) {
			return false;
		}
		return true;
	}

	/** �ж�һ��SQL�ǲ��ǡ�read sql�� */
	boolean isRead(String sql) {
		sql = sql.trim();
		sql = sql.toLowerCase();

		if (sql.startsWith("select")) 
			return true;
		return false;

	}
}
