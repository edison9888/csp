package com.taobao.sentinel.po;

public class FlowControlInterfacePo extends BaseConfigPo {
	
	private String interfaceInfo;
	
	private int limitFlow;
	
	private String strategy;

	public String getInterfaceInfo() {
		return interfaceInfo;
	}

	public void setInterfaceInfo(String interfaceInfo) {
		this.interfaceInfo = interfaceInfo;
	}

	public int getLimitFlow() {
		return limitFlow;
	}

	public void setLimitFlow(int limitFlow) {
		this.limitFlow = limitFlow;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

}
