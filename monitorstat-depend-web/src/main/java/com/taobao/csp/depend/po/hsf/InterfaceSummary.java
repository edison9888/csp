package com.taobao.csp.depend.po.hsf;

import java.util.HashMap;
import java.util.Map;

public class InterfaceSummary implements Comparable<InterfaceSummary>{
	
	private String name;	//interfacename
	
	private String keyName;	
	
	private long callAllNum;
	
	private long preCallAllNum;
	
	private String appName;	//provider name or consumer name ,depends on cases 
	
	private String option = "same";//add sub same	
	
	private Map<String,InterfaceSummary> siteMap = new HashMap<String, InterfaceSummary>();


	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
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

	@Override
	public int compareTo(InterfaceSummary o) {
		
		if(o.getCallAllNum()<getCallAllNum()){
			return -1;
		}else if(o.getCallAllNum()>getCallAllNum()){
			return 1;
		}
		return 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, InterfaceSummary> getSiteMap() {
		return siteMap;
	}

	public void setSiteMap(Map<String, InterfaceSummary> siteMap) {
		this.siteMap = siteMap;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}
