
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
 * @version 2010-4-8 ����04:45:48
 */
public class TopSqlAnalyse extends Analyse {
	private static final Logger logger =  Logger.getLogger(TopSqlAnalyse.class);
	
	private BeidouSqlDao dao = new BeidouSqlDao();
 
	public TopSqlAnalyse( String appName) {
		super(appName); 
	}
	/**
	 * ���������ݿ��TOP10SQL����ȡ�����ձ����
	 */
	public void analyseLogFile(ReportContentInterface content) {
		
		if(!this.getAppName().equals("detail")){
			return ;
		}
		
		
		for (String db : BeidouSqlDao.DB_NAMES) {
			 
			ReportContentInterface rc = content;
			// ִ�д���TOP10
			List<Top10SqlPo> list = dao.findTopSqlBufferGets(db,this.getCollectDate());
			if (list == null) {
				// log
				logger.error("�ӱ�����ȡִ�д���TOP10������Ϊnull");
			} else {
				for (Top10SqlPo po : list) {
					rc.putReportDataByCount(db, "SQL_TOP_10_EXEC_"
							+ po.getSqlFullText(), po.getExecDelta(), this
							.getCollectDate());
				}
				logger.info("ִ�д���TOP10,�������," + list.size());
			}
			// ���������TOP10
			list = dao.findTopSqlDiskGets(db,this.getCollectDate());
			if (list == null) {
				// log
				logger.error("�ӱ�����ȡ���������TOP10������Ϊnull");
			} else {
				for (Top10SqlPo po : list) {
					rc.putReportDataByCount(db, "SQL_TOP_10_DISKGETS_"
							+ po.getSqlFullText(), po.getDiskReads(), this
							.getCollectDate());
				}
				logger.info("���������TOP10,�������," + list.size());
			}
			// �ܺ�ʱTOP10
			list = dao.findTopSqlElapsedTime(db,this.getCollectDate());
			if (list == null) {
				// log
				logger.error("�ӱ�����ȡ�ܺ�ʱTOP10������Ϊnull");
			} else {
				for (Top10SqlPo po : list) {
					rc.putReportDataByCount(db, "SQL_TOP_10_ELAPSED_"
							+ po.getSqlFullText(), po.getElapsedTime(), this
							.getCollectDate());
				}
				logger.info("�ܺ�ʱTOP10,�������," + list.size());
			}

			// �߼���TOP10
			list = dao.findTopSqlBufferGets(db,this.getCollectDate());
			if (list == null) {
				// log
				logger.error("�ӱ�����ȡ�߼���TOP10������Ϊnull");
			} else {
				for (Top10SqlPo po : list) {
					rc.putReportDataByCount(db, "SQL_TOP_10_BUFFERGETS_"
							+ po.getSqlFullText(), po.getBufferGets(), this
							.getCollectDate());
				}
			}
			
			// Ӧ�ú�db��һЩ��Ϣ
			
			List<AppSqlInfo> appInfos = dao.findAppSqlInfo(this.getCollectDate());
			if (appInfos == null) {
				// log
				logger.error("�ӱ�����ȡӦ�ú�db��һЩ��Ϣnull");
			} else {
				for (AppSqlInfo info : appInfos) {
					// appName��������ǵĿ���ﲻ���ڣ����¼������
					// app_DB ���������
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_CONNMAX", info.getConnMax(), this
							.getCollectDate());
					// app_DB ��С������
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_CONNMIN", info.getConnMin(), this
							.getCollectDate());
					// app_DB ������������=������*ƽ�����������
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_CONNSUM", info.getConnSum(), this
							.getCollectDate());
					// app_DB ƽ��ִ�д���
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_EXECAVG", info.getExecAvg(), this
							.getCollectDate());
					// app_DB sqlִ������
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_EXECTOTAL", info.getExecTotal(), this
							.getCollectDate());
					// app_DB �ܵ�sql����
					rc.putReportDataByCount(info.getNewAppName(), "SQL_INFO_"+info.getDb()
							.toUpperCase()
							+ "_SQLTOTAL", info.getSqlTotal(), this
							.getCollectDate());
				}
				logger.info("Db��Ӧ�������Ϣ,�������," + appInfos.size());
			}
			
			// ���¼����������Ϣ
			List<TableInfo> tableInfos = dao.findTableRecordInfo(this.getCollectDate());
			if (tableInfos == null) {
				// log
				logger.error("�ӱ�����ȡ���¼��ϢΪnull");
			} else {
				for (TableInfo info : tableInfos) {
					// ����_RECORDNUM
					rc.putReportDataByCount(info.getAppName(), "SQL_TABLESIZE_"+info.getTableName()
							.toUpperCase()
							+ "_RECORDNUM", info.getRecordNum(), this
							.getCollectDate());
					rc.putReportDataByCount(info.getAppName(), "SQL_TABLESIZE_"+info.getTableName()
							.toUpperCase()
							+ "_INCREMENT", info.getDelta(), this
							.getCollectDate());
				}
				logger.info("����¼����ÿ��������,�������," + tableInfos.size());
			}
			
			// ���ı��¼����������ɾ����������Ϣ
			// ��11�ű�
			List<TableInfo> tableInfos2 = dao.findTableInfo2(this.getCollectDate());
			if (tableInfos == null) {
				// log
				logger.error("�ӱ�����ȡ��tabmon_sum��¼��ϢΪnull");
			} else {
				for (TableInfo info : tableInfos2) {
					// ������¼
					rc.putReportDataByCount(info.getDbName(), info.getOwner() + "_"+info.getTableName()
							.toUpperCase()
							+ "_INSNUM", info.getInsDiff(), this
							.getCollectDate());
					// ����
					rc.putReportDataByCount(info.getDbName(), info.getOwner() + "_"+info.getTableName()
							.toUpperCase()
							+ "_UPDNUM", info.getUpdDiff(), this
							.getCollectDate());
					// ɾ��
					rc.putReportDataByCount(info.getDbName(), info.getOwner() + "_"+info.getTableName()
							.toUpperCase()
							+ "_DELNUM", info.getDelDiff(), this
							.getCollectDate());
					// �ܼ�¼��
					rc.putReportDataByCount(info.getDbName(), info.getOwner() + "_"+info.getTableName()
							.toUpperCase()
							+ "_ROWSNUM", info.getNumRows(), this
							.getCollectDate());
				}
				logger.info("�ӱ�����ȡ��tabmon_sum��¼��Ϣ,�������," + tableInfos.size());
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
