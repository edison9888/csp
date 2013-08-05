package com.taobao.sentinel.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.sentinel.bo.ClientInteractiveBo;
import com.taobao.sentinel.bo.FlowControlDependencyBo;
import com.taobao.sentinel.constant.FlowControlType;
import com.taobao.sentinel.po.FlowControlDependencyPo;

@Controller
@RequestMapping("/configFeedback.do")
public class ConfigFeedbackAction {
	
	@Resource(name = "clientInteractiveBo")
	private ClientInteractiveBo clientInteractiveBo;
	
	@Resource(name = "flowControlDependencyBo")
	private FlowControlDependencyBo flowControlDependencyBo;
	
	@RequestMapping(params="method=checkWhiteListInterface")
	public ModelAndView checkWhiteListInterface(String ip, String interfaceInfo, String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/check_white_list_interface");
		List<String> list = clientInteractiveBo.whiteListInterfaceConfigFromClient(appName, ip, interfaceInfo);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("ips", list);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=checkBlackListInterface")
	public ModelAndView checkBlackListInterface(String ip, String interfaceInfo, String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/check_black_list_interface");
		List<String> list = clientInteractiveBo.blackListInterfaceConfigFromClient(appName, ip, interfaceInfo);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("ips", list);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=checkWhiteListCustomer")
	public ModelAndView checkWhiteListCustomer(String ip, String customerInfo, String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/check_white_list_customer");
		List<String> list = clientInteractiveBo.whiteListCustomerConfigFromClient(appName, ip, customerInfo);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("ips", list);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("customerInfo", customerInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=checkBlackListCustomer")
	public ModelAndView checkBlackListCustomer(String ip, String customerInfo, String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/check_black_list_customer");
		List<String> list = clientInteractiveBo.blackListCustomerConfigFromClient(appName, ip, customerInfo);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("ips", list);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("customerInfo", customerInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=checkBlackListApp")
	public ModelAndView checkBlackListApp(String ip, String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/check_black_list_app");
		List<String> list = clientInteractiveBo.blackListAppConfigFromClient(appName, ip);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("ips", list);
		modelAndView.addObject("self", ip);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=checkFlowControlApp")
	public ModelAndView checkFlowControlApp(String ip, String appName, String strategy) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/check_flow_control_app");
		Map<String, String> map;
		if (strategy.equals(FlowControlType.QPS)) {
			map = clientInteractiveBo.flowControlAppConfigFromClient(appName, ip);
		} else {
			map = clientInteractiveBo.flowControlAppConfigFromClient(appName, ip);
		}
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=checkFlowControlInterface")
	public ModelAndView checkFlowControlInterface(String ip, String interfaceInfo, String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/check_flow_control_interface");
		Map<String, String> map = clientInteractiveBo.flowControlInterfaceConfigFromClient(appName, ip, interfaceInfo);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		
		return modelAndView;
	}

	@RequestMapping(params="method=checkFlowControlDependency")
	public ModelAndView checkFlowControlDependency(String ip, String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/check_flow_control_dependency");
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		List<FlowControlDependencyPo> list = flowControlDependencyBo.find(appName, false);
		
		for (FlowControlDependencyPo po : list) {
			String refApp = po.getRefAppName();
			String interfaceInfo = po.getInterfaceInfo();
			Map<String, String> iMap = clientInteractiveBo.flowControlDependencyConfigFromClient(appName, ip, interfaceInfo);
			
			Map<String, String> cMap;
			if (map.containsKey(refApp)) {
				cMap = map.get(refApp);
			} else {
				cMap = new HashMap<String, String>();
			}
			
			if (iMap != null && iMap.size() > 0) {
				cMap.put(interfaceInfo, iMap.values().toArray()[0].toString());
			}
			
			map.put(refApp, cMap);
		}
		
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		
		return modelAndView;
	}
}
