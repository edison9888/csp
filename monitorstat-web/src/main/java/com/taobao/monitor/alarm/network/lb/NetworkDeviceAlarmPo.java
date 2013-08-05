package com.taobao.monitor.alarm.network.lb;

public class NetworkDeviceAlarmPo {
	private String hostName;
	private String serviceName;
	private String output;
	private String state;
	private String time;
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "NetworkDeviceAlarmPo [hostName=" + hostName + ", serviceName="
				+ serviceName + ", output=" + output + ", state=" + state
				+ ", time=" + time + "]";
	}
	
	

}
