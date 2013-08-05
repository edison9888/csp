package com.taobao.monitor.common.ao.day;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.db.impl.day.ReportDataDao;
import com.taobao.monitor.common.po.ReportBasicDataPo;
import com.taobao.monitor.common.po.ReportInvokeDataPo;

public class ReportDataDaoAo {
	
	private static ReportDataDaoAo ao = new ReportDataDaoAo();
	
	private ReportDataDao dao = new ReportDataDao();
	
	public static ReportDataDaoAo get() {
		return ao;
	}
	
	public List<ReportBasicDataPo> findBasicReportData(String appName,Date startDate,Date endDate) {
		return dao.findBasicReportData(appName, startDate, endDate);
	}
	
	public void addBasicReportData(ReportBasicDataPo po) {
		 dao.addBasicReportData(po);
	}
	
	public Map<String, List<ReportInvokeDataPo>> findInvokeReportData(String appName,Date startDate,Date endDate) {
		return dao.findInvokeReportData(appName, startDate, endDate);
	}
	
	public void addInvokeReportData(ReportInvokeDataPo po) {
		dao.addInvokeReportData(po);
	}

}
