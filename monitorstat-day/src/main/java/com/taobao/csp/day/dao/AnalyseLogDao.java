package com.taobao.csp.day.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.taobao.csp.day.config.DatabaseInfo;
import com.taobao.csp.day.po.ApacheSpecialPo;
import com.taobao.csp.day.po.SphPo;
import com.taobao.csp.day.po.TddlPo;
import com.taobao.csp.day.po.TddlSummaryPo;
import com.taobao.csp.day.po.TdodPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.util.TableNameConverUtil;

/***
 * 日报数据dao
 * @author youji.zj
 * 
 * @version 2012-09-06
 *
 */
public class AnalyseLogDao extends MysqlRouteBase {
	
	private static  Logger log = Logger.getLogger(AnalyseLogDao.class);
		
	public AnalyseLogDao() {
		super(DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN_DAY));
	}

	/***
	 * tddl 临时表插入数据
	 * @param tddlList
	 */
	public void addMonitorDataOfTempTDDL(List<TddlPo> tddlList) {
		String sql;
		try {
			if (tddlList == null || tddlList.size() == 0) return;
			TddlPo sample = tddlList.get(0);
			
			// collectTime 格式 "2012-09-06 17"
			String tableName = TableNameConverUtil.formatTddlTmpTableName(sample.getCollectTime());
			
		    sql = "insert into " + tableName + "(app_name, db_feature, db_name, db_ip, db_port, sql_text, execute_type, execute_sum, time_average, min_time, max_time, app_host_ip, app_site_name, collect_time, gmt_create) " +
					"values " + generateDetailBatchValues(tddlList);
		    
			this.execute(sql, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	/***
	 * tddl detail表插入数据
	 * @param tddlList
	 */
	public void addMonitorDataOfDayTDDL(List<TddlPo> tddlList) {
		String sql;
		try {
			String tableFlag;
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			tableFlag = getTableFlag(calendar.getTime());
			String tableName = "csp_app_consume_tddl_detail_" + tableFlag;
		
		    sql = "insert into " + tableName + " (app_name, db_feature, db_name, db_ip, db_port, sql_text, execute_type, execute_sum, time_average, min_time, max_time, app_host_ip, app_site_name, collect_time, gmt_create) " +
					" values " + generateDetailBatchValues(tddlList);
		    
			this.execute(sql, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	/***
	 * tddl summary表插入数据
	 * @param tddlList
	 */
	public void addMonitorDataOfDayTDDLSummeryNew(List<TddlPo> tddlList) {
		String sql;
		try {
		    sql = "insert into csp_app_consume_tddl_summary_new (app_name, db_feature, db_name, db_ip, db_port, execute_sum, time_average, min_time, max_time,room_feature, collect_time, gmt_create) " +
					" values " + generateSummaryBatchValues(tddlList);
		    
			this.execute(sql, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	

	/***
	 * 插入summary数据
	 * @param collectDay
	 */
	public void addMonitorDataOfDayTDDLSummeryNew(Date collectDay) {
		String sql;
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String tableFlag = getTableFlag(collectDay);
			String tableName = "csp_app_consume_tddl_detail_" + tableFlag;
			String collectTime = sf.format(collectDay);
			
			sql = "insert into csp_app_consume_tddl_summary_new (app_name, db_feature, db_name, db_ip, db_port, execute_sum, time_average, min_time, max_time,room_feature, collect_time, gmt_create) " +
					" select app_name,db_feature,min(db_name),min(db_ip),min(db_port),sum(execute_sum),sum(time_average*execute_sum)/sum(execute_sum),null,null,null," +
					"'" + collectTime + "'," + "NOW() " +
					"from " + tableName + " group by app_name,db_feature";
			this.execute(sql, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public void updateMaxResponseOfDayTDDLSummaryNew(TddlSummaryPo po) {
		String sql;
		try {
			sql = "update csp_app_consume_tddl_summary_new set max_time=? where app_name=? and db_feature=? and DATE_FORMAT(collect_time,\"%Y-%m-%d\")=?";
			log.info(po.getCollectDate());
			this.execute(sql, new Object[] {
					po.getMaxResp() + "#" + po.getMaxRespDate(), po.getAppName(), po.getDbFeature(), po.getCollectDate()
			}
			,DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public void updateMinResponseOfDayTDDLSummaryNew(TddlSummaryPo po) {
		String sql;
		try {
		
			sql = "update csp_app_consume_tddl_summary_new set min_time=? where app_name=? and db_feature=? and DATE_FORMAT(collect_time,\"%Y-%m-%d\")=?";
			this.execute(sql, new Object[] {
					po.getMinResp() + "#" + po.getMinRespDate(), po.getAppName(), po.getDbFeature(), po.getCollectDate()
			}
			,DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	public void deleteTddlData(String opsName, String collectTime) {
		String tableFlag;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date date = calendar.getTime();
		try {
			date = sf.parse(collectTime);
		} catch (ParseException e1) {
			log.error("time parse error and use yesterday");
		}

		tableFlag = getTableFlag(date);
		String tableName = "csp_app_consume_tddl_detail_" + tableFlag;
		
		String tddlDetail = "delete from " + tableName + " where app_name=? and collect_time=?";
		String tddlSummary = "delete from csp_app_consume_tddl_summary_new where app_name=? and collect_time=?";
		
		try {
			this.execute(tddlDetail, new Object[]{opsName, collectTime},DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
			this.execute(tddlSummary, new Object[]{opsName, collectTime},DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public void deleteTempTddl(String tableName, String collectTime) {
		String sql = "delete from " + tableName + " where collect_time < '"+collectTime+"'";
		
		try {
			this.execute(sql,DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
		} catch (SQLException e) {
			log.error("addMonitorSplitCount 出错",e);
			e.printStackTrace();
		}
	}
	
	/***
	 * 生成detail表的表名
	 * @param date
	 * @return
	 */
	private String getTableFlag(Date date) {
		String tableFlag;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		if (day < 10) {
			tableFlag = "00" + day;
		} else if ( day >= 10 && day < 100) {
			tableFlag = "0" + day;
		} else {
			tableFlag = String.valueOf(day);
		}
		
		return tableFlag;
	}
	
	/***
	 * 批量插入的values语句
	 * @param tddlList
	 * @return
	 */
	private String generateDetailBatchValues(List<TddlPo> tddlList) {
		StringBuffer sb = new StringBuffer();
		for (TddlPo po : tddlList) {
			
			sb.append("(");
			sb.append("'").append(po.getAppName()).append("',");
			sb.append("'").append(po.getDbFeature()).append("',");
			sb.append("'").append(po.getDbName()).append("',");
			sb.append("'").append(po.getDbIp()).append("',");
			sb.append("'").append(po.getDbPort()).append("',");
			sb.append("'").append(po.getSqlText()).append(" ',"); // 多写一个空格，防止单引号被转义
			sb.append("'").append(po.getType()).append("',");
			sb.append(po.getExeCount()).append(",");
			sb.append(this.getAveResponse(po)).append(",");
			sb.append("'").append(po.getMinResp() + "#" + po.getMinRespDate()).append("',");
			sb.append("'").append(po.getMaxResp() + "#" + po.getMaxRespDate()).append("',");
			sb.append("'").append(po.getAppHostIp()).append("',");
			sb.append("'").append(po.getAppHostSite()).append("',");
			sb.append("'").append(po.getCollectTime()).append("',");
			sb.append(" NOW() ");
			sb.append("),");
		}
		
		 return sb.substring(0, sb.length() - 1);  // 去掉最后面的逗号
	}
	
	/***
	 * 批量插入的values语句
	 * @param tddlList
	 * @return
	 */
	private String generateSummaryBatchValues(List<TddlPo> tddlList) {
		StringBuffer sb = new StringBuffer();
		for (TddlPo po : tddlList) {
			
			sb.append("(");
			sb.append("'").append(po.getAppName()).append("',");
			sb.append("'").append(po.getDbFeature()).append("',");
			sb.append("'").append(po.getDbName()).append("',");
			sb.append("'").append(po.getDbIp()).append("',");
			sb.append("'").append(po.getDbPort()).append("',");
			sb.append(po.getExeCount()).append(",");
			sb.append(this.getAveResponse(po)).append(",");
			sb.append("'").append(po.getMinResp() + "#" + po.getMinRespDate()).append("',");
			sb.append("'").append(po.getMaxResp() + "#" + po.getMaxRespDate()).append("',");
			sb.append("null,");
			sb.append("'").append(po.getCollectTime()).append("',");
			sb.append(" NOW() ");
			sb.append("),");
		}
		
		 return sb.substring(0, sb.length() - 1);  // 去掉最后面的逗号
	}
	
	/***
	 * 平均响应时间
	 * @param po
	 * @return
	 */
	private String getAveResponse(TddlPo po) {
		DecimalFormat df = new DecimalFormat("#.##");
		double aveResponse = 0;
		String aveResponseS = "0";
		
		if (po.getExeCount() > 0) {
			aveResponse = (double) po.getRespTime() / po.getExeCount();
			aveResponseS = df.format(aveResponse);
		}
		return aveResponseS;
	}
	
	/***
	 * 查询tddl tmp表的数据
	 * @param tableName
	 * @param startTime
	 * @param endTime
	 * @param appName
	 * @param oneTime
	 * @param index
	 * @return
	 */
	public List<TddlPo> findTddlDataFromTemp(String tableName, final String startTime, final String endTime, final String appName, final int oneTime, final int index) {
		String sql = "select * from " + tableName + " where collect_time > '" + startTime +"' and collect_time <'" + endTime + "' and app_name='" + appName + "' limit " + index + "," + oneTime;
		final List<TddlPo> tddl2List = new ArrayList<TddlPo>();
		final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			this.query(sql, new Object [] {}, new SqlCallBack() {
		
				public void readerRows(ResultSet rs) throws Exception {
					TddlPo po = new TddlPo();
					po.setAppName(rs.getString("app_name"));
					po.setDbFeature(rs.getString("db_feature"));
					po.setDbName(rs.getString("db_name"));
					po.setDbIp(rs.getString("db_ip"));
					po.setDbPort(rs.getString("db_port"));
					po.setSqlText(rs.getString("sql_Text"));
					po.setType(rs.getString("execute_type"));
					po.setExeCount(rs.getLong("execute_sum"));
					po.setRespTime(rs.getFloat("time_average"));
					String maxTimeStr = rs.getString("max_time");
					po.setMaxResp(Integer.parseInt(maxTimeStr.split("#")[0]));
					po.setMaxRespDate(maxTimeStr.split("#")[1]);
					String minTimeStr = rs.getString("min_time");
					po.setMinResp(Integer.parseInt(minTimeStr.split("#")[0]));
					po.setMinRespDate(minTimeStr.split("#")[1]);
					po.setAppHostIp(rs.getString("app_host_ip"));
					po.setAppHostSite(rs.getString("app_site_name"));
					po.setCollectTime(sf.format(rs.getTimestamp("collect_time")));
					tddl2List.add(po);
				}
			},DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.DEPEND));
		} catch (Exception e) {
			log.error("findAllAppInfo: ", e);
		}
		
		return tddl2List;
	}
	
	/***
	 * sph summary表插入数据
	 * @param sphList
	 */
	public void addMonitorDataOfSph(List<SphPo> sphList) {
		String sql;
		try {
		    sql = "insert into ms_monitor_sph_record (id, app_name, ip, block_key, block_action, block_count, collect_time) " +
					" values " + generateSummaryBatchValuesSph(sphList);
			this.execute(sql, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	/***
	 * 批量插入的values语句
	 * @param tddlList
	 * @return
	 */
	private String generateSummaryBatchValuesSph(List<SphPo> sphList) {
		StringBuffer sb = new StringBuffer();
		for (SphPo po : sphList) {
			
			sb.append("(");
			sb.append("'").append(UUID.randomUUID().toString()).append("',");
			sb.append("'").append(po.getAppName()).append("',");
			sb.append("'").append(po.getIp()).append("',");
			sb.append("'").append(po.getBlockKey()).append("',");
			sb.append("'").append(po.getBlockAction()).append("',");
			sb.append(po.getBloackCount()).append(",");
			sb.append("'").append(po.getCollectTime()).append("'");
			sb.append("),");
		}
		
		 return sb.substring(0, sb.length() - 1);  // 去掉最后面的逗号
	}
	
	/***
	 * apache special表插入数据
	 * @param list
	 */
	public void addMonitorDataOfApacheSpecial(List<ApacheSpecialPo> list) {
		String sql;
		try {
		    sql = "insert into ms_monitor_special_pv (id, app_name, request_url, http_code, request_num, rt, collect_time) " +
					" values " + generateSummaryBatchValuesApacheSpecial(list);
			this.execute(sql, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	/***
	 * 批量插入的values语句
	 * @param list
	 * @return
	 */
	private String generateSummaryBatchValuesApacheSpecial(List<ApacheSpecialPo> list) {
		StringBuffer sb = new StringBuffer();
		for (ApacheSpecialPo po : list) {
			sb.append("(");
			sb.append("'").append(UUID.randomUUID().toString()).append("',");
			sb.append("'").append(po.getAppName()).append("',");
			sb.append("'").append(po.getRequstUrl()).append("',");
			sb.append("'").append(po.getHttpCode()).append("',");
			sb.append(po.getRequstNum()).append(",");
			sb.append(po.getRt()).append(",");
			sb.append("'").append(po.getCollectTime()).append("'");
			sb.append("),");
		}
		
		 return sb.substring(0, sb.length() - 1);  // 去掉最后面的逗号
	}
	
	/***
	 * tdod 表插入数据
	 * @param list
	 */
	public void addMonitorDataOfTdod(List<TdodPo> list) {
		String sql;
		try {
		    sql = "insert into ms_monitor_tdod_record (id, app_name, block_count, collect_time) " +
					" values " + generateSummaryBatchValuesTdod(list);
			this.execute(sql, DbRouteManage.get().getDbRouteByRouteId(DatabaseInfo.MAIN));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	/***
	 * 批量插入的values语句
	 * @param list
	 * @return
	 */
	private String generateSummaryBatchValuesTdod(List<TdodPo> list) {
		StringBuffer sb = new StringBuffer();
		for (TdodPo po : list) {
			sb.append("(");
			sb.append("'").append(UUID.randomUUID().toString()).append("',");
			sb.append("'").append(po.getAppName()).append("',");
			sb.append(po.getBloackCount()).append(",");
			sb.append("'").append(po.getCollectTime()).append("'");
			sb.append("),");
		}
		
		 return sb.substring(0, sb.length() - 1);  // 去掉最后面的逗号
	}
	
	public static void main(String [] args) {
		AnalyseLogDao dao = new AnalyseLogDao();
		List<SphPo> sphList = new ArrayList<SphPo>();
		SphPo po1 = new SphPo();
		SphPo po2 = new SphPo();
		sphList.add(po1);
		sphList.add(po2);
		
		dao.addMonitorDataOfSph(sphList);
		
	}
	
}
