package com.taobao.sentinel.web.action;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.sentinel.bo.InterfaceInvokeBo;
import com.taobao.sentinel.po.InterfaceInvokePo;

@Controller
@RequestMapping("/invokeManage.do")
public class InterfaceInvokeManageAction {

	@Resource(name = "interfaceInvokeBo")
	private InterfaceInvokeBo interfaceInvokeBo;
	
	@RequestMapping(params="method=goToAddIntefaceInvoke")
	public ModelAndView goToAddIntefaceInvoke() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/invoke/add_interface_invoke");
		return modelAndView;
	}
	
	@RequestMapping(params="method=gotoUpdateInterfaceInvoke")
	public ModelAndView gotoUpdateInterfaceInvoke(String id) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/invoke/update_interface_invoke");
		InterfaceInvokePo po = interfaceInvokeBo.findById(id);
		modelAndView.addObject("po", po);
		return modelAndView;
	}

	@RequestMapping(params="method=addInterfaceInvoke")
	public void addInterfaceInvoke(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String responseMessage = "";
		String appName = request.getParameter("appName").trim();
		String interfaceInfo = request.getParameter("interfaceInfo").trim();
		String refApp = request.getParameter("refApp");
		boolean exist = interfaceInvokeBo.checkExist(appName, refApp, interfaceInfo);
		if (exist) {
			responseMessage = "fail";
		} else {
			interfaceInvokeBo.add(request);
			responseMessage = "success";
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=updateInterfaceInvoke")
	public void updateInterfaceInvoke(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String responseMessage = "";
		String id = request.getParameter("id").trim();
		String appName = request.getParameter("appName").trim();
		String interfaceInfo = request.getParameter("interfaceInfo").trim();
		String refApp = request.getParameter("refApp");
		
		boolean exist = interfaceInvokeBo.checkExistExceptSelf(appName, refApp, interfaceInfo, id);
		if (exist) {
			responseMessage = "fail";
		} else {
			interfaceInvokeBo.update(request);
			responseMessage = "success";
		}
	
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();	
	}
	
	@RequestMapping(params="method=deleteInterfaceInvoke")
	public void deleteInterfaceInvoke(HttpServletRequest request,
			HttpServletResponse response, String id) throws IOException {
		boolean succuss = interfaceInvokeBo.detele(id);
		Writer writer = response.getWriter();
		if (succuss) {
			writer.write(String.valueOf("success"));
		} else {
			writer.write(String.valueOf("fail"));
		}
		
		writer.flush();
	}
}
