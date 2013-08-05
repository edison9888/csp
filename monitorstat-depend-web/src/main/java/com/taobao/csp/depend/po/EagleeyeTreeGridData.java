package com.taobao.csp.depend.po;

import java.util.ArrayList;
import java.util.List;

public class EagleeyeTreeGridData{
	//  private String iconCls = "icon-ok";
	//  private String state = "closed";

	private long id;
	private String keyName;
	private String appName;
	private long callnum;
	private double rate;
	
	private boolean isBlack;	//key本身是黑名单or是黑名单的下一级key
	
	private List<EagleeyeTreeGridData> children = new ArrayList<EagleeyeTreeGridData>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public long getCallnum() {
		return callnum;
	}

	public void setCallnum(long callnum) {
		this.callnum = callnum;
	}

	public List<EagleeyeTreeGridData> getChildren() {
		return children;
	}

	public void setChildren(List<EagleeyeTreeGridData> children) {
		this.children = children;
	}

	public boolean isBlack() {
		return isBlack;
	}

	public void setBlack(boolean isBlack) {
		this.isBlack = isBlack;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
}
