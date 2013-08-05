package com.taobao.sentinel.web.action;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import com.taobao.sentinel.bo.FlowControlParamBo;
import com.taobao.sentinel.bo.SyncBo;
import com.taobao.sentinel.bo.WhiteListCustomerBo;
import com.taobao.sentinel.bo.WhiteListInterfaceBo;
import com.taobao.sentinel.constant.Constants;
import com.taobao.sentinel.mock.InitTestData;
import com.taobao.sentinel.permission.UserPermissionCheck;
import com.taobao.sentinel.po.UserPermissionPo;
import com.taobao.sentinel.util.LocalUtil;

@Controller
@RequestMapping("/manage.do")
public class ConfigManageAction {
	
	@Resource(name = "userPermissionCheck")
	private UserPermissionCheck userPermissionCheck;
	
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
	
	@Resource(name = "flowControlParamBo")
	private FlowControlParamBo flowControlParamBo;
	
	@Resource(name = "flowControlAppBo")
	private FlowControlAppBo flowControlAppBo;
	
	@Resource(name = "flowControlDependencyBo")
	private FlowControlDependencyBo flowControlDependencyBo;
	
	@Resource(name = "syncBo")
	private SyncBo syncBo;

	/***************************interface white list*************************************************/
	@RequestMapping(params="method=goToAddWhiteListInterface")
	public ModelAndView goToAddWhiteListInterface() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/add_white_list_interface");
		return modelAndView;
	}
	
	@RequestMapping(params="method=addWhiteListInterface")
	public void addWhiteListInterface(HttpServletRequest request,
			HttpServletResponse response, String appName, String interfaceInfo, String whiteApp) throws IOException {
		String responseMessage = "";
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
	
		boolean exist = whiteListInterfaceBo.checkExist(appName, whiteApp, interfaceInfo);
		if (!hasPermission || exist) {
			responseMessage = "fail";
		} else {
			int count = whiteListInterfaceBo.add(request);
			responseMessage = String.valueOf(count);
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=deleteWhiteListInterfaceConfig")
	public void deleteWhiteListInterfaceConfig(HttpServletRequest request,
			HttpServletResponse response, String id, String appName) throws IOException {
		
		boolean success = false;
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);

		if (hasPermission) {
			success = whiteListInterfaceBo.deleteConfig(id);
		}

		Writer writer = response.getWriter();
		writer.write(String.valueOf(success));
		writer.flush();
	}
	
	/***************************interface black list*************************************************/
	@RequestMapping(params="method=goToAddBlackListInterface")
	public ModelAndView goToAddBlackListInterface() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/add_black_list_interface");
		return modelAndView;
	}
	
	@RequestMapping(params="method=addBlackListInterface")
	public void addBlackListInterface(HttpServletRequest request,
			HttpServletResponse response, String appName, String interfaceInfo, String interfaceINfo, String blackApp) throws IOException {
		String responseMessage = "";
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		boolean exist = blackListInterfaceBo.checkExist(appName, blackApp, interfaceInfo);
		if (!hasPermission || exist) {
			responseMessage = "fail";
		} else {
			int count = blackListInterfaceBo.add(request);
			responseMessage = String.valueOf(count);
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=deleteBlackListInterfaceConfig")
	public void deleteBlackListInterfaceConfig(HttpServletRequest request,
			HttpServletResponse response, String id, String appName) throws IOException {
		boolean succuss = false;
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			succuss = blackListInterfaceBo.deleteConfig(id);
		}
		Writer writer = response.getWriter();
		writer.write(String.valueOf(succuss));
		writer.flush();
	}
	
	/***************************application black list*************************************************/
	@RequestMapping(params="method=goToAddBlackListApp")
	public ModelAndView goToAddBlackListApp() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/add_black_list_app");
		return modelAndView;
	}
	
	@RequestMapping(params="method=addBlackListApp")
	public void addBlackListApp(HttpServletRequest request,
			HttpServletResponse response, String appName, String blackApp) throws IOException {
		String responseMessage = "";
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		boolean exist = blackListAppBo.checkExist(appName, blackApp);
		if (!hasPermission || exist) {
			responseMessage = "fail";
		} else {
			int count = blackListAppBo.add(request);
			responseMessage = String.valueOf(count);
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=deleteBlackListAppConfig")
	public void deleteBlackListAppConfig(HttpServletRequest request,
			HttpServletResponse response, String id, String appName) throws IOException {
		boolean succuss = false;
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			succuss = blackListAppBo.deleteConfig(id);
		}
		Writer writer = response.getWriter();
		writer.write(String.valueOf(succuss));
		writer.flush();
	}
	
	/***************************customization white list*************************************************/
	@RequestMapping(params="method=goToAddWhiteListCustomer")
	public ModelAndView goToAddWhiteListCustomer() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/add_white_list_customer");
		return modelAndView;
	}
	
	@RequestMapping(params="method=addWhiteListCustomer")
	public void addWhiteListCustomer(HttpServletRequest request,
			HttpServletResponse response, String appName, String customerInfo, String whiteApp) throws IOException {
		String responseMessage = "";
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		boolean exist = whiteListCustomerBo.checkExist(appName, whiteApp, customerInfo);
		if (!hasPermission || exist) {
			responseMessage = "fail";
		} else {
			int count = whiteListCustomerBo.add(request);
			responseMessage = String.valueOf(count);
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=deleteWhiteListCustomerConfig")
	public void deleteWhiteListCustomerConfig(HttpServletRequest request,
			HttpServletResponse response, String id, String appName) throws IOException {
		boolean succuss = false;
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			succuss = whiteListCustomerBo.deleteConfig(id);
		}
		Writer writer = response.getWriter();
		writer.write(String.valueOf(succuss));
		writer.flush();
	}
	
	/***************************customization black list*************************************************/
	@RequestMapping(params="method=goToAddBlackListCustomer")
	public ModelAndView goToAddBlackListCustomer() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/add_black_list_customer");
		return modelAndView;
	}
	
	@RequestMapping(params="method=addBlackListCustomer")
	public void addBlackListCustomer(HttpServletRequest request,
			HttpServletResponse response, String appName, String customerInfo, String blackApp) throws IOException {
		String responseMessage = "";
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		boolean exist = blackListCustomerBo.checkExist(appName, blackApp, customerInfo);
		if (!hasPermission || exist) {
			responseMessage = "fail";
		} else {
			int count = blackListCustomerBo.add(request);
			responseMessage = String.valueOf(count);
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=deleteBlackListCustomerConfig")
	public void deleteBlackListCustomerConfig(HttpServletRequest request,
			HttpServletResponse response, String id, String appName) throws IOException {
		boolean succuss = false;
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			succuss = blackListCustomerBo.deleteConfig(id);
		}
		Writer writer = response.getWriter();
		writer.write(String.valueOf(succuss));
		writer.flush();
	}
	
	/***************************interface flow control*************************************************/
	@RequestMapping(params="method=goToAddFlowControlInterface")
	public ModelAndView goToAddFlowControlInterface(String strategy) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/add_flow_control_interface");
		modelAndView.addObject("strategy", strategy);
		return modelAndView;
	}
	
	@RequestMapping(params="method=addFlowControlInterface")
	public void addFlowControlInterface(HttpServletRequest request,
			HttpServletResponse response, String appName, String interfaceInfo, String flowApp, String strategy) throws IOException {
		String responseMessage = "";
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		boolean exist = flowControlInterfaceBo.checkExist(appName, flowApp, interfaceInfo, strategy);
		
		if (!hasPermission || exist) {
			responseMessage = "fail";
		} else {
			int count = flowControlInterfaceBo.add(request);
			responseMessage = String.valueOf(count);
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=deleteFlowControlInterfaceConfig")
	public void deleteFlowControlInterfaceConfig(HttpServletRequest request,
			HttpServletResponse response, String id, String appName) throws IOException {
		boolean succuss = false;
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		if (hasPermission) {
			succuss = flowControlInterfaceBo.deleteConfig(id);
		} 
		Writer writer = response.getWriter();
		writer.write(String.valueOf(succuss));
		writer.flush();
	}
	
	/***************************application flow control*************************************************/
	@RequestMapping(params="method=goToAddFlowControlApp")
	public ModelAndView goToAddFlowControlApp(String strategy) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/add_flow_control_app");
		modelAndView.addObject("strategy", strategy);
		return modelAndView;
	}
	
	@RequestMapping(params="method=addFlowControlApp")
	public void addFlowControlApp(HttpServletRequest request,
			HttpServletResponse response, String appName, String flowApp, String strategy) throws IOException {
		String responseMessage = "";
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		boolean exist = flowControlAppBo.checkExist(appName, flowApp, strategy);
		if (!hasPermission || exist) {
			responseMessage = "fail";
		} else {
			int count = flowControlAppBo.add(request);
			responseMessage = String.valueOf(count);
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=deleteFlowControlAppConfig")
	public void deleteFlowControlAppConfig(HttpServletRequest request,
			HttpServletResponse response, String id, String appName) throws IOException {
		boolean succuss = false;
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			succuss = flowControlAppBo.deleteConfig(id);
		}
		Writer writer = response.getWriter();
		writer.write(String.valueOf(succuss));
		writer.flush();
	}
	
	/***************************dependency flow control*************************************************/
	@RequestMapping(params="method=goToAddFlowControlDependency")
	public ModelAndView goToAddFlowControlDependency() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/add_flow_control_dependency");
		return modelAndView;
	}
	
	@RequestMapping(params="method=addFlowControlDependency")
	public void addFlowControlDependency(HttpServletRequest request,
			HttpServletResponse response, String appName, String flowApp, String interfaceInfo) throws IOException {
		String responseMessage = "";
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		boolean exist = flowControlDependencyBo.checkExist(appName, flowApp, interfaceInfo);
		if (!hasPermission || exist) {
			responseMessage = "fail";
		} else {
			flowControlDependencyBo.add(request);
			responseMessage = "success";
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=deleteFlowControlDependencyConfig")
	public void deleteFlowControlDependencyConfig(HttpServletRequest request,
			HttpServletResponse response, String id, String appName) throws IOException {
		boolean succuss = false;
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			succuss = flowControlDependencyBo.deleteConfig(id);
		}
		Writer writer = response.getWriter();
		writer.write(String.valueOf(succuss));
		writer.flush();
	}
	
	/***************************param flow control*************************************************/
	@RequestMapping(params="method=goToAddFlowControlParam")
	public ModelAndView goToAddFlowControlParam() {
		ModelAndView modelAndView = new ModelAndView("/sentinel/add_flow_control_param");
		return modelAndView;
	}
	
	@RequestMapping(params="method=addFlowControlParam")
	public void addFlowControlParam(HttpServletRequest request,
			HttpServletResponse response, String appName, String paramInfo, String flowApp) throws IOException {
		String responseMessage = "";
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		boolean exist = flowControlParamBo.checkExist(appName, flowApp, paramInfo);
		
		if (!hasPermission || exist) {
			responseMessage = "fail";
		} else {
			int count = flowControlParamBo.add(request);
			responseMessage = String.valueOf(count);
		}
		
		Writer writer = response.getWriter();
		writer.write(responseMessage);
		writer.flush();
	}
	
	@RequestMapping(params="method=deleteFlowControlParamConfig")
	public void deleteFlowControlParamConfig(HttpServletRequest request,
			HttpServletResponse response, String id, String appName) throws IOException {
		boolean succuss = false;
		
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		if (hasPermission) {
			succuss = flowControlParamBo.deleteConfig(id);
		} 
		Writer writer = response.getWriter();
		writer.write(String.valueOf(succuss));
		writer.flush();
	}
	
	/***************************common operations*************************************************/
	@RequestMapping(params="method=updateIps")
	public void updateIps(HttpServletRequest request,
			HttpServletResponse response, String appName) throws IOException {
		String responseValue = "0";
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		if (!hasPermission) {
			responseValue = "-1";
		} else {
			Set<String> changeRefApps = syncBo.synchApp(appName);
			if (changeRefApps.size() > 0) {
				responseValue = changeRefApps.toString();
			}
		}
		
		Writer writer = response.getWriter();
		writer.write(responseValue);
		writer.flush();
	}
	
	@RequestMapping(params="method=goToPushConfig")
	public ModelAndView goToPushConfig(String appName) {
		ModelAndView modelAndView = new ModelAndView("/sentinel/push/config_push");
		Set<String> appSet = commonBo.findAllApps();
		String apps = StringUtils.EMPTY;
		for (String app : appSet) {
			apps = apps + app + ",";
		}
		
		if (apps.endsWith(",")) {
			apps = apps.substring(0, apps.length() - 1);
		}
		String configInfo = null;
		
		if (!StringUtils.isEmpty(appName)) {
			configInfo = commonBo.generateConfig(appName);
		}
		
		modelAndView.addObject("configInfo", configInfo);
		modelAndView.addObject("apps", apps);
		modelAndView.addObject("appName", appName);
		
		return modelAndView;
	}
	
	@RequestMapping(params="method=pushConfig")
	public void pushConfig(HttpServletRequest request,
			HttpServletResponse response, String appName) throws IOException {
		boolean success = false;
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			success = commonBo.pushConfig(request, appName);
		}
		
		Writer writer = response.getWriter();
		writer.write(String.valueOf(success));
		writer.flush();
	}
	
	@RequestMapping(params="method=alterFlowThread")
	public void alterFlowThread(HttpServletRequest request,
			HttpServletResponse response, String id, String type, String thread, String appName) throws IOException {
		String message = "fail";
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			boolean success = commonBo.alterFlowThread(id, type, thread);
			if (success) {
				message = "success";
			}
		}	
		
		Writer writer = response.getWriter();
		writer.write(message);
		writer.flush();
	}
	
	@RequestMapping(params="method=alterFlowThreadMulti")
	public void alterFlowThreadMulti(HttpServletRequest request,
			HttpServletResponse response, String ids, String type, String thread, String appName) throws IOException {
		String message = "fail";
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			String [] idArr = ids.split(" ");
			for (String id : idArr) {
				boolean success = commonBo.alterFlowThread(id, type, thread);
				if (success) {
					message = "success";
				}
			}	
		}	
		
		Writer writer = response.getWriter();
		writer.write(message);
		writer.flush();
	}
	
	@RequestMapping(params="method=changeState")
	public void changeState(HttpServletRequest request,
			HttpServletResponse response, String id, String type, String appName) throws IOException {
		String message = "fail";
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		if (hasPermission) {
			boolean success = commonBo.changeState(id, type);
			if (success) {
				message = "success";
			}
		}	
		
		Writer writer = response.getWriter();
		writer.write(message);
		writer.flush();
	}
	
	@RequestMapping(params="method=goToAddUserPermission")
	public ModelAndView goToAddUserPermission(HttpServletRequest request) {
		ModelAndView modelAndView;
		boolean isAdministrator = userPermissionCheck.isAdministrator(request);
		
		if (isAdministrator) {
			modelAndView = new ModelAndView("/sentinel/system/add_user_info");
		} else {
			modelAndView = new ModelAndView("/permission");
		}
		return modelAndView;
	}
	
	@RequestMapping(params="method=addUserPermission")
	public void addUserPermission(HttpServletRequest request,
			HttpServletResponse response, String appName, String wangwang, String mail) throws IOException {
		boolean isAdministrator = userPermissionCheck.isAdministrator(request);	
		
		if (!isAdministrator) {
			return;
		} else {
			UserPermissionPo po = new UserPermissionPo();
			po.setAppName(appName);
			po.setWangwang(wangwang);
			po.setMail(mail);
			po.setId(LocalUtil.generateId());
			po.setLevel(Constants.USER);
			commonBo.addUserPermission(po);
		}
	}
	
	@RequestMapping(params="method=alterDataUrl")
	public void alterDataUrl(HttpServletRequest request,
			HttpServletResponse response, String appName, String dataUrl) throws IOException {
		boolean isAdministrator = userPermissionCheck.isAdministrator(request);	
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		Writer writer = response.getWriter();
		
		if (!isAdministrator && !hasPermission) {
			writer.write("fail");
		} else {
			commonBo.alterDataUrl(appName, dataUrl);
			writer.write("success");
		}
		writer.flush();
		
	}
	
	@RequestMapping(params="method=alterPeriod")
	public void alterPeriod(HttpServletRequest request,
			HttpServletResponse response, String appName, String period) throws IOException {
		boolean isAdministrator = userPermissionCheck.isAdministrator(request);	
		boolean hasPermission = userPermissionCheck.hasPermission(request, appName);
		
		Writer writer = response.getWriter();
		
		if (!isAdministrator && !hasPermission) {
			writer.write("fail");
		} else {
			commonBo.alterPeriod(appName, Integer.parseInt(period));
			writer.write("success");
		}
		writer.flush();
		
	}
	
	@RequestMapping(params="method=initTestData")
	public void initTestData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows")) {
			InitTestData.initTestData();
		}
		
	}
}
