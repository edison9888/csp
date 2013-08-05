package com.taobao.csp.depend.po.hsf;

public class HsfProvideMachine implements Comparable<HsfProvideMachine>{
	
	private String providerMachineIp;
	
	private String providerMachineCm;
	
	private long callnum;



	public String getProviderMachineIp() {
		return providerMachineIp;
	}

	public void setProviderMachineIp(String providerMachineIp) {
		this.providerMachineIp = providerMachineIp;
	}

	public String getProviderMachineCm() {
		return providerMachineCm;
	}

	public void setProviderMachineCm(String providerMachineCm) {
		this.providerMachineCm = providerMachineCm;
	}

	public long getCallnum() {
		return callnum;
	}

	public void setCallnum(long callnum) {
		this.callnum = callnum;
	}

	@Override
	public int compareTo(HsfProvideMachine o) {
		if(o.getCallnum()<getCallnum()){
			return -1;
		}else if(o.getCallnum()>getCallnum()){
			return 1;
		}
		return 0;
	}
	
	

}
