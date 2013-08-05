package com.taobao.csp.cost.po;

import java.util.Date;

import com.taobao.csp.cost.service.day.CostType;

/**
 * Ӧ�õĸ��������ɱ��Լ��ܳɱ�
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-19
 */
public class CostAppType {

	//Ӧ����
	private String appName;
	//���ͣ�All|Direct_Cost��
	private String costType;
	//������
	private String costName;
	//���ô���
	private long callNum;
	//�����ɱ���ֱ�������ɱ��������ܳɱ���
	private double dependCost;
	//�ռ�ʱ��
	private Date collectTime;
	//��˾
	private String groupName="";
	//��Ʒ��
	private String line="";
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public long getCallNum() {
		return callNum;
	}
	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}
	
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public double getDependCost() {
		return dependCost;
	}
	public void setDependCost(double dependCost) {
		this.dependCost = dependCost;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
}
