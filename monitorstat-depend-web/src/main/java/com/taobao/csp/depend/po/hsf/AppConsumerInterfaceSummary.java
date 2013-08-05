package com.taobao.csp.depend.po.hsf;


public class AppConsumerInterfaceSummary {
	
	private String interfaceName;
	
	private String consumeSiteName;
	
	private long callAllNum;
	
	private String providerAppName;
	
	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public long getCallAllNum() {
		return callAllNum;
	}

	public void setCallAllNum(long callAllNum) {
		this.callAllNum = callAllNum;
	}

	public String getConsumeSiteName() {
		return consumeSiteName;
	}

	public void setConsumeSiteName(String consumeSiteName) {
		this.consumeSiteName = consumeSiteName;
	}

	public String getProviderAppName() {
		return providerAppName;
	}

	public void setProviderAppName(String providerAppName) {
		this.providerAppName = providerAppName;
	}
}
