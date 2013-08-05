package com.taobao.csp.depend.po.hsf;

import java.util.Date;

public class AppProviderSummary implements Comparable<AppProviderSummary>{
	
	private String opsName;
	
	private String consumeSiteName;
	
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

	public String getConsumeSiteName() {
		return consumeSiteName;
	}

	public void setConsumeSiteName(String consumeSiteName) {
		this.consumeSiteName = consumeSiteName;
	}

	@Override
	public boolean equals(Object obj) {
		AppProviderSummary g = (AppProviderSummary)obj;
		return g.getOpsName().equals(opsName);
	}

  @Override
  public int compareTo(AppProviderSummary o) {
    if(o.getCallAllNum()<getCallAllNum()){
      return -1;
    }else if(o.getCallAllNum()>getCallAllNum()){
      return 1;
    }
    return 0; 
  }
	
	
	
	
	

}
