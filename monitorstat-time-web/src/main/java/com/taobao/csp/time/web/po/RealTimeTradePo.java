package com.taobao.csp.time.web.po;

import java.util.Date;

public class RealTimeTradePo {
	private int c2cCreateCnt;
	private int b2cCreateCnt;
	
	private int c2cPaidCnt;
	private int b2cPaidCnt;
	
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
	@Override
	public String toString() {
		return "RealTimeTradePo [c2cCreateCnt=" + c2cCreateCnt
				+ ", b2cCreateCnt=" + b2cCreateCnt + ", c2cPaidCnt="
				+ c2cPaidCnt + ", b2cPaidCnt=" + b2cPaidCnt + ", time=" + time
				+ "]";
	}

	

}
