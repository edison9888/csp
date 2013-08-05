
package com.taobao.csp.loadrun.web.action;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.arkclient.csp.Permisson;
import com.taobao.arkclient.csp.UserPermissionCheck;
import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.HttpLoadLogType;
import com.taobao.csp.loadrun.web.LoadRunHost;
import com.taobao.csp.loadrun.web.LoadrunTaskMaster;
import com.taobao.csp.loadrun.web.LoadrunWebContainer;
import com.taobao.csp.loadrun.web.bo.CspLoadRunBo;
import com.taobao.csp.loadrun.web.po.LoadrunAppInfoPo;
import com.taobao.csp.loadrun.web.util.LoadrunUtil;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;

/**
 * 
 * @author xiaodu
 * @version 2011-7-6 下午04:27:15
 */

@Controller
@RequestMapping("/loadrun/config.do")
public class LoadrunConfigAction {
	
	@Resource(name="cspLoadRunBo")
	private CspLoadRunBo cspLoadRunBo;
	
	@Resource(name="loadrunTaskMaster")
	private LoadrunTaskMaster loadrunTaskMaster;
	
	@Resource(name="loadrunWebContainer")
	private LoadrunWebContainer loadrunWebContainer;
	
	@Resource(name="userPermissionCheck")
	private UserPermissionCheck userPermissionCheck;
	
	private static final Logger logger = Logger.getLogger(LoadrunConfigAction.class);
	
	

