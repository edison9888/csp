package com.taobao.sentinel.po;

public class FlowControlAppPo extends BaseConfigPo {
	
	private int limitFlow;

	public int getLimitFlow() {
		return limitFlow;
	}
	
	private String strategy;

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
