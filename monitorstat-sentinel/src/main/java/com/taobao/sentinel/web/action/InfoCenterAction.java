package com.taobao.sentinel.web.action;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.sentinel.bo.BlackListAppBo;
import com.taobao.sentinel.bo.BlackListCustomerBo;
import com.taobao.sentinel.bo.BlackListInterfaceBo;
import com.taobao.sentinel.bo.CommonBo;
import com.taobao.sentinel.bo.FlowControlAppBo;
import com.taobao.sentinel.bo.FlowControlDependencyBo;
import com.taobao.sentinel.bo.FlowControlInterfaceBo;
import com.taobao.sentinel.bo.WhiteListCustomerBo;
import com.taobao.sentinel.bo.WhiteListInterfaceBo;
import com.taobao.sentinel.client.SentinelConfig;
import com.taobao.sentinel.constant.FlowControlType;
import com.taobao.sentinel.po.BlackListAppPo;
import com.taobao.sentinel.po.BlackListCustomerPo;
import com.taobao.sentinel.po.BlackListInterfacePo;
import com.taobao.sentinel.po.FlowControlAppPo;
import com.taobao.sentinel.po.FlowControlDependencyPo;
import com.taobao.sentinel.po.FlowControlInterfacePo;
import com.taobao.sentinel.po.WhiteListCustomerPo;
import com.taobao.sentinel.po.WhiteListInterfacePo;
import com.taobao.sentinel.pull.DisplayObject;

@Controller
@RequestMapping("/info.do")
public class InfoCenterAction {
	
	@Resource(name = "whiteListInterfaceBo")
	private WhiteListInterfaceBo whiteListInterfaceBo;
	
	@Resource(name = "blackListInterfaceBo")
	private BlackListInterfaceBo blackListInterfaceBo;
	
	@Resource(name = "blackListAppBo")
	private BlackListAppBo blackListAppBo;
	
	@Resource(name = "commonBo")
	private CommonBo commonBo;
	
	@Resource(name = "whiteListCustomerBo")
	private WhiteListCustomerBo whiteListCustomerBo;
	
	@Resource(name = "blackListCustomerBo")
	private BlackListCustomerBo blackListCustomerBo;
	
	@Resource(name = "flowControlInterfaceBo")
	private FlowControlInterfaceBo flowControlInterfaceBo;
	
	@Resource(name = "flowControlAppBo")
	private FlowControlAppBo flowControlAppBo;
	
	@Resource(name = "flowControlDependencyBo")
	private FlowControlDependencyBo flowControlDependencyBo;
	
	@RequestMapping(params="method=ipsWhiteListInterface")
	public ModelAndView ipsWhiteListInterface(String appName, String interfaceInfo) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/ips_white_list_interface");
		List<WhiteListInterfacePo> pos = whiteListInterfaceBo.findAll();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (WhiteListInterfacePo po : pos) {
			String app = po.getAppName();
			if (map.containsKey(app)) {
				map.get(app).add(po.getInterfaceInfo());
			} else {
				List<String> list = new ArrayList<String>();
				list.add(po.getInterfaceInfo());
				map.put(app, list);
			}
		}
		
