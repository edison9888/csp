package com.taobao.csp.cost.po;

/**
 * 简单host信息
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-8-31
 */
public class SimpleHostInfo extends BasePo{
	private String macName;//机器类型
	
	private String hostType; //是否是虚拟机  vm 表示虚拟机
	
	private int hostNum=1;
	
	private double totalPrice;

	public String getHostType() {
		return hostType;
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

	public int getHostNum() {
		return hostNum;
	}

	public void setHostNum(int hostNum) {
		this.hostNum = hostNum;
	}

	public String getMacName() {
		return macName;
	}

	public void setMacName(String macName) {
		this.macName = macName;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void addTotalPrice(double totalPrice) {
		this.totalPrice+= totalPrice;
	}
}
