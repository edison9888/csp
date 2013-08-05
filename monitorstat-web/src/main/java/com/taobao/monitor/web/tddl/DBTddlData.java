package com.taobao.monitor.web.tddl;

import java.util.ArrayList;
import java.util.List;

public class DBTddlData {
	
	private String DBName;
	
	private List<TddlPo> topTddlList = new ArrayList<TddlPo>();
	
	private long sumExecuteTimes;
	
	public void addTopList(TddlPo po) {
		topTddlList.add(po);
	}
	
	public void addExeTimes(long i) {
		sumExecuteTimes += i; 
	}

	public String getDBName() {
		return DBName;
	}

	public void setDBName(String dBName) {
		DBName = dBName;
	}

	public List<TddlPo> getTopTddlList() {
		return topTddlList;
	}

	public void setTopTddlList(List<TddlPo> topTddlList) {
		this.topTddlList = topTddlList;
	}

	public long getSumExecuteTimes() {
		return sumExecuteTimes;
	}

	public void setSumExecuteTimes(long sumExecuteTimes) {
		this.sumExecuteTimes = sumExecuteTimes;
	}

}
