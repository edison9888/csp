package com.taobao.sentinel.po;

public class FlowControlDependencyPo extends BaseConfigPo {
	
	private int limitFlow;
	
	private String interfaceInfo;

	public int getLimitFlow() {
		return limitFlow;
	}

	public void setLimitFlow(int limitFlow) {
		this.limitFlow = limitFlow;
	}

	public String getInterfaceInfo() {
		return interfaceInfo;
	}

	public void setInterfaceInfo(String interfaceInfo) {
		this.interfaceInfo = interfaceInfo;
	}
}
