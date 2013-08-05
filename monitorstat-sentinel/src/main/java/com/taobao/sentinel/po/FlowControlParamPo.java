package com.taobao.sentinel.po;

public class FlowControlParamPo extends BaseConfigPo {
	
	private String paramInfo;
	
	private int limitFlow;

	public String getParamInfo() {
		return paramInfo;
	}

	public void setParamInfo(String paramInfo) {
		this.paramInfo = paramInfo;
	}

	public int getLimitFlow() {
		return limitFlow;
	}

	public void setLimitFlow(int limitFlow) {
		this.limitFlow = limitFlow;
	}

}
