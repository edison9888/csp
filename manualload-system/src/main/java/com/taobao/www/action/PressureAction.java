package com.taobao.www.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.taobao.www.arkclient.csp.ManualCurUser;
import com.taobao.www.arkclient.csp.ManualUrlFilter;
import com.taobao.www.common.CommonAction;
import com.taobao.www.entity.AppConfig;
import com.taobao.www.entity.AppMachine;
import com.taobao.www.entity.PressureResult;
import com.taobao.www.entity.PressureVo;
import com.taobao.www.manager.PressureService;
import com.taobao.www.manager.impl.PressureServiceImpl;

/**
 * 
 * 功能：全量压测系统处理访问的action
 * 
 * @author wb-tangjinge E-mail:wb-tangjinge@taobao.com
 * @version 1.0
 * @since 2012-11-9 下午6:54:34
 */
@SuppressWarnings("serial")
public class PressureAction extends CommonAction {

	private static final Logger logger = Logger.getLogger(ManualUrlFilter.class);

	ActionContext ctx = ActionContext.getContext();
	HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);

	public String index() {
		return "index";
	}

	/**
	 * 功能：展示压测应用列表信息
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String getAllPressures() {
		logger.info("PressureAction url address");
		listMac = oneService.getAllPreConfigs(manualCurUser);
		return "listpress";
	}

	/**
	 * 功能：展示某个的应用下的所有机器信息
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String getAllAppMachineByAppId() {
		listMac = oneService.getAllPreConfigs(manualCurUser);
		if (appId == null && listMac != null && listMac.size() > 0) {
			appId = listMac.get(0).getId();
		}
		listPressure = oneService.getAllAppMachines(appId);
		other = listPressure.size() == 0 ? 0 : listPressure.size();
		return "listAppMachine";
	}

	/**
	 * 功能：新增压测配置对象
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String addPressures() {
		return "addPressures";
	}

	public String checkAppName() {

		String result = null;
		if (appname == null || appname.equals("")) {
			result = "应用名不能空!";
		} else if (oneService.checkAddAppName(appname)) {
			result = "自动压测系统存在该应用名！";
		} else {
			result = "自动压测系统不存在该应用名！";
		}
		PrintWriter out = null;
		try {
			response.setContentType("text/plain;charset=UTF-8");
			out = response.getWriter();
			out.print(result);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	/**
	 * 功能：保存压测配置对象
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String saveOnePreConfig() {

		if (appConfig.getAppName() == null || appConfig.getAppName().equals("")) {
			appname = "应用名不能空!";
			return "failed";
		} else if (!oneService.checkAddAppName(appConfig.getAppName())) {
			appname = "自动压测系统不存在该压测应用！";
			return "failed";
		}
		if (appConfig.getUserPass() == null || appConfig.getUserPass().equals("")) {
			userPass = "用户密码不能为空！";
			return "failed";
		} else if (appConfig.getUserPass() != null) {
			boolean result = oneService.checkUserAndPass(appConfig);
			if (!result) {
				userPass = "核实用户密码！";
				return "failed";
			}
		}
		if (appConfig.getPort() == null || appConfig.getPort().equals("")) {
			gobleString = "压测端口不能为空！";
			return "failed";
		} else if (appConfig.getPort().equals("0")) {
			gobleString = "压测端口不能为零！";
			return "failed";
		} else {
			Pattern pattern = Pattern.compile("[0-9]*");
			boolean result = pattern.matcher(appConfig.getPort()).matches();
			if (!result) {
				gobleString = "压测端口只能是数字！";
				return "failed";
			}
		}
		String email = (String) requests.getSession().getAttribute("manualCurUserEmail");
		boolean result = oneService.checkUserAddAppName(appConfig.getAppName(), email);
		if (!result) {
			return "addFailed";
		}
		appConfig.setPrePort(Integer.parseInt(appConfig.getPort()));
		other = oneService.saveOneAppConfig(appConfig);
		if (other != null && other != 0) {
			return "savePreConfig";
		} else {
			return "input";
		}
	}

	/**
	 * 功能：展示所有压测配置信息
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String getAllPreConfigs() {
		listMac = oneService.getAllPreConfigs(manualCurUser);
		return "listpreconfig";
	}

	/**
	 * 功能：自动压测参数设置
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String autoReady() {
		boolean result = oneService.executeAndUploadFile(appId);
		if (result) {
			return "readyPressConfig";
		} else {
			gobleString = "压测失败：   1、该应用可能不存在。   2、该应用下可能没有压测的机器。";
			return "input";
		}
	}

	/**
	 * 功能：生成待压测的url地址。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String generatePressUrl() {

		int result = oneService.generatePressUrl(appId);
		if (result == 1) {
			gobleString = "生成压测url地址成功！";
		} else if (result == 2) {
			gobleString = "该应用下无机器可压！";
		} else {
			gobleString = "生成压测url地址失败！";
		}
		PrintWriter out = null;
		try {
			response.setContentType("text/plain;charset=UTF-8");
			out = response.getWriter();
			out.print(gobleString);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	/**
	 * 功能：根据应用id来同步该应用下的机器信息。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String synMacchineInfo() {

		int result = oneService.synchronousMacchineInfo(appId);
		if (result == 1) {
			gobleString = "同步机器成功！";
		} else if (result == 2) {
			gobleString = "该应用下无机器可同步！";
		} else {
			gobleString = "同步机器失败！";
		}
		PrintWriter out = null;
		try {
			response.setContentType("text/plain;charset=UTF-8");
			out = response.getWriter();
			out.print(gobleString);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	/**
	 * 功能：保存压测参数并且跳转到压测进度页面。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String executePressure() {
		Pattern pattern = Pattern.compile("[0-9]*");
		if (appConfig.getReqTotle() == null || appConfig.getReqTotle().equals("")) {
			appname = " 压测时间不能空!";
			return "failed";
		} else if (!pattern.matcher(appConfig.getReqTotle()).matches()) {
			appname = " 压测时间只能是数字!";
			return "failed";
		}
		reqTotle = appConfig.getReqTotle();
		if (appConfig.getExeTime() == null || appConfig.getExeTime().equals("")) {
			userPass = "压测请求数不能为空！";
			return "failed";
		} else if (!pattern.matcher(appConfig.getExeTime()).matches()) {
			userPass = "压测请求数只能是数字！";
			return "failed";
		} else if (pattern.matcher(appConfig.getExeTime()).matches() && Integer.parseInt(appConfig.getExeTime()) > 1000) {
			userPass = "压测请求数请求数不能超过1000个！";
			return "failed";
		}
		exeTime = appConfig.getExeTime();
		appConfig = oneService.getOneAppConfigMessage(appId);
		appConfig.setExeTime(exeTime);
		appConfig.setReqTotle(reqTotle);
		return "executeInfo";
	}

	/**
	 * 功能：压测进度展示页面。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String executeResultInfo() {
		return "resultInfo";
	}

	/**
	 * 功能：进行全量压测应用
	 * 
	 * @param appId
	 *            应用Id
	 * 
	 * @param pretime
	 *            压测时间
	 * 
	 * @param username
	 *            用户名
	 * 
	 * @param password
	 *            密码
	 * 
	 * @param other
	 *            每秒请求
	 * 
	 * @param reqInc
	 *            增加请求数
	 * 
	 * @return 返回执行结果
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String getPressureInformation() {

		appConfig = oneService.getOneAppConfigMessage(appId);
		userName = appConfig.getUserName();
		userPass = appConfig.getUserPass();
		prePort = appConfig.getPrePort();
		// int appId, int exeutetime, String username, String password, int
		// requestTotle,
		gobleString = oneService.getPressureInfo(appId, pretime, userName, userPass, other, prePort, flag);
		PrintWriter out = null;
		try {
			response.setContentType("text/plain;charset=UTF-8");
			out = response.getWriter();
			out.print(gobleString);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	/**
	 * 功能：终止压测。 该功能已废弃使用
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String stopPressInfo() {
		oneService.stopPressInfo(manualCurUser);
		PrintWriter out = null;
		try {
			response.setContentType("text/plain;charset=UTF-8");
			out = response.getWriter();
			out.print("ok");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	/**
	 * 功能：跟踪压测进度信息
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String trackingPressInfo() {
		String result = oneService.trackingPressInfo(manualCurUser);
		PrintWriter out = null;
		try {
			response.setContentType("text/plain;charset=UTF-8");
			out = response.getWriter();
			out.print(result);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	/**
	 * 功能：效验用户和密码是否正确。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String checkUserAndPass() {

		boolean result = oneService.checkUserAndPass(appConfig);
		if (result) {
			gobleString = "OK";
		} else {
			gobleString = "NO";
		}
		PrintWriter out = null;
		try {
			response.setContentType("text/plain;charset=UTF-8");
			out = response.getWriter();
			out.print(gobleString);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	/**
	 * 功能：根据时间和应用来查询压测结果信息。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String getAllPresureResultInfo() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (querydate == null) {
			Calendar ca = Calendar.getInstance();
			querydate = format.format(ca.getTime());
		}
		listMac = oneService.getAllPreConfigs(manualCurUser);
		if (appId == null && listMac != null && listMac.size() > 0) {
			appId = listMac.get(0).getId();
		} else if (appId == null && (listMac == null || listMac.size() == 0)) {
			appId = 0;
		}
		listResult = oneService.getAllPressureResultMessage(querydate, appId);
		return "getAllResultInfo";
	}

	/**
	 * 功能：根据id来删除一个应用。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String delOneAppById() {
		boolean result = oneService.delOneAppConfig(appId);
		if (result) {
			return "delSuccess";
		} else {
			return "input";
		}
	}

	/**
	 * 功能：根据id来删除一个机器信息。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String delAppMachineById() {
		oneService.delAppMachineById(flag);
		return "deleteSuccess";
	}

	/**
	 * 功能：根据id来获取一个配置信息。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String getOneAppConfigById() {
		appConfig = oneService.getOneAppConfigMessage(appId);
		return "oneObject";
	}

	/**
	 * 功能：根据id来修改一个配置信息。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-11-09
	 */
	public String updateOneAppConfig() {
		userName = appConfig.getUserName();
		if (appConfig.getPort() == null || appConfig.getPort().equals("")) {
			gobleString = "压测端口不能为空！";
			appConfig.setPort("7001");
			return "oneObject";
		} else if (appConfig.getPort().equals("0")) {
			gobleString = "压测端口不能为零！";
			appConfig.setPort("7001");
			return "oneObject";
		} else {
			Pattern pattern = Pattern.compile("[0-9]*");
			boolean result = pattern.matcher(appConfig.getPort()).matches();
			if (!result) {
				gobleString = "压测端口只能是数字！";
				appConfig.setPort("7001");
				return "oneObject";
			}
		}
		boolean result = oneService.updateOneAppConfig(appConfig);
		if (result) {
			return "updateSucess";
		} else {
			return "input";
		}
	}

	/**
	 * 功能：处理错误的url地址
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-01-10
	 * 
	 */
	public String manualError() {
		gobleString = (String) requests.getSession().getAttribute("manualErrorUrl");
		requests.getSession().removeAttribute("manualErrorUrl");
		return "manualerror";
	}

	private PressureService oneService = new PressureServiceImpl();

	private String userName;

	private String manualCurUser = ManualCurUser.getLoginUserName(requests);

	private String appname;

	private String title;

	private String gobleString;

	private String preTotle;

	private Integer appId;

	private Integer reqinc;

	private Integer totle;

	private Integer prePort;

	private Integer cycleTotle;

	private Integer other;

	private String userPass;

	private List<AppMachine> listPressure;

	private List<AppConfig> listMac;

	private List<PressureResult> listResult;

	private String flag;

	private Integer pretime;

	private Integer precount;

	private PressureVo preVo;

	private AppConfig appConfig;

	private String querydate;

	private String reqTotle;

	private String exeTime;

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public PressureService getOneService() {
		return oneService;
	}

	public void setOneService(PressureService oneService) {
		this.oneService = oneService;
	}

	public String getGobleString() {
		return gobleString;
	}

	public void setGobleString(String gobleString) {
		this.gobleString = gobleString;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public List<AppMachine> getListPressure() {
		return listPressure;
	}

	public void setListPressure(List<AppMachine> listPressure) {
		this.listPressure = listPressure;
	}

	public String getPreTotle() {
		return preTotle;
	}

	public void setPreTotle(String preTotle) {
		this.preTotle = preTotle;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getTotle() {
		return totle;
	}

	public void setTotle(Integer totle) {
		this.totle = totle;
	}

	public Integer getOther() {
		return other;
	}

	public void setOther(Integer other) {
		this.other = other;
	}

	public List<AppConfig> getListMac() {
		return listMac;
	}

	public void setListMac(List<AppConfig> listMac) {
		this.listMac = listMac;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public Integer getPretime() {
		return pretime;
	}

	public void setPretime(Integer pretime) {
		this.pretime = pretime;
	}

	public Integer getPrecount() {
		return precount;
	}

	public void setPrecount(Integer precount) {
		this.precount = precount;
	}

	public Integer getCycleTotle() {
		return cycleTotle;
	}

	public void setCycleTotle(Integer cycleTotle) {
		this.cycleTotle = cycleTotle;
	}

	public List<PressureResult> getListResult() {
		return listResult;
	}

	public void setListResult(List<PressureResult> listResult) {
		this.listResult = listResult;
	}

	public Integer getReqinc() {
		return reqinc;
	}

	public void setReqinc(Integer reqinc) {
		this.reqinc = reqinc;
	}

	public PressureVo getPreVo() {
		return preVo;
	}

	public void setPreVo(PressureVo preVo) {
		this.preVo = preVo;
	}

	public Integer getPrePort() {
		return prePort;
	}

	public void setPrePort(Integer prePort) {
		this.prePort = prePort;
	}

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	public String getQuerydate() {
		return querydate;
	}

	public void setQuerydate(String querydate) {
		this.querydate = querydate;
	}

	public String getManualCurUser() {
		return manualCurUser;
	}

	public void setManualCurUser(String manualCurUser) {
		this.manualCurUser = manualCurUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getReqTotle() {
		return reqTotle;
	}

	public void setReqTotle(String reqTotle) {
		this.reqTotle = reqTotle;
	}

}
