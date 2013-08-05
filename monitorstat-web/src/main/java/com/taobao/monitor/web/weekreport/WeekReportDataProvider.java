package com.taobao.monitor.web.weekreport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.ao.day.ReportDataDaoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ReportBasicDataPo;
import com.taobao.monitor.common.po.ReportInvokeDataPo;
import com.taobao.monitor.common.util.Utlitites;

/***
 * 
 * 提供的信息包含：
 * pv
 * uv
 * qps
 * rt
 * load
 * full gc 次数
 * full gc 时间
 * young gc 次数
 * young gc 时间
 * 
 * 容量信息
 * 
 * 界面可以选择性进行展示
 * 
 * @author youji.zj
 *
 */
public class WeekReportDataProvider {
	
	/***   basic data of current week ***/
	private Map<String, List<ReportBasicDataPo>> basicInfo = new HashMap<String, List<ReportBasicDataPo>>();
	
	/***   invoke data of current week ***/
	private Map<String, Map<String, List<ReportInvokeDataPo>>> invokeInfo = new HashMap<String, Map<String, List<ReportInvokeDataPo>>>();
	
	/*** basic data of last week ***/
	private Map<String, List<ReportBasicDataPo>> lastWeekBasicInfo = new HashMap<String, List<ReportBasicDataPo>>();
	
	/*** invoke data of last week ***/
	private Map<String, Map<String, List<ReportInvokeDataPo>>> lastWeekInvokeInfo = new HashMap<String, Map<String, List<ReportInvokeDataPo>>>();
	
	/*** trender: app_name,key,trender ***/
	private Map<String, Map<String, Double>> trenders  = new HashMap<String, Map<String,Double>>();
	
	/*** max: app_name,key,trender ***/
	private Map<String, Map<String, Double>> maxers  = new HashMap<String, Map<String,Double>>();
	
	private Date startDate;
	
	private Date endDate;
	
	private Date lastWeekStartDate;
	
	private Date lastWeekEndDate;
	
	private List<AppInfoPo> apps;
	
	// latest day basic data
	private Map<String, ReportBasicDataPo> currentBasicDatas = new HashMap<String, ReportBasicDataPo>();
	
	public WeekReportDataProvider(Date startDate, Date endDate, List<AppInfoPo> apps) {
		this.startDate = startDate;
		this.endDate = endDate;
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(startDate);
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		lastWeekStartDate = calendar.getTime();
		
		calendar.setTime(endDate);
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		lastWeekEndDate = calendar.getTime();
		
		this.apps = apps;
		init();
	}
	
	public ReportBasicDataPo latestBasicDataPo(String appName) {
		if (currentBasicDatas.containsKey(appName)) {
			return currentBasicDatas.get(appName);
		} else {
			return new ReportBasicDataPo();
		}
	}
	
	/***
	 * max value of this week
	 * @param appName
	 * @param keyName
	 * @return
	 */
	public String maxKeyValue(String appName, String keyName) {
		double maxValue = 0d;
		
		if (maxers.containsKey(appName)) {
			Map<String, Double> keyMaxs = maxers.get(appName);
			if (keyMaxs.containsKey(keyName)) {
				maxValue = keyMaxs.get(keyName);
			}
		}
		
		return Utlitites.formatDouble(maxValue);
	}
	
	/***
	 * trend between this week and last week
	 * @param appName
	 * @param keyName
	 * @return
	 */
	public String keyRisingTrendStr(String appName, String keyName){
		double scale = 100.00d;
		
		if (trenders.containsKey(appName)) {
			Map<String, Double> keyTrenders = trenders.get(appName);
			if (keyTrenders.containsKey(keyName)) {
				scale = keyTrenders.get(keyName);
			}
		}
		
		if (scale < 100) {
			return "<font style=\"color: green;\"> " + scale+ "% ↓</font>";
		} else if (scale == 100) {
			return "100.00%";
		} else {
			return "<font style=\"color: red;\"> " + scale + "% ↑</font>";
		}
	}
	
	/***
	 * invoke information of this week
	 * @param appName
	 * @param keyName
	 * @return
	 */
	public List<ReportInvokeDataPo> invokeInfosThisWeek(String appName, String keyName) {
		List<ReportInvokeDataPo> list = new ArrayList<ReportInvokeDataPo>();
		if (invokeInfo.containsKey(appName)) {
			Map<String, List<ReportInvokeDataPo>> invokes = invokeInfo.get(appName);
			if (invokes.containsKey(keyName)) {
				list = invokes.get(keyName);
			}
		}
		
		return list;
	}
	
