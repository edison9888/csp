package com.taobao.monitor.stat.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.stat.db.po.AppSqlInfo;
import com.taobao.monitor.stat.db.po.TableInfo;
import com.taobao.monitor.stat.db.po.Top10SqlPo;

/**   
 * @author xiaoxie   
 * @create time：2010-4-10 下午03:15:09   
 * @description  
 */
public class BeidouSqlDao extends MysqlRouteBase {
	
	public final static String[] TOPSQL_ORDER = { " exec_delta ",
			" exec_delta * buffer_gets ", " exec_delta * disk_reads ",
			" exec_delta * elapsed_time " };
	public final static String[] DB_NAMES = { "tbdb1", "tbdb2", "heart",
			"comm", "bmw" , "misc" };
	private static Logger log = Logger.getLogger(AnalyseLogDao.class);

	public BeidouSqlDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("BEIDOU_URL"));
	}
	/**
	 * 根据制定db,按执行次数来获取TOP10 sql的
	 * @param db
	 * @return
	 */
	public List<Top10SqlPo> findTopSqlExec(String db,String collectTime){
		return findTopSql10(db, TOPSQL_ORDER[0], collectTime);
	}
	/**
	 * 根据制定db,按逻辑读来获取TOP10 sql的
	 * @param db
	 * @return
	 */
	public List<Top10SqlPo> findTopSqlBufferGets(String db,String collectTime){
		return findTopSql10(db, TOPSQL_ORDER[1],collectTime);
	}
	/**
	 * 根据制定db,按物理读来获取TOP10 sql的
	 * @param db
	 * @return
	 */
	public List<Top10SqlPo> findTopSqlDiskGets(String db,String collectTime){
		return findTopSql10(db, TOPSQL_ORDER[2],collectTime);
	}
	/**
	 * 根据制定db,按执行耗时来获取TOP10 sql的
	 * @param db
	 * @return
	 */
	public List<Top10SqlPo> findTopSqlElapsedTime(String db,String collectTime){
		return findTopSql10(db, TOPSQL_ORDER[3],collectTime);
	}
	
 
	/**
	 * 
	 * @param db
	 * @param order
	 * @param collectTime yyyy-MM-dd
	 * @return
	 */
	private List<Top10SqlPo> findTopSql10(String db, String order,String collectTime)   {
		// exec_delta 执行次数
		// exec_delta * buffer_gets  逻辑读
		// exec_delta * disk_reads  物理读
		// exec_delta * elapsed_time 总耗时
		final List<Top10SqlPo>  sqlList = new ArrayList<Top10SqlPo>();
		
		try {
			this.query("select exec_delta,buffer_gets,disk_reads,elapsed_time,sql_fulltext,app_name  from sqlsummary  where service = '"
					+ db
					+ "' and gmt_create = ? order by "
					+ order
					+ " desc limit 5",new Object[]{collectTime+" 00:00:00"}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws SQLException {
		 
					long execDelta = rs.getLong(1);
					long bufferGets = rs.getLong(2);
					long diskReads = rs.getLong(3);
					long elapsedTime = rs.getLong(4);
					String sqlFullText = rs.getString(5);
					String appName = rs.getString(6);
					Top10SqlPo po = new Top10SqlPo();
					po.setExecDelta(execDelta);
					po.setBufferGets(bufferGets * execDelta);
					po.setDiskReads(diskReads * execDelta);
					po.setElapsedTime(elapsedTime * execDelta);
					po.setSqlFullText(sqlFullText);
					po.setAppName(appName);
					sqlList.add(po);

				}
			});
		} catch (Exception e) {
			log.error("读取北斗db列表出错", e);
		}
		return sqlList;
	}
	/**
	 * 应用和db的一些信息查询, 大概有500条左右的记录
	 * @return
	 */
	public List<AppSqlInfo> findAppSqlInfo(String collectTime) {
 
		final List<AppSqlInfo>  sqlList = new ArrayList<AppSqlInfo>();
		
		try {
			this.query("select service,application,module_min,module_max,module_sum,sql_total,exec_total,exec_avg from sqlmodule where  gmt_create=?", 
					new Object[]{collectTime+" 00:00:00"},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws SQLException {
					
					String db = rs.getString("service");
					String appName = rs.getString("application");
					int connMin = rs.getInt("module_min");
					int connMax = rs.getInt("module_max");
					int connSum = rs.getInt("module_sum");
					int sqlTotal = rs.getInt("sql_total");
					int execTotal = rs.getInt("exec_total");
					int execAvg = rs.getInt("exec_avg");
		 
					AppSqlInfo po = new AppSqlInfo();
					po.setDb(db);
					po.setAppName(appName);
					po.setConnMax(connMax);
					po.setConnMin(connMin);
					po.setConnSum(connSum);
					po.setSqlTotal(sqlTotal);
					po.setExecTotal(execTotal);
					po.setExecAvg(execAvg);
					
					 
					sqlList.add(po);

				}
			});
		} catch (Exception e) {
			log.error("读取北斗db列表出错", e);
		}
		return sqlList;
	}

	/**
	 * 应用和db的一些信息查询, 大概有500条左右的记录
	 * 
	 * @return
	 */
	public List<TableInfo> findTableRecordInfo(String collectTime) {

		final List<TableInfo> sqlList = new ArrayList<TableInfo>();

		try {
			this.query("select table_name,record_num,delta from tabmon_records where  gmt_create=?",
					new Object[]{collectTime+" 00:00:00"},new SqlCallBack() {
					public void readerRows(ResultSet rs)
							throws SQLException {

						String tableName = rs.getString("table_name");
						long recordNum = rs.getLong("record_num");
						long delta = rs.getLong("delta");

						TableInfo po = new TableInfo();
						po.setTableName(tableName);
						po.setRecordNum(recordNum);
						po.setDelta(delta);
						sqlList.add(po);

					}
				});
		} catch (Exception e) {
			log.error("读取北斗db列表出错", e);
		}
		return sqlList;
	}
	/**
	 * 北斗表的统计信息和tabmon_records表的统计有所不同，这个tabmon_sum表统计比较全面，不过叶开说数据不太准确
	 * @param collectTime
	 * @return
	 */
	public List<TableInfo> findTableInfo2(String collectTime) {
		String[][] dbusers = { 
				{ "tbdb1", "taobao", "auction_auctions" },
				{ "tbdb1", "TAOBAO", "sku" },
				{ "tbdb2", "TAOBAO", "auction_auctions" },
				{ "tbdb2", "TAOBAO", "sku" },
				{ "comm", "TBCENTER", "TC_BIZ_ORDER" },
				{ "comm", "TBCENTER", "TC_PAY_ORDER" },
				{ "comm", "TBCENTER", "TC_LOGISTICS_ORDER" },
				{ "heart", "TAOBAO", "SPU" }, 
				{ "heart", "TAOBAO", "CART" },
				{ "heart", "TAOBAO", "REP_ITEM_SUM" },
				{ "heart", "TAOBAO", "AUCTION_BUYER_ANONY" } 
		};
		// tbdb1 tbdb2 TAOBAO = auction_auctions,sku
		// comm TBCENTER = TC_BIZ_ORDER TC_PAY_ORDER TC_LOGISTICS_ORDER
		// heart TAOBAO  = SPU CART REP_ITEM_SUM AUCTION_BUYER_ANONY
		final List<TableInfo> sqlList = new ArrayList<TableInfo>();
		// select
		// owner,table_name,gmt_create,ins_diff,upd_diff,del_diff,num_rows,service
		// from tabmon_sum where table_name='auction_auctions' and
		// owner='TAOBAO' order by gmt_create desc limit 10;
		try {
			for (String[] dbuser : dbusers) {
				 
			this.query("select owner,table_name,ins_diff,upd_diff,del_diff,num_rows,service " +
					   "from tabmon_sum where service=? and owner=? and table_name=? and gmt_create=?",
					new Object[]{dbuser[0],dbuser[1],dbuser[2],collectTime+" 00:00:00"},new SqlCallBack() {
					public void readerRows(ResultSet rs)
							throws SQLException {
				 
						String tableName = rs.getString("table_name");
						String dbName = rs.getString("service");
						String owner = rs.getString("owner");
						long ins_diff = rs.getLong("ins_diff");
						long upd_diff = rs.getLong("upd_diff");
						long del_diff = rs.getLong("del_diff");
						long num_rows = rs.getLong("num_rows");
						
						TableInfo po = new TableInfo();
						po.setTableName(tableName);
					 
						po.setInsDiff(ins_diff);
						po.setUpdDiff(upd_diff);
						po.setDelDiff(del_diff);
						po.setNumRows(num_rows);
						po.setDbName(dbName);
						po.setOwner(owner);
						sqlList.add(po);

					}
				});
			}
		} catch (Exception e) {
			log.error("读取北斗db列表出错", e);
		}
		return sqlList;
	}
	
