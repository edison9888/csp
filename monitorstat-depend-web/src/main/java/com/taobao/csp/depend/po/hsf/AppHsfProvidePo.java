package com.taobao.csp.depend.po.hsf;

import java.util.Date;

public class AppHsfProvidePo {
	
	private String providerApp;
	
	private String keyName;
	
	private String customerApp;
	
	private String provideMachineIp;
	
	private String provideMachineCm;
	
	private long callNum;
	
	private double useTime;
	
	private Date collectDay;

	public String getProviderApp() {
		return providerApp;
	}

	public void setProviderApp(String providerApp) {
		this.providerApp = providerApp;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getCustomerApp() {
		return customerApp;
	}

	public void setCustomerApp(String customerApp) {
		this.customerApp = customerApp;
	}

	public String getProvideMachineIp() {
		return provideMachineIp;
	}

	public void setProvideMachineIp(String provideMachineIp) {
		this.provideMachineIp = provideMachineIp;
	}

	public String getProvideMachineCm() {
		return provideMachineCm;
	}

	public void setProvideMachineCm(String provideMachineCm) {
		this.provideMachineCm = provideMachineCm;
	}

	public long getCallNum() {
		return callNum;
	}

	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}

	public double getUseTime() {
		return useTime;
	}

	public void setUseTime(double useTime) {
		this.useTime = useTime;
	}

	public Date getCollectDay() {
		return collectDay;
	}

	public void setCollectDay(Date collectDay) {
		this.collectDay = collectDay;
	}
	
	

}
