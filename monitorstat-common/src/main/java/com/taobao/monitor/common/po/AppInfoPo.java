package com.taobao.monitor.common.po;

import java.util.List;

public class AppInfoPo implements Comparable<AppInfoPo> {

	private int appId;
	
	private String appName;
	
	private String opsName;
	
	private int sortIndex;
	
	private String feature;
	
	private String groupName;

	private int dayDeploy;  // 1 部署0 没有
	
	private int timeDeploy;// 1 部署 0 没有
	
	private ServerInfoPo serverInfoPo;			//对应的server对象
	
	private DataBaseInfoPo dataBaseInfoPo;  	//对应的database对象
	
	private String appType;						//应用的类型（日报 或者 实时）
	
	private int appStatus;  //0 正常 1 删除
	
	private int appDayId;   //老的日报ID
	
	private String appDayFeature;//老的日报 featrue 
	
	
	private String defineType;//老的 应用定义类型
	
	
	private String opsField;//opsfree 上使用的字段
	
	private String loginName;//应用线上机器的登录应用账户
	
	private String loginPassword;//应用线上机器的登录密码
	
	private String appRushHours; //应用的访问高峰期时间段
	
	private String companyName;
	
	private List<HostPo> hostList;
	

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int compareTo(AppInfoPo o) {
		
		if(this.getSortIndex()>o.getSortIndex()){
			return 1;
		}else if(this.getSortIndex()==o.getSortIndex()){
			return 0;
		}else if(this.getSortIndex()<o.getSortIndex()){
			return -1;
		}		
		return 0;
	}

	public ServerInfoPo getServerInfoPo() {
		return serverInfoPo;
	}

	public void setServerInfoPo(ServerInfoPo serverInfoPo) {
		this.serverInfoPo = serverInfoPo;
	}

	public DataBaseInfoPo getDataBaseInfoPo() {
		return dataBaseInfoPo;
	}

	public void setDataBaseInfoPo(DataBaseInfoPo dataBaseInfoPo) {
		this.dataBaseInfoPo = dataBaseInfoPo;
	}

	public int getDayDeploy() {
		return dayDeploy;
	}

	public void setDayDeploy(int i) {
		this.dayDeploy = i;
	}

	public int getTimeDeploy() {
		return timeDeploy;
	}

	public void setTimeDeploy(int timeDeploy) {
		this.timeDeploy = timeDeploy;
	}

	public int getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(int appStatus) {
		this.appStatus = appStatus;
	}

	public List<HostPo> getHostList() {
		return hostList;
	}

	public void setHostList(List<HostPo> hostList) {
		this.hostList = hostList;
	}

	public int getAppDayId() {
		return appDayId;
	}

	public void setAppDayId(int appDayId) {
		this.appDayId = appDayId;
	}

	public String getAppDayFeature() {
		return appDayFeature;
	}

	public void setAppDayFeature(String appDayFeature) {
		this.appDayFeature = appDayFeature;
	}

	public String getDefineType() {
		return defineType;
	}

	public void setDefineType(String defineType) {
		this.defineType = defineType;
	}

	public String getOpsField() {
		return opsField;
	}

	public void setOpsField(String opsField) {
		this.opsField = opsField;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getAppRushHours() {
		return appRushHours;
	}

	public void setAppRushHours(String appRushHours) {
		this.appRushHours = appRushHours;
	}
	
}
