package com.taobao.monitor.common.vo;

import java.util.HashSet;
import java.util.Set;
/**
 * 在统计告警用户信息的时候用到
 *
 */
public class UserAcceptCount implements Comparable<UserAcceptCount>{
	
	private String wwName;
	
	private int allAlarmNum;
	
	private int phoneAlarmNum;
	
	private int phoneSendNum;
	
	private int wwAlarmNum;
	
	private int wwSendNum;
	
	
	
	
	public int getPhoneSendNum() {
		return phoneSendNum;
	}

	public void setPhoneSendNum(int phoneSendNum) {
		this.phoneSendNum = phoneSendNum;
	}

	public int getWwSendNum() {
		return wwSendNum;
	}

	public void setWwSendNum(int wwSendNum) {
		this.wwSendNum = wwSendNum;
	}


	private Set<Integer> appSet = new HashSet<Integer>();
	

	public String getWwName() {
		return wwName;
	}

	public void setWwName(String wwName) {
		this.wwName = wwName;
	}

	public int getAllAlarmNum() {
		return allAlarmNum;
	}

	public void setAllAlarmNum(int allAlarmNum) {
		this.allAlarmNum = allAlarmNum;
	}

	public int getPhoneAlarmNum() {
		return phoneAlarmNum;
	}

	public void setPhoneAlarmNum(int phoneAlarmNum) {
		this.phoneAlarmNum = phoneAlarmNum;
	}

	public int getWwAlarmNum() {
		return wwAlarmNum;
	}

	public void setWwAlarmNum(int wwAlarmNum) {
		this.wwAlarmNum = wwAlarmNum;
	}

	public Set<Integer> getAppSet() {
		return appSet;
	}

	public void setAppSet(Set<Integer> appSet) {
		this.appSet = appSet;
	}

	
	public int compareTo(UserAcceptCount o) {
		
		if(o.getAllAlarmNum()>this.getAllAlarmNum()){
			return 1;
		}else if(o.getAllAlarmNum()<this.getAllAlarmNum()){
			return -1;
		}
		
		return 0;
	}


	

}
