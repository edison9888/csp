package com.taobao.monitor.common.db.impl.capacity.po;

import java.util.Date;

/***
 * 应用的容量能力
 * @author youji.zj
 * @version 2012-08-15
 *
 */
public class CapacityCapPo {
	
	String appName;
	
	double singleCapacity;
	
	/*** 最后更新的时间 ***/
	Date time;
	
	/*** 最好更新的用户 ***/
	String user;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public double getSingleCapacity() {
		return singleCapacity;
	}

	public void setSingleCapacity(double singleCapacity) {
		this.singleCapacity = singleCapacity;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	

}
