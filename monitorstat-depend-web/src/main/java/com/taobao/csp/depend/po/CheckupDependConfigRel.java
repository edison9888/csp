package com.taobao.csp.depend.po;

/**
 * 存放日常环境的应用的信息
 * @author zhongting.zy
 *
 */
public class CheckupDependConfigRel {
	private int opsId;
	private String opsName;
	private String secondMachineIps; // 二套环境应用的机器的Ip
	private String addIps;	//手工添加的IP
	private String dailyMachineIps;
	
	private String startupPath; // 应用启动脚本地址
	private String description; // 说明

	private String seleniumServer;// seleiumserver 地址
	private String seleniumScript;// selenium脚本
	private String checkupUrl;// 检查应用的url
	private String checkupContext;// url检查 需要出现的内容
	private String appStatus;	// 应用是否有效，值: -1 , 0 , 1

	private String stepRecords;	// 记录过程

	public int getOpsId() {
		return opsId;
	}

	public void setOpsId(int opsId) {
		this.opsId = opsId;
	}

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public String getSecondMachineIps() {
		return secondMachineIps;
	}

	public void setSecondMachineIps(String secondMachineIps) {
		this.secondMachineIps = secondMachineIps;
	}

	public String getDailyMachineIps() {
		return dailyMachineIps;
	}

	public void setDailyMachineIps(String dailyMachineIps) {
		this.dailyMachineIps = dailyMachineIps;
	}

	public String getStartupPath() {
		return startupPath;
	}

	public void setStartupPath(String startupPath) {
		this.startupPath = startupPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSeleniumServer() {
		return seleniumServer;
	}

	public void setSeleniumServer(String seleniumServer) {
		this.seleniumServer = seleniumServer;
	}

	public String getSeleniumScript() {
		return seleniumScript;
	}

	public void setSeleniumScript(String seleniumScript) {
		this.seleniumScript = seleniumScript;
	}

	public String getCheckupUrl() {
		return checkupUrl;
	}

	public void setCheckupUrl(String checkupUrl) {
		this.checkupUrl = checkupUrl;
	}

	public String getCheckupContext() {
		return checkupContext;
	}

	public void setCheckupContext(String checkupContext) {
		this.checkupContext = checkupContext;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getStepRecords() {
		return stepRecords;
	}

	public void setStepRecords(String stepRecords) {
		this.stepRecords = stepRecords;
	}

	public String getAddIps() {
		return addIps;
	}

	public void setAddIps(String addIps) {
		this.addIps = addIps;
	}

}
