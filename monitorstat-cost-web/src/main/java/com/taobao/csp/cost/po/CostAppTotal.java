package com.taobao.csp.cost.po;

import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.taobao.csp.cost.service.day.CostType;

/**
 * 成本汇总 按业务线 按公司 按月 按年 按周
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-10-24
 */
public class CostAppTotal {

	//应用名
	private String sName;
	//父产品线名
	private String pName="";
	//类型（GROUP|product)
	private String costType;
	//时间类型（按月|按年）
	private int timeType;
	//调用次数
	private long callNum;
	//千次调用成本
	private double preCost;
	//总成本
	private double costAll;
	
	//一个月内平均每天调用次数
	private double preCallNum;
	//一个月内平均每天千次调用成本
	private double prePreCost;
	//一个月内平均每天总成本
	private double preCostAll;
		

	private String preCallNumStr;

	private String prePreCostStr;

	private String preCostAllStr;
		
		
	public String getPreCallNumStr() {
		if(StringUtils.isBlank(preCallNumStr)){
			preCallNumStr=new DecimalFormat("#.#").format(preCallNum);
		}
		return preCallNumStr;
	}
	public String getPrePreCostStr() {
		if(StringUtils.isBlank(prePreCostStr)){
			prePreCostStr=new DecimalFormat("#.#").format(prePreCost);
		}
		return prePreCostStr;
	}
	public String getPreCostAllStr() {
		if(StringUtils.isBlank(preCostAllStr)){
			preCostAllStr=new DecimalFormat("#.#").format(preCostAll);
		}
		
		return preCostAllStr;
	}
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
		return Double.parseDouble(new DecimalFormat("#.#").format(costAll));
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
	public long getCallNum() {
		return callNum;
	}
	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}
	public double getPreCost() {

		return Double.parseDouble(new DecimalFormat("#.#").format(preCost));
	}
	public void setPreCost(double preCost) {
		this.preCost = preCost;
	}
	public double getPrePreCost() {
		return prePreCost;
	}
	public void setPrePreCost(double prePreCost) {
		this.prePreCost = prePreCost;
	}
	public double getPreCostAll() {
		return preCostAll;
	}
	public void setPreCostAll(double preCostAll) {
		this.preCostAll = preCostAll;
	}
	public double getPreCallNum() {
		return preCallNum;
	}
	public void setPreCallNum(double preCallNum) {
		this.preCallNum = preCallNum;
	}
	private String costString;
	public String getCostString() {
		if(preCostAll>1000 && preCostAll<10000){
			costString="￥"+(new DecimalFormat("#.#").format(preCostAll/1000))+"千";
		}
		if(preCostAll>10000 && preCostAll<10000*1000){
			costString="￥"+(new DecimalFormat("#.#").format(preCostAll/10000))+"万";
		}
		if(preCostAll>10000*10000){
			costString="￥"+(new DecimalFormat("#.#").format(preCostAll/10000/1000))+"亿";
		}
//		if(costString==null){
//			java.text.NumberFormat currencyFormatA = java.text.NumberFormat
//	                .getCurrencyInstance(java.util.Locale.CHINA);
//			costString=currencyFormatA.format(preCostAll);
//		}
		return costString;
	}
}
