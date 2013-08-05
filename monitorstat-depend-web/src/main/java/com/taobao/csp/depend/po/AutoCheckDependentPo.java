
package com.taobao.csp.depend.po;

import java.util.Date;

/**
 * 依赖自动检查数据封装使用
 * @author xiaodu
 * @version 2011-5-10 上午11:35:26
 */
public class AutoCheckDependentPo {
	
	private String id;//   UUID生成的唯一序列
	
	private String appName; //检查的应用名称
	
	private String targetId;//目标机器IP
	
	private String userName;//机器用户
	
	private String userPwd; //机器密码
	
	private String autoRunScriptId;//需要执行的脚本Id，automan 那边提供的
	
	private String forbidIps; //需要屏蔽的Ip  ip用短号隔开
	
	private String runStatus;//wait_run 表示未执行过  		running 正在执行中 run_finish表示执行结束
	
	private Date runStartTime; //开始运行这个检查时间
	
	private Date runEndTime;//结束运行这个检查时间
	
	private String runResult; //运行结果
	
	private String runCourseMsg;//执行过程的一些信息

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getAutoRunScriptId() {
		return autoRunScriptId;
	}

	public void setAutoRunScriptId(String autoRunScriptId) {
		this.autoRunScriptId = autoRunScriptId;
	}

	public String getForbidIps() {
		return forbidIps;
	}

	public void setForbidIps(String forbidIps) {
		this.forbidIps = forbidIps;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public Date getRunStartTime() {
		return runStartTime;
	}

	public void setRunStartTime(Date runStartTime) {
		this.runStartTime = runStartTime;
	}

	public Date getRunEndTime() {
		return runEndTime;
	}

	public void setRunEndTime(Date runEndTime) {
		this.runEndTime = runEndTime;
	}

	public String getRunResult() {
		return runResult;
	}

	public void setRunResult(String runResult) {
		this.runResult = runResult;
	}

	public String getRunCourseMsg() {
		return runCourseMsg;
	}

	public void setRunCourseMsg(String runCourseMsg) {
		this.runCourseMsg = runCourseMsg;
	}
	
	

}
