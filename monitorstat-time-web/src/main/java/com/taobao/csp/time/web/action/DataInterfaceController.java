package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.text.DecimalFormat;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/***
 * 数据接口
 * @author youji.zj
 * @version 2012-07-23
 *
 */
@Controller
@RequestMapping("/data.do")
public class DataInterfaceController extends BaseController{
	
	public static Logger logger = Logger.getLogger(DataInterfaceController.class);
	
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	
	@RequestMapping(params = "method=queryPerformaceData")
	public void queryPerformaceData(HttpServletResponse response, int appId, String date) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		if (appInfo == null) return;
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date sDate = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sDate = df.parse(date);
			calendar.setTime(sDate);
		} catch (Exception e) {
			return;
		}

		String appName = appInfo.getAppName();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		List<HostPo> ipList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appInfo.getAppName());
		int hostSize = ipList.size();
		if (hostSize == 0) {
			logger.info("机器数为0");
			return;
		}
		Set<String> sampleIps = new HashSet<String>();
		for (int i = 0; i < 3 && i < hostSize; i++) {
			HostPo hostPo = ipList.get(i);
			sampleIps.add(hostPo.getHostIp());
		}
		
		// 系统数据
		Map<String, TimeDataInfo> cpuMap = prepareDetailDataHost(appName, KeyConstants.TOPINFO, PropConstants.CPU, sampleIps, sDate);
		Map<String, TimeDataInfo> loadMap = prepareDetailDataHost(appName, KeyConstants.TOPINFO, PropConstants.LOAD, sampleIps, sDate);
		Map<String, TimeDataInfo> swapMap = prepareDetailDataHost(appName, KeyConstants.TOPINFO, PropConstants.SWAP, sampleIps, sDate);
		Map<String, TimeDataInfo> jvmMemoryMap = prepareDetailDataHost(appName, KeyConstants.JVMINFO, PropConstants.JVMMEMORY, sampleIps, sDate);
		// iowait没有

		// 访问数据
		Map<String, TimeDataInfo> pvMap = prepareDetailData(appName, KeyConstants.PV, PropConstants.E_TIMES, sDate);
		// qps通过pv计算
		Map<String, TimeDataInfo> pvRt = prepareDetailData(appName, KeyConstants.PV, PropConstants.C_TIME, sDate);
		Map<String, TimeDataInfo> hsfMap = prepareDetailData(appName, KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES, sDate);
		// qps通过pv计算
		Map<String, TimeDataInfo> hsfRt = prepareDetailData(appName, KeyConstants.HSF_PROVIDER, PropConstants.C_TIME, sDate);
		
		// gc信息
		Map<String, TimeDataInfo> gcsMap = prepareDetailDataHost(appName, KeyConstants.JVMINFO, PropConstants.JVMGC, sampleIps, sDate);
		Map<String, TimeDataInfo> cmsRemarkMap = prepareDetailDataHost(appName, KeyConstants.JVMINFO, PropConstants.JVMCMSGC, sampleIps, sDate);
		// GC_CMS-initial-mark次数 
		// GC_CMS-remark时间 
		// GC_CMS-initial-mark时间 
		
		SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
		DecimalFormat dFormat = new DecimalFormat("0.00");
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		for (int i = 0; i < 24; i++) {
			calendar.set(Calendar.HOUR_OF_DAY, i);
			for(int j = 0; j < 60; j++) {
				calendar.set(Calendar.MINUTE, j);
				String time = df1.format(calendar.getTime());
				
				Map<String, Object> info = new HashMap<String, Object>();
				dataList.add(info);
				info.put("时间", time);
				Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();
				info.put("数据", values);
				
				Map<String, String> systemMap = new LinkedHashMap<String, String>();  // 系统数据
				values.put("系统数据", systemMap);
				TimeDataInfo cpuInfo = cpuMap.get(time);
				double cpu = cpuInfo == null ? 0 : cpuInfo.getMainValue();
				systemMap.put("CPU", dFormat.format(cpu));
				
				TimeDataInfo loadInfo = loadMap.get(time);
				double load = loadInfo == null ? 0 : loadInfo.getMainValue();
				systemMap.put("LOAD", dFormat.format(load));
				
				TimeDataInfo jvmMeInfo = jvmMemoryMap.get(time);
				double jvmMem = jvmMeInfo == null ? 0 : jvmMeInfo.getMainValue();
				systemMap.put("JVM Mem", dFormat.format(jvmMem));
				
				TimeDataInfo swapInfo = swapMap.get(time);
				double swap = swapInfo == null ? 0 : swapInfo.getMainValue();
				systemMap.put("Swap", dFormat.format(swap));
			
				
				Map<String, String> accessMap = new LinkedHashMap<String, String>();  
				values.put("访问信息", accessMap);
				TimeDataInfo pvInfo = pvMap.get(time);
				double pv = pvInfo == null ? 0 : pvInfo.getMainValue() / hostSize;
				accessMap.put("PV", dFormat.format(pv));
				accessMap.put("Qps", dFormat.format(pv / 60));
				
				TimeDataInfo rtInfo = pvRt.get(time);
				double rt = rtInfo == null ? 0 : rtInfo.getMainValue();
				accessMap.put("Rt", dFormat.format(rt));
				
				TimeDataInfo hsfInfo = hsfMap.get(time);
				double hsf = hsfInfo == null ? 0 : hsfInfo.getMainValue() / hostSize;
				accessMap.put("hsf接口调用总量", dFormat.format(hsf));
				accessMap.put("hsf接口QPS", dFormat.format(hsf / 60));
				
				TimeDataInfo hsfRtInfo = hsfRt.get(time);
				double hRt = hsfRtInfo == null ? 0 : hsfRtInfo.getMainValue();
				accessMap.put("接口Rt", dFormat.format(hRt));
				
				Map<String, String> gcMap = new LinkedHashMap<String, String>();  
				values.put("GC信息", gcMap);
				TimeDataInfo gcInfo = gcsMap.get(time);
				double gc = gcInfo == null ? 0 : gcInfo.getMainValue();
				gcMap.put("GC_GC次数", dFormat.format(gc));
				
				TimeDataInfo cmsRemarkInfo = cmsRemarkMap.get(time);
				double cmsRemark = cmsRemarkInfo == null ? 0 : cmsRemarkInfo.getMainValue();
				gcMap.put("GC_CMS-remark次数", dFormat.format(cmsRemark));
			}
		}
		
		try {
			writeJSONToResponseJSONArray(response, dataList);
		} catch (IOException e) {
		}

	}
	
	@RequestMapping(params = "method=queryPerformaceDataHost")
	public void queryPerformaceDataHost(HttpServletResponse response, int appId, String date, String ip) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		if (appInfo == null) return;
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date sDate = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sDate = df.parse(date);
			calendar.setTime(sDate);
		} catch (Exception e) {
			return;
		}

		String appName = appInfo.getAppName();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		
		Set<String> sampleIps = new HashSet<String>();
		sampleIps.add(ip);
		
		// 系统数据
		Map<String, TimeDataInfo> cpuMap = prepareDetailDataHost(appName, KeyConstants.TOPINFO, PropConstants.CPU, sampleIps, sDate);
		Map<String, TimeDataInfo> loadMap = prepareDetailDataHost(appName, KeyConstants.TOPINFO, PropConstants.LOAD, sampleIps, sDate);
		Map<String, TimeDataInfo> swapMap = prepareDetailDataHost(appName, KeyConstants.TOPINFO, PropConstants.SWAP, sampleIps, sDate);
		Map<String, TimeDataInfo> jvmMemoryMap = prepareDetailDataHost(appName, KeyConstants.JVMINFO, PropConstants.JVMMEMORY, sampleIps, sDate);
		// iowait没有

		// 访问数据
		Map<String, TimeDataInfo> pvMap = prepareDetailDataHost(appName, KeyConstants.PV, PropConstants.E_TIMES, sampleIps, sDate);
		// qps通过pv计算
		Map<String, TimeDataInfo> pvRt = prepareDetailDataHost(appName, KeyConstants.PV, PropConstants.C_TIME, sampleIps, sDate);
		Map<String, TimeDataInfo> hsfMap = prepareDetailDataHost(appName, KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES, sampleIps, sDate);
		// qps通过pv计算
		Map<String, TimeDataInfo> hsfRt = prepareDetailDataHost(appName, KeyConstants.HSF_PROVIDER, PropConstants.C_TIME, sampleIps, sDate);
		
		// gc信息
		Map<String, TimeDataInfo> gcsMap = prepareDetailDataHost(appName, KeyConstants.JVMINFO, PropConstants.JVMGC, sampleIps, sDate);
		Map<String, TimeDataInfo> cmsRemarkMap = prepareDetailDataHost(appName, KeyConstants.JVMINFO, PropConstants.JVMCMSGC, sampleIps, sDate);
		// GC_CMS-initial-mark次数 
		// GC_CMS-remark时间 
		// GC_CMS-initial-mark时间 
		
		SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
		DecimalFormat dFormat = new DecimalFormat("0.00");
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		for (int i = 0; i < 24; i++) {
			calendar.set(Calendar.HOUR_OF_DAY, i);
			for(int j = 0; j < 60; j++) {
				calendar.set(Calendar.MINUTE, j);
				String time = df1.format(calendar.getTime());
				
				Map<String, Object> info = new HashMap<String, Object>();
				dataList.add(info);
				info.put("时间", time);
				Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();
				info.put("数据", values);
				
				Map<String, String> systemMap = new LinkedHashMap<String, String>();  // 系统数据
				values.put("系统数据", systemMap);
				TimeDataInfo cpuInfo = cpuMap.get(time);
				double cpu = cpuInfo == null ? 0 : cpuInfo.getMainValue();
				systemMap.put("CPU", dFormat.format(cpu));
				
				TimeDataInfo loadInfo = loadMap.get(time);
				double load = loadInfo == null ? 0 : loadInfo.getMainValue();
				systemMap.put("LOAD", dFormat.format(load));
				
				TimeDataInfo jvmMeInfo = jvmMemoryMap.get(time);
				double jvmMem = jvmMeInfo == null ? 0 : jvmMeInfo.getMainValue();
				systemMap.put("JVM Mem", dFormat.format(jvmMem));
				
				TimeDataInfo swapInfo = swapMap.get(time);
				double swap = swapInfo == null ? 0 : swapInfo.getMainValue();
				systemMap.put("Swap", dFormat.format(swap));
			
				
				Map<String, String> accessMap = new LinkedHashMap<String, String>();  
				values.put("访问信息", accessMap);
				TimeDataInfo pvInfo = pvMap.get(time);
				double pv = pvInfo == null ? 0 : pvInfo.getMainValue();
				accessMap.put("PV", dFormat.format(pv));
				accessMap.put("Qps", dFormat.format(pv / 60));
				
				TimeDataInfo rtInfo = pvRt.get(time);
				double rt = rtInfo == null ? 0 : rtInfo.getMainValue();
				accessMap.put("Rt", dFormat.format(rt));
				
				TimeDataInfo hsfInfo = hsfMap.get(time);
				double hsf = hsfInfo == null ? 0 : hsfInfo.getMainValue();
				accessMap.put("hsf接口调用总量", dFormat.format(hsf));
				accessMap.put("hsf接口QPS", dFormat.format(hsf / 60));
				
				TimeDataInfo hsfRtInfo = hsfRt.get(time);
				double hRt = hsfRtInfo == null ? 0 : hsfRtInfo.getMainValue();
				accessMap.put("接口Rt", dFormat.format(hRt));
				
				Map<String, String> gcMap = new LinkedHashMap<String, String>();  
				values.put("GC信息", gcMap);
				TimeDataInfo gcInfo = gcsMap.get(time);
				double gc = gcInfo == null ? 0 : gcInfo.getMainValue();
				gcMap.put("GC_GC次数", dFormat.format(gc));
				
				TimeDataInfo cmsRemarkInfo = cmsRemarkMap.get(time);
				double cmsRemark = cmsRemarkInfo == null ? 0 : cmsRemarkInfo.getMainValue();
				gcMap.put("GC_CMS-remark次数", dFormat.format(cmsRemark));
			}
		}
		
		try {
			writeJSONToResponseJSONArray(response, dataList);
		} catch (IOException e) {
		}

	}
	
	private Map<String, TimeDataInfo> prepareDetailData(String appName, String key,String mainProp,Date sDate) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		List<TimeDataInfo> list = commonService.queryKeyDataHistory(appName, key, mainProp, sDate);
		Map<String, TimeDataInfo> map = new HashMap<String, TimeDataInfo>();
		for (TimeDataInfo info : list) {
			Date date = new Date(info.getTime());
			map.put(df.format(date), info);
		}
		
		return map;
	}
	
	private Map<String, TimeDataInfo> prepareDetailDataHost(String appName, String key,String mainProp, Set<String> sampleIps, Date sDate) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Map<String, TimeDataInfo> map = new HashMap<String, TimeDataInfo>();
		
		for (String ip : sampleIps) {
			List<TimeDataInfo> list = commonService.queryKeyDataHistory(appName, key, mainProp, ip, sDate);
			for (TimeDataInfo info : list) {
				Date date = new Date(info.getTime());
				String timeKey = df.format(date);
				
				// 已经存在取均值
				if (map.containsKey(timeKey)) {
					TimeDataInfo oldValue = map.get(timeKey);
					info.setMainValue((oldValue.getMainValue() + info.getMainValue() / 2.0));
				} 
				
				map.put(timeKey, info);
			}
		}
		
		return map;
	}
	
	public static void main(String [] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		
	}
}
