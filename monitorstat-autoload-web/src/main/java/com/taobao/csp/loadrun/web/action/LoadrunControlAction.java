
package com.taobao.csp.loadrun.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.arkclient.csp.Permisson;
import com.taobao.arkclient.csp.UserPermissionCheck;
import com.taobao.csp.assign.classloader.ClassEntry;
import com.taobao.csp.assign.master.ClassLoaderProxy;
import com.taobao.csp.assign.master.JobFeature;
import com.taobao.csp.assign.master.SlaveProxy;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.run.BaseLoadrunTask;
import com.taobao.csp.loadrun.web.LoadRunHost;
import com.taobao.csp.loadrun.web.LoadrunTaskMaster;
import com.taobao.csp.loadrun.web.LoadrunWebContainer;
import com.taobao.csp.loadrun.web.bo.CspLoadRunBo;

/**
 * 
 * @author xiaodu
 * @version 2011-7-10 下午10:12:11
 */
@Controller
@RequestMapping("/loadrun/control.do")
public class LoadrunControlAction {
	
	private static final Logger logger = Logger.getLogger(LoadrunControlAction.class);
	
	@Resource(name="cspLoadRunBo")
	private CspLoadRunBo cspLoadRunBo;
	
	@Resource(name="loadrunTaskMaster")
	private LoadrunTaskMaster loadrunTaskMaster;
	
	@Resource(name="loadrunWebContainer")
	private LoadrunWebContainer loadrunWebContainer;
	
	@Resource(name="userPermissionCheck")
	private UserPermissionCheck userPermissionCheck;
	
	
	
	
	
	
	@RequestMapping(params="method=showClassCache")
	public ModelAndView showSlaveClassCache(String id,HttpServletResponse response){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ClassLoaderProxy> list = loadrunTaskMaster.getLoadMaster().getClassLoader();
		for(ClassLoaderProxy p:list){
			String r = p.getSlaveId();
			
			if(r.equals(id)){
				List<ClassEntry> classEntryList = p.querySlaveClassCache();
				List<String[]> rList = new ArrayList<String[]>();
				for(ClassEntry ce:classEntryList){
					String[] n = new String[3];
					n[0] = ce.getClassName();
					n[1] = ce.getFileSize()+"";
					n[2] = sdf.format(new Date(ce.getCreateTime()));
					rList.add(n);
				}
				
				ModelAndView view = new ModelAndView("/autoload/show/app_loadrun_classcache_show");
				view.addObject("classEntryList", rList);
				
				return view;
				
			}
		}
		
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("ErrorMsg", "查询不到");
		modelView.addObject("backurl", "/loadrun/control.do?method=list");
		modelView.setViewName("/message");
		return modelView;
	}
	
	
	@RequestMapping(params="method=list")
	public ModelAndView listLoadrunConfig(){
		List<LoadRunHost> list = cspLoadRunBo.findAllLoadRunHost();
		
		ModelAndView model = new ModelAndView("/autoload/control/app_loadrun_list");
		model.addObject("loadconfigList", list);
		return model;
	}
	
	@RequestMapping(params="method=stopJob")
	public ModelAndView stopJob(String slaveId,String jobId){
		
		List<SlaveProxy> list = loadrunTaskMaster.getLoadMaster().getSlave();
		for(SlaveProxy proxy:list){
			
			if(proxy.getSlaveId().equals(slaveId)){
				proxy.stopJob(jobId);
			}
		}
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("ErrorMsg", "发送停止任务请求");
		modelView.setViewName("/message");
		return modelView;
	}
	
	
	
	
	
	@RequestMapping(params="method=auto")
	public ModelAndView controlAutoRunLoad(HttpServletRequest request,
			   HttpServletResponse response, int appId){
		//LoadRunHost load = cspLoadRunBo.findLoadRunHostByAppId(appId);
		boolean hasPermission = userPermissionCheck.check(request, Permisson.AUTO_LOAD, String.valueOf(appId), appId);
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		
		try {
			loadrunTaskMaster.runLoadTask(appId);
		} catch (Exception e) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "自动执行失败!，"+e.getMessage());
			modelView.addObject("backurl", "/loadrun/control.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("ErrorMsg", "已经放入自动执行队列，请等待结果");
		modelView.addObject("backurl", "/loadrun/control.do?method=list");
		modelView.setViewName("/message");
		return modelView;
	}
	
	
	@RequestMapping(params="method=manuals")
	public synchronized ModelAndView controlManualRunLoad(HttpServletRequest request,
			   HttpServletResponse response,int appId){
		boolean hasPermission = userPermissionCheck.check(request, Permisson.MANUAL_LOAD, String.valueOf(appId), appId);
		if (!hasPermission) {
			ModelAndView noPermisson = new ModelAndView("/permission");
			return noPermisson;
		}
		
		LoadRunHost load = cspLoadRunBo.findLoadRunHostByAppId(appId);
		
		try {
			if(!loadrunTaskMaster.isRun(appId)&&!loadrunWebContainer.isRun(appId)){
				try {
					loadrunWebContainer.startup(appId,load.getTarget());
				} catch (Exception e) {
					ModelAndView modelView = new ModelAndView();
					modelView.addObject("ErrorMsg", "错误"+e.getMessage());
					modelView.addObject("backurl", "/loadrun/control.do?method=list");
					modelView.setViewName("/message");
					return modelView;
				}
			}
		} catch (Exception e) {
			ModelAndView modelView = new ModelAndView();
			modelView.addObject("ErrorMsg", "错误"+e.getMessage());
			modelView.addObject("backurl", "/loadrun/control.do?method=list");
			modelView.setViewName("/message");
			return modelView;
		}
		
		AutoLoadType loadtype = load.getLoadType();
		ModelAndView modelView = new ModelAndView("/autoload/control/app_load_control_"+loadtype.name().toLowerCase());
		modelView.addObject("loadConfig", load);
		modelView.addObject("currentTask", loadrunWebContainer.getTask(appId));
		analyseApacheFeature(modelView,load);
		
		return modelView;
				
	}
	
	
	private void analyseApacheFeature(ModelAndView modelView ,LoadRunHost load){
		String feature = load.getLoadFeature();
		List<String> ipList = new ArrayList<String>();
		String[] ips = feature.split(",");
		for(String ip:ips){
			ipList.add(ip);
		}
		
		modelView.addObject("ipList", ipList);
	}
	
	
	
	
	@RequestMapping(params="method=doControl")
	public synchronized ModelAndView doLoadControl(HttpServletRequest request,int appId){
		LoadRunHost load = cspLoadRunBo.findLoadRunHostByAppId(appId);
		
		AutoLoadType loadtype = load.getLoadType();
		ModelAndView modelView = new ModelAndView("/autoload/control/app_load_control_"+loadtype.name().toLowerCase());
		
		switch (loadtype) {
			case httpLoad:
				doHttpload(appId,request,modelView);
				break;
			case apache:
				doApacheLoad(appId,request,modelView);
				break;
			case hsf:
				doHsfLoad(appId,request,modelView);
				break;
			case apacheProxy:
				doApacheProxyLoad(appId,request,modelView);
				break;
			case nginxProxy:
				doNginxProxyLoad(appId,request,modelView);
				break;	
			default:
				break;
		}
		
		modelView.addObject("loadConfig", load);
		modelView.addObject("currentTask",loadrunWebContainer.getTask(appId));
		return modelView;
	}
	
