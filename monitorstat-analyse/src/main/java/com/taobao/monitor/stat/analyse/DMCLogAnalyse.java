package com.taobao.monitor.stat.analyse;

import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.stat.content.ReportContentInterface;

/**
 * 分析 DMC（交易线历史订单迁移） 日志
 * 
 * @author xuezhu
 */
public class DMCLogAnalyse extends AnalyseFile {
	private static final Logger logger = Logger.getLogger(DMCLogAnalyse.class);

	AtomicLong returnNum = new AtomicLong(0);
	AtomicLong exceptionNum = new AtomicLong(0);
	AtomicLong insertLogErrorNum = new AtomicLong(0);
	AtomicLong deleteOnlineDBNum = new AtomicLong(0);
	long mainTaskCount = 0L;
	long deleteCount = 0L;

	public DMCLogAnalyse(String appName) throws Exception {
		super(appName, "o2h_main.log");
	}

	/**
	 * 分析临时文件，并统计得到采样时间内的 平均数据
	 * 
	 * @param filepath
	 */
	private void parseTimeSelectLogLine(String logRecord) {
		if (logRecord.indexOf("so return") > -1) {
			returnNum.addAndGet(1);
		}
		if (logRecord.indexOf("exception") > -1 || logRecord.indexOf("Exception") > -1) {
			exceptionNum.addAndGet(1);
		}
		if (logRecord.indexOf("insert log error") > -1) {
			insertLogErrorNum.addAndGet(1);
		}
		if (logRecord.indexOf("OnlineLogicDBDaoImpl.deleteOrder") > -1
				|| logRecord.indexOf("OnlineLogicDBDaoImpl.batchDelete") > -1
				|| logRecord.indexOf("onlineLogicDBDao.deleteOrder fail") > -1
				|| logRecord.indexOf("deleteOnlineOrder error") > -1) {
			deleteOnlineDBNum.addAndGet(1);
		}

		if (logRecord.indexOf("shutdown main thread CompletedTaskCount") > -1) {
			if (logRecord.split("=").length == 2) {
				mainTaskCount = Long.parseLong(logRecord.split("=")[1]);
			}
		}
		
		if (logRecord.indexOf("delete CompletedTaskCount") > -1) {
			if (logRecord.split("=").length == 2) {
				deleteCount = Long.parseLong(logRecord.split("=")[1]);
			}
		}
	}

	@Override
	protected void parseLogLine(String logRecord) {
		parseTimeSelectLogLine(logRecord);
	}

	@Override
	protected String parseLogLineCollectTime(String logRecord) {
		return null;
	}

	@Override
	protected String parseLogLineCollectDate(String logRecord) {
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d) ");
		Matcher m = pattern.matcher(logRecord);
		if (m.find()) {
			return m.group(1);
		}

		return null;
	}

	@Override
	protected void insertToDb(ReportContentInterface content) {

		content.putReportDataByCount(this.getAppName(), "OTHER_dmc_returnNum_" + Constants.COUNT_TIMES_FLAG, returnNum.get(), this.getCollectDate());
		content.putReportDataByCount(this.getAppName(), "OTHER_dmc_exceptionNum_" + Constants.COUNT_TIMES_FLAG, exceptionNum.get(), this.getCollectDate());
		content.putReportDataByCount(this.getAppName(), "OTHER_dmc_insertLogErrorNum_" + Constants.COUNT_TIMES_FLAG, insertLogErrorNum.get(), this.getCollectDate());
		content.putReportDataByCount(this.getAppName(), "OTHER_dmc_deleteOnlineDBNum_" + Constants.COUNT_TIMES_FLAG, deleteOnlineDBNum.get(), this.getCollectDate());
		content.putReportDataByCount(this.getAppName(), "OTHER_dmc_mainTaskCount_" + Constants.COUNT_TIMES_FLAG, mainTaskCount, this.getCollectDate());
		content.putReportDataByCount(this.getAppName(), "OTHER_dmc_deleteCount_" + Constants.COUNT_TIMES_FLAG, deleteCount, this.getCollectDate());

		logger.warn("分析 DMC（交易线历史订单迁移） 日志 完了");
	}

}
