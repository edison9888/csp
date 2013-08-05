//package com.taobao.monitor.stat.config;
//
//import java.util.Map;
//
//public class HostInfo {
//	
//	private String apacheversion;
//	
//	private String cpunumber;
//	
//	private String hostIp;
//	
//	private String hostName;
//	
//	private String hostSite;
//	
//	private String hostType;
//	
//	private String javaopt;
//	
//	private String javaversion;
//	
//	private String jbossversion;
//	
//	private String memtotal;
//	
//	private String osversion;
//	
//	private String psvalue;
//	
//	private String time ;
//	
//	private String switchPort;
//	
//	private String switchUri;
//	
//	private boolean runStatus;
//	
//	private String appName;
//	
//	
//	public String getAppName() {
//		return appName;
//	}
//
//	public void setAppName(String appName) {
//		this.appName = appName;
//	}
//
//	public boolean isRunStatus() {
//		return runStatus;
//	}
//
//	public void setRunStatus(boolean runStatus) {
//		this.runStatus = runStatus;
//	}
//
//	public String getSwitchPort() {
//		return switchPort;
//	}
//
//	public void setSwitchPort(String switchPort) {
//		this.switchPort = switchPort;
//	}
//
//	public String getSwitchUri() {
//		return switchUri;
//	}
//
//	public void setSwitchUri(String switchUri) {
//		this.switchUri = switchUri;
//	}
//
//	private Map<String,String> switchMap;
//
//	public Map<String, String> getSwitchMap() {
//		return switchMap;
//	}
//
//	public void setSwitchMap(Map<String, String> switchMap) {
//		this.switchMap = switchMap;
//	}
//
//	public String getApacheversion() {
//		return apacheversion;
//	}
//
//	public void setApacheversion(String apacheversion) {
//		this.apacheversion = apacheversion;
//	}
//
//	public String getCpunumber() {
//		return cpunumber;
//	}
//
//	public void setCpunumber(String cpunumber) {
//		this.cpunumber = cpunumber;
//	}
//
//	public String getHostIp() {
//		return hostIp;
//	}
//
//	public void setHostIp(String hostIp) {
//		this.hostIp = hostIp;
//	}
//
//	public String getHostName() {
//		return hostName;
//	}
//
//	public void setHostName(String hostName) {
//		this.hostName = hostName;
//	}
//
//	public String getHostSite() {
//		return hostSite;
//	}
//
//	public void setHostSite(String hostSite) {
//		this.hostSite = hostSite;
//	}
//
//	public String getHostType() {
//		return hostType;
//	}
//
//	public void setHostType(String hostType) {
//		this.hostType = hostType;
//	}
//
//	public String getJavaopt() {
//		return javaopt;
//	}
//
//	public void setJavaopt(String javaopt) {
//		this.javaopt = javaopt;
//	}
//
//	public String getJavaversion() {
//		return javaversion;
//	}
//
//	public void setJavaversion(String javaversion) {
//		this.javaversion = javaversion;
//	}
//
//	public String getJbossversion() {
//		return jbossversion;
//	}
//
//	public void setJbossversion(String jbossversion) {
//		this.jbossversion = jbossversion;
//	}
//
//	public String getMemtotal() {
//		return memtotal;
//	}
//
//	public void setMemtotal(String memtotal) {
//		this.memtotal = memtotal;
//	}
//
//	public String getOsversion() {
//		return osversion;
//	}
//
//	public void setOsversion(String osversion) {
//		this.osversion = osversion;
//	}
//
//	public String getPsvalue() {
//		return psvalue;
//	}
//
//	public void setPsvalue(String psvalue) {
//		this.psvalue = psvalue;
//	}
//
//	public String getTime() {
//		return time;
//	}
//
//	public void setTime(String time) {
//		this.time = time;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if(obj instanceof HostInfo){
//			if(obj != null){
//				HostInfo info = (HostInfo)obj;
//				if(info.getHostIp().equals(this.hostIp)&&info.getAppName().equals(this.appName)){
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//	
//	
//	
//
//}