		StringBuffer appsBuffer = new StringBuffer(StringUtils.EMPTY);
		StringBuffer interfacesBuffer = new StringBuffer(StringUtils.EMPTY);
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			appsBuffer.append(entry.getKey()).append(",");
			appName = appName == null ? entry.getKey() : appName;
			interfaceInfo = interfaceInfo == null ? entry.getValue().get(0) : interfaceInfo;
		}
		
		// the map may be empty
		if (map.containsKey(appName)) {
			for (String interfaceName : map.get(appName)) {
				if (interfacesBuffer.indexOf(interfaceName) == -1) {
					interfacesBuffer.append(interfaceName).append("%");
				}
			}
		}
		
		String apps = appsBuffer.toString();
		String interfaces = interfacesBuffer.toString();
		if (apps.endsWith(",")) {
			apps = apps.substring(0, apps.length() - 1);
		}
		if (interfaces.endsWith("%")) {
			interfaces = interfaces.substring(0, interfaces.length() - 1);
		}
		
		List<DisplayObject> list = commonBo.appIpsFromOpsFree(appName);
		
		// whether add mock ip
		if (SentinelConfig.ADD_MOCK_IP) {
			DisplayObject mockObject = new DisplayObject();
			mockObject.setIp(SentinelConfig.MOCK_IP);
			list.add(mockObject);
		}
		
		modelAndView.addObject("ips", list);
		modelAndView.addObject("apps", apps);
		modelAndView.addObject("interfaces", interfaces);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=ipsBlackListInterface")
	public ModelAndView ipsBlackListInterface(String appName, String interfaceInfo) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/ips_black_list_interface");
		List<BlackListInterfacePo> pos = blackListInterfaceBo.findAll();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (BlackListInterfacePo po : pos) {
			String app = po.getAppName();
			if (map.containsKey(app)) {
				map.get(app).add(po.getInterfaceInfo());
			} else {
				List<String> list = new ArrayList<String>();
				list.add(po.getInterfaceInfo());
				map.put(app, list);
			}
		}
		
		StringBuffer appsBuffer = new StringBuffer(StringUtils.EMPTY);
		StringBuffer interfacesBuffer = new StringBuffer(StringUtils.EMPTY);
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			appsBuffer.append(entry.getKey()).append(",");
			appName = appName == null ? entry.getKey() : appName;
			interfaceInfo = interfaceInfo == null ? entry.getValue().get(0) : interfaceInfo;
		}
		
		// the map may be empty
		if (map.containsKey(appName)) {
			for (String interfaceName : map.get(appName)) {
				if (interfacesBuffer.indexOf(interfaceName) == -1) {
					interfacesBuffer.append(interfaceName).append("%");
				}
			}
		}
		
		String apps = appsBuffer.toString();
		String interfaces = interfacesBuffer.toString();
		if (apps.endsWith(",")) {
			apps = apps.substring(0, apps.length() - 1);
		}
		if (interfaces.endsWith("%")) {
			interfaces = interfaces.substring(0, interfaces.length() - 1);
		}
		
		List<DisplayObject> list = commonBo.appIpsFromOpsFree(appName);
		
		// whether add mock ip
		if (SentinelConfig.ADD_MOCK_IP) {
			DisplayObject mockObject = new DisplayObject();
			mockObject.setIp(SentinelConfig.MOCK_IP);
			list.add(mockObject);
		}
		
		modelAndView.addObject("ips", list);
		modelAndView.addObject("apps", apps);
		modelAndView.addObject("interfaces", interfaces);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=ipsWhiteListCustomer")
	public ModelAndView ipsWhiteListCustomer(String appName, String customerInfo) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/ips_white_list_customer");
		List<WhiteListCustomerPo> pos = whiteListCustomerBo.findAll();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (WhiteListCustomerPo po : pos) {
			String app = po.getAppName();
			if (map.containsKey(app)) {
				map.get(app).add(po.getCustomerInfo());
			} else {
				List<String> list = new ArrayList<String>();
				list.add(po.getCustomerInfo());
				map.put(app, list);
			}
		}
		
		StringBuffer appsBuffer = new StringBuffer(StringUtils.EMPTY);
		StringBuffer customersBuffer = new StringBuffer(StringUtils.EMPTY);
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			appsBuffer.append(entry.getKey()).append(",");
			appName = appName == null ? entry.getKey() : appName;
			customerInfo = customerInfo == null ? entry.getValue().get(0) : customerInfo;
		}
		
		// the map may be empty
		if (map.containsKey(appName)) {
			for (String customerName : map.get(appName)) {
				if (customersBuffer.indexOf(customerName) == -1) {
					customersBuffer.append(customerName).append("%");
				}
			}
		}

		String apps = appsBuffer.toString();
		String customers = customersBuffer.toString();
		if (apps.endsWith(",")) {
			apps = apps.substring(0, apps.length() - 1);
		}
		if (customers.endsWith("%")) {
			customers = customers.substring(0, customers.length() - 1);
		}
		
		List<DisplayObject> list = commonBo.appIpsFromOpsFree(appName);
		
		// whether add mock ip
		if (SentinelConfig.ADD_MOCK_IP) {
			DisplayObject mockObject = new DisplayObject();
			mockObject.setIp(SentinelConfig.MOCK_IP);
			list.add(mockObject);
		}
		
		modelAndView.addObject("ips", list);
		modelAndView.addObject("apps", apps);
		modelAndView.addObject("customers", customers);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("customerInfo", customerInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=ipsBlackListCustomer")
	public ModelAndView ipsBlackListCustomer(String appName, String customerInfo) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/ips_black_list_customer");
		List<BlackListCustomerPo> pos = blackListCustomerBo.findAll();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (BlackListCustomerPo po : pos) {
			String app = po.getAppName();
			if (map.containsKey(app)) {
				map.get(app).add(po.getCustomerInfo());
			} else {
				List<String> list = new ArrayList<String>();
				list.add(po.getCustomerInfo());
				map.put(app, list);
			}
		}
		
		StringBuffer appsBuffer = new StringBuffer(StringUtils.EMPTY);
		StringBuffer customersBuffer = new StringBuffer(StringUtils.EMPTY);
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			appsBuffer.append(entry.getKey()).append(",");
			appName = appName == null ? entry.getKey() : appName;
			customerInfo = customerInfo == null ? entry.getValue().get(0) : customerInfo;
		}
		
		// the map may be empty
		if (map.containsKey(appName)) {
			for (String customerName : map.get(appName)) {
				if (customersBuffer.indexOf(customerName) == -1) {
					customersBuffer.append(customerName).append("%");
				}
			}
		}
		
		String apps = appsBuffer.toString();
		String customers = customersBuffer.toString();
		if (apps.endsWith(",")) {
			apps = apps.substring(0, apps.length() - 1);
		}
		if (customers.endsWith("%")) {
			customers = customers.substring(0, customers.length() - 1);
		}
		
		List<DisplayObject> list = commonBo.appIpsFromOpsFree(appName);
		
		// whether add mock ip
		if (SentinelConfig.ADD_MOCK_IP) {
			DisplayObject mockObject = new DisplayObject();
			mockObject.setIp(SentinelConfig.MOCK_IP);
			list.add(mockObject);
		}
		
		modelAndView.addObject("ips", list);
		modelAndView.addObject("apps", apps);
		modelAndView.addObject("customers", customers);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("customerInfo", customerInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=ipsBlackListApp")
	public ModelAndView ipsBlackListApp(String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/ips_black_list_app");
		List<BlackListAppPo> pos = blackListAppBo.findAll();
		StringBuffer appsBuffer = new StringBuffer(StringUtils.EMPTY);
		for (BlackListAppPo po : pos) {
			if (appsBuffer.indexOf(po.getAppName()) == -1) {
				appsBuffer.append(po.getAppName()).append(",");
				appName = appName == null ? po.getAppName() : appName;
			}
		}
		
		String apps = appsBuffer.toString();
		if (apps.endsWith(",")) {
			apps = apps.substring(0, apps.length() - 1);
		}
		List<DisplayObject> list = commonBo.appIpsFromOpsFree(appName);
		
		// whether add mock ip
		if (SentinelConfig.ADD_MOCK_IP) {
			DisplayObject mockObject = new DisplayObject();
			mockObject.setIp(SentinelConfig.MOCK_IP);
			list.add(mockObject);
		}
		
		modelAndView.addObject("ips", list);
		modelAndView.addObject("apps", apps);
		modelAndView.addObject("appName", appName);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=ipsFlowControlDependency")
	public ModelAndView ipsFlowControlDependency(String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/ips_flow_control_dependency");
		List<FlowControlDependencyPo> pos = flowControlDependencyBo.findAll();
		StringBuffer appsBuffer = new StringBuffer(StringUtils.EMPTY);
		for (FlowControlDependencyPo po : pos) {
			if (appsBuffer.indexOf(po.getAppName()) == -1) {
				appsBuffer.append(po.getAppName()).append(",");
				appName = appName == null ? po.getAppName() : appName;
			}
		}
		
		String apps = appsBuffer.toString();
		if (apps.endsWith(",")) {
			apps = apps.substring(0, apps.length() - 1);
		}
		List<DisplayObject> list = commonBo.appIpsFromOpsFree(appName);
		
		// whether add mock ip
		if (SentinelConfig.ADD_MOCK_IP) {
			DisplayObject mockObject = new DisplayObject();
			mockObject.setIp(SentinelConfig.MOCK_IP);
			list.add(mockObject);
		}
		
		modelAndView.addObject("ips", list);
		modelAndView.addObject("apps", apps);
		modelAndView.addObject("appName", appName);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=ipsFlowControlInterface")
	public ModelAndView ipsFlowControlInterface(String appName, String interfaceInfo, String strategy) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/ips_flow_control_interface");
		List<FlowControlInterfacePo> pos = flowControlInterfaceBo.findAll(strategy);
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (FlowControlInterfacePo po : pos) {
			String app = po.getAppName();
			if (map.containsKey(app)) {
				map.get(app).add(po.getInterfaceInfo());
			} else {
				List<String> list = new ArrayList<String>();
				list.add(po.getInterfaceInfo());
				map.put(app, list);
			}
		}
		
		StringBuffer appsBuffer = new StringBuffer(StringUtils.EMPTY);
		StringBuffer interfacesBuffer = new StringBuffer(StringUtils.EMPTY);
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			appsBuffer.append(entry.getKey()).append(",");
			appName = appName == null ? entry.getKey() : appName;
			interfaceInfo = interfaceInfo == null ? entry.getValue().get(0) : interfaceInfo;
		}
		
		if (map.containsKey(appName)) {
			for (String interfaceName : map.get(appName)) {
				if (interfacesBuffer.indexOf(interfaceName) == -1) {
					interfacesBuffer.append(interfaceName).append(",");
				}
			}
		}
		
		String apps = appsBuffer.toString();
		String interfaces = interfacesBuffer.toString();
		if (apps.endsWith(",")) {
			apps = apps.substring(0, apps.length() - 1);
		}
		if (interfaces.endsWith(",")) {
			interfaces = interfaces.substring(0, interfaces.length() - 1);
		}
		
		List<DisplayObject> list = commonBo.appIpsFromOpsFree(appName);
		
		// whether add mock ip
		if (SentinelConfig.ADD_MOCK_IP) {
			DisplayObject mockObject = new DisplayObject();
			mockObject.setIp(SentinelConfig.MOCK_IP);
			list.add(mockObject);
		}
		
		modelAndView.addObject("ips", list);
		modelAndView.addObject("apps", apps);
		modelAndView.addObject("interfaces", interfaces);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("strategy", strategy);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=ipsFlowControlApp")
	public ModelAndView ipsFlowControlApp(String appName, String strategy) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/client/ips_flow_control_app");
		List<FlowControlAppPo> pos = flowControlAppBo.findAll(strategy);
		StringBuffer appsBuffer = new StringBuffer(StringUtils.EMPTY);
		for (FlowControlAppPo po : pos) {
			if (appsBuffer.indexOf(po.getAppName()) == -1) {
				appsBuffer.append(po.getAppName()).append(",");
				appName = appName == null ? po.getAppName() : appName;
			}
		}
		
		String apps = appsBuffer.toString();
		if (apps.endsWith(",")) {
			apps = apps.substring(0, apps.length() - 1);
		}
		List<DisplayObject> list = commonBo.appIpsFromOpsFree(appName);
		
		// whether add mock ip
		if (SentinelConfig.ADD_MOCK_IP) {
			DisplayObject mockObject = new DisplayObject();
			mockObject.setIp(SentinelConfig.MOCK_IP);
			list.add(mockObject);
		}
		
		modelAndView.addObject("ips", list);
		modelAndView.addObject("apps", apps);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("strategy", strategy);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=whiteListInterface")
	public void whiteListInterface(HttpServletResponse response, String appName) throws Exception {
		 List<WhiteListInterfacePo> pos = whiteListInterfaceBo.find(appName, true);
		 StringBuffer interfaceS = new StringBuffer(StringUtils.EMPTY);
		 
		 for (WhiteListInterfacePo po : pos) {
			 if (interfaceS.indexOf(po.getInterfaceInfo()) == -1) {
				 interfaceS.append(po.getInterfaceInfo()).append("%");
			 }
		 }
		 
		 String interfaceNames = interfaceS.toString();
		 if (interfaceNames.endsWith("%")) {
			 interfaceNames = interfaceNames.substring(0, interfaceNames.length() - 1);
		 }
		 
		 Writer writer = null;
		 try {
			 writer = response.getWriter();
			 writer.write(interfaceNames);
			 writer.flush();
		 } finally {
			 if (writer != null) {
				 writer.close();
			 }
		 }
	}
	
	@RequestMapping(params="method=blackListInterface")
	public void blackListInterface(HttpServletResponse response, String appName) throws Exception {
		 List<BlackListInterfacePo> pos = blackListInterfaceBo.find(appName, true);
		 StringBuffer interfaceS = new StringBuffer(StringUtils.EMPTY);
		 
		 for (BlackListInterfacePo po : pos) {
			 if (interfaceS.indexOf(po.getInterfaceInfo()) == -1) {
				 interfaceS.append(po.getInterfaceInfo()).append("%");
			 }
		 }
		 
		 String interfaceNames = interfaceS.toString();
		 if (interfaceNames.endsWith("%")) {
			 interfaceNames = interfaceNames.substring(0, interfaceNames.length() - 1);
		 }
		 
		 Writer writer = null;
		 try {
			 writer = response.getWriter();
			 writer.write(interfaceNames);
			 writer.flush();
		 } finally {
			 if (writer != null) {
				 writer.close();
			 }
		 }
	}
	
	@RequestMapping(params="method=whiteListCustomer")
	public void whiteListCustomer(HttpServletResponse response, String appName) throws Exception {
		 List<WhiteListCustomerPo> pos = whiteListCustomerBo.find(appName, true);
		 StringBuffer customerS = new StringBuffer(StringUtils.EMPTY);
		 
		 for (WhiteListCustomerPo po : pos) {
			 if (customerS.indexOf(po.getCustomerInfo()) == -1) {
				 customerS.append(po.getCustomerInfo()).append("%"); 
			 }
		 }
		 
		 String customerNames = customerS.toString();
		 if (customerNames.endsWith("%")) {
			 customerNames = customerNames.substring(0, customerNames.length() - 1);
		 }
		 
		 Writer writer = null;
		 try {
			 writer = response.getWriter();
			 writer.write(customerNames);
			 writer.flush();
		 } finally {
			 if (writer != null) {
				 writer.close();
			 }
		 }
	}
	
	@RequestMapping(params="method=blackListCustomer")
	public void blackListCustomer(HttpServletResponse response, String appName) throws Exception {
		 List<BlackListCustomerPo> pos = blackListCustomerBo.find(appName, true);
		 StringBuffer customerS = new StringBuffer(StringUtils.EMPTY);
		 
		 for (BlackListCustomerPo po : pos) {
			 if (customerS.indexOf(po.getCustomerInfo()) == -1) {
				 customerS.append(po.getCustomerInfo()).append("%");
			 }
		 }
		 
		 String customerNames = customerS.toString();
		 if (customerNames.endsWith("%")) {
			 customerNames = customerNames.substring(0, customerNames.length() - 1);
		 }
		 
		 Writer writer = null;
		 try {
			 writer = response.getWriter();
			 writer.write(customerNames);
			 writer.flush();
		 } finally {
			 if (writer != null) {
				 writer.close();
			 }
		 }
	}
	
	@RequestMapping(params="method=flowControlInterface")
	public void flowControlInterface(HttpServletResponse response, String appName, String strategy) throws Exception {
		 List<FlowControlInterfacePo> pos = flowControlInterfaceBo.find(appName, true, strategy);
		 StringBuffer interfaceS = new StringBuffer(StringUtils.EMPTY);
		 
		 for (FlowControlInterfacePo po : pos) {
			 if (interfaceS.indexOf(po.getInterfaceInfo()) == -1) {
				 interfaceS.append(po.getInterfaceInfo()).append("%");
			 }
		 }
		 
		 String interfaceNames = interfaceS.toString();
		 if (interfaceNames.endsWith("%")) {
			 interfaceNames = interfaceNames.substring(0, interfaceNames.length() - 1);
		 }
		 
		 Writer writer = null;
		 try {
			 writer = response.getWriter();
			 writer.write(interfaceNames);
			 writer.flush();
		 } finally {
			 if (writer != null) {
				 writer.close();
			 }
		 }
	}
}
