
/**
 * monitorstat-time-web
 */
package com.taobao.csp.other.artoo;

/**
 * @author xiaodu
 *
 * ÉÏÎç11:23:04
 */
public class Artoo {
	
	private String id;
	private String appName;
	private String creator;
	private String deployTime;
	private String state;
	private String planType;
	private String completeServerNum;
	private String totalServerNum;
	private String callSystem;
	private String finishTime;
	
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
	public String getDeployTime() {
		return deployTime;
	}
	public void setDeployTime(String deployTime) {
		this.deployTime = deployTime;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCallSystem() {
		return callSystem;
	}
	public void setCallSystem(String callSystem) {
		this.callSystem = callSystem;
	}
	public String getCompleteServerNum() {
		return completeServerNum;
	}
	public void setCompleteServerNum(String completeServerNum) {
		this.completeServerNum = completeServerNum;
	}
	public String getTotalServerNum() {
		return totalServerNum;
	}
	public void setTotalServerNum(String totalServerNum) {
		this.totalServerNum = totalServerNum;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	@Override
	public String toString() {
		return "ArtooPo [id=" + id + ", appName=" + appName + ", creator="
				+ creator + ", deployTime=" + deployTime + ", state=" + state
				+ ", planType=" + planType + ", completeServerNum="
				+ completeServerNum + ", totalServerNum=" + totalServerNum
				+ ", callSystem=" + callSystem + ", finishTime=" + finishTime
				+ "]";
	}
	
	

}
