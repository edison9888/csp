package com.taobao.csp.cost.po;

import java.util.Date;

import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.CostConstants;
import com.taobao.monitor.common.util.Utlitites;

public class CapacityCostInfoPo extends BasePo {
	
	private String appName;
	
	private CostType costType;
	
	private long pv;
	private String formatPv;
	
	private int machineNum;
	private String indirectMachine; // for display
	
	private double perCost;
	private String formatPerCost; // for display
	
	private long selfCost;
	private String formatSelfCost; // for display
	
	private long dependCost;
	private String formatDependCost; // for display
	
	private long totalCost;
	private String formatTotalCost; // for display
	
	private Date collectTime;
	
	private double dependPerCost;
	private String formatDependPerCost; // for display
	
	private double totalPerCost;
	private String formatTotalPerCost; // for display

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public CostType getCostType() {
		return costType;
	}

	public void setCostType(CostType costType) {
		this.costType = costType;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
		this.formatPv = Utlitites.fromatLong(String.valueOf(pv));
	}

	public int getMachineNum() {
		return machineNum;
	}

	public void setMachineNum(int machineNum) {
		this.machineNum = machineNum;
	}

	public double getPerCost() {
		return perCost;
	}

	public void setPerCost(double perCost) {
		this.perCost = perCost;
		this.formatPerCost = DF_DOUBLE.format(perCost);
	}

	public long getSelfCost() {
		return selfCost;
	}

	public void setSelfCost(long selfCost) {
		this.selfCost = selfCost;
		this.formatSelfCost = DF_LONG.format(selfCost);
	}

	public long getDependCost() {
		return dependCost;
	}

	public void setDependCost(long dependCost) {
		this.dependCost = dependCost;
		this.formatDependCost = DF_LONG.format(dependCost);
		double indirectM = (double)dependCost / CostConstants.MACHINE_COST;
		this.indirectMachine = DF_DOUBLE.format(indirectM);
	}

	public long getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(long totalCost) {
		this.totalCost = totalCost;
		this.formatTotalCost = DF_LONG.format(totalCost);
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public String getFPerCost() {
		return this.formatPerCost;
	}

	public void setFPerCost(String fPerCost) {
		this.formatPerCost = fPerCost;
	}

	public String getFormatPerCost() {
		return formatPerCost;
	}

	public void setFormatPerCost(String formatPerCost) {
		this.formatPerCost = formatPerCost;
	}

	public String getFormatSelfCost() {
		return formatSelfCost;
	}

	public void setFormatSelfCost(String formatSelfCost) {
		this.formatSelfCost = formatSelfCost;
	}

	public String getFormatDependCost() {
		return formatDependCost;
	}

	public void setFormatDependCost(String formatDependCost) {
		this.formatDependCost = formatDependCost;
	}

	public String getFormatTotalCost() {
		return formatTotalCost;
	}

	public void setFormatTotalCost(String formatTotalCost) {
		this.formatTotalCost = formatTotalCost;
	}

	public String getFormatPv() {
		return formatPv;
	}

	public void setFormatPv(String formatPv) {
		this.formatPv = formatPv;
	}

	public double getDependPerCost() {
		return dependPerCost;
	}

	public void setDependPerCost(double dependPerCost) {
		this.dependPerCost = dependPerCost;
		this.formatDependPerCost = DF_DOUBLE.format(dependPerCost);
	}

	public String getFormatDependPerCost() {
		return formatDependPerCost;
	}

	public void setFormatDependPerCost(String formatDependPerCost) {
		this.formatDependPerCost = formatDependPerCost;
	}

	public double getTotalPerCost() {
		return totalPerCost;
	}

	public void setTotalPerCost(double totalPerCost) {
		this.totalPerCost = totalPerCost;
		this.formatTotalPerCost = DF_DOUBLE.format(totalPerCost);
	}

	public String getFormatTotalPerCost() {
		return formatTotalPerCost;
	}

	public void setFormatTotalPerCost(String formatTotalPerCost) {
		this.formatTotalPerCost = formatTotalPerCost;
	}

	public String getIndirectMachine() {
		return indirectMachine;
	}

	public void setIndirectMachine(String indirectMachine) {
		this.indirectMachine = indirectMachine;
	}

}
