package com.taobao.monitor.common.po;

import java.util.List;

public class AppInfoPo implements Comparable<AppInfoPo> {

	private int appId;
	
	private String appName;
	
	private String opsName;
	
	private int sortIndex;
	
	private String feature;
	
	private String groupName;

	private int dayDeploy;  // 1 ����0 û��
	
	private int timeDeploy;// 1 ���� 0 û��
	
	private ServerInfoPo serverInfoPo;			//��Ӧ��server����
	
	private DataBaseInfoPo dataBaseInfoPo;  	//��Ӧ��database����
	
	private String appType;						//Ӧ�õ����ͣ��ձ� ���� ʵʱ��
	
	private int appStatus;  //0 ���� 1 ɾ��
	
	private int appDayId;   //�ϵ��ձ�ID
	
	private String appDayFeature;//�ϵ��ձ� featrue 
	
	
	private String defineType;//�ϵ� Ӧ�ö�������
	
	
	private String opsField;//opsfree ��ʹ�õ��ֶ�
	
	private String loginName;//Ӧ�����ϻ����ĵ�¼Ӧ���˻�
	
	private String loginPassword;//Ӧ�����ϻ����ĵ�¼����
	
	private String appRushHours; //Ӧ�õķ��ʸ߷���ʱ���
	
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
