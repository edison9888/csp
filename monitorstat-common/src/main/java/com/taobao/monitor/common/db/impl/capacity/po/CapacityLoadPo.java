package com.taobao.monitor.common.db.impl.capacity.po;

import java.util.Date;

/***
 * 应用的容量负荷
 * @author youji.zj
 * @version 2012-08-15
 *
 */
public class CapacityLoadPo {
	
	/*** 应用名 ***/
	private String appName;
	
	/*** 负荷值 ***/
	private double qps;
	
	/*** 负荷类型：single(单机) group集群. ***/
	private String dataType;
	
	/*** 负荷的数据源：realtime(实时) day(日报) ***/
	private String dataSouce;
	
	/*** 采集时间 ***/
	private Date collectTime;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public double getQps() {
		return qps;
	}

	public void setQps(double qps) {
		this.qps = qps;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataSouce() {
		return dataSouce;
	}

	public void setDataSouce(String dataSouce) {
		this.dataSouce = dataSouce;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

}
