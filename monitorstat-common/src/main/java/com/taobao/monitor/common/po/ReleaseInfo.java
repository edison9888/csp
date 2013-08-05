package com.taobao.monitor.common.po;

public class ReleaseInfo {

	private String planId;		//发布单id
	private String appName;	//应用名
	private String planTime;	//发布计划时间
	private String pubType;	//发布类型：APP_PUB（应用发布）
	private String planKind;	//发布种类：normal  发布, rollback  回滚
	private String pubLevel;	//发布级别：prepub(预发), beta（beta发布）, publish（正式发布）
	private String releaseId;	//调用方单子id
	private String callSystem;	//调用系统
	private String creator;	//创建人工号
	private String sign;		//验证串：md5(key+url) key为验证key，url为提供订阅的url
	private String notifyType;	//通知类型：start  开始,end 结束
	private String finishTime;	//yyyy-MM-dd HH:mm   （notifyType为end有该参数）
	private String result;		//发布单结果  SUC 成功  （notifyType为end有该参数） FAIL 失败

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
