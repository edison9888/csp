package com.taobao.csp.loadrun.web.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.assign.master.JobFeature;
import com.taobao.csp.assign.master.SlaveProxy;
import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.LoadrunResultDetail;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.run.BaseLoadrunTask;
import com.taobao.csp.loadrun.web.LoadRunHost;
import com.taobao.csp.loadrun.web.LoadrunTaskMaster;
import com.taobao.csp.loadrun.web.LoadrunWebContainer;
import com.taobao.csp.loadrun.web.action.show.LoadrunReportObject;
import com.taobao.csp.loadrun.web.bo.CspLoadRunBo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.util.Arith;

/**
 * 
 * @author xiaodu
 * @version 2011-7-10 下午10:12:11
 */
@Controller
@RequestMapping("/loadrun/show.do")
public class LoadrunShowAction {

	private static final Logger logger = Logger.getLogger(LoadrunShowAction.class);

	@Resource(name = "cspLoadRunBo")
	private CspLoadRunBo cspLoadRunBo;
	
	@Resource(name="loadrunTaskMaster")
	private LoadrunTaskMaster loadrunTaskMaster;
	
	@Resource(name="loadrunWebContainer")
	private LoadrunWebContainer loadrunWebContainer;

	@RequestMapping(params = "method=list")
	public ModelAndView listLoadrunConfig() {
		List<LoadRunHost> list = cspLoadRunBo.findAllLoadRunHost();

		ModelAndView model = new ModelAndView("/autoload/show/app_loadrun_list");
		model.addObject("loadconfigList", list);
		return model;
	}
	
	
	@RequestMapping(params="method=showManual")
	public void showManual(HttpServletResponse response) {
		List<String> manualList = new ArrayList<String>();
		Map<Integer, BaseLoadrunTask> runManualTasks = loadrunWebContainer.getAllManualTaskInRun();
		for (Map.Entry<Integer, BaseLoadrunTask> entry : runManualTasks.entrySet()) {
			manualList.add(entry.getValue().getTarget().toString());
		}
		
		JSONArray json = JSONArray.fromObject(manualList);
		response.setContentType("test/html;charset=utf-8");
		
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();
		} catch (IOException e) {
			logger.error("", e);
		}
		return ;
	}
	
	
	@RequestMapping(params="method=showSlave")
	public void showSlave(HttpServletResponse response){
		List<SlaveProxy> list = loadrunTaskMaster.getLoadMaster().getSlave();
		
		List<Map<String,String>> listM = new ArrayList<Map<String,String>>();
		
		for(SlaveProxy p:list){
			int c = p.getCapacity();
			int j = p.getJobNum();
			String id = p.getSlaveId();
			String s = p.getSession().getRemoteAddress().toString();
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("c", c+"");
			map.put("j", j+"");
			map.put("id", id);
			map.put("s", s);
			listM.add(map);
		}
		
		JSONArray json = JSONArray.fromObject(listM);
		
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();	
		} catch (IOException e) {
			logger.error("", e);
		}
		return ;
	}
	
	@RequestMapping(params="method=showJob")
	public void showJob(HttpServletResponse response){
		List<SlaveProxy> list = loadrunTaskMaster.getLoadMaster().getSlave();
		Map<String,List<String>> map = new HashMap<String, List<String>>();
		for(SlaveProxy proxy:list){
			Set<JobFeature> set = proxy.jobs();
			List<String> jobList = new ArrayList<String>();
			for(JobFeature job:set){
				jobList.add(proxy.getSlaveId()+":"+job.getJobId()+":"+job.getJob().getDetail().toString());
			}
			map.put(proxy.getSession().getRemoteAddress().toString(), jobList);
		}
		JSONObject json = JSONObject.fromObject(map);
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();	
		} catch (IOException e) {
			logger.error("", e);
		}
		return ;
		
	}
	
	
	
	
	@RequestMapping(params = "method=showAll")
	public ModelAndView showAllLoadrunResult(String appIds, String collectTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		Date searchDate = null;
		boolean hasDateInput = true;
		if (StringUtils.isBlank(collectTime)) {
			hasDateInput = false;
		} else {
			try {
				searchDate = format.parse(collectTime);
			} catch (ParseException e) {
				logger.error("", e);
			}
		}
		
		Map<String,Map<String,Object>> loadrunMap = new HashMap<String, Map<String,Object>>();
		
		String[] tmp = appIds.split(",");
		
		for(String id:tmp ){
			int appId = Integer.parseInt(id);
			
			LoadRunHost loadconfig = cspLoadRunBo.findLoadRunHostByAppId(appId);
			
			if(loadconfig == null){
				continue;
			}

			Set<Integer> sortSet = new HashSet<Integer>();
			Map<String, Map<Integer, Map<ResultKey, LoadrunResult>>> map = new HashMap<String, Map<Integer, Map<ResultKey, LoadrunResult>>>();

			Map<String, Map<Integer, String>> loadControlMap = new HashMap<String, Map<Integer, String>>();

			Set<ResultKey> keys = new HashSet<ResultKey>();

			if (!hasDateInput) {
				searchDate = cspLoadRunBo.findRecentlyLoadDate(appId);
			} 
			List<LoadrunResult> list = cspLoadRunBo.findLoadrunResult(appId, searchDate);
			
			if(list.size() ==0){
				continue;
			}
			
			for (LoadrunResult load : list) {

				Map<Integer, Map<ResultKey, LoadrunResult>> sortMap = map.get(load.getLoadId());
				Map<Integer, String> controlMap = loadControlMap.get(load.getLoadId());
				if (controlMap == null) {
					controlMap = new HashMap<Integer, String>();
					loadControlMap.put(load.getLoadId(), controlMap);
				}

				controlMap.put(load.getLoadrunOrder(), load.getControlFeature());

				if (sortMap == null) {
					sortMap = new HashMap<Integer, Map<ResultKey, LoadrunResult>>();
					map.put(load.getLoadId(), sortMap);
				}

				Map<ResultKey, LoadrunResult> keyMap = sortMap.get(load.getLoadrunOrder());
				if (keyMap == null) {
					keyMap = new HashMap<ResultKey, LoadrunResult>();
					sortMap.put(load.getLoadrunOrder(), keyMap);
					sortSet.add(load.getLoadrunOrder());
				}
				keyMap.put(load.getKey(), load);

				keys.add(load.getKey());

			}

			List<Integer> sortList = new ArrayList<Integer>();
			sortList.addAll(sortSet);
			Collections.sort(sortList);

			List<ResultKey> keyList = new ArrayList<ResultKey>();
			keyList.addAll(keys);
			Collections.sort(keyList, new Comparator<ResultKey>() {

				@Override
				public int compare(ResultKey o1, ResultKey o2) {
					if (o1.getSort() > o2.getSort()) {
						return 1;
					} else if (o1.getSort() < o2.getSort()) {
						return -1;
					}
					return 0;
				}
			});

			for (Map.Entry<String, Map<Integer, Map<ResultKey, LoadrunResult>>> entry : map.entrySet()) {
				// String id = entry.getKey();
				Map<Integer, Map<ResultKey, LoadrunResult>> sortLoadMap = entry.getValue();
				for (Map.Entry<Integer, Map<ResultKey, LoadrunResult>> loadEntry : sortLoadMap.entrySet()) {
					Map<ResultKey, LoadrunResult> m = loadEntry.getValue();

					LoadrunResult r1 = m.get(ResultKey.GC_Memory);
					if (r1 != null) {
						LoadrunResult r2 = null;
						AutoLoadType type = r1.getLoadrunType();
						r2 = m.get(type.getQpsKey());
						if (r1 != null && r2 != null) {
							r1.setValue(Arith.div(r1.getValue(), r2.getValue(), 2));
						}
					}
				}
			}

			String loadmessage = loadconfig.getLoadType().getLoadDesc();

			Map<String,Object> model = new HashMap<String,Object>();
			model.put("loadconfig", loadconfig);
			model.put("keys", keyList);
			model.put("loadrunMap", map);
			model.put("collectTime", format.format(searchDate));
			model.put("sortList", sortList);
			model.put("loadControlMap", loadControlMap);
			model.put("loadmessage", loadmessage);
			loadrunMap.put(loadconfig.getAppName(), model);
			
		}
		
		ModelAndView model = new ModelAndView("/autoload/show/app_loadrun_report");
		model.addObject("loadrunMap", loadrunMap);
		return model;
	}
	
	@RequestMapping(params = "method=showToday")
	public ModelAndView showTodayLoadRun(String collectTime) {
		ModelAndView view = new ModelAndView("/autoload/show/app_loadrun_today");
		Map<String, LoadrunResult> max = cspLoadRunBo.getMax();
		Map<String, LoadrunResult> min = cspLoadRunBo.getMin();
		
		view.addObject("max", max);
		view.addObject("min", min);
		view.addObject("date", collectTime);
		return view;
		
	}
	

	@RequestMapping(params = "method=show")
	public ModelAndView showLoadrunResult(int appId, String collectTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat   runTimeFormat   =   new   SimpleDateFormat( " HH:mm:ss "); 
		Map<String,Map<Integer, String>> runTime = new HashMap<String, Map<Integer, String>>();
		Map<String, Integer> size = new HashMap<String,Integer>();
		if (StringUtils.isBlank(collectTime)) {
			collectTime = format.format(new Date());
		}

		LoadRunHost loadconfig = cspLoadRunBo.findLoadRunHostByAppId(appId);
		
		
		if(loadconfig == null || loadconfig.getLoadType() == null) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "此应用还没有加入自动压测系统!");
			modelView.setViewName("/message");
			return modelView;
		}
		

		Set<Integer> sortSet = new HashSet<Integer>();
		Map<String, Map<Integer, Map<ResultKey, LoadrunResult>>> map = new HashMap<String, Map<Integer, Map<ResultKey, LoadrunResult>>>();

		Map<String, Map<Integer, String>> loadControlMap = new HashMap<String, Map<Integer, String>>();

		Set<ResultKey> keys = new HashSet<ResultKey>();
		
		Map<String, String> limitMap = new HashMap<String, String>();

		try {
			List<LoadrunResult> list = cspLoadRunBo.findLoadrunResult(appId, format.parse(collectTime));

			for (LoadrunResult load : list) {

				Map<Integer, Map<ResultKey, LoadrunResult>> sortMap = map.get(load.getLoadId());
				Map<Integer, String> controlMap = loadControlMap.get(load.getLoadId());
				if (controlMap == null) {
					controlMap = new HashMap<Integer, String>();
					loadControlMap.put(load.getLoadId(), controlMap);
				}

				controlMap.put(load.getLoadrunOrder(), load.getControlFeature());

				
				if (sortMap == null) {
					sortMap = new HashMap<Integer, Map<ResultKey, LoadrunResult>>();
					map.put(load.getLoadId(), sortMap);
					
				}
				
				Map <Integer, String> timeMap = runTime.get(load.getLoadId());
				if (timeMap == null) {
					timeMap = new HashMap<Integer, String>();
					runTime.put(load.getLoadId(), timeMap);
				}
				
				Integer order = size.get(load.getLoadId());
				if (order == null || load.getLoadrunOrder() > order) {
					size.put(load.getLoadId(), load.getLoadrunOrder());
				} 

				Map<ResultKey, LoadrunResult> keyMap = sortMap.get(load.getLoadrunOrder());
				if (keyMap == null) {
					keyMap = new HashMap<ResultKey, LoadrunResult>();
					sortMap.put(load.getLoadrunOrder(), keyMap);
					sortSet.add(load.getLoadrunOrder());
				}
				keyMap.put(load.getKey(), load);

				keys.add(load.getKey());
				
				// 取采集的时间
				String time = timeMap.get(load.getLoadrunOrder());
				if (time == null) {
					time = runTimeFormat.format(load.getCollectTime());
					timeMap.put(load.getLoadrunOrder(), time);
				}
				
				// 压测时的阀值
				if (!limitMap.keySet().contains(load.getLoadId())) {
					String limitFeature = cspLoadRunBo.findLoadrunThreshold(load.getLoadId());
					limitMap.put(load.getLoadId(), limitFeature);
				}

			}
		} catch (ParseException e) {
			logger.error("", e);
		}

		List<Integer> sortList = new ArrayList<Integer>();
		sortList.addAll(sortSet);
		Collections.sort(sortList);

		List<ResultKey> keyList = new ArrayList<ResultKey>();
		keyList.addAll(keys);
		Collections.sort(keyList, new Comparator<ResultKey>() {

			@Override
			public int compare(ResultKey o1, ResultKey o2) {
				if (o1.getSort() > o2.getSort()) {
					return 1;
				} else if (o1.getSort() < o2.getSort()) {
					return -1;
				}
				return 0;
			}
		});

		for (Map.Entry<String, Map<Integer, Map<ResultKey, LoadrunResult>>> entry : map.entrySet()) {
			// String id = entry.getKey();
			Map<Integer, Map<ResultKey, LoadrunResult>> sortLoadMap = entry.getValue();
			for (Map.Entry<Integer, Map<ResultKey, LoadrunResult>> loadEntry : sortLoadMap.entrySet()) {
				Map<ResultKey, LoadrunResult> m = loadEntry.getValue();

				LoadrunResult r1 = m.get(ResultKey.GC_Memory);
				if (r1 != null) {
					LoadrunResult r2 = null;
					AutoLoadType type = r1.getLoadrunType();
					r2 = m.get(type.getQpsKey());
					if (r1 != null && r2 != null) {
						r1.setValue(Arith.div(r1.getValue(), r2.getValue(), 2));
					}
				}
			}
		}

		String loadmessage = loadconfig.getLoadType().getLoadDesc();

		ModelAndView model = new ModelAndView("/autoload/show/app_loadrun_show");
		model.addObject("loadconfig", loadconfig);
		model.addObject("keys", keyList);
		model.addObject("loadrunMap", map);
		model.addObject("collectTime", collectTime);
		model.addObject("sortList", sortList);
		model.addObject("loadControlMap", loadControlMap);
		model.addObject("loadmessage", loadmessage);
		model.addObject("runTime", runTime);
		model.addObject("size", size);
		model.addObject("searchDate", collectTime);
		model.addObject("appId", appId);
		model.addObject("limitFeature", limitMap);
		
		return model;
	}
	
	@RequestMapping(params = "method=showDetail")
	public ModelAndView showLoadrunResultDetail(String id, String filter) {
		
		ModelAndView model = new ModelAndView("/autoload/show/app_loadrun_show_detail");
		model.addObject("id", id);
		model.addObject("filter", filter);
		
		List<String> timeAxis =  cspLoadRunBo.findLoadrunResultDetailTimes(id);
		model.addObject("times", timeAxis);
		
		return model;
		
	}
	
	@RequestMapping(params = "method=showReport")
	public ModelAndView showReport(String id) {
		
		ModelAndView model = new ModelAndView("/autoload/show/loadrun_show_report");
		model.addObject("id", id);
		
		List<LoadrunReportObject> urlL =  cspLoadRunBo.findLoadrunResultDetailSum(id, ResultDetailType.URL.toString());
		List<LoadrunReportObject> hsfProviderL =  cspLoadRunBo.findLoadrunResultDetailSum(id, ResultDetailType.HSF_PROVIDER.toString());
		
		model.addObject("urls", urlL);
		model.addObject("hsfProviders", hsfProviderL);
		
		return model;
	}
	
	@RequestMapping(params = "method=showReportChart")
	public ModelAndView showReportChart(String id, String mkey, String skey) {
		
		ModelAndView model = new ModelAndView("/autoload/show/loadrun_report_chart");

		Map<String,Map<String, Double>> mapPv = new HashMap<String, Map<String,Double>>();
		List<LoadrunResultDetail> pvL = cspLoadRunBo.findLoadrunResultDetailSameTimeSum(id, mkey, skey);
		Map<String, Double> timeQpsMapPv = transferReportDataUseCounts(pvL);
		mapPv.put(skey, timeQpsMapPv);
		String pv = createCharXml(mapPv);
		model.addObject("pv", pv);
		
		Map<String,Map<String, Double>> mapRt = new HashMap<String, Map<String,Double>>();
		List<LoadrunResultDetail> rtL = cspLoadRunBo.findLoadrunResultDetailSameTimeSum(id, mkey, skey);
		Map<String, Double> timeQpsMapRt = transferReportDataUseTimes(rtL);
		mapRt.put(skey, timeQpsMapRt);
		String rt = createCharXml(mapRt);
		model.addObject("rt", rt);
		
		
		
		model.addObject("pv", pv);
		model.addObject("rt", rt);
		model.addObject("id", id);
		model.addObject("mkey", mkey);
		model.addObject("skey", skey);
		
		return model;
	}
	
	@RequestMapping(params = "method=showGuide")
	public ModelAndView showGuide() {
		
		ModelAndView model = new ModelAndView("/autoload/show/loadrun_guide");
		return model;
		
	}
	
	@RequestMapping(params = "method=showWarn")
	public ModelAndView showWarn() {
		
		ModelAndView model = new ModelAndView("/autoload/show/loadrun_warn");
		return model;
		
	}
	
	@RequestMapping(params = "method=tairGuide")
	public ModelAndView tairGuide() {
		
		ModelAndView model = new ModelAndView("/autoload/show/tair_guide");
		return model;
		
	}

	@RequestMapping(params = "method=qps")
	public void showQpsCalculate(int appId,HttpServletResponse response) {
		
		Calendar cal = Calendar.getInstance();
		Date  endTime= cal.getTime(); 
		
		cal.add(Calendar.DAY_OF_YEAR, -60);
		
		Date startTime = cal.getTime(); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		LoadRunHost host = cspLoadRunBo.findLoadRunHostByAppId(appId);
		ResultKey key = host.getLoadType().getQpsKey();
		
		Map<String,Map<String, Double>> map = new HashMap<String, Map<String,Double>>();
		
		Map<String, Double> timeQpsMap = new HashMap<String, Double>();
		Map<String, Double> yaceQpsMap = new HashMap<String, Double>();
		
		int qpsKeyId = 16931;
		switch (host.getLoadType()) {
		case apache:
			qpsKeyId = 16931;
			break;
		case httpLoad:
			qpsKeyId = 16931;
			break;
		case hsf:
			qpsKeyId = 27734;
			break;
		}
		List<KeyValuePo> qpsList = cspLoadRunBo.findMonitorCountByDate(appId, qpsKeyId, startTime, endTime);
		

		for (KeyValuePo po : qpsList) {
			double pQps = Double.parseDouble(po.getValueStr());
			timeQpsMap.put(sdf.format(po.getCollectTime()), pQps);
		}

		List<LoadrunResult> list = cspLoadRunBo.findLoadrunResult(appId, key, startTime, endTime);
		
		for (LoadrunResult entry : list) {
			String time = sdf.format(entry.getCollectTime());
			double maxyaceqps = entry.getValue();
			
			if(yaceQpsMap.get(time)!=null){
				if(yaceQpsMap.get(time) <maxyaceqps){
					yaceQpsMap.put(time, maxyaceqps);
				}
			}else{
				yaceQpsMap.put(time, maxyaceqps);
			}

		}

		map.put("当前QPS", timeQpsMap);
		map.put("压测QPS", yaceQpsMap);
		
		try{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(createCharXml(map));
			response.flushBuffer();	
		}catch (Exception e) {
		}
		return ;
	}
	
	@RequestMapping(params = "method=reportCpuInfo")
	public void reportCpuInfo(String loadrunId,HttpServletResponse response) {
		
		Map<String,Map<String, Double>> map = new HashMap<String, Map<String,Double>>();
		
		List<LoadrunResultDetail> dataL = cspLoadRunBo.findLoadrunResultDetailSameTimeAve(loadrunId, ResultDetailType.PERFORMANCE_INDEX.toString(), "CPU");
		Map<String, Double> timeQpsMap = transferReportDataUseTimes(dataL);
		map.put("CPU%", timeQpsMap);
		
		try{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(createCharXml(map));
			response.flushBuffer();	
		}catch (Exception e) {
		}
		return ;
	}
	
	@RequestMapping(params = "method=reportLoadInfo")
	public void reportLoadInfo(String loadrunId,HttpServletResponse response) {
		
		Map<String,Map<String, Double>> map = new HashMap<String, Map<String,Double>>();
		
		List<LoadrunResultDetail> dataL = cspLoadRunBo.findLoadrunResultDetailSameTimeAve(loadrunId, ResultDetailType.PERFORMANCE_INDEX.toString(), "LOAD");
		Map<String, Double> timeQpsMap = transferReportDataUseTimes(dataL);
		map.put("LOAD", timeQpsMap);
		
		try{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(createCharXml(map));
			response.flushBuffer();	
		}catch (Exception e) {
		}
		return ;
	}
	
	@RequestMapping(params = "method=reportThreadInfo")
	public void reportThreadInfo(String loadrunId,HttpServletResponse response) {
		
		Map<String,Map<String, Double>> map = new HashMap<String, Map<String,Double>>();
		
		List<LoadrunResultDetail> dataL = cspLoadRunBo.findLoadrunResultDetailSameTimeAve(loadrunId, ResultDetailType.PERFORMANCE_INDEX.toString(), "ThreadCount");
		Map<String, Double> timeQpsMap = transferReportDataUseTimes(dataL);
		map.put("Thread Count", timeQpsMap);
		
		try{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(createCharXml(map));
			response.flushBuffer();	
		}catch (Exception e) {
		}
		return ;
	}
	
	@RequestMapping(params = "method=reportIoInfo")
	public void reportIoInfo(String loadrunId,HttpServletResponse response) {
		
		Map<String,Map<String, Double>> map = new HashMap<String, Map<String,Double>>();
		
		List<LoadrunResultDetail> bytinL = cspLoadRunBo.findLoadrunResultDetailSameTimeAve(loadrunId, ResultDetailType.IO_DATA.toString(), "bytin");
		List<LoadrunResultDetail> bytoutL = cspLoadRunBo.findLoadrunResultDetailSameTimeAve(loadrunId, ResultDetailType.IO_DATA.toString(), "bytout");
		List<LoadrunResultDetail> pktinL = cspLoadRunBo.findLoadrunResultDetailSameTimeAve(loadrunId, ResultDetailType.IO_DATA.toString(), "pktin");
		List<LoadrunResultDetail> pktoutL = cspLoadRunBo.findLoadrunResultDetailSameTimeAve(loadrunId, ResultDetailType.IO_DATA.toString(), "pktout");
		
		Map<String, Double> timeQpsMap1 = transferReportDataUseTimes(bytinL);
		Map<String, Double> timeQpsMap2 = transferReportDataUseTimes(bytoutL);
		Map<String, Double> timeQpsMap3 = transferReportDataUseTimes(pktinL);
		Map<String, Double> timeQpsMap4 = transferReportDataUseTimes(pktoutL);
		map.put("bytin(B)", timeQpsMap1);
		map.put("bytout(B)", timeQpsMap2);
		map.put("pktin", timeQpsMap3);
		map.put("pktout", timeQpsMap4);
		
		try{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(createCharXml(map));
			response.flushBuffer();	
		}catch (Exception e) {
		}
		return ;
	}
	
	@RequestMapping(params = "method=reportPv")
	public void reportPv(String loadrunId, String mkey, String skey, HttpServletResponse response) {
		
		Map<String,Map<String, Double>> map = new HashMap<String, Map<String,Double>>();
		
		List<LoadrunResultDetail> dataL = cspLoadRunBo.findLoadrunResultDetailSameTimeSum(loadrunId, mkey, mkey);
		Map<String, Double> timeQpsMap = transferReportDataUseCounts(dataL);
		map.put("pv", timeQpsMap);
		
		try{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(createCharXml(map));
			response.flushBuffer();	
		}catch (Exception e) {
		}
		return ;
	}
	
	@RequestMapping(params = "method=reportRt")
	public void reportRt(String loadrunId,  String mkey, String skey, HttpServletResponse response) {
		
		Map<String,Map<String, Double>> map = new HashMap<String, Map<String,Double>>();
		
		List<LoadrunResultDetail> dataL = cspLoadRunBo.findLoadrunResultDetailSameTimeSum(loadrunId, mkey, mkey);
		Map<String, Double> timeQpsMap = transferReportDataUseTimes(dataL);
		map.put("rt", timeQpsMap);
		
		try{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(createCharXml(map));
			response.flushBuffer();	
		}catch (Exception e) {
		}
		return ;
	}
	
	private Map<String, Double> transferReportDataUseCounts(List<LoadrunResultDetail> detailL) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Map<String, Double> timeQpsMap = new HashMap<String, Double>();
		
		for (LoadrunResultDetail detail : detailL) {
			Date collectTime = detail.getCollectTime();
			if (collectTime != null) {
				String time = sdf.format(collectTime);
				timeQpsMap.put(time, detail.getCount());
			}
		}
		
		return timeQpsMap;
	}
	
	private Map<String, Double> transferReportDataUseTimes(List<LoadrunResultDetail> detailL) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Map<String, Double> timeQpsMap = new HashMap<String, Double>();
		
		for (LoadrunResultDetail detail : detailL) {
			Date collectTime = detail.getCollectTime();
			if (collectTime != null) {
				String time = sdf.format(collectTime);
				timeQpsMap.put(time, detail.getTimes());
			}
		}
		
		return timeQpsMap;
	}

	private String createCharXml(Map<String, Map<String, Double>> mapvalue) {

		if (mapvalue == null || mapvalue.size() == 0) {
			return "<chart><series><value xid='0'>0</value></series><graphs><graph gid='0' title='test'><value xid='0'>0</value></graph></graphs></chart>";
		}

		StringBuffer head = new StringBuffer("<chart>");
		 String cat = createCharCategories(mapvalue);
		 head.append(cat);
		 String dataSet = createDataSet(mapvalue);
		 head.append(dataSet);
		head.append("</chart>");
		return head.toString();
	}

	private String createDataSet(Map<String, Map<String, Double>> mapvalue) {

		List<String> list = new ArrayList<String>();
		
		for(Map.Entry<String, Map<String, Double>> entry:mapvalue.entrySet()){
			list.addAll(entry.getValue().keySet());
		}
		Collections.sort(list);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<graphs>");
		
		for(Map.Entry<String, Map<String, Double>> entry:mapvalue.entrySet()){
			sb.append("<graph gid='0' title='" + entry.getKey() + "'>");
			for (int i = 0; i < list.size(); i++) {
				String label = list.get(i);
				sb.append("<value xid='" + i + "'>" + (entry.getValue().get(label)==null?0:entry.getValue().get(label)) + "</value>");
			}
			sb.append("</graph>");
		}
		sb.append("</graphs>");
		return sb.toString();
	}

	private String createCharCategories(Map<String, Map<String, Double>> mapvalue) {

		List<String> list = new ArrayList<String>();
		
		for(Map.Entry<String, Map<String, Double>> entry:mapvalue.entrySet()){
			list.addAll(entry.getValue().keySet());
		}

		Collections.sort(list);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<series>");

		for (int i = 0; i < list.size(); i++) {
			String key = list.get(i);
			sb.append("<value xid='" + i + "'>" + key + "</value>");
		}
		
		sb.append("</series>");
		return sb.toString();
	}

}
