package com.taobao.csp.cost.po;

import java.util.List;
import java.util.Map;

/**
 * ��Ӧ�õĽǶȵ�ȫ���ɱ�
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-13
 */
public class AppCostPo {

	//Ӧ������
	private String appName;
	//�����ɱ��������ͻ���
	private List<SimpleHostInfo> hostInfos;
	//�ܵĻ����ɱ�
	private double totalHostCost;
	//�ܵ�hsf�����ɱ�
	private double totalHsfCost;
	//�ܻ��������ɱ�
	private double totalBaseDependCost;	
	//ȫ���ܳɱ�
	private double totalAllCost;	
	//ÿǧ�ε��óɱ�
	private double preThoundCost;	
	//���������ɱ�
	private double directAll;	
	
	
	//���������б�
	private Map<String,List<SimpleCostPo>> baseDependList;
	//��������������ɵ��ܳɱ�
	private Map<String,Double> baseDependCostTotal;
	
	//hsf����
	private List<SimpleCostPo> hsfCost;
	
	//json��ʽ�ı�������
	private String jsonChart;
	
	//�ɱ�����
	private String costTrend;
	//��������
	private String costTrendDay;
	//ռ�����APP�ɱ����б�
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
