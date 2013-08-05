package com.taobao.csp.depend.po;

/**
 * ����ճ�������Ӧ�õ���Ϣ
 * @author zhongting.zy
 *
 */
public class CheckupDependConfigRel {
	private int opsId;
	private String opsName;
	private String secondMachineIps; // ���׻���Ӧ�õĻ�����Ip
	private String addIps;	//�ֹ���ӵ�IP
	private String dailyMachineIps;
	
	private String startupPath; // Ӧ�������ű���ַ
	private String description; // ˵��

	private String seleniumServer;// seleiumserver ��ַ
	private String seleniumScript;// selenium�ű�
	private String checkupUrl;// ���Ӧ�õ�url
	private String checkupContext;// url��� ��Ҫ���ֵ�����
	private String appStatus;	// Ӧ���Ƿ���Ч��ֵ: -1 , 0 , 1

	private String stepRecords;	// ��¼����

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
