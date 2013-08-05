package com.taobao.monitor.web.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.ao.capacity.CspCapacityAo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.ao.day.ReportDataDaoAo;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityRankingPo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.po.ReportBasicDataPo;
import com.taobao.monitor.common.po.ReportInvokeDataPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
import com.taobao.monitor.common.util.GroupManager;
import com.taobao.monitor.common.util.KeyConstants;
import com.taobao.monitor.web.ao.MonitorDayAo;

public class CollectReportDataJob implements Job {
	
	public static Logger logger = Logger.getLogger(CollectReportDataJob.class);
	
	/*** 计算指定日期  ***/
	private Date date;
	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		
		Date date = this.getDate() == null ? calendar.getTime() : this.getDate();
		List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();
		
		ExecutorService threadPool = Executors.newFixedThreadPool(16);
		
		for(AppInfoPo po : appList) {
			SyncDataTask task = new SyncDataTask(date, po);
			threadPool.execute(task);
		}
		
		threadPool.shutdown();
	}
	
	
	
	public static void main(String [] args) throws JobExecutionException {
		CollectReportDataJob job = new CollectReportDataJob();
		
		if (args != null && args.length > 0) {
			SimpleDateFormat sf = new SimpleDateFormat();
			try {
				job.setDate(sf.parse(args[0].trim()));
			} catch (ParseException e) {
				logger.info("param error");
			}
		}
		job.execute(null);
		
	}

}

class SyncDataTask implements Runnable {
	
	public static Logger logger = Logger.getLogger(SyncDataTask.class);
	
	private Date date;
	
	private AppInfoPo po;
	
	public SyncDataTask(Date date, AppInfoPo po) {
		this.date = date;
		this.po = po;
	}

	@Override
	public void run() {
		insertReportData(date, po);
	}
	
