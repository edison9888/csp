package com.taobao.csp.depend.po.hsf;

import java.util.HashMap;
import java.util.Map;

public class AppSummary implements Comparable<AppSummary>{
	
	private String opsName;
	
	private long callAllNum;
	
	private long preCallAllNum;
	
	private String option = "same";//add sub same
	
	
	private Map<String,AppSummary> siteMap = new HashMap<String, AppSummary>();

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

	public long getPreCallAllNum() {
		return preCallAllNum;
	}

	public void setPreCallAllNum(long preCallAllNum) {
		this.preCallAllNum = preCallAllNum;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
	
	
	public Map<String, AppSummary> getSiteMap() {
		return siteMap;
	}

	public void setSiteMap(Map<String, AppSummary> siteMap) {
		this.siteMap = siteMap;
	}

	@Override
	public boolean equals(Object obj) {
		AppSummary g = (AppSummary)obj;
		return g.getOpsName().equals(opsName);
	}

	@Override
	public int compareTo(AppSummary o) {
		
		if(o.getCallAllNum() >getCallAllNum()){
			return 1;
		}else if(o.getCallAllNum() ==getCallAllNum()){
			return 0;
		}else{
			return -1;
		}
	}
	

}
