package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.time.util.MonitorAppUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.TimeConfAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ProductLine;
import com.taobao.monitor.common.po.TimeConfPo;
import com.taobao.monitor.common.po.TimeConfTmpPo;
import com.taobao.monitor.common.util.TBProductCache;

@Controller
@RequestMapping("/app_info.do")
public class AppInfoController extends BaseController {

	public static final Logger log = Logger.getLogger(AppInfoController.class);

	
	
	@RequestMapping(params = "method=collectUpdate")
	public void collectUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String appId = request.getParameter("appId");
		String split = request.getParameter("split");
		String tmpConfigId = request.getParameter("tmpConfigId");
		String frequency = request.getParameter("frequency");
		String future = request.getParameter("future");
		
		String filePath =request.getParameter("filePath");
		String obtainType = request.getParameter("obtainType");
		String tailType = request.getParameter("tailType");
		String className = request.getParameter("analyseClass");
		String analyseType = request.getParameter("analyseType");
		

		TimeConfPo po = TimeConfAo.get().findTimeConfByConfId(Integer.parseInt(tmpConfigId));
		po.setClassName(className);
		po.setSplitChar(split);
		po.setFilePath(filePath);
		po.setAnalyseFuture(future);
		po.setAnalyseFrequency(Integer.parseInt(frequency));
		po.setTailType(tailType);
		po.setAnalyseType(Integer.parseInt(analyseType));
		po.setObtainType(Integer.parseInt(obtainType));
		
		boolean isSuccess = TimeConfAo.get().updateTimeConf(po);
		
