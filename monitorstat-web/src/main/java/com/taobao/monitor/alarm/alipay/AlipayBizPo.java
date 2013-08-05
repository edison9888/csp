package com.taobao.monitor.alarm.alipay;

import java.util.Date;

/**
 * 支付宝业务数据的Domain
 * @author baiyan
 *
 */
public class AlipayBizPo {
	private int createTotal;
	private int createSuccess;
	private float createTime;
	
	private int payTotal;
	private int paySuccess;
	private float payTime;
	
	private int sendTotal;
	private int sendSuccess;
	private float sendTime;
	
	private int confirmTotal;
	private int confirmSuccess;
	private float confirmTime;
	
	private Date time;

	public int getCreateTotal() {
		return createTotal;
	}

	public void setCreateTotal(int createTotal) {
		this.createTotal = createTotal;
	}

	public int getCreateSuccess() {
		return createSuccess;
	}

	public void setCreateSuccess(int createSuccess) {
		this.createSuccess = createSuccess;
	}

	public float getCreateTime() {
		return createTime;
	}

	public void setCreateTime(float createTime) {
		this.createTime = createTime;
	}

	public int getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(int payTotal) {
		this.payTotal = payTotal;
	}

	public int getPaySuccess() {
		return paySuccess;
	}

	public void setPaySuccess(int paySuccess) {
		this.paySuccess = paySuccess;
	}

	public float getPayTime() {
		return payTime;
	}

	public void setPayTime(float payTime) {
		this.payTime = payTime;
	}

	public int getSendTotal() {
		return sendTotal;
	}

	public void setSendTotal(int sendTotal) {
		this.sendTotal = sendTotal;
	}

	public int getSendSuccess() {
		return sendSuccess;
	}

	public void setSendSuccess(int sendSuccess) {
		this.sendSuccess = sendSuccess;
	}

	public float getSendTime() {
		return sendTime;
	}

	public void setSendTime(float sendTime) {
		this.sendTime = sendTime;
	}

	public int getConfirmTotal() {
		return confirmTotal;
	}

	public void setConfirmTotal(int confirmTotal) {
		this.confirmTotal = confirmTotal;
	}

	public int getConfirmSuccess() {
		return confirmSuccess;
	}

	public void setConfirmSuccess(int confirmSuccess) {
		this.confirmSuccess = confirmSuccess;
	}

	public float getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(float confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "AlipayBizDo [createTotal=" + createTotal + ", createSuccess="
				+ createSuccess + ", createTime=" + createTime + ", payTotal="
				+ payTotal + ", paySuccess=" + paySuccess + ", payTime="
				+ payTime + ", sendTotal=" + sendTotal + ", sendSuccess="
				+ sendSuccess + ", sendTime=" + sendTime + ", confirmTotal="
				+ confirmTotal + ", confirmSuccess=" + confirmSuccess
				+ ", confirmTime=" + confirmTime + ", time=" + time + "]";
	}
	
	
}
