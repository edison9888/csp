package com.taobao.csp.capacity.po;

public class CapacityCostConfigPo extends BasePo {

	private String appName;
	
	private String configGroup;
	
	private int groupMachineNum;
	
	private double perMchine;
	private String formatPerMachine;
	
	private String depType;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getConfigGroup() {
		return configGroup;
	}
	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
	}
	public int getGroupMachineNum() {
		return groupMachineNum;
	}
	public void setGroupMachineNum(int groupMachineNum) {
		this.groupMachineNum = groupMachineNum;
	}
	public double getPerMchine() {
		return perMchine;
	}
	public void setPerMchine(double perMchine) {
		this.perMchine = perMchine;
		this.formatPerMachine = DF_DOUBLE_4.format(perMchine);
	}
	public String getFormatPerMachine() {
		return formatPerMachine;
	}
	public void setFormatPerMachine(String formatPerMachine) {
		this.formatPerMachine = formatPerMachine;
	}
	public String getDepType() {
		return depType;
	}
	public void setDepType(String depType) {
		this.depType = depType;
	} 
	
}
