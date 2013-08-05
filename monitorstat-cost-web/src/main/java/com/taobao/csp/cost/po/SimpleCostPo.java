package com.taobao.csp.cost.po;

/**
 * һЩ���������ĳɱ���
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-13
 */
public class SimpleCostPo {

	//�������ͣ�TDDL/TFS/TAIR��
	private String dependType;
	//������
	private String dependName;
	//���ô���
	private long callNum;
	//�����߳ɱ�
	private double consumerCost;
	
	public SimpleCostPo(String dependType){
		this.dependType=dependType;
	}
	
	public String getDependType() {
		return dependType;
	}
	public void setDependType(String dependType) {
		this.dependType = dependType;
	}
	public String getDependName() {
		return dependName;
	}
	public void setDependName(String dependName) {
		this.dependName = dependName;
	}
	public long getCallNum() {
		return callNum;
	}
	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}
	public double getConsumerCost() {
		return consumerCost;
	}
	public void setConsumerCost(double consumerCost) {
		this.consumerCost = consumerCost;
	}
	
	
}
