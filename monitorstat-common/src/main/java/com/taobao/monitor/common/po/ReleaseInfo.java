package com.taobao.monitor.common.po;

public class ReleaseInfo {

	private String planId;		//������id
	private String appName;	//Ӧ����
	private String planTime;	//�����ƻ�ʱ��
	private String pubType;	//�������ͣ�APP_PUB��Ӧ�÷�����
	private String planKind;	//�������ࣺnormal  ����, rollback  �ع�
	private String pubLevel;	//��������prepub(Ԥ��), beta��beta������, publish����ʽ������
	private String releaseId;	//���÷�����id
	private String callSystem;	//����ϵͳ
	private String creator;	//�����˹���
	private String sign;		//��֤����md5(key+url) keyΪ��֤key��urlΪ�ṩ���ĵ�url
	private String notifyType;	//֪ͨ���ͣ�start  ��ʼ,end ����
	private String finishTime;	//yyyy-MM-dd HH:mm   ��notifyTypeΪend�иò�����
	private String result;		//���������  SUC �ɹ�  ��notifyTypeΪend�иò����� FAIL ʧ��

	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPlanTime() {
		return planTime;
	}
	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}
	public String getPubType() {
		return pubType;
	}
	public void setPubType(String pubType) {
		this.pubType = pubType;
	}
	public String getPlanKind() {
		return planKind;
	}
	public void setPlanKind(String planKind) {
		this.planKind = planKind;
	}
	public String getPubLevel() {
		return pubLevel;
	}
	public void setPubLevel(String pubLevel) {
		this.pubLevel = pubLevel;
	}
	public String getReleaseId() {
		return releaseId;
	}
	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}
	public String getCallSystem() {
		return callSystem;
	}
	public void setCallSystem(String callSystem) {
		this.callSystem = callSystem;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
