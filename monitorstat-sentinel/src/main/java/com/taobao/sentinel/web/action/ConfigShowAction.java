package com.taobao.sentinel.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.taobao.sentinel.bo.FlowControlParamBo;
import com.taobao.sentinel.bo.WhiteListCustomerBo;
import com.taobao.sentinel.bo.WhiteListInterfaceBo;
import com.taobao.sentinel.po.BlackListAppPo;
import com.taobao.sentinel.po.BlackListCustomerPo;
import com.taobao.sentinel.po.BlackListInterfacePo;
import com.taobao.sentinel.po.ConfigVersionPo;
import com.taobao.sentinel.po.DataUrlPo;
import com.taobao.sentinel.po.FlowControlAppPo;
import com.taobao.sentinel.po.FlowControlDependencyPo;
import com.taobao.sentinel.po.FlowControlInterfacePo;
import com.taobao.sentinel.po.FlowControlParamPo;
import com.taobao.sentinel.po.QpsPeriodPo;
import com.taobao.sentinel.po.RefIpPo;
import com.taobao.sentinel.po.UserPermissionPo;
import com.taobao.sentinel.po.WhiteListCustomerPo;
import com.taobao.sentinel.po.WhiteListInterfacePo;
import com.taobao.sentinel.util.LocalUtil;

@Controller
@RequestMapping("/show.do")
public class ConfigShowAction {
	
	public static Logger logger = Logger.getLogger(ConfigShowAction.class);
	
	@Resource(name = "whiteListInterfaceBo")
	private WhiteListInterfaceBo whiteListInterfaceBo;
	
	@Resource(name = "blackListInterfaceBo")
	private BlackListInterfaceBo blackListInterfaceBo;
	
	@Resource(name = "blackListAppBo")
	private BlackListAppBo blackListAppBo;
	
	@Resource(name = "whiteListCustomerBo")
	private WhiteListCustomerBo whiteListCustomerBo;
	
	@Resource(name = "blackListCustomerBo")
	private BlackListCustomerBo blackListCustomerBo;
	
	@Resource(name = "flowControlInterfaceBo")
	private FlowControlInterfaceBo flowControlInterfaceBo;
	
	@Resource(name = "flowControlParamBo")
	private FlowControlParamBo flowControlParamBo;
	
	@Resource(name = "flowControlAppBo")
	private FlowControlAppBo flowControlAppBo;
	
	@Resource(name = "flowControlDependencyBo")
	private FlowControlDependencyBo flowControlDependencyBo;
	
	@Resource(name = "commonBo")
	private CommonBo commonBo;
	
