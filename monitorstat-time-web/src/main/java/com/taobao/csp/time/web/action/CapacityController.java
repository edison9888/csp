package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.db.impl.capacity.CspCapacityDao;
import com.taobao.monitor.common.db.impl.capacity.CspLoadRunDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspTimeAppDependInfo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

@Controller
@RequestMapping("/app/capacity.do")
public class CapacityController extends BaseController {
	
	private static Logger logger = Logger.getLogger(CapacityController.class);
	
	public static final Logger log = Logger.getLogger(CapacityController.class);
	
	private static Map<Integer, String> pvCoreAppL = new LinkedHashMap<Integer, String>();
	
	private static Map<Integer, String> hsfCoreAppL = new LinkedHashMap<Integer, String>();
	
	
	static {
		pvCoreAppL.put(16, "login");
		pvCoreAppL.put(1, "detail");
		pvCoreAppL.put(2, "hesper");
		pvCoreAppL.put(3, "shopsystem");
		pvCoreAppL.put(11, "mytaobao");
		pvCoreAppL.put(330, "tf_buy");
		pvCoreAppL.put(341, "cart");
		pvCoreAppL.put(369, "malldetail");
		pvCoreAppL.put(379, "tmallbuy");
		pvCoreAppL.put(688, "tmallcart");
		pvCoreAppL.put(323, "tf_tm");
		pvCoreAppL.put(25, "snsju");
		pvCoreAppL.put(12, "mercury");
		
		hsfCoreAppL.put(375, "ic-L0-d");
		hsfCoreAppL.put(376, "ic-L0-c");
		hsfCoreAppL.put(377, "ic-L0-o");
		hsfCoreAppL.put(378, "ic-L1");
		hsfCoreAppL.put(383, "tp-g1");
		hsfCoreAppL.put(384, "tp-g2");
		hsfCoreAppL.put(385, "tp-g3");
		hsfCoreAppL.put(431, "Cart_ump");
		hsfCoreAppL.put(432, "detail_ump");
		hsfCoreAppL.put(456, "Noraml_ump");
		hsfCoreAppL.put(729, "Tmall_ump");
		hsfCoreAppL.put(21, "uicfinal");
		hsfCoreAppL.put(4, "shopcenter");
		hsfCoreAppL.put(585, "inventoryplatform");
	}
	
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	
	@Resource(name = "cspLoadRunDao")
	private CspLoadRunDao cspLoadRunDao;
	
	@Resource(name = "cspCapacityDao")
	private CspCapacityDao cspCapacityDao;
	
	@RequestMapping(params = "method=queryAppRealTimeQps")
	public void queryAppRealTimeQps(HttpServletResponse response, int appId) {
		Map<String, String> infoM = new LinkedHashMap<String, String>();
		DecimalFormat df = new DecimalFormat("##.##");
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		AppInfoPo appInfoPo = AppInfoAo.get().findAppInfoById(appId);
		
		List<TimeDataInfo>  dataL;
		String appName = appInfoPo.getAppName();
		if (("pv".equals(appInfoPo.getAppType()))) {
			dataL = commonService.querySingleKeyData(appName, KeyConstants.PV, PropConstants.E_TIMES);

			for (TimeDataInfo info : dataL) {
				String time = sf.format(new Date(info.getTime()));
				
				double totalQps = info.getMainValue();
				int machineNum = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appName).size();
				int loadrunQps = (int)cspLoadRunDao.findRecentlyAppLoadRunQps(appId);

				if (loadrunQps == 0) {
					loadrunQps = 1;
				}
				if (machineNum == 0) {
					machineNum = 1;
				}
				double level = totalQps * 100 / machineNum / loadrunQps / 60;
				String formatLevel = df.format(level) + "%";
				infoM.put(time, formatLevel);
			}
		}
			
