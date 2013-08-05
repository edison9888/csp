package com.taobao.csp.day.base;

public class HostInfo {
	
	private String ip;
	
	private String room;
	
	private String appGroup;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getAppGroup() {
		return appGroup;
	}

	public void setAppGroup(String appGroup) {
		this.appGroup = appGroup;
	}
	
	@Override
	public String toString() {
		if (appGroup == null) {
			return ip + ":" + room;
		}
		
		return ip + ":" + room + ":" + appGroup;
	}
}
