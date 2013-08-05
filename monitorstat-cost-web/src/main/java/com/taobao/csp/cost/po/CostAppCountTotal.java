package com.taobao.csp.cost.po;

import java.util.Date;

import com.taobao.csp.cost.service.day.CostType;

/**
 * 成本汇总 按业务线 按公司 按月 按年 按周
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-10-24
 */
public class CostAppCountTotal {

	//应用名
	private String sName;
	//父产品线名
	private String pName="";
	//类型（GROUP|product)
	private String costType;
	//时间类型（按月|按年）
	private int timeType;
	//总成本
	private double costAll;
	//收集时间
	private Date sTime;
	
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	
	public int getTimeType() {
		return timeType;
	}
	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}
	public double getCostAll() {
		return costAll;
	}
	public void setCostAll(double costAll) {
		this.costAll = costAll;
	}
	public Date getsTime() {
		return sTime;
	}
	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	
}
