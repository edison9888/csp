
package com.taobao.monitor.web.distrib;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2010-11-8 ÏÂÎç04:49:25
 */
public class DistribFlowPo {
	
	private String providerApp;
	
	private String keyName;
	
	private String customerApp;
	
	private String machine_ip;
	
	private String machine_cm;
	
	private long callNum;
	
	private double useTime;
	
	private Date collectDay;
	
	
	

	@Override
	public boolean equals(Object obj) {
		try{
			if(obj instanceof DistribFlowPo){
				DistribFlowPo po = (DistribFlowPo)obj;
				if(this.providerApp.equals(po.providerApp)
						&&keyName.equals(po.keyName)
						&&customerApp.equals(po.customerApp)
						&&machine_ip.equals(po.machine_ip)
						&&machine_cm.equals(po.machine_cm)&&collectDay.equals(po.collectDay)){
					return true;
				}
				
			}
		}catch (Exception e) {
		}
		return false;
	}

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

	public String getMachine_ip() {
		return machine_ip;
	}

	public void setMachine_ip(String machine_ip) {
		this.machine_ip = machine_ip;
	}

	public String getMachine_cm() {
		return machine_cm;
	}

	public void setMachine_cm(String machine_cm) {
		this.machine_cm = machine_cm;
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
