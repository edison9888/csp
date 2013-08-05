package com.taobao.monitor.alarm.trade.realtime.po;

import java.util.Date;

public class RealTimeTradePo {
	private int c2cCreateCnt;
	private int b2cCreateCnt;
	private int jhsCreateCnt;
	private String c2cCreateSum;
	private String b2cCreateSum;
	private String jhsCreateSum;

	private int c2cPaidCnt;
	private int b2cPaidCnt;
	private int jhsPaidCnt;
	private String c2cPaidSum;
	private String b2cPaidSum;
	private String jhsPaidSum;
	private String paidSum;
	private String createSum;
	private Date time;
	
	public int getC2cCreateCnt() {
		return c2cCreateCnt;
	}
	public void setC2cCreateCnt(int c2cCreateCnt) {
		this.c2cCreateCnt = c2cCreateCnt;
	}
	public int getB2cCreateCnt() {
		return b2cCreateCnt;
	}
	public void setB2cCreateCnt(int b2cCreateCnt) {
		this.b2cCreateCnt = b2cCreateCnt;
	}
	public int getC2cPaidCnt() {
		return c2cPaidCnt;
	}
	public void setC2cPaidCnt(int c2cPaidCnt) {
		this.c2cPaidCnt = c2cPaidCnt;
	}
	public int getB2cPaidCnt() {
		return b2cPaidCnt;
	}
	public void setB2cPaidCnt(int b2cPaidCnt) {
		this.b2cPaidCnt = b2cPaidCnt;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}

	public int getJhsPaidCnt() {
		return jhsPaidCnt;
	}
	public void setJhsPaidCnt(int jhsPaidCnt) {
		this.jhsPaidCnt = jhsPaidCnt;
	}
	public int getJhsCreateCnt() {
		return jhsCreateCnt;
	}
	public void setJhsCreateCnt(int jhsCreateCnt) {
		this.jhsCreateCnt = jhsCreateCnt;
	}
	public String getC2cCreateSum() {
		return c2cCreateSum;
	}
	public void setC2cCreateSum(String c2cCreateSum) {
		this.c2cCreateSum = c2cCreateSum;
	}
	public String getB2cCreateSum() {
		return b2cCreateSum;
	}
	public void setB2cCreateSum(String b2cCreateSum) {
		this.b2cCreateSum = b2cCreateSum;
	}
	public String getJhsCreateSum() {
		return jhsCreateSum;
	}
	public void setJhsCreateSum(String jhsCreateSum) {
		this.jhsCreateSum = jhsCreateSum;
	}
	public String getC2cPaidSum() {
		return c2cPaidSum;
	}
	public void setC2cPaidSum(String c2cPaidSum) {
		this.c2cPaidSum = c2cPaidSum;
	}
	public String getB2cPaidSum() {
		return b2cPaidSum;
	}
	public void setB2cPaidSum(String b2cPaidSum) {
		this.b2cPaidSum = b2cPaidSum;
	}
	public String getJhsPaidSum() {
		return jhsPaidSum;
	}
	public void setJhsPaidSum(String jhsPaidSum) {
		this.jhsPaidSum = jhsPaidSum;
	}

	@Override
	public String toString() {
		return "RealTimeTradePo [c2cCreateCnt=" + c2cCreateCnt
				+ ", b2cCreateCnt=" + b2cCreateCnt + ", c2cPaidCnt="+ c2cPaidCnt + "c2cCreateSum"+c2cCreateSum + ",jhsPaidCnt" + jhsPaidCnt + "c2cCreateSum" + c2cCreateSum 
				+ ", b2cPaidCnt=" + b2cPaidCnt + "jhsCreateCnt=" + jhsCreateCnt + ", time=" + time
				+ "]";
	}
	public String getCreateSum() {
		return createSum;
	}
	public void setCreateSum(String createSum) {
		this.createSum = createSum;
	}
	public String getPaidSum() {
		return paidSum;
	}
	public void setPaidSum(String paidSum) {
		this.paidSum = paidSum;
	}
}
