
package com.taobao.monitor.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.taobao.monitor.common.util.Utlitites;

/**
 * 
 * @author xiaodu
 * @version 2010-4-15 ÏÂÎç03:34:15
 */
public class PingPo {
	
	private String appName;
	
	private String hostSize;
	
	private String averageTime;
	
	private String lossPacket=" ";	
	private List<String> lossHostList = new ArrayList<String>();
	
	public String getLostHostIp(){
		
		if(lossHostList.size()==0)return " - ";
		
		StringBuilder sb = new StringBuilder();
		for(String ip:lossHostList){
			sb.append(ip);			
		}
		
		return Utlitites.formatHtmlStr(sb.toString(), 20);
	}
	
	
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getHostSize() {
		return hostSize;
	}
	public void setHostSize(String hostSize) {
		this.hostSize = hostSize;
	}
	public String getAverageTime() {
		return averageTime;
	}
	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}
	public String getLossPacket() {
		return lossPacket;
	}
	public void setLossPacket(String lossPacket) {
		this.lossPacket = lossPacket;
	}
	public List<String> getLossHostList() {
		return lossHostList;
	}
	public void setLossHostList(List<String> lossHostList) {
		this.lossHostList = lossHostList;
	}
	
	
	
	

}
