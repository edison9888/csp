package com.taobao.csp.depend.po.hsf;

import java.util.Date;

public class AppConsumerSummary {
	
	private String opsName;
	
	private String provideSiteName;
	
	private long callAllNum;
	
	private Date collectDate;

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public long getCallAllNum() {
		return callAllNum;
	}

	public void setCallAllNum(long callAllNum) {
		this.callAllNum = callAllNum;
	}

	public Date getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}

	public String getProvideSiteName() {
		return provideSiteName;
	}

	public void setProvideSiteName(String provideSiteName) {
		this.provideSiteName = provideSiteName;
	}

	@Override
	public boolean equals(Object obj) {
		AppConsumerSummary g = (AppConsumerSummary)obj;
		return g.getOpsName().equals(opsName);
	}
	
	
	
	
	

}