//	private Date parseDate(String collectTime){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		return sdf.parse(collectTime);
//	}
	
	

}




/**

| sqlsummary       | 

+------------------+---------------+------+-----+---------+-------+
| Field            | Type          | Null | Key | Default | Extra |
+------------------+---------------+------+-----+---------+-------+
| service          | varchar(12)   | NO   | MUL | NULL    |       | 
| sql_id           | varchar(13)   | NO   |     | NULL    |       | 
| sql_fulltext     | varchar(4000) | NO   |     | NULL    |       | 
| sorts            | decimal(14,4) | YES  |     | NULL    |       | 
| version_count    | int(11)       | NO   |     | NULL    |       | 
| loaded_versions  | int(11)       | NO   |     | NULL    |       | 
| fetches          | bigint(20)    | NO   |     | NULL    |       | 
| executions       | bigint(20)    | NO   |     | NULL    |       | 
| parse_calls      | decimal(14,4) | YES  |     | NULL    |       | 
| disk_reads       | decimal(14,4) | YES  |     | NULL    |       | 
| buffer_gets      | decimal(14,4) | YES  |     | NULL    |       | 
| app_wait_time    | decimal(12,2) | NO   |     | NULL    |       | 
| con_wait_time    | decimal(12,2) | NO   |     | NULL    |       | 
| io_wait_time     | decimal(12,2) | NO   |     | NULL    |       | 
| rows_processed   | decimal(12,2) | YES  |     | NULL    |       | 
| schema_name      | varchar(30)   | NO   |     | NULL    |       | 
| plan_hash_value  | bigint(20)    | NO   |     | NULL    |       | 
| cpu_time         | decimal(12,4) | YES  |     | NULL    |       | 
| elapsed_time     | decimal(12,4) | YES  |     | NULL    |       | 
| last_active_time | datetime      | NO   |     | NULL    |       | 
| gmt_create       | datetime      | YES  | MUL | NULL    |       | 
| exec_delta       | bigint(20)    | YES  |     | NULL    |       | 
| module           | varchar(64)   | YES  |     | NULL    |       | 
| scan_type        | varchar(10)   | YES  |     | NULL    |       | 
| exec_per         | decimal(14,2) | YES  |     | NULL    |       | 
| disk_per         | decimal(14,2) | YES  |     | NULL    |       | 
| buff_per         | decimal(14,2) | YES  |     | NULL    |       | 
| elap_per         | decimal(14,2) | YES  |     | NULL    |       | 
| cput_per         | decimal(14,2) | YES  |     | NULL    |       | 
| sort_per         | decimal(14,2) | YES  |     | NULL    |       | 
| parse_per        | decimal(14,2) | YES  |     | NULL    |       | 
| app_name         | varchar(64)   | YES  |     | NULL    |       | 
| curve            | varchar(64)   | YES  |     | NULL    |       | 
+------------------+---------------+------+-----+---------+-------+

sqlmodule
+-------------+------------------+------+-----+---------+----------------+
| Field       | Type             | Null | Key | Default | Extra          |
+-------------+------------------+------+-----+---------+----------------+
| id          | int(10) unsigned | NO   | PRI | NULL    | auto_increment | 
| gmt_create  | date             | YES  | MUL | NULL    |                | 
| service     | varchar(20)      | YES  | MUL | NULL    |                | 
| application | varchar(64)      | YES  |     | NULL    |                | 
| module_cnt  | int(11)          | YES  |     | 0       |                | 
| module_min  | int(11)          | YES  |     | 0       |                | 
| module_max  | int(11)          | YES  |     | 0       |                | 
| module_sum  | int(11)          | YES  |     | 0       |                | 
| sql_total   | int(11)          | YES  |     | 0       |                | 
| exec_total  | int(11)          | YES  |     | 0       |                | 
| exec_avg    | decimal(14,2)    | YES  |     | 0.00    |                | 
| module_qps  | decimal(14,2)    | YES  |     | 0.00    |                | 
| module_avg  | decimal(14,2)    | YES  |     | 0.00    |                | 
| type        | varchar(20)      | YES  |     | NULL    |                | 
+-------------+------------------+------+-----+---------+----------------+


describe tabmon_records
+------------+-------------+------+-----+---------------------+-------+
| Field      | Type        | Null | Key | Default             | Extra |
+------------+-------------+------+-----+---------------------+-------+
| gmt_create | datetime    | NO   | PRI | 0000-00-00 00:00:00 |       | 
| table_name | varchar(30) | NO   | PRI |                     |       | 
| record_num | bigint(20)  | YES  |     | NULL                |       | 
| delta      | bigint(20)  | YES  |     | NULL                |       | 
| service    | varchar(30) | NO   | PRI |                     |       | 
+------------+-------------+------+-----+---------------------+-------+
desc tabmon_sum;
+------------+-------------+------+-----+---------+----------------+
| Field      | Type        | Null | Key | Default | Extra          |
+------------+-------------+------+-----+---------+----------------+
| id         | bigint(20)  | NO   | PRI | NULL    | auto_increment | 
| owner      | varchar(30) | NO   |     | NULL    |                | 
| table_name | varchar(30) | NO   |     | NULL    |                | 
| gmt_create | datetime    | NO   |     | NULL    |                | 
| ins_diff   | bigint(20)  | NO   |     | NULL    |                | 
| upd_diff   | bigint(20)  | NO   |     | NULL    |                | 
| del_diff   | bigint(20)  | NO   |     | NULL    |                | 
| dml_diff   | bigint(20)  | NO   |     | NULL    |                | 
| service    | varchar(12) | NO   | MUL | NULL    |                | 
| truncated  | varchar(3)  | YES  |     | NULL    |                | 
| num_rows   | bigint(20)  | YES  |     | NULL    |                | 
+------------+-------------+------+-----+---------+----------------+
*/