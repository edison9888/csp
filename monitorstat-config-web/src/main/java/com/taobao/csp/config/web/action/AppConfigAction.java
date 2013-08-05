package com.taobao.csp.config.web.action;


import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.DataBaseInfoAo;
import com.taobao.monitor.common.ao.center.HostAo;
import com.taobao.monitor.common.ao.center.ServerInfoAo;
import com.taobao.monitor.common.ao.center.TimeConfAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.DataBaseInfoPo;
import com.taobao.monitor.common.po.DatabaseAppRelPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.po.ServerAppRelPo;
import com.taobao.monitor.common.po.ServerInfoPo;
import com.taobao.monitor.common.po.TimeConfPo;
import com.taobao.monitor.common.po.TimeConfTmpPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * 对应用设置的action
 * @author denghaichuan.pt
 * @version 2011-12-26
 */

@Controller
@RequestMapping("/show/appconfig.do")
public class AppConfigAction {

	private static final Logger logger =  Logger.getLogger(AppConfigAction.class);
	
	@Resource(name = "appInfoAo")
	private AppInfoAo appInfoAo;
	
	/**
	 * show the app info for config
	 * @author denghaichuan.pt
	 * @return
	 */
	@RequestMapping(params = "method=showAppInfo")
	public ModelAndView showAppInfo(String groupName) {
		
		try {
			groupName = URLDecoder.decode(URLDecoder.decode(groupName, "utf-8"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		List<AppInfoPo> allAppList = appInfoAo.findAllAppInfo();
		
		List<AppInfoPo> groupAppList = new ArrayList<AppInfoPo>();
		
		Set<String> groupNameSet = new HashSet<String>();
		
		for (AppInfoPo po : allAppList) {
			
			if (po.getGroupName() != null) {
				groupNameSet.add(po.getGroupName());
				
				if (po.getGroupName().equals(groupName)) {
					groupAppList.add(po);
				}
			}
			
		}
		
		if (groupName.equals("AllApp")) {
			groupAppList = allAppList;
		}
		
		ModelAndView view = new ModelAndView("config/appconf/app_show");
		view.addObject("groupAppList", groupAppList);
		view.addObject("groupNameSet", groupNameSet);
		view.addObject("groupName", groupName);
		
		return view;
		
	}
	
	@RequestMapping(params = "method=showAppDetail")
	public ModelAndView showAppDetail(int appId) {
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		
		ModelAndView view = new ModelAndView("config/appconf/app_info_update");
		view.addObject("appInfoPo", appInfoPo);
		return view;
	}
	
	@RequestMapping(params = "method=checkAppDetail")
	public ModelAndView checkAppDetail(int appId) {
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		
		List<HostPo>  hostList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appInfoPo.getOpsName());
		
		Set<HostPo>  monitorHostSet =new HashSet<HostPo>();
		for(HostPo p : hostList) {
			
			// 检查是否为实时监控的主机
			if(!HostAo.get().isExistHostByHostIpAndAppId(appInfoPo.getAppId(),p.getHostIp())) {
				monitorHostSet.add(p);
			}
		}
		
		ModelAndView view = new ModelAndView("config/appconf/app_info_check");
		view.addObject("hostList", hostList);
		view.addObject("monitorHostSet", monitorHostSet);
		view.addObject("appInfoPo", appInfoPo);
		
		return view;
	}
	
	@RequestMapping(params = "method=addAppDetail")
	public void addAppDetail(HttpServletResponse response, String appName, String appType, String feature, String opsName, String opsField, String group, String rushTime, String userName, String password, String dayDeploy, String timeDeploy, String status) {
		
		AppInfoPo po = new AppInfoPo();
		
		po.setAppName(appName);
		po.setFeature(feature);
		po.setAppType(appType);
		po.setOpsName(opsName);
		po.setGroupName(group);
		po.setDayDeploy(Integer.parseInt(dayDeploy));
		po.setTimeDeploy(Integer.parseInt(timeDeploy));
		po.setAppStatus(Integer.parseInt(status));
		po.setOpsField(opsField);
		po.setLoginName(userName);
		po.setLoginPassword(password);
		po.setAppRushHours(rushTime);
		
		boolean isSuccess = appInfoAo.addAppInfoData(po);
		
		PrintWriter pw = null;
		try{
			response.setContentType("text/html;charset=utf-8");
			pw = response.getWriter();
			if (isSuccess) {
				pw.write("添加成功");
			} else {
				pw.write("添加失败");
			}
			response.flushBuffer();
		}catch (Exception e) {
			logger.error("error", e);
		}
		finally {
			pw.close();
		}
	}
	
	
	
	@RequestMapping(params = "method=updateAppInfo")
	public ModelAndView updateAppInfo(AppInfoPo po) {
		
		boolean isSuccess = appInfoAo.updateappInfo(po);
		
		ModelAndView view = showAppDetail(po.getAppId());
		view.addObject("isSuccess", isSuccess);
		
		return view;
	}
	
	@RequestMapping(params = "method=deleteApp")
	public ModelAndView deleteApp(int appId, String groupName) {
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		appInfoPo.setAppStatus(1);
		
		appInfoAo.updateappInfo(appInfoPo);
		
		return showAppInfo(groupName);
		
	}
	
	@RequestMapping(params = "method=cancelDeleteApp")
	public ModelAndView cancelDeleteApp(int appId, String groupName) {
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		appInfoPo.setAppStatus(0);
		
		appInfoAo.updateappInfo(appInfoPo);
		
		return showAppInfo(groupName);
	}
	
	
	@RequestMapping(params = "method=addAppPage")
	public ModelAndView addAppPage() {
		ModelAndView view = new ModelAndView("config/appconf/app_info_add");
		return view;
	}
	
	@RequestMapping(params = "method=getTimeConfig")
	public ModelAndView getTimeConfig(int appId) {
		
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		
		List<AppInfoPo> appInfo4DbPoList = AppInfoAo.get().findDatabaseRel(appId);
		
		List<DataBaseInfoPo>  dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
		
		List<AppInfoPo> appInfo4ServerPoList = AppInfoAo.get().findServerRel(appId);
		
		List<ServerInfoPo>  serverInfoList = ServerInfoAo.get().findAllServerInfo();
		
		List<TimeConfPo> timeConfPoList = TimeConfAo.get().findTimeConfByAppId(appId);
		
		List<TimeConfTmpPo> timeConfTmpList = TimeConfAo.get().findAllAppTimeConfTmp();
		
		ModelAndView view = new ModelAndView("config/appconf/time/time_config");
		
		AppInfoPo appInfoPo1 = AppInfoAo.get().findAppWithHostListByAppId(appId);
		int sumCM3 = 0;
		int sumCM4 = 0;
		int sumCM3Limit = 0;	//临时表
		int sumCM3Save = 0;	//持久表
		int sumCM4Limit = 0;	//临时表
		int sumCM4Save = 0;	//持久表
		
		for(HostPo hostPo : appInfoPo1.getHostList()) {
			if("CM3".equals(hostPo.getHostSite())) {
				sumCM3++;
				if(hostPo.getSavedata().charAt(0) == '1') {
					sumCM3Limit++;
				}
				if(hostPo.getSavedata().charAt(1) == '1') {
					sumCM3Save++;
				}
			} else if("CM4".equals(hostPo.getHostSite())) {
				sumCM4++;
				if(hostPo.getSavedata().charAt(0) == '1') {
					sumCM4Limit++;
				}
				if(hostPo.getSavedata().charAt(1) == '1') {
					sumCM4Save++;
				}
			}
		}
		
		Map<String, Integer> hostDataMap = new HashMap<String, Integer>();
		hostDataMap.put("sumCM3", sumCM3);
		hostDataMap.put("sumCM4", sumCM4);
		hostDataMap.put("sumCM3Limit", sumCM3Limit);
		hostDataMap.put("sumCM3Save", sumCM3Save);
		hostDataMap.put("sumCM4Limit", sumCM4Limit);
		hostDataMap.put("sumCM4Save", sumCM4Save);
		
		view.addObject("appInfoPo", appInfoPo);
		view.addObject("appInfo4DbPoList", appInfo4DbPoList);
		view.addObject("dbInfoList", dbInfoList);
		view.addObject("appInfo4ServerPoList", appInfo4ServerPoList);
		view.addObject("serverInfoList", serverInfoList);
		view.addObject("timeConfPoList", timeConfPoList);
		view.addObject("timeConfTmpList", timeConfTmpList);
		view.addObject("hostDataMap", hostDataMap);
		
		return view;
	}
	
	@RequestMapping(params = "method=deleteDB") 
	public ModelAndView deleteDB(HttpServletResponse response, int appId, String appType, int databaseId ) {
		DatabaseAppRelPo relPo = new DatabaseAppRelPo();
		relPo.setAppId(appId);
		relPo.setAppType(appType);
		relPo.setDatabaseId(databaseId);
		boolean isSuccess = appInfoAo.deleteRel(relPo);
		
		return getTimeConfig(appId);
	}
	
	@RequestMapping(params = "method=addDBRel") 
	public ModelAndView addDBRel(HttpServletResponse response, int appId, String appType, int databaseId ) {
		DatabaseAppRelPo relPo = new DatabaseAppRelPo();
		relPo.setAppId(appId);
		relPo.setAppType(appType);
		relPo.setDatabaseId(databaseId);
		
		boolean  isSuccess = false;
		if (appInfoAo.isExistDatabaseAppRel(relPo)) {
			isSuccess = false;
		} else {
			isSuccess = appInfoAo.addRel(relPo);
		}
		
		return getTimeConfig(appId);
	}
	
	@RequestMapping(params = "method=updateServer") 
	public ModelAndView updateServer(HttpServletResponse response, int appId, String appType, int serverId, String type) {
		ServerAppRelPo relPo = new ServerAppRelPo();
		relPo.setAppId(appId);
		relPo.setAppType(appType);
		relPo.setServerId(serverId);
		
		boolean isSuccess;
		
		if (type.equals("update")) {
			isSuccess = appInfoAo.updateRel(relPo);
		} else if (type.equals("delete")) {
			isSuccess = appInfoAo.deleteRel(relPo);
		} else if (type.equals("add")) {
			if (appInfoAo.isExistServerAppRel(relPo)) {
				isSuccess = false;
			} else {
				isSuccess = appInfoAo.addRel(relPo);
			}
		}
		
		return getTimeConfig(appId);
	}
	
	@RequestMapping(params = "method=getTimeModel")
	public ModelAndView getTimeModel(int appId) {
		
		List<TimeConfTmpPo> timeConfTmpList = TimeConfAo.get().findAllAppTimeConfTmp();
		
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		
		ModelAndView view = new ModelAndView("config/appconf/time/time_model");
		view.addObject("appInfoPo", appInfoPo);
		view.addObject("timeConfTmpList", timeConfTmpList);
		
		return view;
		
	}
	
	@RequestMapping(params = "method=getModelById")
	public void getModelById(HttpServletRequest request, HttpServletResponse response) {
		int tmpId = Integer.parseInt(request.getParameter("tmpId"));
		TimeConfTmpPo timeConfTmpPo = TimeConfAo.get().findTimeConfTmpById(tmpId);
		PrintWriter pw = null;
		try{
			response.setContentType("application/json");
			pw = response.getWriter();
			JSONObject resultJSON = JSONObject.fromObject(timeConfTmpPo); 
			pw.write(resultJSON.toString());
			response.flushBuffer();
		}catch (Exception e) {
			logger.error("error", e);
		}
		finally {
			pw.close();
		}
	}
	
	@RequestMapping(params = "method=addAppModel")
	public ModelAndView addAppModel(int appId, TimeConfTmpPo tmpPo) {
		
		TimeConfPo po = new TimeConfPo();
		po.setAppId(appId);
		po.setAliasLogName(tmpPo.getAliasLogName());
		po.setClassName(tmpPo.getClassName());
		po.setSplitChar(tmpPo.getSplitChar());
		po.setFilePath(tmpPo.getFilePath());
		po.setAnalyseFuture(tmpPo.getAnalyseFuture());
		po.setAnalyseFrequency(tmpPo.getAnalyseFrequency());
		po.setTailType(tmpPo.getTailType());
		po.setAnalyseType(tmpPo.getAnalyseType());
		po.setObtainType(tmpPo.getObtainType());
		
		boolean isSuccess = false;
		isSuccess = TimeConfAo.get().addTimeConfData(po);		
		
		return getTimeConfig(appId);
	}
	
	@RequestMapping(params = "method=deleteAppModel")
	public ModelAndView deleteAppModel(int appId, int confId) {
		TimeConfAo.get().deleteTimeConfByConfId(confId);
		return getTimeConfig(appId);
	}
	
	@RequestMapping(params = "method=getConfPo")
	public void getConfPo(int confId, HttpServletResponse response) {
		TimeConfPo timeConfPo = TimeConfAo.get().findTimeConfByConfId(confId);
		PrintWriter pw = null;
		try{
			response.setContentType("application/json");
			pw = response.getWriter();
			JSONObject resultJSON = JSONObject.fromObject(timeConfPo);
			pw.write(resultJSON.toString());
			response.flushBuffer();
		}catch (Exception e) {
			logger.error("error", e);
		}
		finally {
			pw.close();
		}
	}
	
	@RequestMapping(params = "method=updateAppModel")
	public ModelAndView updateAppModel(TimeConfPo po, int appId) {
		TimeConfAo.get().updateTimeConf(po);
		return getTimeConfig(appId);
	}
	
	@RequestMapping(params = "method=checkAppHost")
	public ModelAndView checkAppHost(int appId) {
		
		List<HostPo> hostList = appInfoAo.findAppWithHostListByAppId(appId).getHostList();

		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		
		ModelAndView view = new ModelAndView("config/appconf/time/app_host_check");
		view.addObject("appInfoPo", appInfoPo);
		view.addObject("hostList", hostList);
		
		return view;
	}
	
	@RequestMapping(params = "method=gotoSingleAdd")
	public ModelAndView gotoSingleAdd(int appId) {
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		ModelAndView view = new ModelAndView("config/appconf/time/app_host_addsingle");
		view.addObject("appInfoPo", appInfoPo);
		return view;
	}
	
	@RequestMapping(params = "method=singleAddAppHost")
	public ModelAndView singleAddAppHost(int appId, String hostName,String hostIp, String hostSite, String saveData) {
		
		List<HostPo> selectedHostList = new ArrayList<HostPo>();
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		
		HostPo po = new HostPo();
		po.setAppId(appId);
		po.setHostIp(hostIp);
		po.setHostName(hostName);
		po.setHostSite(hostSite);
		po.setSavedata(saveData);
		po.setUserName(appInfoPo.getLoginName());
		po.setUserPassword(appInfoPo.getLoginPassword());
		
		selectedHostList.add(po);
		
		boolean b = HostAo.get().addHostList(selectedHostList);
		
		return checkAppHost(appId);
	}
	
	
	@RequestMapping(params = "method=deleteAppHost")
	public ModelAndView deleteAppHost(HttpServletRequest request, int appId) {
		String[] deleteId = request.getParameterValues("selectId");
		HostAo.get().deleteHostList(deleteId);
		return checkAppHost(appId);
	}
	
	@RequestMapping(params = "method=addAppHost")
	public ModelAndView addAppHost(int appId, Boolean issuccess) {
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		List<HostPo>  hostList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appInfoPo.getOpsName());
		List<HostPo>  cmHostList = new ArrayList<HostPo>();
		for(HostPo p : hostList) {
			if(!HostAo.get().isExistHostByHostIpAndAppId(appInfoPo.getAppId(),p.getHostIp())) {
				cmHostList.add(p);
			}
		}
		ModelAndView view = new ModelAndView("config/appconf/time/app_host_add");
		view.addObject("cmHostList", cmHostList);
		view.addObject("appInfoPo", appInfoPo);
		view.addObject("issuccess", issuccess);
		return view;
	}
	
	@RequestMapping(params = "method=insertAppHost")
	public ModelAndView insertAppHost(HttpServletRequest request, int appId) {
		AppInfoPo appInfoPo = appInfoAo.findAppInfoById(appId);
		String[] selectHostIps = request.getParameterValues("selectId");
		List<HostPo> selectedHostList = new ArrayList<HostPo>();
		int sumOfSaveData2 = 0;		//标记持久表个数
		for(String ip:selectHostIps){
			String hostName = request.getParameter("hostName_"+ip);
			String hostSite = request.getParameter("hostSite_"+ip);
			String saveData1 = request.getParameter("saveData1_"+ip);
			String saveData2 = request.getParameter("saveData2_"+ip);
			if(saveData2 != null) {
				sumOfSaveData2++;	//统计勾选持久表个数
			}
			HostPo po = new HostPo();
			po.setAppId(appId);
			po.setHostIp(ip);
			po.setHostName(hostName);
			po.setHostSite(hostSite);
			String saveData = (saveData1==null?"0":"1")+(saveData2==null?"0":"1");
			po.setSavedata(saveData);
			po.setUserName(appInfoPo.getLoginName());
			po.setUserPassword(appInfoPo.getLoginPassword());

			selectedHostList.add(po);
		}
		int all = sumOfSaveData2 + HostAo.get().sumOfSaveData(appId);	//当前选择的持久表和数据库已经存在的持久表的累加
		
		boolean b = false;
		if(all <= 6) {		//如果持久表大于6，就不执行添加，返回添加失败页面
			b = HostAo.get().addHostList(selectedHostList);
			return checkAppHost(appId);
		} else {
			return addAppHost(appId , false);
		}
	}
	
	@RequestMapping(params = "method=gotoTimeTmp")
	public ModelAndView gotoTimeTmp(Boolean issuccess) {
		List<TimeConfTmpPo> tmpList = TimeConfAo.get().findAllAppTimeConfTmp();
		ModelAndView view = new ModelAndView("config/appconf/timetmp/time_tmp");
		view.addObject("tmpList", tmpList);
		view.addObject("issuccess", issuccess);
		return view;
	}
	
	@RequestMapping(params = "method=gotoTimeTmpUpdate")
	public ModelAndView gotoTimeTmpUpdate(int tmpId) {
		TimeConfTmpPo timeConfTmpPo = TimeConfAo.get().findTimeConfTmpById(tmpId);
		ModelAndView view = new ModelAndView("config/appconf/timetmp/time_tmp_update");
		view.addObject("timeConfTmpPo", timeConfTmpPo);
		return view;
	}
	
	@RequestMapping(params = "method=updateTimeTmp")
	public ModelAndView updateTimeTmp(TimeConfTmpPo po) {
		boolean b = TimeConfAo.get().updateTimeConfTmp(po);
		return gotoTimeTmp(b);
	}
	
	@RequestMapping(params = "method=deleteTimeTmp")
	public ModelAndView deleteTimeTmp(int tmpId) {
		TimeConfAo.get().deleteTimeConfTmp(tmpId);
		return gotoTimeTmp(null);
	}
	
	@RequestMapping(params = "method=gotoTimeTmpCheck")
	public ModelAndView gotoTimeTmpCheck(int tmpId) {
		TimeConfTmpPo timeConfTmpPo = TimeConfAo.get().findTimeConfTmpById(tmpId);
		ModelAndView view = new ModelAndView("config/appconf/timetmp/time_tmp_check");
		view.addObject("timeConfTmpPo", timeConfTmpPo);
		return view;
	}
	
	@RequestMapping(params = "method=gotoAddTmpPage")
	public ModelAndView gotoAddTmpPage() {
		ModelAndView view = new ModelAndView("config/appconf/timetmp/time_tmp_add");
		return view;
	}
	
	@RequestMapping(params = "method=addTimeTmp")
	public ModelAndView addTimeTmp(TimeConfTmpPo po) {
		boolean b = TimeConfAo.get().addTimeConfTmp(po);
		return gotoTimeTmp(null);
	}
	
}