	private void insertReportData(Date date, AppInfoPo po) {
		if (po.getAppType() == null || (!po.getAppType().equals("pv") && !po.getAppType().equals("center"))) return;
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = df.format(date);
		
		// 应用名
		int appId = po.getAppDayId();
		String appName = po.getOpsName();
		
		// 机器信息
		int [] machines = CspCacheTBHostInfos.get().getHostType(appName);
		String machineInfo = machines[0] + "/" + machines[1];
		
		// load
		KeyValuePo loadPo = MonitorDayAo.get().findKeyValueFromCountByDate(KeyConstants.LOAD, appId, sDate);
		double load = loadPo == null ? 0 : Double.parseDouble(loadPo.getValueStr());
		
		// pv
		int pvKey = po.getAppType().equals("pv") ? KeyConstants.URL_PV : KeyConstants.HSF_PV;
		KeyValuePo pvPo = MonitorDayAo.get().findKeyValueFromCountByDate(pvKey, appId, sDate);
		long pv = pvPo == null ? 0 : Long.parseLong(pvPo.getValueStr());
		
		// qps
		int qpsKey = po.getAppType().equals("pv") ? KeyConstants.URL_QPS : KeyConstants.HSF_QPS;
		KeyValuePo qpsPo = MonitorDayAo.get().findKeyValueFromCountByDate(qpsKey, appId, sDate);
		double qps = qpsPo == null ? 0 : Double.parseDouble(qpsPo.getValueStr());
		
		// rt
		int rtKey = po.getAppType().equals("pv") ? KeyConstants.URL_RT : KeyConstants.HSF_RT;
		KeyValuePo rtPo = MonitorDayAo.get().findKeyValueFromCountByDate(rtKey, appId, sDate);
		double rt = rtPo == null ? 0 : Double.parseDouble(rtPo.getValueStr());
		if (po.getAppType().equals("pv")) {
			rt = (double)((int)(rt / 1000 * 100)) / 100;
		}
		
		// full gc count
		KeyValuePo fullGcCountPo = MonitorDayAo.get().findKeyValueFromCountByDate(KeyConstants.FULL_GC_COUNT, appId, sDate);
		int fullGcCount = fullGcCountPo == null ? 0 : (int)Double.parseDouble(fullGcCountPo.getValueStr());
		
		// full gc time
		KeyValuePo fullGcTimePo = MonitorDayAo.get().findKeyValueFromCountByDate(KeyConstants.FULL_GC_TIME, appId, sDate);
		double fullGcTime = fullGcTimePo == null ? 0 : Double.parseDouble(fullGcTimePo.getValueStr());
		
		// young gc count
		KeyValuePo youngGcCountPo = MonitorDayAo.get().findKeyValueFromCountByDate(KeyConstants.YOUNG_GC_COUNT, appId, sDate);
		int youngGcCount = youngGcCountPo == null ? 0 : (int)Double.parseDouble(youngGcCountPo.getValueStr());
		
		// yong gc time
		KeyValuePo youngGcTimePo = MonitorDayAo.get().findKeyValueFromCountByDate(KeyConstants.YOUNG_GC_TIME, appId, sDate);
		double youngGcTime = youngGcTimePo == null ? 0 : Double.parseDouble(youngGcTimePo.getValueStr());
		
		// capacity
		double capacityLevel = 0;
		CapacityRankingPo rankPo = CspCapacityAo.get().findCapacityLatestRankingPo(GroupManager.getAppId(po.getAppId()));
		if (rankPo != null) {
			capacityLevel = rankPo.getCData();
		}
		
		// cost
		double singleCost = 0;
		Map<String, String> costMap = CspCapacityAo.get().findLatestCapacityCostByApp(appName);
		if (costMap.containsKey("totalPerCost")) {
			singleCost = Double.parseDouble(costMap.get("totalPerCost"));
		}
		
		// uv
		int uv = 0;
		
		// 前5的url信息<url,num>
		List<ReportInvokeDataPo> urls = CspDependInfoAo.get().getTopNUrls(5, appName, date);
		for (ReportInvokeDataPo urlPo : urls) {
			logger.info(urlPo);
			ReportDataDaoAo.get().addInvokeReportData(urlPo);
			
			String url = urlPo.getResouceName();
			String noProtocalUrl = url.substring(7);
			String [] array = noProtocalUrl.split("/");
			if (array.length > 0) {
				String domain = array[0];
				int domainUv = CspDependInfoAo.get().getLatestUvByDomain(domain);
				if (domainUv > uv) {
					uv =  domainUv;
				}
			}
		}
		
		// 前5的接口提供信息<interface,num>
		List<ReportInvokeDataPo> ins = CspDependInfoAo.get().getTopNInInterface(5, appName, date);
		for (ReportInvokeDataPo inPo : ins) {
			logger.info(inPo);
			ReportDataDaoAo.get().addInvokeReportData(inPo);
		}
		
		// 前5的接口调用信息<interface,num>
		List<ReportInvokeDataPo> outs = CspDependInfoAo.get().getTopNOutInterface(5, appName, date);
		for (ReportInvokeDataPo outPo : outs) {
			logger.info(outPo);
			ReportDataDaoAo.get().addInvokeReportData(outPo);
		}
		
		ReportBasicDataPo basicDataPo = new ReportBasicDataPo();
		basicDataPo.setAppName(appName);
		basicDataPo.setMachineInfo(machineInfo);
		basicDataPo.setLoad(load);
		basicDataPo.setPv(pv);
		basicDataPo.setUv(uv);
		basicDataPo.setQps(qps);
		basicDataPo.setRt(rt);
		basicDataPo.setCapacityLevel(capacityLevel);
		basicDataPo.setSingleCost(singleCost);
		basicDataPo.setFullGcCount(fullGcCount);
		basicDataPo.setFullGcTime(fullGcTime);
		basicDataPo.setYoungGcCount(youngGcCount);
		basicDataPo.setYoungGcTime(youngGcTime);
		basicDataPo.setCollectDate(date);
		
		logger.info(basicDataPo);
		ReportDataDaoAo.get().addBasicReportData(basicDataPo);
	}
	
}
