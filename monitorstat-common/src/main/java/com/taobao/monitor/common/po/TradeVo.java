
package com.taobao.monitor.common.po;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author xiaodu
 * @version 2010-5-4 ÏÂÎç07:34:15
 */
public class TradeVo implements Comparable<TradeVo>{	
	private Date date;
	private String collectTime; //HHmm
	
	private double traderAmount;
	private double traderCount;
	
	
	private Map<Integer,Double[]> traderMap = new HashMap<Integer, Double[]>();
	
	
	private String traderAmount100;
	private String traderCount100;
	
	private String traderAmount0;
	private String traderCount0;
	
	private String traderAmount1;
	private String traderCount1;
	
	private String traderAmount2;
	private String traderCount2;
	
	private String traderAmount9;
	private String traderCount9;
	
	private String countday100;
	private String amountday100;	
	
	
	private String countday0;
	private String amountday0;	
	
	private String countday1;
	private String amountday1;
	private String countday2;
	private String amountday2;
	private String countday9;
	private String amountday9;

	public String getCollectTime() {
		return collectTime;
	}



	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}

	public String getTraderAmount100() {
		return traderAmount100;
	}



	public void setTraderAmount100(String traderAmount100) {
		this.traderAmount100 = traderAmount100;
	}



	public String getTraderCount100() {
		return traderCount100;
	}



	public void setTraderCount100(String traderCount100) {
		this.traderCount100 = traderCount100;
	}



	public String getTraderAmount0() {
		return traderAmount0;
	}



	public void setTraderAmount0(String traderAmount0) {
		this.traderAmount0 = traderAmount0;
	}



	public String getTraderCount0() {
		return traderCount0;
	}



	public void setTraderCount0(String traderCount0) {
		this.traderCount0 = traderCount0;
	}



	public String getTraderAmount1() {
		return traderAmount1;
	}



	public void setTraderAmount1(String traderAmount1) {
		this.traderAmount1 = traderAmount1;
	}



	public String getTraderCount1() {
		return traderCount1;
	}



	public void setTraderCount1(String traderCount1) {
		this.traderCount1 = traderCount1;
	}



	public String getTraderAmount2() {
		return traderAmount2;
	}



	public void setTraderAmount2(String traderAmount2) {
		this.traderAmount2 = traderAmount2;
	}



	public String getTraderCount2() {
		return traderCount2;
	}



	public void setTraderCount2(String traderCount2) {
		this.traderCount2 = traderCount2;
	}



	public String getTraderAmount9() {
		return traderAmount9;
	}



	public void setTraderAmount9(String traderAmount9) {
		this.traderAmount9 = traderAmount9;
	}



	public String getTraderCount9() {
		return traderCount9;
	}



	public void setTraderCount9(String traderCount9) {
		this.traderCount9 = traderCount9;
	}

	public String getCountday100() {
		return countday100;
	}



	public void setCountday100(String countday100) {
		this.countday100 = countday100;
	}



	public String getAmountday100() {
		return amountday100;
	}



	public void setAmountday100(String amountday100) {
		this.amountday100 = amountday100;
	}



	public String getCountday0() {
		return countday0;
	}



	public void setCountday0(String countday0) {
		this.countday0 = countday0;
	}



	public String getAmountday0() {
		return amountday0;
	}



	public void setAmountday0(String amountday0) {
		this.amountday0 = amountday0;
	}



	public String getCountday1() {
		return countday1;
	}



	public void setCountday1(String countday1) {
		this.countday1 = countday1;
	}



	public String getAmountday1() {
		return amountday1;
	}



	public void setAmountday1(String amountday1) {
		this.amountday1 = amountday1;
	}



	public String getCountday2() {
		return countday2;
	}



	public void setCountday2(String countday2) {
		this.countday2 = countday2;
	}



	public String getAmountday2() {
		return amountday2;
	}



	public void setAmountday2(String amountday2) {
		this.amountday2 = amountday2;
	}



	public String getCountday9() {
		return countday9;
	}



	public void setCountday9(String countday9) {
		this.countday9 = countday9;
	}



	public String getAmountday9() {
		return amountday9;
	}



	public void setAmountday9(String amountday9) {
		this.amountday9 = amountday9;
	}



	public Map<Integer, Double[]> getTraderMap() {
		return traderMap;
	}



	public void setTraderMap(Map<Integer, Double[]> traderMap) {
		this.traderMap = traderMap;
	}



	public double getTraderAmount() {
		return traderAmount;
	}



	public void setTraderAmount(double traderAmount) {
		this.traderAmount = traderAmount;
	}



	public double getTraderCount() {
		return traderCount;
	}



	public void setTraderCount(double traderCount) {
		this.traderCount = traderCount;
	}



	public int compareTo(TradeVo o) {
		
		if(o.getDate().equals(this.getDate())){
			return 0;
		}else if(o.getDate().getTime()>this.getDate().getTime()){
			return 1;
		}else if(o.getDate().getTime()<this.getDate().getTime()){
			return -1;
		}
		return 0;
		
	}

}