	/***************************interface white list*************************************************/
	@RequestMapping(params="method=showWhiteListInterface")
	public ModelAndView showWhiteListInterface() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/white_list_interface");
		List<WhiteListInterfacePo> whiteList = whiteListInterfaceBo.findAll();
		modelAndView.addObject("whiteList", whiteList);
		return modelAndView;
	}
	
	@RequestMapping(params="method=showWhiteListInterfaceIps")
	public ModelAndView showWhiteListInterfaceIps(HttpServletRequest request,
			HttpServletResponse response, String refId, String appName, String interfaceInfo, String refApp) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/white_list_interface_ip");
		List<RefIpPo> whiteListIps = whiteListInterfaceBo.findIps(LocalUtil.generateRefId(appName, refApp));
		modelAndView.addObject("whiteListIps", whiteListIps);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		modelAndView.addObject("refApp", refApp);
		return modelAndView;
	}
	
	/***************************interface black list*************************************************/
	@RequestMapping(params="method=showBlackListInterface")
	public ModelAndView showBlackListInterface(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/black_list_interface");
		List<BlackListInterfacePo> blackList = blackListInterfaceBo.findAll();
		modelAndView.addObject("blackList", blackList);
		return modelAndView;
	}
	
	@RequestMapping(params="method=showBlackListInterfaceIps")
	public ModelAndView showBlackListInterfaceIps(HttpServletRequest request,
			HttpServletResponse response, String refId, String appName, String interfaceInfo, String refApp) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/black_list_interface_ip");
		List<RefIpPo> blackListIps = blackListInterfaceBo.findIps(LocalUtil.generateRefId(appName, refApp));
		modelAndView.addObject("blackListIps", blackListIps);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		modelAndView.addObject("refApp", refApp);
		return modelAndView;
	}
	
	/***************************application black list*************************************************/
	@RequestMapping(params="method=showBlackListApp")
	public ModelAndView showBlackListApp(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/black_list_app");
		List<BlackListAppPo> blackList = blackListAppBo.findAll();
		modelAndView.addObject("blackList", blackList);
		return modelAndView;
	}
	
	@RequestMapping(params="method=showBlackListAppIps")
	public ModelAndView showBlackListAppIps(HttpServletRequest request,
			HttpServletResponse response, String refId, String appName, String appInfo, String refApp) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/black_list_app_ip");
		List<RefIpPo> blackListIps = blackListAppBo.findIps(LocalUtil.generateRefId(appName, refApp));
		modelAndView.addObject("blackListIps", blackListIps);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("appInfo", appInfo);
		modelAndView.addObject("refApp", refApp);
		return modelAndView;
	}
	
	/***************************customization white list*************************************************/
	@RequestMapping(params="method=showWhiteListCustomer")
	public ModelAndView showWhiteListCustomer() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/white_list_customer");
		List<WhiteListCustomerPo> whiteList = whiteListCustomerBo.findAll();
		modelAndView.addObject("whiteList", whiteList);
		return modelAndView;
	}
	
	@RequestMapping(params="method=showWhiteListCustomerIps")
	public ModelAndView showWhiteListCustomerIps(HttpServletRequest request,
			HttpServletResponse response, String refId, String appName, String customerInfo, String refApp) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/white_list_customer_ip");
		List<RefIpPo> whiteListIps = whiteListCustomerBo.findIps(LocalUtil.generateRefId(appName, refApp));
		modelAndView.addObject("whiteListIps", whiteListIps);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("customerInfo", customerInfo);
		modelAndView.addObject("refApp", refApp);
		return modelAndView;
	}
	
	/***************************customization black list*************************************************/
	@RequestMapping(params="method=showBlackListCustomer")
	public ModelAndView showBlackListCustomer(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/black_list_customer");
		List<BlackListCustomerPo> blackList = blackListCustomerBo.findAll();
		modelAndView.addObject("blackList", blackList);
		return modelAndView;
	}
	
	@RequestMapping(params="method=showBlackListCustomerIps")
	public ModelAndView showBlackListCustomerIps(HttpServletRequest request,
			HttpServletResponse response, String refId, String appName, String customerInfo, String refApp) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/black_list_customer_ip");
		List<RefIpPo> blackListIps = blackListCustomerBo.findIps(LocalUtil.generateRefId(appName, refApp));
		modelAndView.addObject("blackListIps", blackListIps);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("customerInfo", customerInfo);
		modelAndView.addObject("refApp", refApp);
		return modelAndView;
	}
	
	/***************************interface flow control *************************************************/
	@RequestMapping(params="method=showFlowControlInterface")
	public ModelAndView showFlowControlInterface(HttpServletRequest request,
			HttpServletResponse response, String strategy, String index) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/flow_control_interface");
		List<FlowControlInterfacePo> flowList = flowControlInterfaceBo.findAll(strategy);
		
		List<FlowControlInterfacePo> showFlowList = new ArrayList<FlowControlInterfacePo>();;
		if (StringUtils.isNotEmpty(index)) {
			for (FlowControlInterfacePo po : flowList) {
				if (po.getInterfaceInfo().contains(index)) {
					showFlowList.add(po);
				}
			}
		} else {
			showFlowList = flowList;
		}
		modelAndView.addObject("flowList", showFlowList);
		modelAndView.addObject("strategy", strategy);
		return modelAndView;
	}
	
	@RequestMapping(params="method=showFlowControlInterfaceIps")
	public ModelAndView showFlowControlInterfaceIps(HttpServletRequest request,
			HttpServletResponse response, String refId, String appName, String interfaceInfo, String refApp) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/flow_control_interface_ip");
		List<RefIpPo> flowControlIps = flowControlInterfaceBo.findIps(LocalUtil.generateRefId(appName, refApp));
		modelAndView.addObject("flowControlIps", flowControlIps);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		modelAndView.addObject("refApp", refApp);
		return modelAndView;
	}
	
	/***************************application flow control *************************************************/
	@RequestMapping(params="method=showFlowControlApp")
	public ModelAndView showFlowControlApp(HttpServletRequest request,
			HttpServletResponse response, String strategy) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/flow_control_app");
		List<FlowControlAppPo> flowList = flowControlAppBo.findAll(strategy);
		modelAndView.addObject("flowList", flowList);
		modelAndView.addObject("strategy", strategy);
		return modelAndView;
	}
	
	@RequestMapping(params="method=showFlowControlAppIps")
	public ModelAndView showFlowControlAppIps(HttpServletRequest request,
			HttpServletResponse response, String refId, String appName, String interfaceInfo, String refApp) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/flow_control_app_ip");
		List<RefIpPo> flowControlIps = flowControlAppBo.findIps(LocalUtil.generateRefId(appName, refApp));
		modelAndView.addObject("flowControlIps", flowControlIps);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("interfaceInfo", interfaceInfo);
		modelAndView.addObject("refApp", refApp);
		return modelAndView;
	}
	
	/***************************dependency flow control*************************************************/
	@RequestMapping(params="method=showFlowControlDependency")
	public ModelAndView showFlowControlDependency(HttpServletRequest request,
			HttpServletResponse response, String index) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/flow_control_dependency");
		List<FlowControlDependencyPo> flowList = flowControlDependencyBo.findAll();
		
		List<FlowControlDependencyPo> showFlowList = new ArrayList<FlowControlDependencyPo>();;
		if (StringUtils.isNotEmpty(index)) {
			for (FlowControlDependencyPo po : flowList) {
				if (po.getInterfaceInfo().contains(index)) {
					showFlowList.add(po);
				}
			}
		} else {
			showFlowList = flowList;
		}
		modelAndView.addObject("flowList", showFlowList);
		return modelAndView;
	}
	
	/***************************param flow control *************************************************/
	@RequestMapping(params="method=showFlowControlParam")
	public ModelAndView showFlowControlParam(HttpServletRequest request,
			HttpServletResponse response, String index) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/flow_control_param");
		List<FlowControlParamPo> flowList = flowControlParamBo.findAll();
		
		List<FlowControlParamPo> showFlowList = new ArrayList<FlowControlParamPo>();;
		if (StringUtils.isNotEmpty(index)) {
			for (FlowControlParamPo po : flowList) {
				if (po.getParamInfo().contains(index)) {
					showFlowList.add(po);
				}
			}
		} else {
			showFlowList = flowList;
		}
		modelAndView.addObject("flowList", showFlowList);
		return modelAndView;
	}
	
	@RequestMapping(params="method=showFlowControlParamIps")
	public ModelAndView showFlowControlParamIps(HttpServletRequest request,
			HttpServletResponse response, String refId, String appName, String paramInfo, String refApp) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/flow_control_param_ip");
		List<RefIpPo> flowControlIps = flowControlParamBo.findIps(LocalUtil.generateRefId(appName, refApp));
		modelAndView.addObject("flowControlIps", flowControlIps);
		modelAndView.addObject("appName", appName);
		modelAndView.addObject("paramInfo", paramInfo);
		modelAndView.addObject("refApp", refApp);
		return modelAndView;
	}
	
	/***************************common operations*************************************************/
	
	@RequestMapping(params="method=pushRecord")
	public ModelAndView pushRecord(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/push/push_record");
		
		List<ConfigVersionPo> list = commonBo.findRecentConfigVersion();
		modelAndView.addObject("list", list);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=permissionList")
	public ModelAndView permissionList(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/system/user_info");
		
		List<UserPermissionPo> list = commonBo.findAllUserPsermission();
		modelAndView.addObject("list", list);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=dataUrlList")
	public ModelAndView dataUrlList(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/system/data_url");
		
		List<DataUrlPo> list = commonBo.findAllDataUrl();
		modelAndView.addObject("list", list);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=showQpsPeriod")
	public ModelAndView showQpsPeriod(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/qps_period");
		
		List<QpsPeriodPo> list = commonBo.findAllQpsPeriod();
		modelAndView.addObject("list", list);
		
		return modelAndView;
	}

}
