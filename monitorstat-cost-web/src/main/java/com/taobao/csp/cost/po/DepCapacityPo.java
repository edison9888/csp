package com.taobao.csp.cost.po;

import java.util.Date;
import java.util.Map;

/***
 * 依赖容量数据的对象类
 * 与存储相对应
 * @author youji.zj 2011的最后一天
 *
 */
public class DepCapacityPo {
	
	/*** 应用名 ***/
	private String providerApp;
	
	/***依赖本应用的应用名***/
	private String consumerApp;
	
	/***分组信息***/
	private String providerGroup;
	
	/*** 依赖qps ***/
	private double depQps;
	
	private String roomQps;

	private long callSum;
	
	/*** 时间，精确到天***/
	private Date collectTime;
	//消费者角度的成本（调用次数/总次数）*应用总成本
	private double consumer_cost;
	
	/*******************************************************************/
	/*** 分机房依赖qps***/
	private Map<String, Double> roomQpsMap;
	
	/***对应日期应用qps 该字段不从依赖系统取数***/
	private double qps;
	
	
	public DepCapacityPo() {}
	
	public long getCallSum() {
		return callSum;
	}

	public void setCallSum(long callSum) {
		this.callSum = callSum;
	}

	/***
	 * 构造依赖容量数据对象
	 * @param providerApp 被依赖的应用名
	 * @param consumerApp 依赖的应用名
	 * @param depQps 依赖qps
	 * @param roomQps 分机房依赖qps
	 * @param collectTime 采集时间，精确到天
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
