package com.taobao.csp.capacity.po;

import java.util.Date;


public class DomainPvPo {
	
	private Date creatTime;
	
	private String domain;
	
	private double allPv;
	
	private double memberPv;
	
	private double visitPv;

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public double getAllPv() {
		return allPv;
	}

	public void setAllPv(double allPv) {
		this.allPv = allPv;
	}

	public double getMemberPv() {
		return memberPv;
	}

	public void setMemberPv(double memberPv) {
		this.memberPv = memberPv;
	}

	public double getVisitPv() {
		return visitPv;
	}

	public void setVisitPv(double visitPv) {
		this.visitPv = visitPv;
	}
}
