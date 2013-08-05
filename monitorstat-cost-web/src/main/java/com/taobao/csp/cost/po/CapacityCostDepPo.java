package com.taobao.csp.cost.po;

import java.util.Date;

import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.util.DepMode;
import com.taobao.monitor.common.util.Utlitites;

public class CapacityCostDepPo extends BasePo {
	
	private String appName;
	
	private String depAppName;
	
	private CostType depType;
	
	private DepMode depMode;
	
	private long depPv;
	private String formatDepPv;  // for display
	
	private long depCost;
	private String formatDepCost;  // for display
	
	private Date collectTime;
	
	/*** fields below are not persistent ***/
	
	private int depAppMachineNum;
	
	private long depAppTotalPv;
	private String formatDepAppTotalPv;  // for display
	
	private long depAppSelfCost;
	private String formatDepAppSelfCost;  // for display
	
	private double depAppPerCost;
	private String formatDepAppPerCost;  // for display
	
	private double perDepCost;
	private String formatPerDepCost;   // for display

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDepAppName() {
		return depAppName;
	}

	public void setDepAppName(String depAppName) {
		this.depAppName = depAppName;
	}

	public CostType getDepType() {
		return depType;
	}

	public void setDepType(CostType depType) {
		this.depType = depType;
	}

	public DepMode getDepMode() {
		return depMode;
	}

	public void setDepMode(DepMode depMode) {
		this.depMode = depMode;
	}

	public long getDepPv() {
		return depPv;
	}

	public void setDepPv(long depPv) {
		this.depPv = depPv;
		this.formatDepPv = Utlitites.fromatLong(String.valueOf(depPv));
	}

	public long getDepCost() {
		return depCost;
	}

	public void setDepCost(long depCost) {
		this.depCost = depCost;
		this.formatDepCost = Utlitites.fromatLong(String.valueOf(depCost));
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public int getDepAppMachineNum() {
		return depAppMachineNum;
	}

	public void setDepAppMachineNum(int depAppMachineNum) {
		this.depAppMachineNum = depAppMachineNum;
	}

	public long getDepAppTotalPv() {
		return depAppTotalPv;
	}

	public void setDepAppTotalPv(long depAppTotalPv) {
		this.depAppTotalPv = depAppTotalPv;
		this.formatDepAppTotalPv = Utlitites.fromatLong(String.valueOf(depAppTotalPv));
	}

	public long getDepAppSelfCost() {
		return depAppSelfCost;
	}

	public void setDepAppSelfCost(long depAppSelfCost) {
		this.depAppSelfCost = depAppSelfCost;
		this.formatDepAppSelfCost = Utlitites.fromatLong(String.valueOf(depAppSelfCost));
	}

	public double getDepAppPerCost() {
		return depAppPerCost;
	}

	public void setDepAppPerCost(double depAppPerCost) {
		this.depAppPerCost = depAppPerCost;
		this.formatDepAppPerCost = DF_DOUBLE.format(depAppPerCost);
	}

	public String getFormatDepPv() {
		return formatDepPv;
	}

	public void setFormatDepPv(String formatDepPv) {
		this.formatDepPv = formatDepPv;
	}

	public String getFormatDepCost() {
		return formatDepCost;
	}

	public void setFormatDepCost(String formatDepCost) {
		this.formatDepCost = formatDepCost;
	}

	public String getFormatDepAppTotalPv() {
		return formatDepAppTotalPv;
	}

	public void setFormatDepAppTotalPv(String formatDepAppTotalPv) {
		this.formatDepAppTotalPv = formatDepAppTotalPv;
	}

	public String getFormatDepAppSelfCost() {
		return formatDepAppSelfCost;
	}

	public void setFormatDepAppSelfCost(String formatDepAppSelfCost) {
		this.formatDepAppSelfCost = formatDepAppSelfCost;
	}

	public String getFormatDepAppPerCost() {
		return formatDepAppPerCost;
	}

	public void setFormatDepAppPerCost(String formatDepAppPerCost) {
		this.formatDepAppPerCost = formatDepAppPerCost;
	}
	
	public double getPerDepCost() {
		return perDepCost;
	}

	public void setPerDepCost(double perDepCost) {
		this.perDepCost = perDepCost;
		this.formatPerDepCost = DF_DOUBLE_4.format(perDepCost);
	}

	public String getFormatPerDepCost() {
		return formatPerDepCost;
	}

	public void setFormatPerDepCost(String formatPerDepCost) {
		this.formatPerDepCost = formatPerDepCost;
	}
	
}