		if ("center".equals(appInfoPo.getAppType())) {
			dataL = commonService.querySingleKeyData(appName, KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);
			
			for (TimeDataInfo info : dataL) {
				String time = sf.format(new Date(info.getTime()));
				
				double totalQps = info.getMainValue();
				int machineNum = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appName).size();
				int loadrunQps = (int)cspLoadRunDao.findRecentlyAppLoadRunQps(appId);
				if (loadrunQps == 0) {
					loadrunQps = 1;
				}
				if (machineNum == 0) {
					machineNum = 1;
				}
				double level = totalQps * 100 / machineNum / loadrunQps /60;
				String formatLevel = df.format(level) + "%";
				infoM.put(time, formatLevel);
			}
		}
		
		JSONObject json = JSONObject.fromObject(infoM);
		
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();	
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	@RequestMapping(params="method=showRealTimeCapacity")
	public void showRealTimeCapacity(HttpServletResponse response){
		// <time,<appname,level>>
		Map<String, Map<String, String>> infoM = new LinkedHashMap<String, Map<String,String>>();
		DecimalFormat df = new DecimalFormat("##.##");
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("detail", "20%");
//		map.put("hesper", "50%");
//		infoM.put("12:00", map);
//		
//		Map<String, String> map1 = new HashMap<String, String>();
//		map1.put("detail", "20%");
//		map1.put("hesper", "50%");
//		infoM.put("11:00", map1);
		
		for (int appId : pvCoreAppL.keySet()) {
			String appName = pvCoreAppL.get(appId);
			List<TimeDataInfo>  dataL = commonService.querySingleKeyData(appName, KeyConstants.PV, PropConstants.E_TIMES);

			for (TimeDataInfo info : dataL) {
				String time = sf.format(new Date(info.getTime()));
				Map<String, String> timeLevelM = getAppCapacityLevelMap(infoM, time);
				if (timeLevelM == null) {
					continue;
				}
				
				double totalQps = info.getMainValue();
				int machineNum = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appName).size();
				int loadrunQps = (int)cspLoadRunDao.findRecentlyAppLoadRunQps(appId);
				logger.info(appName + ":" + time + ":" + totalQps  + ":" + machineNum + ":" + loadrunQps);
				if (loadrunQps == 0) {
					logger.warn(appName + " loadrun qps is 0!");
					loadrunQps = 1;
				}
				if (machineNum == 0) {
					logger.warn(appName + " machine num is 0!");
					machineNum = 1;
				}
				double level = totalQps * 100 / machineNum / loadrunQps / 60;
				String formatLevel = df.format(level) + "%";
				timeLevelM.put(appName, formatLevel);
			}
		}
		
		for (int appId : hsfCoreAppL.keySet()) {
			String appName = hsfCoreAppL.get(appId);
			List<TimeDataInfo>  dataL = commonService.querySingleKeyData(appName, KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);
			
			for (TimeDataInfo info : dataL) {
				String time = sf.format(new Date(info.getTime()));
				Map<String, String> timeLevelM = getAppCapacityLevelMap(infoM, time);
				if (timeLevelM == null) {
					continue;
				}
				
				double totalQps = info.getMainValue();
				int machineNum = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appName).size();
				int loadrunQps = (int)cspLoadRunDao.findRecentlyAppLoadRunQps(appId);
				if (loadrunQps == 0) {
					logger.warn(appName + " loadrun qps is 0!");
					loadrunQps = 1;
				}
				if (machineNum == 0) {
					logger.warn(appName + " machine num is 0!");
					machineNum = 1;
				}
				double level = totalQps * 100 / machineNum / loadrunQps /60;
				String formatLevel = df.format(level) + "%";
				timeLevelM.put(appName, formatLevel);
			}
		}
		
		JSONObject json = JSONObject.fromObject(infoM);
		
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();	
		} catch (IOException e) {
			logger.error("", e);
		}
		return ;
	}
	
	@RequestMapping(params = "method=showRealTimeDepCapacity")
	public ModelAndView showRealTimeDepCapacity(String appName) {
		if (StringUtils.isEmpty(appName)) {
			return null;
		}
		
		ModelAndView view = new ModelAndView("/time/capacity/realTimeDepCapacity");
		
		List<CspTimeAppDependInfo> list = CspDependInfoAo.get().getCspTimeAppDependInfoBySourceNameByDepth(appName, 1);
		if (list.size() == 0) {
			return null;
		}
		
		int xNum = 1;
		StringBuffer xSb = new StringBuffer();
		StringBuffer y1Sb = new StringBuffer();   // 当前qps
		StringBuffer y3Sb = new StringBuffer();   // 压测qps
		
		for (CspTimeAppDependInfo po : list) {
			String depApp = po.getDepAppName();
			AppInfoPo appInfo = AppInfoCache.getAppInfoByAppName(depApp);
			
		    // 目前只针对hsf
			if (appInfo == null || appInfo.getAppType() == null || (!appInfo.getAppType().toLowerCase().equals("center"))) {
				continue;
			}
	
			// 分组处理
			String groups = cspCapacityDao.findCapacityGroups(appInfo.getAppId());
			if (groups != null && !StringUtils.isEmpty(groups) && (!groups.toLowerCase().equals("null"))) {
				String[] groupsArray = groups.split(";");
				for (String group : groupsArray) {
					String[] infos = group.split(":");
					if (infos.length == 3) {
						// String groupName = infos[0];
						String name = infos[1];
						int groupId = Integer.parseInt(infos[2]);
						
						xSb.append("<value xid='" + xNum + "'>" + name + "</value>");
						
						int currentQps = findQps(name);
						int loadQps = findLoadQps(groupId);
						
						y1Sb.append("<value xid='" + xNum + "'>" + currentQps + "</value>");
						y3Sb.append("<value xid='" + xNum + "'>" + findLoadRemainQps(loadQps, currentQps) + "</value>");
						
						xNum ++;
					}
				}
			} else {
				xSb.append("<value xid='" + xNum + "'>" + appInfo.getAppName() + "</value>");
				
				int currentQps = findQps(appInfo.getAppName());
				int loadQps = findLoadQps(appInfo.getAppId());
				
				y1Sb.append("<value xid='" + xNum + "'>" + currentQps + "</value>");
				y3Sb.append("<value xid='" + xNum + "'>" + findLoadRemainQps(loadQps, currentQps) + "</value>");
				
				xNum++;
			}
			
			
		}
		
		String chartLoad = generateChart(xSb.toString(), y1Sb.toString(), y3Sb.toString(), "当前qps", "剩余压测qps");
		view.addObject("opsName", appName);
		view.addObject("chartLoad", chartLoad);
		
		return view;
	}
	
	private int findQps(String appName) {

		Map<String, Float> map = commonService.queryAverageKeyDataByHost(appName, KeyConstants.HSF_PROVIDER, PropConstants.E_TIMES);
		if (map == null || map.size() == 0) {
			return 0;
		}
		
		Object [] timesS = map.keySet().toArray();
		if (timesS != null && timesS.length > 1) {
			Arrays.sort(timesS);
		}
		
		// get value before latest minute
		return map.get(timesS[timesS.length - 1]).intValue();
	}
	
	private int findLoadQps(int appId) {
		try{
			return (int)(cspLoadRunDao.findRecentlyAppLoadRunQps(appId));
		}catch (Exception e) {
			return 0;
		}
	}

	
	private int findLoadRemainQps(int loadQps, int currentQps) {
		int remainQps = loadQps - currentQps;
		if (remainQps <= 0) {
			remainQps = 0;
		}
		
		return remainQps;
	}
	
	
	private String generateChart(String x, String y1, String y2, String title1, String title2) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<chart>");
		
		sb.append("<series>");
		sb.append(x);
		sb.append("</series>");
		
		sb.append("<graphs>");
		sb.append("<graph gid='qps' title='" + title1 + "'>");
		sb.append(y1);
		sb.append("</graph>");
		sb.append("<graph gid='remainQqs' title='" + title2 + "' color='10BD10'>");
		sb.append(y2);
		sb.append("</graph>");
		
		sb.append("</graphs>");
		sb.append("</chart>");
		
		return sb.toString();
	}
	
	private Map<String, String> getAppCapacityLevelMap(Map<String, Map<String, String>> infoM, String time) {
		
		Map<String, String> appCapacityLevelM;
		if (!infoM.containsKey(time)) {
			// 出现时间比之前都大的，去掉
			for (String existTime : infoM.keySet()) {
				if (time.compareTo(existTime) > 0) {
					return null;
				}
			}
			appCapacityLevelM = new LinkedHashMap<String, String>();
			infoM.put(time, appCapacityLevelM);
			for (int appId : pvCoreAppL.keySet()) {
				String appName = pvCoreAppL.get(appId);
				appCapacityLevelM.put(appName, "-");
			}
			
			for (int appId : hsfCoreAppL.keySet()) {
				String appName = hsfCoreAppL.get(appId);
				appCapacityLevelM.put(appName, "-");
			}
		} else {
			appCapacityLevelM = infoM.get(time);
		}
		
		return appCapacityLevelM;
	}

}
