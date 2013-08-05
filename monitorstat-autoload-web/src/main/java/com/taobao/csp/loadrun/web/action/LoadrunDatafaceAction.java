package com.taobao.csp.loadrun.web.action;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.LoadrunResultDetail;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.web.LoadRunHost;
import com.taobao.csp.loadrun.web.bo.CspLoadRunBo;

@Controller
@RequestMapping("/loadrun/data.do")
public class LoadrunDatafaceAction {

	@Resource(name = "cspLoadRunBo")
	private CspLoadRunBo cspLoadRunBo;
	
	@RequestMapping(params="method=loadQps")
	public void loadQps(HttpServletResponse response) {
		Map<String, Double> qpsM = new HashMap<String, Double>();
		
		List<LoadRunHost> loadRunHostL = cspLoadRunBo.findAllLoadRunHost();
		for (LoadRunHost host : loadRunHostL) {
			int appId = host.getAppId();
			String appName = host.getAppName();
			LoadrunResult result = cspLoadRunBo.findRecentLoadRunResults(appId);
			if (result == null) continue;
			qpsM.put(appName, result.getValue());
		}
		
		JSONObject json = JSONObject.fromObject(qpsM);
		// response.setContentType("test/html;charset=utf-8");
		
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();
		} catch (IOException e) {
			
		}
		return ;
	}
	
	@RequestMapping(params="method=detailData")
	public void detailData(HttpServletResponse response, String id, String filter) {
		
		Set<String> filterSet = new HashSet<String>();
		if (filter != null && filter.trim().length() > 0) {
			String [] filters = filter.trim().split(",");
			for (String item : filters) {
				filterSet.add(item.trim());
			}
		}
		List<LoadrunResultDetail> results = cspLoadRunBo.findLoadrunResultDetail(id, filterSet);
		
		Map<String, Object> showDatas = transferDisplayData(results);
		
		JSONObject json = JSONObject.fromObject(showDatas);
		// response.setContentType("test/html;charset=utf-8");
		
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();
		} catch (IOException e) {
			
		}
		return ;
	}
	
	private Map<String, Object> transferDisplayData(List<LoadrunResultDetail> results) {
		Map<String, Object> datas = new HashMap<String, Object>();
		List<Map<String, Object>> nodes = new ArrayList<Map<String,Object>>();
		datas.put("rows", nodes);
		
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
		DecimalFormat df = new DecimalFormat("#.00");
		
		Map<String, Object> first = null;
		
		Map<String, Object> secend = null;
		
		Collections.sort(results);
		ResultDetailType lastMkey = null;
		String lastSkey = null;
		int id = 0;
		int parentId = 0;
		for (LoadrunResultDetail cell : results) {
			// 一级key不一样
			ResultDetailType mKey = cell.getMainKey();
			if (mKey != lastMkey) {
				id ++;
				parentId = id;
				first = new LinkedHashMap<String, Object>();
				first.put("id", parentId);
				first.put("name", mKey);
				nodes.add(first);
				lastMkey = mKey;

			}
			
			// 二级key不一样建次级节点
			String sKey = cell.getSecendaryKey();
			if (!sKey.equals(lastSkey)) {
				id++;
				secend = new LinkedHashMap<String, Object>();
				secend.put("id", id);
				secend.put("name", sKey);
				secend.put("_parentId", parentId);
				nodes.add(secend);
				lastSkey = sKey;
			}
			
			String fTime = sf.format(cell.getCollectTime()) + "(" + cell.getLoadrunOrder() + ")";
			secend.put(fTime, cell.diaplayString());
			
			// 性能指标取均值
//			if (cell.getMainKey() == ResultDetailType.PERFORMANCE_INDEX) {
//				double time = 0;
//				first.put(fTime, df.format(time));
//			} else {
//				if (first.get(fTime) != null) {
//					String display = (String)first.get(fTime);
//					Pattern pattern = Pattern.compile("^(\\d+\\.?\\d*)/(\\d+\\.?\\d*)=(\\d+\\.?\\d*)$");
//					Matcher matcher = pattern.matcher(display);
//					if (matcher.matches()) {
//						double time = Double.parseDouble(matcher.group(1));
//						double count = Double.parseDouble(matcher.group(2));
//						time += cell.getTimes();
//						count += cell.getCount();
//						double divotor = divideTwoPrecision(time, count);
//						first.put(fTime, count + "-" + divotor);
//					}
//				} else {
//					double time = cell.getTimes();
//					double count = cell.getCount();
//					double divotor = divideTwoPrecision(time, count);
//					first.put(fTime, count + "-" + divotor);
//				}
//			}
		
		}
		
		datas.put("total", nodes.size());
	
		return datas;
	}
	
	private double divideTwoPrecision(double a, double b) {
		DecimalFormat df = new DecimalFormat("#.00");
		return Double.parseDouble(df.format((double)a / b));
	}
	
}
