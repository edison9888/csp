package com.taobao.csp.cost.po;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 应用成本排行
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-10-24
 */
public class CostTreePo {

	//业务线名字
	private String appName;
	//下级
	private List<CostTreePo> nextCosts;
	//层级（1|2）
	private String level;
	//成本
	private double appCost;
	
	private String costString;
	private String urlNameString;
	
	public List<CostTreePo> getNextCosts() {
		return nextCosts;
	}
	public void setNextCosts(List<CostTreePo> nextCosts) {
		this.nextCosts = nextCosts;
	}
	public double getAppCost() {
		return appCost;
	}
	public void setAppCost(double appCost) {
		this.appCost = appCost;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getCostString() {
		if(costString==null){
			java.text.NumberFormat currencyFormatA = java.text.NumberFormat
	                .getCurrencyInstance(java.util.Locale.CHINA);
			costString=currencyFormatA.format(appCost);
		}
		return costString;
	}
	public String getUrlNameString() {
		if(urlNameString==null){
			try {
				urlNameString=URLEncoder.encode(appName,"gbk");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return urlNameString;
	}
	
	
}
