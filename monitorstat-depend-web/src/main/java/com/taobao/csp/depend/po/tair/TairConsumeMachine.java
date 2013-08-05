package com.taobao.csp.depend.po.tair;

public class TairConsumeMachine implements Comparable<TairConsumeMachine>{
	
	private String consumeMachineIp;
	
	private String consumeMachineCm;
	
	private String actionType;
	
	private long callnum;
	
	private long calltime;
	
	public String getConsumeMachineIp() {
		return consumeMachineIp;
	}

	public void setConsumeMachineIp(String consumeMachineIp) {
		this.consumeMachineIp = consumeMachineIp;
	}

	public String getConsumeMachineCm() {
		return consumeMachineCm;
	}

	public void setConsumeMachineCm(String consumeMachineCm) {
		this.consumeMachineCm = consumeMachineCm;
	}

	public long getCallnum() {
		return callnum;
	}

	public void setCallnum(long callnum) {
		this.callnum = callnum;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public long getCalltime() {
		return calltime;
	}

	public void setCalltime(long calltime) {
		this.calltime = calltime;
	}

	@Override
	public int compareTo(TairConsumeMachine o) {
		if(o.getCallnum()<getCallnum()){
			return -1;
		}else if(o.getCallnum()>getCallnum()){
			return 1;
		}
		return 0;
	}
	
	

}