		String url = request.getContextPath()+"/app_info.do?method=collect&appId="+ appId;
		response.sendRedirect(url);
		
	}
	
	
	/**
	 *实时采集配置信息,更新 
	 * 
	 */
	@RequestMapping(params = "method=gotoCollectUpdate")
	public ModelAndView gotoCollectUpdate(int appId, int confId, HttpServletRequest request) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		


		TimeConfPo confPo = TimeConfAo.get().findTimeConfByConfId(confId);

		ModelAndView view = new ModelAndView("time/conf/collect_update");
		view.addObject("appInfo", appInfo);
		request.setAttribute("confPo", confPo);
		return view;
	}

	
	
	@RequestMapping(params = "method=collectDelete")
	public void collectDelete(HttpServletRequest request, HttpServletResponse response, int appId, int confId) throws IOException{
		boolean r = TimeConfAo.get().deleteTimeConfByConfId(confId);
		String url = request.getContextPath()+"/app_info.do?method=collect&appId="+ appId;
		response.sendRedirect(url);
	}
	
	@RequestMapping(params = "method=collectAdd")
	public void collectAdd(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String appId = request.getParameter("appId");
		String aliasLogName = request.getParameter("aliasLogName");
		String filePath =request.getParameter("filePath");
		String future = request.getParameter("future");
		String frequency = request.getParameter("frequency");
		String className = request.getParameter("analyseClass");
		String split = request.getParameter("split");
		String tailType = request.getParameter("tailType");
		String analyseType = request.getParameter("analyseType");
		String analysedesc = request.getParameter("analyseDesc");
		String obtainType = request.getParameter("obtainType");
		
		TimeConfPo po = new TimeConfPo();
		po.setAppId(Integer.parseInt(appId));
		po.setAliasLogName(aliasLogName);
		po.setClassName(className);
		po.setSplitChar(split);
		po.setFilePath(filePath);
		po.setAnalyseFuture(future);
		po.setAnalyseFrequency(Integer.parseInt(frequency));
		po.setTailType(tailType);
		po.setAnalyseType(Integer.parseInt(analyseType));
		
		po.setObtainType(Integer.parseInt(obtainType));
		
		boolean isSuccess = false;
		isSuccess = TimeConfAo.get().addTimeConfData(po);		
		
		String url = request.getContextPath()+"/app_info.do?method=collect&appId="+ appId;
		response.sendRedirect(url);
		
	}
	
	/**
	 *实时采集配置信息,新增
	 * 
	 */
	@RequestMapping(params = "method=gotoCollectAdd")
	public ModelAndView gotoCollectAdd(int appId, HttpServletRequest request) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		String tmpId = request.getParameter("tmpId");
		List<TimeConfTmpPo> timeConfTmpList = TimeConfAo.get()
				.findAllAppTimeConfTmp();

		TimeConfTmpPo tmp = null;
		if (tmpId != null) {
			tmp = TimeConfAo.get().findTimeConfTmpById(Integer.parseInt(tmpId));
		}

		ModelAndView view = new ModelAndView("time/conf/collect_add");
		request.setAttribute("tmp", tmp);
		request.setAttribute("tmpId", tmpId);
		request.setAttribute("timeConfTmpList", timeConfTmpList);
		view.addObject("appInfo", appInfo);

		return view;
	}

	/**
	 *实时采集配置信息
	 * 
	 */
	@RequestMapping(params = "method=collect")
	public ModelAndView collect(int appId, HttpServletRequest request) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		List<TimeConfPo> poList = TimeConfAo.get().findTimeConfByAppId(appId);

		ModelAndView view = new ModelAndView("time/conf/collect");
		view.addObject("list", poList);
		view.addObject("appInfo", appInfo);
		return view;
	}
	
	/**
	 *实时采集配置信息
	 * 
	 */
	@RequestMapping(params = "method=changeconfig")
	public ModelAndView changeconfig(int appId, String action,HttpServletRequest request) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		MonitorAppUtil.changeconfig(action,appInfo.getAppName());
		return collect(appId,request);
	}

	/**
	 *@author wb-lixing 2012-3-21 上午11:21:57
	 */
	@RequestMapping(params = "method=edit")
	public ModelAndView edit(int appId, HttpServletRequest request) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView("time/conf/app_info");
		view.addObject("appInfo", appInfo);
		view.addObject("po", appInfo);
		return view;
	}

	@RequestMapping(params = "method=add")
	public void add(AppInfoPo po, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		boolean res = false;
		String cn = po.getCompanyName();
		if (cn != null && !cn.trim().equals("")) {
			cn = new String(cn.getBytes("GBK"), "UTF-8");
			po.setCompanyName(cn);
		}
		String gn = po.getGroupName();
		if (gn != null && !gn.trim().equals("")) {
			gn = new String(gn.getBytes("GBK"), "UTF-8");
			po.setGroupName(gn);
		}

		res = AppInfoAo.get().updateappInfo(po);

		Map map = new HashMap();
		// 1成功 0失败
		map.put("result", res ? 1 : 0);
		map.put("operate", "update");

		writeJSONToResponseJSONObject(response, map);

	}
	
	/**从PE处获取*/
	@RequestMapping(params = "method=allEffectiveAppInfo3")
	public void allEffectiveAppInfo3(HttpServletResponse response)
			throws IOException {
		
		
		List<String> monitorlist = MonitorAppUtil.getMonitorApps();

		// 一个idMap<appId,po>，用于id初始化组件
		Map<String, AppInfoPo> appIdMap = new HashMap<String, AppInfoPo>();
		// 一个nameMap<appName, id>，用于应用名搜索
		Map<String, Integer> appNameIdMap = new HashMap<String, Integer>();
		// <公司名`组名, List<应用名>>
		Map<String, List<String>> appListMap = new HashMap<String, List<String>>();

		Map<String, List<String>> groupListMap = new HashMap<String, List<String>>();
		//应用名列表，用于自动填充
		List<String> appNameList = new ArrayList<String>();

		// 公司名列表
		List<String> companyNameList = new ArrayList<String>();

		for (String  app : monitorlist) {
			AppInfoPo po = AppInfoCache.getAppInfoByAppName(app);
			if(po == null){
				continue;
			}
			ProductLine pline = TBProductCache.getProductLineByAppName(po.getAppName());
			if("未知".equals(pline.getDevelopGroup())){
				pline.setName(po.getCompanyName());
				pline.setDevelopGroup(po.getCompanyName());
				pline.setProductline(po.getGroupName());
				pline.setAppName(po.getAppName());
			}
			//第一级：开发组
			String companyName = pline.getDevelopGroup();
			if(companyName == null) {
				log.error("companyName=null,app=" + po.getAppName());
				companyName = "无效";
			}
			
			// companyName不能为null，因为json key不能为null
			// 另外对null、空值进行处理
		//	companyName = isBlank(companyName) ? "is_blank" : companyName;
			po.setCompanyName(companyName);
			//第二级：产品线
			String groupName = pline.getProductline();
			//groupName = isBlank(groupName) ? "is_blank" : groupName;
			po.setGroupName(groupName);
			String appName = po.getAppName();
			//appName = isBlank(appName) ? "is_blank" : appName;
			po.setAppName(appName);
			appNameList.add(appName);
			
			// 公司名列表
			if (!companyNameList.contains(companyName))
				companyNameList.add(companyName);

			// company group key
			String cgkey = companyName + '`' + groupName;
			List<String> perGroupAppList = appListMap.get(cgkey);
			if (perGroupAppList == null) {

				perGroupAppList = new ArrayList();
				appListMap.put(cgkey, perGroupAppList);
			}
			perGroupAppList.add(appName);

			// groupListMap
			List<String> groupList = groupListMap.get(companyName);
			if (groupList == null) {
				groupList = new ArrayList<String>();
				groupListMap.put(companyName, groupList);
			}
			if(!groupList.contains(groupName)){
				groupList.add(groupName);
			}
			// net sf json key不能为数字
			appIdMap.put(Integer.toString(po.getAppId()), po);
			appNameIdMap.put(po.getAppName(), po.getAppId());
		}

		Map map = new HashMap();
		Collections.sort(companyNameList);
		map.put("companyNameList", companyNameList);
		
		map.put("groupListMap", groupListMap);
		map.put("appListMap", appListMap);
		map.put("appIdMap", appIdMap);
		map.put("appNameIdMap", appNameIdMap);
		map.put("appNameList", appNameList);
		
		
		JSONObject jsonObj = JSONObject.fromObject(map);
		String jsonStr = jsonObj.toString();
		writeJSONToResponse(response, jsonStr);

	}
	
	@RequestMapping(params = "method=allEffectiveAppInfo2")
	public void allEffectiveAppInfo2(HttpServletResponse response)
			throws IOException {
		List<String> monitorlist = MonitorAppUtil.getMonitorApps();

		// 一个idMap<appId,po>，用于id初始化组件
		Map<String, AppInfoPo> appIdMap = new HashMap<String, AppInfoPo>();
		// 一个nameMap<appName, id>，用于应用名搜索
		Map<String, Integer> appNameIdMap = new HashMap<String, Integer>();
		// <公司名`组名, List<应用名>>
		Map<String, List<String>> appListMap = new HashMap<String, List<String>>();

		Map<String, List<String>> groupListMap = new HashMap<String, List<String>>();
		// 应用名列表，用于自动填充
		List<String> appNameList = new ArrayList<String>();

		// 公司名列表
		List<String> companyNameList = new ArrayList<String>();

		List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();

		for (AppInfoPo po : appList) {
			if (monitorlist != null && monitorlist.size() > 0) {
				if (!monitorlist.contains(po.getAppName())) {
					continue;
				}
			}
			String companyName = po.getCompanyName();
			// companyName不能为null，因为json key不能为null
			// 另外对null、空值进行处理
			companyName = isBlank(companyName) ? "is_blank" : companyName;
			po.setCompanyName(companyName);
			String groupName = po.getGroupName();
			groupName = isBlank(groupName) ? "is_blank" : groupName;
			po.setGroupName(groupName);
			String appName = po.getAppName();
			appName = isBlank(appName) ? "is_blank" : appName;
			po.setAppName(appName);
			appNameList.add(appName);

			// 公司名列表
			if (!companyNameList.contains(companyName))
				companyNameList.add(companyName);

			// company group key
			String cgkey = companyName + '`' + groupName;
			List<String> perGroupAppList = appListMap.get(cgkey);
			if (perGroupAppList == null) {

				perGroupAppList = new ArrayList();
				appListMap.put(cgkey, perGroupAppList);
			}
			perGroupAppList.add(appName);

			// groupListMap
			List<String> groupList = groupListMap.get(companyName);
			if (groupList == null) {
				groupList = new ArrayList<String>();
				groupListMap.put(companyName, groupList);
			}
			if (!groupList.contains(groupName)) {
				groupList.add(groupName);
			}
			// net sf json key不能为数字
			appIdMap.put(Integer.toString(po.getAppId()), po);
			appNameIdMap.put(po.getAppName(), po.getAppId());
		}

		Map map = new HashMap();
		map.put("companyNameList", companyNameList);
		map.put("groupListMap", groupListMap);
		map.put("appListMap", appListMap);
		map.put("appIdMap", appIdMap);
		map.put("appNameIdMap", appNameIdMap);
		map.put("appNameList", appNameList);

		writeJSONToResponseJSONObject(response, map);

	}

	/**
	 *@author wb-lixing 2012-3-30 下午06:43:36
	 *@param companyName
	 */
	private boolean isBlank(String str) {
		if (str == null || str.trim().equals(""))
			return true;
		return false;

	}

	@RequestMapping(params = "method=allEffectiveAppInfo")
	public void allEffectiveAppInfo(HttpServletResponse response)
			throws IOException {

		List<String> monitorlist = MonitorAppUtil.getMonitorApps();

		// 将公司名、组名、应用名保存在树中
		Map tree = new HashMap();

		List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();

		// 这个过程，可在client、或server端完成
		List<String> groupList = new ArrayList<String>();

		Map<String, List<AppInfoPo>> groupMap = new HashMap<String, List<AppInfoPo>>();
		for (AppInfoPo po : appList) {
			if (monitorlist != null && monitorlist.size() > 0) {
				if (!monitorlist.contains(po.getAppName())) {
					continue;
				}
			}

			String groupName = po.getGroupName();
			if (!groupList.contains(groupName))
				groupList.add(groupName);
			List<AppInfoPo> list = null;
			if (!groupMap.containsKey(groupName)) {
				list = new ArrayList<AppInfoPo>();
				groupMap.put(groupName, list);
			} else {
				list = groupMap.get(groupName);
			}
			list.add(po);
		}

		Map map = new HashMap();
		map.put("groupList", groupList);
		map.put("groupMap", groupMap);
		writeJSONToResponseJSONObject(response, map);
	}

	@RequestMapping(params = "method=monitorAppInfo")
	public void monitorAppInfo(HttpServletResponse response) throws IOException {

		List<String> monitorlist = MonitorAppUtil.getMonitorApps();

		List<AppInfoPo> tmpList = new ArrayList<AppInfoPo>();

		List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();

		// 这个过程，可在client、或server端完成
		for (AppInfoPo po : appList) {
			if (monitorlist != null && monitorlist.size() > 0) {
				if (!monitorlist.contains(po.getAppName())) {
					continue;
				}
			}

			tmpList.add(po);

		}

		writeJSONToResponseJSONArray(response, tmpList);
	}

}
