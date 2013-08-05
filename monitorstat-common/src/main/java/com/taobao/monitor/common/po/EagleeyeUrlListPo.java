package com.taobao.monitor.common.po;

import java.util.ArrayList;
import java.util.List;


/**
 * 接口1：返回应用对应的重要url或接口方法组合
 * @author zhongting.zy
 *
 */
public class EagleeyeUrlListPo {
	private String appName;
	private String time;
//	private long successCallNum;
//	private long successRt;
	private List<String> keylist = new ArrayList<String>();
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
//	public long getSuccessCallNum() {
//		return successCallNum;
//	}
//	public void setSuccessCallNum(long successCallNum) {
//		this.successCallNum = successCallNum;
//	}
//	public long getSuccessRt() {
//		return successRt;
//	}
//	public void setSuccessRt(long successRt) {
//		this.successRt = successRt;
//	}
	public List<String> getKeylist() {
		return keylist;
	}
	public void setKeylist(List<String> keylist) {
		this.keylist = keylist;
	}
}
