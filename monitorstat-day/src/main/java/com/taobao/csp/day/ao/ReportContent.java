package com.taobao.csp.day.ao;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.csp.day.dao.AnalyseLogDao;
import com.taobao.csp.day.po.ApacheSpecialPo;
import com.taobao.csp.day.po.SphPo;
import com.taobao.csp.day.po.TddlPo;
import com.taobao.csp.day.po.TddlSummaryPo;
import com.taobao.csp.day.po.TdodPo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;

/***
 * 日报数据入库
 * 
 * @author youji.zj
 * @version 1.0 2012-09-06
 *
 */
public class ReportContent implements ReportContentInterface {

	private static Logger log = Logger.getLogger(ReportContent.class);

	private AnalyseLogDao dao = new AnalyseLogDao();

	private ConcurrentHashMap<String, AppInfoPo> appCacheMap = new ConcurrentHashMap<String, AppInfoPo>();//
	
	public AppInfoPo getAppByName(String opsName) {
		AppInfoPo po = appCacheMap.get(opsName);
		
		// 缓存没有上数据库查，查到后更新缓存
		if (po == null) {
			po = AppInfoAo.get().getAppInfoByOpsName(opsName);
			if (po != null && po.getDayDeploy() == 1) {
				appCacheMap.put(opsName, po);
			}
		}
		
		return po;
	}

	private ReportContent() {
		// 将所有应用放入内存中
		log.info("载入应用名称");
		List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();
		for (AppInfoPo po : appList) {

			// 如果日报无效，则不需要此应用
			if (po.getDayDeploy() != 1) {
				continue;
			}

			appCacheMap.put(po.getOpsName(), po);
		}
	}
	
	public static ReportContent content = new ReportContent();
	
	public static ReportContent getInstance() {
		return content;
	}
	
	@Override
	public void putReportDateOfTempTDDL(List<TddlPo> tddlList) {
		dao.addMonitorDataOfTempTDDL(tddlList);
	}
	
	@Override
	public void putReportDateOfDayTDDL(List<TddlPo> tddlList) {
		dao.addMonitorDataOfDayTDDL(tddlList);
	}
	
	@Override
	public void putReportDateOfDayTDDLSummaryNew(Date date) {
		dao.addMonitorDataOfDayTDDLSummeryNew(date);
	}
	

	@Override
	public void updateMaxResponseOfDayTDDLSummaryNew(TddlSummaryPo po) {
		dao.updateMaxResponseOfDayTDDLSummaryNew(po);
	}

	@Override
	public void updateMinResponseOfDayTDDLSummaryNew(TddlSummaryPo po) {
		dao.updateMinResponseOfDayTDDLSummaryNew(po);
	}
	
	@Override
	public void deleteTempTddl(String tableName, String collectTime) {
		dao.deleteTempTddl(tableName, collectTime);
	}

	@Override
	public List<TddlPo> findReportDataOfTddlTemp(String tableName,
			String startTime, String endTime, String appName, int oneTime,
			int index) {
		return dao.findTddlDataFromTemp(tableName, startTime, endTime, appName, oneTime, index);
	}

	@Override
	public void putReportDateOfDayTDDLSummaryNew(List<TddlPo> tddlList) {
		dao.addMonitorDataOfDayTDDLSummeryNew(tddlList);

	}

	@Override
	public void putReportDateOfSph(List<SphPo> sphList) {
		dao.addMonitorDataOfSph(sphList);
	}

	@Override
	public void putReportDateOfApacheSpecial(List<ApacheSpecialPo> list) {
		dao.addMonitorDataOfApacheSpecial(list);
	}

	@Override
	public void putReportDateOfTdod(List<TdodPo> list) {
		dao.addMonitorDataOfTdod(list);
	}
}
