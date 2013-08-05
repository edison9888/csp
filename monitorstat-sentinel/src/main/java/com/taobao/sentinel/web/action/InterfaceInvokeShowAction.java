package com.taobao.sentinel.web.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.sentinel.bo.InterfaceInvokeBo;
import com.taobao.sentinel.po.InterfaceInvokePo;

@Controller
@RequestMapping("/invokeShow.do")
public class InterfaceInvokeShowAction {
	
	@Resource(name = "interfaceInvokeBo")
	private InterfaceInvokeBo interfaceInvokeBo;
	
	@RequestMapping(params="method=searchInterfaceInvoke")
	public ModelAndView searchInterfaceInvoke(HttpServletRequest request,
			HttpServletResponse response, String appName, String refApp) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/invoke/search_interface_invoke");
		List<InterfaceInvokePo> list = interfaceInvokeBo.findList(appName, refApp);
		
		StringBuffer apps = new StringBuffer(StringUtils.EMPTY);
		StringBuffer refApps = new StringBuffer(StringUtils.EMPTY);
		List<String> appList = interfaceInvokeBo.findAppNames();
		List<String> refAppList = interfaceInvokeBo.findRefAppNames();
		for(String app : appList) {
			apps.append(",").append(app);
		}
		for(String ref : refAppList) {
			refApps.append(",").append(ref);
		}
			
		modelAndView.addObject("list", list);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("refApp", refApp);
		modelAndView.addObject("apps", apps.toString());
		modelAndView.addObject("refApps", refApps.toString());
		return modelAndView;
	}
	
	@RequestMapping(params="method=viewInterfaceInvoke")
	public ModelAndView viewInterfaceInvoke(HttpServletRequest request,
			HttpServletResponse response, String id) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/invoke/view_interface_invoke");
		InterfaceInvokePo po = interfaceInvokeBo.findById(id);
		modelAndView.addObject("po", po);
		return modelAndView;
	}

}