	public UserPermissionCheck getUserPermissionCheck() {
		return userPermissionCheck;
	}
	public void setUserPermissionCheck(UserPermissionCheck userPermissionCheck) {
		this.userPermissionCheck = userPermissionCheck;
	}
	@RequestMapping(params="method=detail")
	public ModelAndView detailLoadrunConfig(int appId){
		LoadRunHost load = cspLoadRunBo.findLoadRunHostByAppId(appId);
		
		
		ModelAndView model = new ModelAndView("/autoload/conf/app_loadrun_detail");
		model.addObject("loadconfig", load);
		return model;
		
	}
	
	
	@RequestMapping(params="method=list")
	public ModelAndView listLoadrunConfig(){
		List<LoadRunHost> list = cspLoadRunBo.findAllLoadRunHost();
		
		ModelAndView model = new ModelAndView("/autoload/conf/app_loadrun_list");
		model.addObject("loadconfigList", list);
		return model;
	}
	
	
	@RequestMapping(params="method=gotoAdd")
	public ModelAndView gotoAddAppLoadrunConfig(HttpServletRequest request,
			   HttpServletResponse response){
		boolean hasPermission = userPermissionCheck.check(request, Permisson.ADD_APP, Permisson.NO_AIM, 0);
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		
		List<AppInfoPo> list = cspLoadRunBo.findAllAppInfo();
		List<LoadrunAppInfoPo> apps = transferApp(list);
		
		ModelAndView modelView  = new ModelAndView("/autoload/conf/app_loadrun_add");
		modelView.addObject("appList", apps);
		modelView.addObject("appInfo", apps.get(0));
		modelView.addObject("loadrunTypes", AutoLoadType.values());
		modelView.addObject("loadrunModes", AutoLoadMode.values());
		modelView.addObject("httploadlogtype", HttpLoadLogType.values());
		
		return modelView;
	}
	
	
	@RequestMapping(params="method=add")
	public ModelAndView addAppLoadrunConfig(HttpServletRequest request,
			   HttpServletResponse response, LoadRunHost target){
		boolean hasPermission = userPermissionCheck.check(request, Permisson.ADD_APP, Permisson.NO_AIM, 0);
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		
		AppInfoPo app = cspLoadRunBo.findAppInfoById(target.getAppId());
		if(app != null){
			target.setAppName(app.getAppName());
			target.setAppRushHours(app.getAppRushHours());
			target.setOpsField(app.getOpsField());
			target.setOpsName(app.getOpsName());
		}
		
		try {
			LoadrunUtil.checkTargetMessage(target.getTarget());
		} catch (Exception e2) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", e2.getMessage());
			modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		
		String time = target.getStartTime();
		try {
			CronExpression cron = new CronExpression(time);
			cron.getCronExpression();
		} catch (ParseException e1) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "执行时间设置错误!"+e1.getMessage());
			modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		boolean b = cspLoadRunBo.addLoadRunHost(target);
		if(b){
			try {
				loadrunTaskMaster.addTask(target.getTarget());
			} catch (Exception e) {
				ModelAndView modelView = new ModelAndView();
				modelView.addObject("ErrorMsg", "添加数据库成功，加入执行容器失败，"+e.getMessage());
				modelView.addObject("backurl", "/loadrun/config.do?method=list");
				modelView.setViewName("/message");
				return modelView;
			}
			return listLoadrunConfig();
		}else{
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "应用已经存在或添加出错!");
			modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		
	}
	
	@RequestMapping(params="method=delete")
	public ModelAndView deleteAppLoadrunConfig(HttpServletRequest request,
			   HttpServletResponse response, int appId){
		boolean hasPermission = userPermissionCheck.check(request, Permisson.DELETE_APP, String.valueOf(appId), appId);
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		
		LoadRunHost load = cspLoadRunBo.findLoadRunHostByAppId(appId);
		try {
			loadrunTaskMaster.deleteTask(load.getTarget());
		} catch (Exception e) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "删除失败!,无法删除执行容器内的"+e.getMessage());
			modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		logger.info("delete loadrun from ip:" + request.getRemoteAddr());
		boolean b = cspLoadRunBo.deleteLoadRunHost(appId);
		if(b){
			return listLoadrunConfig();
		}else{
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "删除失败!");
			modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
	}
	
	@RequestMapping(params="method=gotoUpdate")
	public ModelAndView goUpdateLoadrunConfig(HttpServletRequest request,
			   HttpServletResponse response, int appId){
		boolean hasPermission = userPermissionCheck.check(request, Permisson.EDIT_APP, String.valueOf(appId), appId);
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		
		try {
			if (loadrunTaskMaster.isRun(appId) || loadrunWebContainer.isRun(appId)) {
				ModelAndView modelView = new ModelAndView();
				modelView.addObject("ErrorMsg", "应用正在进行压测，不允许修改配置.");
				// modelView.addObject("backurl", "/loadrun/config.do?method=list");
				modelView.setViewName("/message");
				return modelView;
			}
		} catch (Exception e) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", e.getMessage());
			// modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		LoadRunHost load = cspLoadRunBo.findLoadRunHostByAppId(appId);
		
		ModelAndView model = new ModelAndView("/autoload/conf/app_loadrun_update");
		model.addObject("loadconfig", load);
		model.addObject("loadrunTypes", AutoLoadType.values());
		model.addObject("loadrunModes", AutoLoadMode.values());
		model.addObject("httploadlogtype", HttpLoadLogType.values());
		return model;
	}
	
	
	@RequestMapping(params="method=update")
	public ModelAndView updateAppLoadrunConfig(HttpServletRequest request,
			   HttpServletResponse response, LoadRunHost target){
		boolean hasPermission = userPermissionCheck.check(request, Permisson.EDIT_APP, String.valueOf(target.getAppId()), target.getAppId());
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		
		try {
			if (loadrunTaskMaster.isRun(target.getAppId()) || loadrunWebContainer.isRun(target.getAppId())) {
				ModelAndView modelView = new ModelAndView();
				modelView.addObject("ErrorMsg", "应用正在进行压测，不允许修改配置!");
				// modelView.addObject("backurl", "/loadrun/config.do?method=list");
				modelView.setViewName("/message");
				return modelView;
			}
		} catch (Exception e) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", e.getMessage());
			// modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		AppInfoPo app = cspLoadRunBo.findAppInfoById(target.getAppId());
		if(app != null){
			target.setAppName(app.getAppName());
			target.setAppRushHours(app.getAppRushHours());
			target.setOpsField(app.getOpsField());
			target.setOpsName(app.getOpsName());
		}
		
		try {
			LoadrunUtil.checkTargetMessage(target.getTarget());
		} catch (Exception e2) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", e2.getMessage());
			modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		String time = target.getStartTime();
		try {
			CronExpression cron = new CronExpression(time);
			cron.getCronExpression();
		} catch (ParseException e1) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "执行时间设置错误!"+e1.getMessage());
			modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		
		try {
			logger.info("update loadrun from ip:" + request.getRemoteAddr());
			loadrunTaskMaster.modifyTask(target.getTarget());
		} catch (Exception e) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "修改失败!,无法修改执行容器内的"+e.getMessage());
			modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		boolean b =cspLoadRunBo.updateLoadRunHost(target);
		if(b){
			return detailLoadrunConfig(target.getAppId());
		}else{
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "更新失败!");
			modelView.addObject("backurl", "/loadrun/config.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
	}
	
	@RequestMapping(params="method=check")
	public void updateAppLoadrunConfigs(HttpServletRequest request,
			   HttpServletResponse response, String appId, String appName, String target, 
			   String loadType, String loadFuture) throws UnsupportedEncodingException, IOException {
		OutputStreamWriter writer = new OutputStreamWriter(
				response.getOutputStream(), "UTF-8");
		// appId与appName至少要提供一个
		if (appId == null && appName == null) {
			writer.write("未提供应用信息");
			writer.flush();
			return;
		}
		if (target == null || target.trim().length() == 0) {
			writer.write("目标机器为空");
			writer.flush();
			return;
		}
		
		AppInfoPo appInfo = null;
		if (appId != null) {
			appInfo = cspLoadRunBo.findAppInfoById(Integer.parseInt(appId));
		} else {
			appInfo = cspLoadRunBo.findAppInfoByName(appName);
			appId = String.valueOf(appInfo.getAppId());
		}
		
		Set<String> allIps = new HashSet<String>(); 
		Collection<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(appInfo.getOpsName());
		for (HostPo hostPo : hosts) {
			allIps.add(hostPo.getHostIp().trim());
		}

		String message = "";
		String [] ips = null;
		if (loadType.toLowerCase().indexOf("apache") > -1 || loadType.toLowerCase().indexOf("nginx") > -1) {
			ips = loadFuture.split(",");
		}
		
		if (!allIps.contains(target.trim())) {
			message = message + target + " ";
		}
		
		if (ips != null && ips.length > 0) {
			for (String ip : ips) {
				if (!allIps.contains(ip.trim())) {
					message = message + ip + " ";
				}
			}
		}
		
		if (message.length() > 0) {
			message += "与应用不匹配";
		} else {
			message = "ip与应用匹配成功";
		}
		
		writer.write(message);
		writer.flush();
	}
	
	private List<LoadrunAppInfoPo> transferApp(List<AppInfoPo> list) {
		List<LoadrunAppInfoPo> apps = new ArrayList<LoadrunAppInfoPo>();
		List<LoadrunAppInfoPo> others = new ArrayList<LoadrunAppInfoPo>();
		for (AppInfoPo po : list) {
			LoadrunAppInfoPo newPo = new LoadrunAppInfoPo(po);
			if (newPo.getProductName().equals("未知")) {
				others.add(newPo);
			} else {
				apps.add(newPo);
			}
		}
		
		apps.addAll(others);
		return apps;
	}

}
