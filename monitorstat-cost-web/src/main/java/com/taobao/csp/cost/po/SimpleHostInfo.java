package com.taobao.csp.cost.po;

/**
 * ��host��Ϣ
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-8-31
 */
public class SimpleHostInfo extends BasePo{
	private String macName;//��������
	
	private String hostType; //�Ƿ��������  vm ��ʾ�����
	
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
