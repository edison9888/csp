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
import com.taobao.sentinel.po.FlowControlDependencyPo;

@Controller
@RequestMapping("/resultFeedback.do")
public class PerformanceFeedbackAction {
	
	@Resource(name = "clientInteractiveBo")
	private ClientInteractiveBo clientInteractiveBo;
	
	@Resource(name = "flowControlDependencyBo")
	private FlowControlDependencyBo flowControlDependencyBo;
	
	@RequestMapping(params="method=resultWhiteListInterface")
	public ModelAndView resultWhiteListInterface(String appName, String ip, String interfaceInfo) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/result_white_list_interface");
		Map<String, String> map = clientInteractiveBo.whiteListInterfaceResultFromClient(appName, ip, interfaceInfo);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=resultBlackListInterface")
	public ModelAndView resultBlackListApp(String appName, String ip, String interfaceInfo) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/result_black_list_interface");
		Map<String, String> map = clientInteractiveBo.blackListInterfaceResultFromClient(appName, ip, interfaceInfo);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=resultWhiteListCustomer")
	public ModelAndView resultWhiteListCustomer(String appName, String ip, String customerInfo) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/result_white_list_customer");
		Map<String, String> map = clientInteractiveBo.whiteListCustomerResultFromClient(appName, ip, customerInfo);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("customerInfo", customerInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=resultBlackListCustomer")
	public ModelAndView resultBlackListCustomer(String appName, String ip, String customerInfo) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/result_black_list_customer");
		Map<String, String> map = clientInteractiveBo.blackListCustomerResultFromClient(appName, ip, customerInfo);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("customerInfo", customerInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=resultBlackListApp")
	public ModelAndView resultBlackListApp(String appName, String ip) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/result_black_list_app");
		Map<String, String> map = clientInteractiveBo.blackListAppResultFromClient(appName, ip);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=resultFlowControlApp")
	public ModelAndView resultFlowControlApp(String appName, String ip) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/result_flow_control_app");
		Map<String, String> map = clientInteractiveBo.flowControlAppResultFromClient(appName, ip);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=resultFlowControlInterface")
	public ModelAndView resultFlowControlInterface(String appName, String ip, String interfaceInfo) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/result_flow_control_interface");
		Map<String, String> map = clientInteractiveBo.flowControlInterfaceResultFromClient(appName, ip, interfaceInfo);
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		
		return modelAndView;
	}

	@RequestMapping(params="method=resultFlowControlDependency")
	public ModelAndView resultFlowControlDependency(String appName, String ip) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/result_flow_control_dependency");
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		List<FlowControlDependencyPo> list = flowControlDependencyBo.find(appName, false);
		
		for (FlowControlDependencyPo po : list) {
			String refApp = po.getRefAppName();
			String interfaceInfo = po.getInterfaceInfo();
			Map<String, String> iMap = clientInteractiveBo.flowControlDependencyResultFromClient(appName, ip, interfaceInfo);
			
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
		
		modelAndView.addObject("ips", map);
		modelAndView.addObject("self", ip);
		
		return modelAndView;
	}
}
