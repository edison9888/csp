
package com.taobao.csp.depend.po;

import java.util.Date;

/**
 * �����Զ�������ݷ�װʹ��
 * @author xiaodu
 * @version 2011-5-10 ����11:35:26
 */
public class AutoCheckDependentPo {
	
	private String id;//   UUID���ɵ�Ψһ����
	
	private String appName; //����Ӧ������
	
	private String targetId;//Ŀ�����IP
	
	private String userName;//�����û�
	
	private String userPwd; //��������
	
	private String autoRunScriptId;//��Ҫִ�еĽű�Id��automan �Ǳ��ṩ��
	
	private String forbidIps; //��Ҫ���ε�Ip  ip�ö̺Ÿ���
	
	private String runStatus;//wait_run ��ʾδִ�й�  		running ����ִ���� run_finish��ʾִ�н���
	
	private Date runStartTime; //��ʼ����������ʱ��
	
	private Date runEndTime;//��������������ʱ��
	
	private String runResult; //���н��
	
	private String runCourseMsg;//ִ�й��̵�һЩ��Ϣ

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