	/***
	 * invoke information of last week
	 * @param appName
	 * @param keyName
	 * @return
	 */
	public List<ReportInvokeDataPo> invokeInfosLastWeek(String appName, String keyName) {
		List<ReportInvokeDataPo> list = new ArrayList<ReportInvokeDataPo>();
		if (lastWeekInvokeInfo.containsKey(appName)) {
			Map<String, List<ReportInvokeDataPo>> invokes = lastWeekInvokeInfo.get(appName);
			if (invokes.containsKey(keyName)) {
				list = invokes.get(keyName);
			}
		}
		
		return list;
	}
	
	/***
	 * 准备数据
	 */
	private void init() {
		// 获取基础数据（basic info和invoke info）
		for (AppInfoPo po : apps) {
			List<ReportBasicDataPo> basics = ReportDataDaoAo.get().findBasicReportData(po.getOpsName(), startDate, endDate);
			basicInfo.put(po.getOpsName(), basics);
			
			// 最新的basic信息
			if (basics.size() > 0) {
				currentBasicDatas.put(po.getOpsName(), basics.get(0));
			} 
			
			List<ReportBasicDataPo> lastWeekBasics = ReportDataDaoAo.get().findBasicReportData(po.getOpsName(), lastWeekStartDate, lastWeekEndDate);
			lastWeekBasicInfo.put(po.getOpsName(), lastWeekBasics);
			
			Map<String, List<ReportInvokeDataPo>> invoke = ReportDataDaoAo.get().findInvokeReportData(po.getOpsName(), startDate, endDate);
			invokeInfo.put(po.getOpsName(), invoke);
			
			Map<String, List<ReportInvokeDataPo>> lastWeekInvoke = ReportDataDaoAo.get().findInvokeReportData(po.getOpsName(), lastWeekStartDate, lastWeekEndDate);
			lastWeekInvokeInfo.put(po.getOpsName(), lastWeekInvoke);
			
			
		}
		
		prepareConcretData();
	}
	
