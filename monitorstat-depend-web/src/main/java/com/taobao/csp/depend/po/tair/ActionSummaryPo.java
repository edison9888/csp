package com.taobao.csp.depend.po.tair;

public class ActionSummaryPo implements Comparable<ActionSummaryPo>{
	private String actionName;
	private long callAllNum;
	private long preCallAllNum;
	private String option = "same";//add sub same
	
	
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
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
	public int compareTo(ActionSummaryPo o) {
		if(o.getCallAllNum() < getCallAllNum()){
			return -1;
		}else if(o.getCallAllNum() > getCallAllNum()){
			return 1;
		}
		return 0;
	}
}
