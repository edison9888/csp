package com.taobao.csp.cost.po;

import java.util.Date;
import java.util.Map;

/***
 * �����������ݵĶ�����
 * ��洢���Ӧ
 * @author youji.zj 2011�����һ��
 *
 */
public class DepCapacityPo {
	
	/*** Ӧ���� ***/
	private String providerApp;
	
	/***������Ӧ�õ�Ӧ����***/
	private String consumerApp;
	
	/***������Ϣ***/
	private String providerGroup;
	
	/*** ����qps ***/
	private double depQps;
	
	private String roomQps;

	private long callSum;
	
	/*** ʱ�䣬��ȷ����***/
	private Date collectTime;
	//�����߽Ƕȵĳɱ������ô���/�ܴ�����*Ӧ���ܳɱ�
	private double consumer_cost;
	
	/*******************************************************************/
	/*** �ֻ�������qps***/
	private Map<String, Double> roomQpsMap;
	
	/***��Ӧ����Ӧ��qps ���ֶβ�������ϵͳȡ��***/
	private double qps;
	
	
	public DepCapacityPo() {}
	
	public long getCallSum() {
		return callSum;
	}

	public void setCallSum(long callSum) {
		this.callSum = callSum;
	}

	/***
	 * ���������������ݶ���
	 * @param providerApp ��������Ӧ����
	 * @param consumerApp ������Ӧ����
	 * @param depQps ����qps
	 * @param roomQps �ֻ�������qps
	 * @param collectTime �ɼ�ʱ�䣬��ȷ����
	 */
	public DepCapacityPo(String providerApp, String consumerApp, String providerGroup,
			double depQps, Map<String, Double> roomQps, Date collectTime) {
		this.providerApp = providerApp;
		this.consumerApp = consumerApp;
		this.providerGroup = providerGroup;
		this.depQps = depQps;
		this.roomQpsMap = roomQps;
		this.collectTime = collectTime;
	}

	public double getQps() {
		return qps;
	}

	public void setQps(double qps) {
		this.qps = qps;
	}

	public String getProviderApp() {
		return providerApp;
	}

	public double getConsumer_cost() {
		return consumer_cost;
	}

	public void setConsumer_cost(double consumer_cost) {
		this.consumer_cost = consumer_cost;
	}

	public void setProviderApp(String providerApp) {
		this.providerApp = providerApp;
	}

	public String getConsumerApp() {
		return consumerApp;
	}

	public void setConsumerApp(String consumerApp) {
		this.consumerApp = consumerApp;
	}

	public String getProviderGroup() {
		return providerGroup;
	}

	public void setProviderGroup(String providerGroup) {
		this.providerGroup = providerGroup;
	}

	public double getDepQps() {
		return depQps;
	}

	public void setDepQps(double depQps) {
		this.depQps = depQps;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public Map<String, Double> getRoomQpsMap() {
		return roomQpsMap;
	}

	public void setRoomQpsMap(Map<String, Double> roomQpsMap) {
		this.roomQpsMap = roomQpsMap;
	}

	public String getRoomQps() {
		return roomQps;
	}

	public void setRoomQps(String roomQps) {
		this.roomQps = roomQps;
	}
	
}
