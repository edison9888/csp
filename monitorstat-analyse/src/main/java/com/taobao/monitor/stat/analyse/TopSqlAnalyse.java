
package com.taobao.monitor.stat.analyse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.stat.content.ReportContentInterface;
import com.taobao.monitor.stat.db.impl.BeidouSqlDao;
import com.taobao.monitor.stat.db.po.AppSqlInfo;
import com.taobao.monitor.stat.db.po.TableInfo;
import com.taobao.monitor.stat.db.po.Top10SqlPo;

/**
 * IP address: 172.23.110.200
Port:3306
Dbname:beidou
Tablename: sqlsummary
User: appops
Password: appops

 * @author xiaodu
 * @version 2010-4-8 下午04:45:48
 */
public class TopSqlAnalyse extends Analyse {
	private static final Logger logger =  Logger.getLogger(TopSqlAnalyse.class);
	
	private BeidouSqlDao dao = new BeidouSqlDao();
 
	public TopSqlAnalyse( String appName) {
		super(appName); 
	}
	/**
	 * 将北斗数据库的TOP10SQL，读取放入日报表库
	 */
	public void analyseLogFile(ReportContentInterface content) {
		
		if(!this.getAppName().equals("detail")){
			return ;
		}
		
		
		for (String db : BeidouSqlDao.DB_NAMES) {
			 
			ReportContentInterface rc = content;
			// 执行次数TOP10
			List<Top10SqlPo> list = dao.findTopSqlBufferGets(db,this.getCollectDate());
			if (list == null) {
				// log
				logger.error("从北斗获取执行次数TOP10的数据为null");
			} else {
				for (Top10SqlPo po : list) {
					rc.putReportDataByCount(db, "SQL_TOP_10_EXEC_"
							+ po.getSqlFullText(), po.getExecDelta(), this
							.getCollectDate());
				}
				logger.info("执行次数TOP10,导入结束," + list.size());
			}
			// 磁盘物理读TOP10
			list = dao.findTopSqlDiskGets(db,this.getCollectDate());
			if (list == null) {
				// log
				logger.error("从北斗获取磁盘物理读TOP10的数据为null");
			} else {
				for (Top10SqlPo po : list) {
					rc.putReportDataByCount(db, "SQL_TOP_10_DISKGETS_"
							+ po.getSqlFullText(), po.getDiskReads(), this
							.getCollectDate());
				}
				logger.info("磁盘物理读TOP10,导入结束," + list.size());
			}
			// 总耗时TOP10
			list = dao.findTopSqlElapsedTime(db,this.getCollectDate());
			if (list == null) {
				// log
				logger.error("从北斗获取总耗时TOP10的数据为null");
			} else {
				for (Top10SqlPo po : list) {
					rc.putReportDataByCount(db, "SQL_TOP_10_ELAPSED_"
							+ po.getSqlFullText(), po.getElapsedTime(), this
							.getCollectDate());
				}
				logger.info("总耗时TOP10,导入结束," + list.size());
			}

			// 逻辑读TOP10
			list = dao.findTopSqlBufferGets(db,this.getCollectDate());
			if (list == null) {
				// log
				logger.error("从北斗获取逻辑读TOP10的数据为null");
			} else {
				for (Top10SqlPo po : list) {
					rc.putReportDataByCount(db, "SQL_TOP_10_BUFFERGETS_"
							+ po.getSqlFullText(), po.getBufferGets(), this
							.getCollectDate());
				}
			}
			
			// 应用和db的一些信息
			
			List<AppSqlInfo> appInfos = dao.findAppSqlInfo(this.getCollectDate());
			if (appInfos == null) {
				// log
				logger.error("从北斗获取应用和db的一些信息null");
			} else {
				for (AppSqlInfo info : appInfos) {
					// appName如果在我们的库表里不存在，则记录被忽略
					// app_DB 最大连接数
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_CONNMAX", info.getConnMax(), this
							.getCollectDate());
					// app_DB 最小连接数
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_CONNMIN", info.getConnMin(), this
							.getCollectDate());
					// app_DB 所有连接总数=机器数*平均最大连接数
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_CONNSUM", info.getConnSum(), this
							.getCollectDate());
					// app_DB 平均执行次数
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_EXECAVG", info.getExecAvg(), this
							.getCollectDate());
					// app_DB sql执行总数
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_EXECTOTAL", info.getExecTotal(), this
							.getCollectDate());
					// app_DB 总的sql数量
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_SQLTOTAL", info.getSqlTotal(), this
							.getCollectDate());
				}
				logger.info("Db和应用相关信息,导入结束," + appInfos.size());
			}
			
			// 表记录数、增量信息
			List<TableInfo> tableInfos = dao.findTableRecordInfo(this.getCollectDate());
			if (tableInfos == null) {
				// log
				logger.error("从北斗获取表记录信息为null");
			} else {
				for (TableInfo info : tableInfos) {
					// 表名_RECORDNUM
					rc.putReportDataByCount(info.getAppName(), "SQL_TABLESIZE_"+info.getTableName()
							.toUpperCase()
							+ "_RECORDNUM", info.getRecordNum(), this
							.getCollectDate());
					rc.putReportDataByCount(info.getAppName(), "SQL_TABLESIZE_"+info.getTableName()
							.toUpperCase()
							+ "_INCREMENT", info.getDelta(), this
							.getCollectDate());
				}
				logger.info("大表记录数和每天增量数,导入结束," + tableInfos.size());
			}
			
			// 核心表记录数、新增、删除、更新信息
			// 共11张表：
			List<TableInfo> tableInfos2 = dao.findTableInfo2(this.getCollectDate());
			if (tableInfos == null) {
				// log
				logger.error("从北斗获取表tabmon_sum记录信息为null");
			} else {
				for (TableInfo info : tableInfos2) {
					// 新增记录
					rc.putReportDataByCount(info.getDbName(), info.getOwner() + "_"+info.getTableName()
							.toUpperCase()
							+ "_INSNUM", info.getInsDiff(), this
							.getCollectDate());
					// 更新
					rc.putReportDataByCount(info.getDbName(), info.getOwner() + "_"+info.getTableName()
							.toUpperCase()
							+ "_UPDNUM", info.getUpdDiff(), this
							.getCollectDate());
					// 删除
					rc.putReportDataByCount(info.getDbName(), info.getOwner() + "_"+info.getTableName()
							.toUpperCase()
							+ "_DELNUM", info.getDelDiff(), this
							.getCollectDate());
					// 总记录数
					rc.putReportDataByCount(info.getDbName(), info.getOwner() + "_"+info.getTableName()
							.toUpperCase()
							+ "_ROWSNUM", info.getNumRows(), this
							.getCollectDate());
				}
				logger.info("从北斗获取表tabmon_sum记录信息,导入结束," + tableInfos.size());
			}
		}
	}
	public static void main(String[] args) {
		Date d = new Date();
		java.util.Calendar c = Calendar.getInstance();
		System.out.println(c.get(c.DAY_OF_YEAR));
	}
	@Override
	protected void insertToDb(ReportContentInterface content) {
		// TODO Auto-generated method stub
		
	}
}
