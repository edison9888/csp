package com.taobao.csp.capacity.web.action;

import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.capacity.bo.DataInterfaceBo;
import com.taobao.csp.capacity.constant.Constants;
import com.taobao.csp.capacity.constant.CostConstants;
import com.taobao.csp.capacity.dao.CapacityDao;
import com.taobao.csp.capacity.dao.CapacityRankingDao;
import com.taobao.csp.capacity.po.CapacityAppPo;
import com.taobao.csp.capacity.po.CapacityCostInfoPo;
import com.taobao.csp.capacity.po.CapacityRankingPo;
import com.taobao.csp.capacity.po.GroupAppPo;
import com.taobao.csp.capacity.util.AppGroupUtil;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;

/***
 * 容量数据的外部接口
 * @author youji.zj
 *
 */
@Controller
@RequestMapping("/data.do")
public class CapacityDataInterfaceAction {
	
	@Resource(name = "dataInterfaceBo")
	private DataInterfaceBo dateInterfaceBo;
	
	@Resource(name = "capacityRankingDao")
	private CapacityRankingDao capacityRankingDao;
	
	@Resource(name = "capacityDao")
	private CapacityDao capacityDao;
	
	@Resource(name = "appInfoDao")
	private AppInfoDao appInfoDao;
	
	@Resource(name = "constants")
	private Constants constants;
	
	@RequestMapping(params = "method=capRankData", method = RequestMethod.GET)
	public ModelAndView getCapacityRankData(HttpServletRequest request,
			   HttpServletResponse response) {
		
		String returnView = "/capacity/interface/data_interface";
		String appName = request.getParameter("appName");
		AppInfoPo appInfoPo = appInfoDao.getAppInfoPoByAppName(appName);
		CapacityAppPo capacityAppPo = null;
		if (appInfoPo != null) {
			capacityAppPo = capacityDao.getCapacityApp(appInfoPo.getAppId());
		}
		
		// 杨曦不愿意按分组取，给他第一个吧，个人觉得还是按分组靠谱
		List<GroupAppPo> capAppPos = AppGroupUtil.getAppGroupIds(capacityAppPo);  // 分组
		
		Map<String,String> randDataMap = new LinkedHashMap<String, String>();
		
		if (appInfoPo != null && appInfoPo.getAppId() > 0) {
			String rankingName = "容量排名";
			int appId = appInfoPo.getAppId();
			if (capAppPos != null && capAppPos.size() > 0) {
				appId = capAppPos.get(0).getAppId();
			}
			Calendar cal1 = constants.getRankingTime();
			
			CapacityRankingPo capacityRankingPo = capacityRankingDao.getCapacityRanking(appId, rankingName, cal1.getTime());
			if (capacityRankingPo != null) {
				String singleOnlineQps = capacityRankingPo.getFeatureData("单台线上平均");
				String loadQps = capacityRankingPo.getFeatureData("单台能力");
				String mechineNum = capacityRankingPo.getFeatureData("机器数");
				String addMachine = capacityRankingPo.getFeatureData("预测机器增减");
				String capacity = capacityRankingPo.getFeatureData("容量水位");
				capacity = capacity.substring(0, capacity.length() - 1);
				
				randDataMap.put("单台线上QPS", singleOnlineQps);
				randDataMap.put("单台安全QPS", String.valueOf(Double.parseDouble(loadQps) / 2));
				randDataMap.put("压测QPS", loadQps);
				randDataMap.put("机器数", mechineNum);
				randDataMap.put("安全机器数", String.valueOf(Integer.parseInt(mechineNum) + Integer.parseInt(addMachine)));
				randDataMap.put("容量剩余率", 100 - Double.parseDouble(capacity) + "%");
			}			
		}
		
		JSONObject jsonObject = JSONObject.fromObject(randDataMap);
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("jsonData", jsonObject);
		
		return modelAndView;
	}
	
