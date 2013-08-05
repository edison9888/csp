
package com.taobao.csp.depend.po;

import java.util.Date;

/**
 * 每次检查的总体结果
 */
public class CheckupDependConfig {

	/*csp_checkup_depend_config中的字段*/
	private String uuid;	//作为主键，外联csp_checkup_depend_result
	
	private int codeVersion;	//代码版本号
	
	private String opsName;

	private String selfIp; // 被测主机的Ip

	private String targetOpsName;

	private String targetAppType;

	private Integer port;
	
	private String targetIps;
	
	private Date collect_Time;
	
	private String startPreventIntensity = "";

	private String runPreventIntensity = "";

	private String startDelayIntensity = "";

	private String runDelayIntensity = "";

	private String checkdimension;
	
	private String codePath;
	
	private long costtime;	//检查消耗时间
	
	/*csp_checkup_depend_config_rel中的字段*/
	private CheckupDependConfigRel opsApp;
	private CheckupDependConfigRel opsTarget;
	
	private Date addTime = new Date();
	private String status = "waiting";	//waiting doing
	
	public String getOpsName() {
		return opsName;
	}
	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}
	public String getSelfIp() {
		return selfIp;
	}
	public void setSelfIp(String selfIp) {
		this.selfIp = selfIp;
	}
	public String getTargetOpsName() {
		return targetOpsName;
	}
	public void setTargetOpsName(String targetOpsName) {
		this.targetOpsName = targetOpsName;
	}
	public String getTargetAppType() {
		return targetAppType;
	}
	public void setTargetAppType(String targetAppType) {
		this.targetAppType = targetAppType;
	}
	public String getTargetIps() {
		if(targetIps == null)
			targetIps = "";
		return targetIps;
	}
	public void setTargetIps(String targetIps) {
		this.targetIps = targetIps;
	}
	public Date getCollect_Time() {
		return collect_Time;
	}
	public void setCollect_Time(Date collect_Time) {
		this.collect_Time = collect_Time;
	}
	public String getStartPreventIntensity() {
		return startPreventIntensity;
	}
	public void setStartPreventIntensity(String startPreventIntensity) {
		this.startPreventIntensity = startPreventIntensity;
	}
	public String getRunPreventIntensity() {
		return runPreventIntensity;
	}
	public void setRunPreventIntensity(String runPreventIntensity) {
		this.runPreventIntensity = runPreventIntensity;
	}
	public String getStartDelayIntensity() {
		return startDelayIntensity;
	}
	public void setStartDelayIntensity(String startDelayIntensity) {
		this.startDelayIntensity = startDelayIntensity;
	}
	public String getRunDelayIntensity() {
		return runDelayIntensity;
	}
	public void setRunDelayIntensity(String runDelayIntensity) {
		this.runDelayIntensity = runDelayIntensity;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCodeVersion() {
		return codeVersion;
	}
	public void setCodeVersion(int codeVersion) {
		this.codeVersion = codeVersion;
	}
	public CheckupDependConfigRel getOpsApp() {
		return opsApp;
	}
	public void setOpsApp(CheckupDependConfigRel opsApp) {
		this.opsApp = opsApp;
	}
	public CheckupDependConfigRel getOpsTarget() {
		return opsTarget;
	}
	public void setOpsTarget(CheckupDependConfigRel opsTarget) {
		this.opsTarget = opsTarget;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getCheckdimension() {
		return checkdimension;
	}
	public void setCheckdimension(String checkdimension) {
		this.checkdimension = checkdimension;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getCodePath() {
		return codePath;
	}
	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}
	public long getCosttime() {
		return costtime;
	}
	public void setCosttime(long costtime) {
		this.costtime = costtime;
	}
}
