package com.taobao.csp.cost.po;

import java.util.List;
import java.util.Map;

/**
 * 单应用的角度的全部成本
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-13
 */
public class AppCostPo {

	//应用名称
	private String appName;
	//机器成本，按类型划分
	private List<SimpleHostInfo> hostInfos;
	//总的机器成本
	private double totalHostCost;
	//总的hsf依赖成本
	private double totalHsfCost;
	//总基础依赖成本
	private double totalBaseDependCost;	
	//全部总成本
	private double totalAllCost;	
	//每千次调用成本
	private double preThoundCost;	
	//基础依赖成本
	private double directAll;	
	
	
	//基础依赖列表
	private Map<String,List<SimpleCostPo>> baseDependList;
	//基础依赖各个组成的总成本
	private Map<String,Double> baseDependCostTotal;
	
	//hsf依赖
	private List<SimpleCostPo> hsfCost;
	
	//json格式的报表数据
	private String jsonChart;
	
	//成本趋势
	private String costTrend;
	//趋势日期
	private String costTrendDay;
	//占用这个APP成本的列表
	private List<CostAppType> costCat;
	
	
	public List<CostAppType> getCostCat() {
		return costCat;
	}
	public void setCostCat(List<CostAppType> costCat) {
		this.costCat = costCat;
	}
	public double getDirectAll() {
		return directAll;
	}
	public void setDirectAll(double directAll) {
		this.directAll = directAll;
	}
	public String getCostTrendDay() {
		return costTrendDay;
	}
	public void setCostTrendDay(String costTrendDay) {
		this.costTrendDay = costTrendDay;
	}
	
	public String getCostTrend() {
		return costTrend;
	}
	public void setCostTrend(String costTrend) {
		this.costTrend = costTrend;
	}
	public String getJsonChart() {
		return jsonChart;
	}
	public void setJsonChart(String jsonChart) {
		this.jsonChart = jsonChart;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public List<SimpleHostInfo> getHostInfos() {
		return hostInfos;
	}
	public void setHostInfos(List<SimpleHostInfo> hostInfos) {
		this.hostInfos = hostInfos;
	}
	public double getTotalHostCost() {
		return totalHostCost;
	}
	public void setTotalHostCost(double totalHostCost) {
		this.totalHostCost = totalHostCost;
	}
	public double getTotalHsfCost() {
		return totalHsfCost;
	}
	public void setTotalHsfCost(double totalHsfCost) {
		this.totalHsfCost = totalHsfCost;
	}
	public double getTotalBaseDependCost() {
		return totalBaseDependCost;
	}
	public void setTotalBaseDependCost(double totalBaseDependCost) {
		this.totalBaseDependCost = totalBaseDependCost;
	}
	public Map<String, List<SimpleCostPo>> getBaseDependList() {
		return baseDependList;
	}
	public void setBaseDependList(Map<String, List<SimpleCostPo>> baseDependList) {
		this.baseDependList = baseDependList;
	}
	public double getTotalAllCost() {
		return totalAllCost;
	}
	public void setTotalAllCost(double totalAllCost) {
		this.totalAllCost = totalAllCost;
	}
	public Map<String, Double> getBaseDependCostTotal() {
		return baseDependCostTotal;
	}
	public void setBaseDependCostTotal(Map<String, Double> baseDependCostTotal) {
		this.baseDependCostTotal = baseDependCostTotal;
	}
	public List<SimpleCostPo> getHsfCost() {
		return hsfCost;
	}
	public void setHsfCost(List<SimpleCostPo> hsfCost) {
		this.hsfCost = hsfCost;
	}
	public double getPreThoundCost() {
		return preThoundCost;
	}
	public void setPreThoundCost(double preThoundCost) {
		this.preThoundCost = preThoundCost;
	}
	
}
