
package com.taobao.csp.capacity.po;


/***
 * ����Ӧ��app
 * @author youji.zj
 *
 */
public class CapacityAppPo {
	
	private int appId;
	
	private String datasourceName;
	
	private String appType;
	
	private String dataFeature;
	
	private String groupNames;
	
	private int growthRate;
	
	/*** ���e���ݽӿڣ���������� ***/
	private String itemName;
	
	/*** ���e���ݽӿ�: ���������� ***/
	private String dataName;

	public String getDataFeature() {
		return dataFeature;
	}

	public void setDataFeature(String dataFeature) {
		this.dataFeature = dataFeature;
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

	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public int getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(int growthRate) {
		this.growthRate = growthRate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

}