	/***
	 * 计算最大值与趋势数据
	 */
	private void prepareConcretData() {
		for (AppInfoPo po : apps) {
			Map<String, Double> keyTrender = new HashMap<String, Double>();
			Map<String, Double> keyMaxer = new HashMap<String, Double>();
			
			trenders.put(po.getOpsName(), keyTrender);
			maxers.put(po.getOpsName(), keyMaxer);
			
			// initial value;
			keyTrender.put("load", 100d);
			keyTrender.put("pv", 100d);
			keyTrender.put("uv", 100d);
			keyTrender.put("qps", 100d);
			keyTrender.put("rt", 100d);
			keyTrender.put("fGcCount", 100d);
			keyTrender.put("fGcTime", 100d);
			keyTrender.put("yGcCount", 100d);
			keyTrender.put("yGcTime", 100d);
			keyTrender.put("capacityLevel", 100d);
			keyTrender.put("singleCost", 100d);
			
			keyMaxer.put("load", 0d);
			keyMaxer.put("pv", 0d);
			keyMaxer.put("uv", 0d);
			keyMaxer.put("qps", 0d);
			keyMaxer.put("rt", 0d);
			keyMaxer.put("fGcCount", 0d);
			keyMaxer.put("fGcTime", 0d);
			keyMaxer.put("yGcCount", 0d);
			keyMaxer.put("yGcTime", 0d);
			keyMaxer.put("capacityLevel", 0d);
			keyMaxer.put("singleCost", 0d);
			
			int count = 0;
			double totalLoad = 0;
			long totalPv = 0;
			int totalUv = 0;
			double totalQps = 0;
			double totalRt = 0;
			int totalFgcCount = 0;
			double totalFgcTime = 0;
			int totalYgcCount = 0;
			double totalYgcTime = 0;
			double totalCapacityLevel = 0;
			double totalSingleCost = 0;
			List<ReportBasicDataPo> basics = basicInfo.get(po.getOpsName());
			for (ReportBasicDataPo bPo : basics) {
				count ++;
				totalLoad += bPo.getLoad();
				totalPv += bPo.getPv();
				totalUv += bPo.getUv();
				totalQps += bPo.getQps();
				totalRt += bPo.getRt();
				totalFgcCount += bPo.getFullGcCount();
				totalFgcTime += bPo.getFullGcTime();
				totalYgcCount += bPo.getYoungGcCount();
				totalYgcTime += bPo.getYoungGcTime();
				totalCapacityLevel += bPo.getCapacityLevel();
				totalSingleCost += bPo.getSingleCost();
				
				// max values
				if (bPo.getLoad() > keyMaxer.get("load")) keyMaxer.put("load", bPo.getLoad());
				if (bPo.getPv() > keyMaxer.get("pv")) keyMaxer.put("pv", (double)bPo.getPv());
				if (bPo.getUv() > keyMaxer.get("uv")) keyMaxer.put("uv", (double)bPo.getUv());
				if (bPo.getQps() > keyMaxer.get("qps")) keyMaxer.put("qps", bPo.getQps());
				if (bPo.getRt() > keyMaxer.get("rt")) keyMaxer.put("rt", bPo.getRt());
				if (bPo.getFullGcCount() > keyMaxer.get("fGcCount")) keyMaxer.put("fGcCount", (double)bPo.getFullGcCount());
				if (bPo.getFullGcTime() > keyMaxer.get("fGcTime")) keyMaxer.put("fGcTime", bPo.getFullGcTime());
				if (bPo.getYoungGcCount() > keyMaxer.get("yGcCount")) keyMaxer.put("yGcCount", (double)bPo.getYoungGcCount());
				if (bPo.getYoungGcTime() > keyMaxer.get("yGcTime")) keyMaxer.put("yGcTime", bPo.getYoungGcTime());
				if (bPo.getCapacityLevel() > keyMaxer.get("capacityLevel")) keyMaxer.put("capacityLevel", bPo.getCapacityLevel());
				if (bPo.getSingleCost() > keyMaxer.get("singleCost")) keyMaxer.put("singleCost", bPo.getSingleCost());
			}
			
			int lCount = 0;
			double lTotalLoad = 0;
			long lTotalPv = 0;
			int lTotalUv = 0;
			double lTotalQps = 0;
			double lTotalRt = 0;
			int lTotalFgcCount = 0;		
			double lTotalFgcTime = 0;
			int lTotalYgcCount = 0;
			double lTotalYgcTime = 0;
			double lTotalCapacityLevel = 0;
			double lTotalSingleCost = 0;
			List<ReportBasicDataPo> lBasics = lastWeekBasicInfo.get(po.getOpsName());
			for (ReportBasicDataPo bPo : lBasics) {
				lCount ++;
				lTotalLoad += bPo.getLoad();
				lTotalPv += bPo.getPv();
				lTotalUv += bPo.getUv();
				lTotalQps += bPo.getQps();
				lTotalRt += bPo.getRt();
				lTotalFgcCount += bPo.getFullGcCount();
				lTotalFgcTime += bPo.getFullGcTime();
				lTotalYgcCount += bPo.getYoungGcCount();
				lTotalYgcTime += bPo.getYoungGcTime();
				lTotalCapacityLevel += bPo.getCapacityLevel();
				lTotalSingleCost += bPo.getSingleCost();
			}
			
			if (count == 0 || lCount == 0) continue;
			
			DecimalFormat df = new DecimalFormat("00.00");
			if (lTotalLoad != 0) {
				keyTrender.put("load", Double.parseDouble(df.format((totalLoad / count) / (lTotalLoad / lCount) * 100 )));
			}
			if (lTotalPv != 0) {
				keyTrender.put("pv", Double.parseDouble(df.format(((double)totalPv / count) / ((double)lTotalPv / lCount) * 100 )));
			}
			if (lTotalUv != 0) {
				keyTrender.put("uv", Double.parseDouble(df.format(((double)totalUv / count) / ((double)lTotalUv / lCount) * 100 )));
			}
			if (lTotalQps != 0) {
				keyTrender.put("qps", Double.parseDouble(df.format((totalQps / count) / (lTotalQps / lCount) * 100 )));
			}
			if (lTotalRt != 0) {
				keyTrender.put("rt", Double.parseDouble(df.format((totalRt / count) / (lTotalRt / lCount) * 100 )));
			}
			if (lTotalFgcCount != 0) {
				keyTrender.put("fGcCount", Double.parseDouble(df.format(((double)totalFgcCount / count) / ((double)lTotalFgcCount / lCount) * 100 )));
			}
			if (lTotalFgcTime != 0) {
				keyTrender.put("fGcTime", Double.parseDouble(df.format((totalFgcTime / count) / (lTotalFgcTime / lCount) * 100 )));
			}
			if (lTotalYgcCount != 0) {
				keyTrender.put("yGcCount", Double.parseDouble(df.format(((double)totalYgcCount / count) / ((double)lTotalYgcCount / lCount))));
			}
			if (lTotalYgcTime != 0) {
				keyTrender.put("yGcTime", Double.parseDouble(df.format((totalYgcTime / count) / (lTotalYgcTime / lCount))));
			}
		}
		
	}

}