	/***
	 * 给hsf服务治理做的数据接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "method=hafCap", method = RequestMethod.GET)
	public ModelAndView hafCap(HttpServletRequest request,
			   HttpServletResponse response) {
		
		String returnView = "/capacity/interface/data_interface";
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date date = calendar.getTime();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String rankingName = "容量排名";
		String dateS = request.getParameter("date");
		if (dateS != null) {
			try {
				date = sf.parse(dateS);
			} catch (Exception e) {
			}
		}
		
		Map<String, Map<String,String>> randDataMap = new LinkedHashMap<String, Map<String,String>>();
		List<CapacityRankingPo> ranks = capacityRankingDao.findCapacityRankingPo(rankingName, date);
		for (CapacityRankingPo po : ranks) {
			if ("center".equals(po.getAppType())) {
				Map<String,String> appM = new LinkedHashMap<String, String>();
				randDataMap.put(po.getAppName(), appM);
				appM.put("room_num", po.getFeatureData("机房数"));
				appM.put("machine_num", po.getFeatureData("机器数"));
				appM.put("single_max_cap", po.getFeatureData("单台能力"));
				appM.put("single_max_cap", po.getFeatureData("单台能力"));
				appM.put("single_max_load", po.getFeatureData("单台负荷"));
				appM.put("cluster_max_cap", po.getFeatureData("集群能力"));
				appM.put("cluster_max_load", po.getFeatureData("集群负荷"));
				appM.put("cap_level", po.getFeatureData("容量水位"));
				appM.put("standart_level", po.getFeatureData("容量标准"));
			}
		}
		
		JSONObject jsonObject = JSONObject.fromObject(randDataMap);
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("jsonData", jsonObject);
		
		return modelAndView;
	}
	
	@RequestMapping(params = "method=depMeApp", method = RequestMethod.GET)
	public ModelAndView getDepMeApp(HttpServletRequest request,
			   HttpServletResponse response) {
		
		String returnView = "/capacity/interface/data_interface";
		String appName = request.getParameter("appName");
		Map<String, Set<String>> meDep = dateInterfaceBo.getDepMeApp(appName);
		
		JSONObject jsonObject = JSONObject.fromObject(meDep);
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("jsonData", jsonObject);
		
		return modelAndView;
	}
	
	@RequestMapping(params = "method=meDepApp", method = RequestMethod.GET)
	public ModelAndView getMeDepApp(HttpServletRequest request,
			   HttpServletResponse response) {
		
		String returnView = "/capacity/interface/data_interface";
		String appName = request.getParameter("appName");
		Map<String, Set<String>> meDep = dateInterfaceBo.getMeDepApp(appName);
		
		JSONObject jsonObject = JSONObject.fromObject(meDep);
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("jsonData", jsonObject);
		
		return modelAndView;
	}
	
	@RequestMapping(params = "method=getOriginUrl", method = RequestMethod.GET)
	public ModelAndView getOriginUrl(HttpServletRequest request,
			   HttpServletResponse response) {
		
		String returnView = "/capacity/interface/data_interface";
		String appName = request.getParameter("appName");
		String date = request.getParameter("date");
		if (date == null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			date = format.format(calendar.getTime());
		}
		Map<String, Integer> meDep = dateInterfaceBo.getOrigin(appName, date);
		
		JSONObject jsonObject = JSONObject.fromObject(meDep);
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("jsonData", jsonObject);
		
		return modelAndView;
	}
	
	@RequestMapping(params = "method=getRequestUrl", method = RequestMethod.GET)
	public ModelAndView getRequestUrl(HttpServletRequest request,
			   HttpServletResponse response) {
		
		String returnView = "/capacity/interface/data_interface";
		String appName = request.getParameter("appName");
		String date = request.getParameter("date");
		if (date == null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			date = format.format(calendar.getTime());
		}
		Map<String, Integer> meDep = dateInterfaceBo.getRequest(appName, date);
		
		JSONObject jsonObject = JSONObject.fromObject(meDep);
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("jsonData", jsonObject);
		
		return modelAndView;
	}
	
	@RequestMapping(params = "method=getHsfProviderInfo", method = RequestMethod.GET)
	public ModelAndView getHsfProviderInfo(HttpServletRequest request,
			   HttpServletResponse response) {
		
		String returnView = "/capacity/interface/data_interface";
		String appName = request.getParameter("appName");
		String date = request.getParameter("date");
		if (date == null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			date = format.format(calendar.getTime());
		}
		Map<String, Integer> meDep = dateInterfaceBo.getHsfProviderInfo(appName, date);
		
		JSONObject jsonObject = JSONObject.fromObject(meDep);
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("jsonData", jsonObject);
		
		return modelAndView;
	}
	
	@RequestMapping(params = "method=getCSPInfo", method = RequestMethod.GET)
	public ModelAndView getCSPInfo(HttpServletRequest request,
			   HttpServletResponse response, String appname, String date) {
		
		String returnView = "/capacity/interface/data_interface";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dDate;
		Date pvDate;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		if (date == null) {
			dDate = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			pvDate = calendar.getTime();
		} else {
			try {
				dDate = format.parse(date);
				calendar.setTime(dDate);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				pvDate = calendar.getTime();
			} catch (ParseException e) {
				dDate = calendar.getTime();
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				pvDate = calendar.getTime();
			}
		}
		Map<String, Long> pvStatic = dateInterfaceBo.getPvRequestStatics(appname, dDate);
		CapacityCostInfoPo costPo = dateInterfaceBo.getCapacityCost(appname);
		
		// hsf pv
		Long hsfPv = dateInterfaceBo.getHsfProviderPv(appname, dDate);
		
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		
		long totalPv = 0;
		Map<String, Long> mPvStatics = new LinkedHashMap<String, Long>();
		long spv = 0; 
		Set<String> domains = new LinkedHashSet<String>();
		int i = 0;
		String mDomain = null;
		
		// 域名与url的处理
		for (Map.Entry<String, Long> entry : pvStatic.entrySet()) {
			String url = entry.getKey();
			Long requetNum = entry.getValue();
			if (i < 3) {
				mPvStatics.put(url, requetNum);
				totalPv += requetNum;
				i++;
			} else {
				spv += requetNum;
				totalPv += requetNum;
			}
			
			if (url.length() > 10 && domains.size() < 3) {
				String noProtocalUrl = url.substring(7);
				String[] array = noProtocalUrl.split("/");

				if (domains.isEmpty()) {
					mDomain = array[0];
				}

				if (array[0] != null
						&& (array[0].endsWith("com") || array[0].endsWith("cn") || array[0]
								.endsWith("org"))) {
					domains.add(array[0]);
				}
				
				if (array[0] != null && array[0].endsWith("-")) {
					String domain = array[0].substring(0, array[0].length() - 1);
					if (StringUtils.isNotEmpty(domain) && (domain.endsWith("com") || domain.endsWith("cn") || domain.endsWith("org"))) {
						domains.add(domain);
					}
				}
			}
		}
		
		// 成本数据处理
		long totalCost = 0;
		String unitCost = "0";
		if (costPo != null) {
			totalCost = costPo.getTotalCost() / CostConstants.MACHINE_COST;
			unitCost = costPo.getFormatTotalPerCost();
		}

		// 机器数
		long machine = 0;
		List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(appname);
		machine = hosts.size();

		
		// uv 数据
		long uv = dateInterfaceBo.getUvByDomainAndDate(mDomain, pvDate);
		
		// page pv
		long pagePv = 0;
		for (String domain : domains) {
			pagePv += dateInterfaceBo.getIPvByDomainAndDate(domain, pvDate);
		}
		
		
		if (totalCost < machine) {
			totalCost = machine;
		}
		
		resultMap.put("appname", appname);
		resultMap.put("pv", pagePv);
		resultMap.put("apache_requst", totalPv);
		resultMap.put("hsf_pv", hsfPv);
		resultMap.put("uv", uv);
		resultMap.put("mpv", mPvStatics);
		resultMap.put("spv", spv);
		resultMap.put("hardwareCost", machine);
		resultMap.put("total_hardwareCost", totalCost);
		resultMap.put("unitHardwareCost", unitCost);
		resultMap.put("domains", domains);

		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		ModelAndView modelAndView = new ModelAndView(returnView);
		modelAndView.addObject("jsonData", jsonObject);
		
		return modelAndView;
	}
	
	@RequestMapping(params = "method=getRealTimeCapacityHtml", method = RequestMethod.GET)
	public void getRealTimeCapacityHtml(HttpServletRequest request,
			   HttpServletResponse response, String appId, String appName) throws Exception {
		PrintWriter writer = response.getWriter();
		StringBuffer sb = new StringBuffer(
				"<table width=400 align=center><tr align=center>"
						+ "<th width=150>appName</th>"
						+ "<th width=150>time</th>"
						+ "<th width=50>capacity</th></tr>");
		
		String url = "http://172.23.152.138:8080/time/app/capacity.do?method=queryAppRealTimeQps&appId=" + appId;
		GetMethod getMethod = new GetMethod(url);
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		int status = httpClient.executeMethod(getMethod);
		if (status == HttpStatus.SC_OK) {
			InputStream in = getMethod.getResponseBodyAsStream();
			byte[] byteA = new byte[1024];
			int size = 0;
			StringBuffer contentBuffer = new StringBuffer();
			while ((size = in.read(byteA)) > 0) {
				contentBuffer.append(new String(byteA, 0, size, "GBK"));
			}
			
			String json = contentBuffer.toString();
			if (StringUtils.isNotBlank(json)) {
				JSONObject jsonObject = JSONObject.fromObject(json);
				
				int count = 0;
				for (Object key : jsonObject.keySet()) {
					count ++;
					if (count > 10) {
						break;
					}
					sb.append("<tr><td>").append(appName).append("</td><td>").append(key.toString()).append("</td>" +
							"<td>").append(jsonObject.get(key).toString()).append("</td></tr>");
				}
			}
		}
		sb.append("</table>");
		
		writer.write(sb.toString());
		writer.close();
		getMethod.releaseConnection();
	}

}
