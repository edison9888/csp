package com.taobao.monitor.stat.analyse;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.taobao.monitor.stat.content.ReportContentInterface;
import com.taobao.monitor.stat.db.impl.HaBoDataDao;
import com.taobao.monitor.stat.db.impl.ShareReportDao;
import com.taobao.monitor.stat.db.po.ShareReportPo;

/**
 * xiaoxie 2010-4-28
 */
public class ShareReportAnalyse extends Analyse {
	private static final Logger logger = Logger
			.getLogger(ShareReportAnalyse.class);
	private ShareReportDao dao = new ShareReportDao();
	
	private HaBoDataDao habo = new HaBoDataDao();

	public ShareReportAnalyse(String appName) {
		super(appName);

	}

	@Override
	public void analyseLogFile(ReportContentInterface content) {
		
		if(this.getAppName().equals("tf_buy")){
			return ;
		}
		
		logger.info("ShareReportAnalyse");
		
		String appName = "tf_buy";
		
		ShareReportPo po = dao.findAppSqlInfo(this.getCollectDate());		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		String startTime = this.getCollectDate();
		
		try {
			long amount = dao.sumAllamount(startTime);
			content.putReportDataByCount(appName, "AMOUNT_ORDER_COUNT", amount, this.getCollectDate());
		} catch (Exception e) {
			logger.error("sumAllamount ",e);
		}	
		
		if (po != null) {
			
			content.putReportDataByCount(appName, "CREATE_ORDER_COUNT", po
					.getCreateOrderCount(), this.getCollectDate());
			content.putReportDataByCount(appName, "PAY_ORDER_COUNT", po
					.getPayOrderCount(), this.getCollectDate());
		} else {
			logger.error("ShareReportPo is null ");
		}

	}

	@Override
	protected void insertToDb(ReportContentInterface content) {
		// TODO Auto-generated method stub
		
	}

}