	private void doHttpload(Integer appId,HttpServletRequest request,ModelAndView modelView){
		
		String parallel = request.getParameter("parallel");
		
		if(loadrunWebContainer.isRun(appId)){
			try {
				loadrunWebContainer.doControl(appId,parallel);
			} catch (Exception e) {
				
				modelView.addObject("loadMsg", "提供新的压测参数失败!");
			}
		}
		
	}
	
	private void doApacheLoad(Integer appId,HttpServletRequest request,ModelAndView modelView){
		String targetip = request.getParameter("targetip");
		String split_local = request.getParameter("split_local");
		String split_target = request.getParameter("split_target");
		if(loadrunWebContainer.isRun(appId)){
			try {
				loadrunWebContainer.doControl(appId,targetip,split_local+"_"+split_target);
			} catch (Exception e) {
				
				modelView.addObject("loadMsg", "提供新的压测参数失败!");
			}
		}
		
	}
	
	private void doApacheProxyLoad(Integer appId,HttpServletRequest request,ModelAndView modelView){
		String targetip = request.getParameter("targetip");
		String split_local = request.getParameter("split_local");
		String split_target = request.getParameter("split_target");
		if(loadrunWebContainer.isRun(appId)){
			try {
				loadrunWebContainer.doControl(appId,targetip,split_local+"_"+split_target);
			} catch (Exception e) {
				
				modelView.addObject("loadMsg", "提供新的压测参数失败!");
			}
		}
		
	}
	
	private void doNginxProxyLoad(Integer appId,HttpServletRequest request,ModelAndView modelView){
		String targetip = request.getParameter("targetip");
		if(loadrunWebContainer.isRun(appId)){
			try {
				loadrunWebContainer.doControl(appId,targetip);
			} catch (Exception e) {
				
				modelView.addObject("loadMsg", "提供新的压测参数失败!");
			}
		}
		
	}
	
	private void doHsfLoad(Integer appId,HttpServletRequest request,ModelAndView modelView){
		String ipCopy = request.getParameter("ipCopy");
		if(loadrunWebContainer.isRun(appId)){
			try {
				loadrunWebContainer.doControl(appId,ipCopy);
			} catch (Exception e) {
				
				modelView.addObject("loadMsg", "提供新的压测参数失败!");
			}
		}
	}
	
	@RequestMapping(params="method=stopTask")
	public synchronized ModelAndView stopLoadrunTask(Integer appId){
		loadrunWebContainer.stop(appId);
		
		
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("ErrorMsg", "关闭压测");
		modelView.addObject("backurl", "/loadrun/control.do?method=list");
		modelView.setViewName("/message");
		return modelView;
	}
	
	
	@RequestMapping(params="method=showValue")
	public void showLoadRunValue(int appId,HttpServletResponse response){
		
		Map<ResultKey, Double> map =loadrunWebContainer.getResult(appId);
		Map<String,String> rMap = new HashMap<String, String>();
		if(map != null){
			for(Map.Entry<ResultKey, Double> entry:map.entrySet())
				rMap.put(entry.getKey().getName(), entry.getValue().toString());
		}
		JSONObject json = JSONObject.fromObject(rMap);
		
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();	
		} catch (IOException e) {
		}
		return ;
	}
	
	
	@RequestMapping(params="method=autoLoadOption")
	public ModelAndView showSlaveClassCache(String option){
		if("yes".equals(option)){
			loadrunTaskMaster.setLimitLoadrun(true);
		}else{
			loadrunTaskMaster.setLimitLoadrun(false);
		}
		ModelAndView modelView = new ModelAndView();
		modelView.addObject("ErrorMsg", "当前自动压测状态:"+loadrunTaskMaster.isLimitLoadrun());
		modelView.setViewName("/message");
		return modelView;
		
	} 
	
	

}
